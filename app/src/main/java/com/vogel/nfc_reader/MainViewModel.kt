package com.vogel.nfc_reader


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vogel.nfc_reader.nfc.api.CardReader
import com.vogel.nfc_reader.nfc.api.CardReaderListener
import com.vogel.nfc_reader.nfc.model.CardData
import com.vogel.nfc_reader.nfc.utils.NFCState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {


    val cardReader = CardReader.newInstance()
    val cardReaderListener = CardReaderListener.newInstance(cardReader)

    var uiState by mutableStateOf(UiState())
        private set

    init {
        listener()
    }

    fun listener() {
        viewModelScope.launch {
            cardReaderListener.nfcStatus.collect {
                uiState = uiState.copy(settingsButtonVisible = false)
                uiState = uiState.copy(
                    cardStateText = when (it) {
                        NFCState.CardLost -> "Keep it steady, card lost!"
                        is NFCState.Error -> CardData.fetchErrorInformation(it.throwable)
                        NFCState.Disabled -> {
                            uiState = uiState.copy(settingsButtonVisible = true)
                            "NFC is disabled. Go to settings to activate it"
                        }

                        NFCState.NotSupported -> "NFC is not supported in this device"
                        NFCState.ReadyToScan -> "The device is ready to scan"
                        NFCState.StartReading -> "The device is reading the card..."
                        is NFCState.Success -> CardData.fetchCardInformation(it.card)
                    }
                )
            }
        }
    }
}

data class UiState(
    val cardStateText: String = "",
    val settingsButtonVisible: Boolean = false
)