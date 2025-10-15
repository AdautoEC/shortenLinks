package br.com.warehouse.shortenlinks.features.shorten.presentation

import android.content.ClipData
import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.warehouse.shortenlinks.core.presentation.model.SubmitLoadingState
import br.com.warehouse.shortenlinks.core.presentation.model.isLoading
import br.com.warehouse.shortenlinks.features.shorten.presentation.components.ErrorDialog
import br.com.warehouse.shortenlinks.features.shorten.presentation.components.LinkHistoryList
import br.com.warehouse.shortenlinks.features.shorten.presentation.components.UrlInputSection
import br.com.warehouse.shortenlinks.ui.theme.spacing
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ShortenScreen(
    modifier: Modifier = Modifier,
    viewModel: ShortenViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val clipboardManager = remember {
        context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
    }

    if (uiState.networkError != null) {
        ErrorDialog(
            title = "Error",
            message = uiState.networkError!!,
            onDismiss = { viewModel.onEvent(ShortenScreenEvent.OnDismissErrorDialog) }
        )
    }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
            }
        }
    }

    LaunchedEffect(uiState.shortenUrlState) {
        val localState = uiState.shortenUrlState
        if (localState is SubmitLoadingState.Success) {
            val shortUrl = localState.data.links.shortUrl
            val clip = ClipData.newPlainText("shortened url", shortUrl)
            clipboardManager.setPrimaryClip(clip)
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Shorten Links",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
        UrlInputSection(
            url = uiState.url,
            onUrlChange = { viewModel.onEvent(ShortenScreenEvent.OnUrlChange(it)) },
            onShortenClick = { viewModel.onEvent(ShortenScreenEvent.OnShortenClick) },
            onClearClick = { viewModel.onEvent(ShortenScreenEvent.OnClearClick) },
            urlError = uiState.urlError,
            isLoading = uiState.shortenUrlState.isLoading()
        )
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