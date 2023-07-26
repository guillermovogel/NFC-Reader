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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vogel.nfc_reader.ui.theme.NFCReaderTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NFCReaderTheme {
                CardDataScreen()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.cardReaderListener.stopReading(this)
    }

}

@Composable
fun CardDataScreen(
    viewModel: MainViewModel = hiltViewModel()
) {

    val state = viewModel.uiState
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.cardReaderListener.startReading(context as MainActivity)
    }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colors.background)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = state.cardStateText)
        if (state.settingsButtonVisible) {
            Button(
                onClick = { viewModel.cardReader.openSettings(context) },
                modifier = Modifier.padding(top = 15.dp)
            ) {
                Text("SETTINGS")
            }
        }
    }
}