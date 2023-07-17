package com.vogel.nfc_reader.nfc.api

import android.app.Activity
import android.content.Context
import com.vogel.nfc_reader.nfc.implementations.ListenerImplementation
import com.vogel.nfc_reader.nfc.utils.NFCState
import kotlinx.coroutines.flow.Flow

interface CardReaderListener {
    val nfcStatus: Flow<NFCState>

    fun start(activity: Activity)
    fun stop(activity: Activity)
    fun openSettings(context: Context){}

    companion object {
        fun newInstance(cardReader: CardReader): CardReaderListener {
            return ListenerImplementation(cardReader)
        }
    }
}
