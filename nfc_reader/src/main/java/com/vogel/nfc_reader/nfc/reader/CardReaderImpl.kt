package com.vogel.nfc_reader.nfc.reader

import android.content.Context
import android.content.Intent
import android.nfc.tech.IsoDep
import android.provider.Settings
import com.github.devnied.emvnfccard.parser.EmvTemplate
import com.vogel.nfc_reader.nfc.api.CardReader
import com.vogel.nfc_reader.nfc.mapToCardData
import com.vogel.nfc_reader.nfc.model.CardData
import com.vogel.nfc_reader.nfc.model.CardState
import com.vogel.nfc_reader.nfc.provider.TransceiverProvider

internal class CardReaderImpl constructor(
    private val builder: EmvTemplate.Builder,
    private val config: EmvTemplate.Config,
    private val provider: TransceiverProvider,
    private val intentProvider: NfcIntentProvider
) : CardReader {

    companion object {
        val EMPTY_CARD = CardData(
            aids = emptyList(),
            types = emptyList(),
            expireDate = null,
            number = null,
            state = CardState.UNKNOWN,
            holderFirstName = "NO ENCONTRADO EL NOMBRE",
            holderLastName = "NO ENCONTRADO EL APELLIDO",
        )
    }

    override fun getCard(isoDep: IsoDep): CardData = getCardResult(isoDep).getOrElse { EMPTY_CARD }

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

    override fun openSettings(context: Context) {
        context.startActivity(intentProvider.settings())
    }

}

internal class NfcIntentProvider {
    fun settings(): Intent = Intent(Settings.ACTION_NFC_SETTINGS)
}