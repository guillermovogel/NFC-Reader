package com.vogel.nfc_reader.nfc.provider

import android.nfc.tech.IsoDep
import com.github.devnied.emvnfccard.parser.IProvider

class TransceiverProvider {
    fun getTransceiver(isoDep: IsoDep): IProvider = Provider(isoDep)
}
