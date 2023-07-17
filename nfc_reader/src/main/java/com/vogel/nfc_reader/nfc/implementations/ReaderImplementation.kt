package com.vogel.nfc_reader.nfc.implementations

import android.content.Context
import android.nfc.tech.IsoDep
import com.github.devnied.emvnfccard.parser.EmvTemplate
import com.vogel.nfc_reader.nfc.api.CardReader
import com.vogel.nfc_reader.nfc.mapToCardData
import com.vogel.nfc_reader.nfc.model.CardData
import com.vogel.nfc_reader.nfc.model.CardState
import com.vogel.nfc_reader.nfc.provider.TransceiverProvider
import com.vogel.nfc_reader.nfc.utils.ACTION_NFC_SETTINGS

class ReaderImplementation constructor(
    private val builder: EmvTemplate.Builder,
    private val config: EmvTemplate.Config,
    private val provider: TransceiverProvider,
) : CardReader {

    /**
     * Get card data from NFC card, and map it to [CardData]
     */
    override fun getCardResult(isoDep: IsoDep): Result<CardData> {
        return runCatching {
            isoDep.connect()
            builder.setConfig(config)
                .setProvider(provider.getTransceiver(isoDep))
                .build()
                .readEmvCard()
                .mapToCardData()
        }
            .also {
                runCatching { isoDep.close() }
            }
    }

    /**
     * Open NFC settings in the device
     */
    override fun openSettings(context: Context) {
        context.startActivity(ACTION_NFC_SETTINGS)
    }
}