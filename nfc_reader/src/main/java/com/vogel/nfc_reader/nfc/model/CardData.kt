package com.vogel.nfc_reader.nfc.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CardData(
    val aids: List<String>,
    val types: List<String>,
    val expireDate: Date?,
    val number: String?,
    val state: CardState,
    val holderFirstName: String? = "First name not found",
    val holderLastName: String? = "Lastname not found",
) {

    var formattedNumber: String? = null
        private set

    var formattedDate: String? = null
        private set

    var expireMonth: String? = null
        private set

    var expireYear: String? = null
        private set

    val isValid: Boolean
        get() = number != null && expireDate != null && state != CardState.LOCKED

    init {
        if (number != null) {
            formattedNumber = number.filter { it.isDigit() }
        }

        if (expireDate != null) {
            formattedDate = EXPECTED_DATE_FORMAT.format(expireDate).also {
                expireMonth = it.take(2)
                expireYear = it.takeLast(2)
            }
        }
    }

    companion object {
        internal val EXPECTED_DATE_FORMAT = SimpleDateFormat("MM/yy", Locale.US)
    }
}
