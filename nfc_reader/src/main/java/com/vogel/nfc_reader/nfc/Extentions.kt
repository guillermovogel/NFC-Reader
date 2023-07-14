package com.vogel.nfc_reader.nfc

import com.vogel.nfc_reader.nfc.model.CardData
import com.github.devnied.emvnfccard.enums.EmvCardScheme
import com.github.devnied.emvnfccard.model.EmvCard
import com.github.devnied.emvnfccard.model.enums.CardStateEnum
import com.vogel.nfc_reader.nfc.model.CardState

internal fun ByteArray.toHex(space: Boolean = false): String =
    joinToString(separator = if (space) " " else "") { "%02x".format(it) }.uppercase()

internal fun EmvCard.mapToCardData(): CardData {
    val aids = applications.map { it.aid.toHex() }
    val types = aids.map { EmvCardScheme.getCardTypeByAid(it).name }

    return CardData(
        aids = aids,
        types = types,
        expireDate = expireDate,
        number = cardNumber,
        state = when (state) {
            null, CardStateEnum.UNKNOWN -> CardState.UNKNOWN
            CardStateEnum.LOCKED -> CardState.LOCKED
            CardStateEnum.ACTIVE -> CardState.ACTIVE
        },
        holderFirstName = holderFirstname,
        holderLastName = holderLastname,
    )
}
