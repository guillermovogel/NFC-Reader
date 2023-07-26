package com.vogel.nfc_reader.nfc.utils

import android.content.Context
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.NfcManager
import android.provider.Settings
import com.vogel.nfc_reader.nfc.model.Card


object NFCUtils {

    fun getNfcAdapter(context: Context): NfcAdapter? {
        val manager = context.getSystemService(Context.NFC_SERVICE) as NfcManager
        return manager.defaultAdapter
    }

    fun deviceHasNfc(context: Context): NFCState {
        val adapter = getNfcAdapter(context)
        return if (adapter != null && adapter.isEnabled) {
            // NFC is supported and enabled
            NFCState.ReadyToScan
        } else if (adapter != null && !adapter.isEnabled) {
            // NFC is not enabled. Need to enable by the user.
            NFCState.Disabled
        } else {
            // NFC is not supported
            NFCState.NotSupported
        }
    }
}

sealed class NFCState {
    object ReadyToScan : NFCState()
    object StartReading : NFCState()
    object CardLost : NFCState()
    object Disabled : NFCState()
    object NotSupported : NFCState()
    data class Error(val throwable: Throwable) : NFCState()
    data class Success(val card: Card) : NFCState()
}

val ACTION_NFC_SETTINGS = Intent(Settings.ACTION_NFC_SETTINGS)