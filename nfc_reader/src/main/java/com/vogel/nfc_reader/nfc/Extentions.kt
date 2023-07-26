package com.vogel.nfc_reader.nfc

import com.github.devnied.emvnfccard.model.EmvCard
import com.github.devnied.emvnfccard.model.enums.CardStateEnum
import com.vogel.nfc_reader.nfc.model.Card
import com.vogel.nfc_reader.nfc.model.CardState

internal fun EmvCard.mapToCard(): Card =
    Card(
        expireDate = expireDate,
        number = cardNumber,
        state = when (state) {
            null, CardStateEnum.UNKNOWN -> CardState.UNKNOWN
            CardStateEnum.LOCKED -> CardState.LOCKED
            CardStateEnum.ACTIVE -> CardState.ACTIVE
        },
    )
