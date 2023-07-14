package com.vogel.nfc_reader


import androidx.lifecycle.ViewModel
import com.vogel.nfc_reader.nfc.api.CardReader
import com.vogel.nfc_reader.nfc.api.CardReaderObservable

class MainViewModel : ViewModel() {
    val cardReader = CardReader.newInstance()
    val cardReaderObservable = CardReaderObservable.newInstance(cardReader)
}