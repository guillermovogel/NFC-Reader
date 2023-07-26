package com.vogel.nfc_reader.nfc.implementations

import android.content.Context
import android.nfc.tech.IsoDep
import com.github.devnied.emvnfccard.parser.EmvTemplate
import com.vogel.nfc_reader.nfc.api.CardReader
import com.vogel.nfc_reader.nfc.mapToCard
import com.vogel.nfc_reader.nfc.model.Card
import com.vogel.nfc_reader.nfc.provider.Provider
import com.vogel.nfc_reader.nfc.utils.ACTION_NFC_SETTINGS

class ReaderImplementation constructor(
    private val builder: EmvTemplate.Builder,
    private val config: EmvTemplate.Config,
) : CardReader {

    /**
     * Get card data from NFC card, and map it to [Card]
     */
    override fun getCardResult(isoDep: IsoDep): Result<Card> {
        return runCatching {
            isoDep.connect()
            builder.setConfig(config)
                .setProvider(Provider(isoDep))
                .build()
                .readEmvCard()
                .mapToCard()
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