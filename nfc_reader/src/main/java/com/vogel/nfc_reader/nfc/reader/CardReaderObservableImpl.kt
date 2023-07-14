package com.vogel.nfc_reader.nfc.reader

import android.app.Activity
import android.content.Context
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.TagLostException
import android.nfc.tech.IsoDep
import com.vogel.nfc_reader.nfc.api.CardReader
import com.vogel.nfc_reader.nfc.api.CardReaderObservable
import com.vogel.nfc_reader.nfc.utils.NFCState
import com.vogel.nfc_reader.nfc.utils.NFCUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull

// This class is responsible for observing the NFC state of the user's device.
// It is also responsible for reading the NFC card and returning the result.
internal class CardReaderObservableImpl constructor(
    private val cardReader: CardReader
) : CardReaderObservable, NfcAdapter.ReaderCallback {

    private var adapter: NfcAdapter? = null

    private val _event = MutableStateFlow<NFCState?>(null)

    override val event: Flow<NFCState>
        get() = _event.filterNotNull()

    /**
     * This function starts the NFC reader to scan for NFC cards.
     * Also checks if the user has NFC enabled on their device.
     * If enabled, then reader mode is activated so that the user can
     * scan their NFC card (debit/credit).
     */
    override fun start(activity: Activity) {
        adapter = NFCUtils.getNfcAdapter(activity)?.apply {
            // NFC Supported
            if (!isEnabled) {
                _event.tryEmit(NFCState.Disabled)
                adapter = null
                return
            }
            if (adapter == null) {
                _event.tryEmit(NFCState.ReadyToScan)
            }
        } ?: run {
            // NFC Not Supported
            _event.tryEmit(NFCState.NotSupported)
            null
        }

        // Start reading NFC card
        if (adapter != null) {
            adapter?.enableReaderMode(
                activity,
                this,
                NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK or
                        NfcAdapter.FLAG_READER_NFC_A or
                        NfcAdapter.FLAG_READER_NFC_B,
                null
            )
        }
    }

    /**
     * This function stops the NFC reader from scanning for NFC cards.
     */
    override fun stop(activity: Activity) {
        adapter?.disableReaderMode(activity)
    }

    /**
     * This function allows the user to open the NFC settings on their device
     * if they have NFC disabled, in order to enable it.
     */
    override fun openSettings(context: Context) {
        cardReader.openSettings(context)
    }

    /**
    * When a tag is discovered, this function is called.
    * If the tag is successfully read, then the result is emitted, otherwise
    * an error is emitted with the corresponding error message.
    */
    override fun onTagDiscovered(tag: Tag?) {
        _event.tryEmit(NFCState.StartReading)
        val isoDep = IsoDep.get(tag)
        val resultEvent = cardReader.getCardResult(isoDep).fold(
            onSuccess = NFCState::Success,
            onFailure = {
                when (it) {
                    is TagLostException -> NFCState.CardLost
                    else -> NFCState.Error(it)
                }
            }
        )
        _event.tryEmit(resultEvent)
    }
}
