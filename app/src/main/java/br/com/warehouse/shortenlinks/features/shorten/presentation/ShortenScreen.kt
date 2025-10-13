package br.com.warehouse.shortenlinks.features.shorten.presentation

import android.content.ClipData
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.warehouse.shortenlinks.core.presentation.model.SubmitLoadingState
import br.com.warehouse.shortenlinks.core.presentation.model.isLoading
import br.com.warehouse.shortenlinks.features.shorten.presentation.components.LinkHistoryList
import br.com.warehouse.shortenlinks.features.shorten.presentation.components.ResultSection
import br.com.warehouse.shortenlinks.features.shorten.presentation.components.UrlInputSection
import br.com.warehouse.shortenlinks.ui.theme.spacing

@Composable
fun ShortenScreen(modifier: Modifier = Modifier, viewModel: ShortenViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val clipboardManager = remember {
        context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
    }

    LaunchedEffect(uiState.shortenUrlState) {
        val localState = uiState.shortenUrlState
        if (localState is SubmitLoadingState.Success) {
            val shortUrl = localState.data.links.shortUrl
            val clip = ClipData.newPlainText("shortened url", shortUrl)
            clipboardManager.setPrimaryClip(clip)
            Toast.makeText(context, "Shortened URL copied to clipboard", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = modifier.padding(MaterialTheme.spacing.medium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        UrlInputSection(
            url = uiState.url,
            onUrlChange = { viewModel.onEvent(ShortenScreenEvent.OnUrlChange(it)) },
            onShortenClick = { viewModel.onEvent(ShortenScreenEvent.OnShortenClick) },
            onClearClick = { viewModel.onEvent(ShortenScreenEvent.OnClearClick) },
            urlError = uiState.urlError
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        ResultSection(state = uiState.shortenUrlState)
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
        Text(
            text = "Recently shortened URLs",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
        LinkHistoryList(
            history = uiState.history,
            isLoading = uiState.shortenUrlState.isLoading(),
            modifier = Modifier.weight(1f)
        )
    }
}