package com.vogel.nfc_reader.nfc.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Card(
    val expireDate: Date?,
    val number: String?,
    val state: CardState,
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

        fun fetchCardInformation(card: Card): String = """
                State: ${card.state}
                
                Number: ${card.formattedNumber}
                Expires: ${card.formattedDate}
                
                Valid: ${card.isValid}
                
            """.trimIndent()

        fun fetchErrorInformation(error: Throwable): String = """
                ERROR
                Message: ${error.message ?: error.cause?.message}
                
            """.trimIndent()
    }

}
