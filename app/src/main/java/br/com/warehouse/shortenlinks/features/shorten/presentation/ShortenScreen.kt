package br.com.warehouse.shortenlinks.features.shorten.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.warehouse.shortenlinks.core.presentation.model.SubmitLoadingState
import br.com.warehouse.shortenlinks.features.shorten.presentation.components.ResultSection
import br.com.warehouse.shortenlinks.features.shorten.presentation.components.UrlInputSection
import br.com.warehouse.shortenlinks.ui.theme.spacing

@Composable
fun ShortenScreen(modifier: Modifier = Modifier, viewModel: ShortenViewModel = hiltViewModel()) {
    val url by viewModel.url.collectAsState()
    val state by viewModel.shortenUrlState.flow.collectAsState(initial = SubmitLoadingState.Idle)

    Column(
        modifier = modifier.padding(MaterialTheme.spacing.medium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        UrlInputSection(
            url = url,
            onUrlChange = { viewModel.url.value = it },
            onShortenClick = { viewModel.shortenUrlState.submit() }
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        ResultSection(state = state)
    }
}