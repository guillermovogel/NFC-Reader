package com.vogel.nfc_reader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.vogel.nfc_reader.nfc.model.CardData
import com.vogel.nfc_reader.nfc.utils.NFCState
import com.vogel.nfc_reader.ui.theme.NFCReaderTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val cardStateText = mutableStateOf("")
        val settingsButtonVisible = mutableStateOf(false)

        lifecycleScope.launchWhenStarted {
            viewModel.cardReaderObservable.event.collect {
                settingsButtonVisible.value = false
                cardStateText.value = when (it) {
                    NFCState.CardLost -> "Keep it steady. Card lost!"
                    is NFCState.Error -> getErrorLabel(it.throwable)
                    NFCState.Disabled -> {
                        settingsButtonVisible.value = true
                        "NFC Disabled"
                    }

                    NFCState.NotSupported -> "NFC Not Supported"
                    NFCState.ReadyToScan -> "Ready to Scan"
                    NFCState.StartReading -> "Reading card..."
                    is NFCState.Success -> getCardLabel(it.card)
                }
            }
        }
        setContent {
            NFCReaderTheme {
                CardDataScreen(
                    data = cardStateText.value,
                    settingsButtonVisible = settingsButtonVisible.value
                ) {
                    viewModel.cardReader.openSettings(this)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.cardReaderObservable.start(this)
    }

    override fun onPause() {
        super.onPause()
        viewModel.cardReaderObservable.stop(this)
    }

    companion object {
        internal fun getCardLabel(cardData: CardData): String {
            return """
                AID: ${cardData.aids.joinToString(" | ")}
                Type: ${cardData.types.joinToString(" | ")}
                State: ${cardData.state.name}
                
                Number: ${cardData.formattedNumber}
                Expires: ${cardData.formattedDate}
                
                Valid: ${cardData.isValid}
                
                Holder: ${cardData.holderLastName}
                
            """.trimIndent()
        }

        internal fun getErrorLabel(error: Throwable): String {
            return """
                ERROR
                Message: ${error.message ?: error.cause?.message}
                
            """.trimIndent()
        }
    }

    @Composable
    fun CardDataScreen(
        data: String,
        settingsButtonVisible: Boolean,
        openNfcSettings: () -> Unit = {}
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colors.background)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = data)
            if (settingsButtonVisible) {
                Button(
                    onClick = openNfcSettings,
                    modifier = Modifier.padding(top = 15.dp)
                ) {
                    Text("SETTINGS")
                }
            }
        }
    }
}