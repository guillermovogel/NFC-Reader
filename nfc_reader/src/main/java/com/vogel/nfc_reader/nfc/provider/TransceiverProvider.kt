package com.vogel.nfc_reader.nfc.provider

import android.nfc.tech.IsoDep
import com.github.devnied.emvnfccard.parser.IProvider
import com.vogel.nfc_reader.nfc.provider.IProviderImpl

internal class TransceiverProvider {
    fun getTransceiver(isoDep: IsoDep): IProvider = IProviderImpl(isoDep)
}
