package br.com.warehouse.shortenlinks.features.shorten.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.warehouse.shortenlinks.core.presentation.model.isError
import br.com.warehouse.shortenlinks.core.presentation.model.isLoading
import br.com.warehouse.shortenlinks.core.presentation.model.isSuccess
import br.com.warehouse.shortenlinks.core.presentation.model.getSuccessDataOrNull
import br.com.warehouse.shortenlinks.core.presentation.model.getErrorThrowableOrNull

@Composable
fun ShortenScreen(modifier: Modifier = Modifier, viewModel: ShortenViewModel = hiltViewModel()) {
    val url by viewModel.url.collectAsState()
    val state by viewModel.shortenUrlState.flow.collectAsState(initial = br.com.warehouse.shortenlinks.core.presentation.model.SubmitLoadingState.Idle)

    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = url,
            onValueChange = { viewModel.url.value = it },
            label = { Text("Enter URL") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { viewModel.shortenUrlState.submit() }) {
            Text("Shorten")
        }

        if (state.isLoading()) {
            CircularProgressIndicator()
        }
        if (state.isSuccess()) {
            Text("Shortened URL: ${state.getSuccessDataOrNull()?.shortenedUrl}")
        }
        if (state.isError()) {
            Text("Error: ${state.getErrorThrowableOrNull()?.message}")
        }
    }
}