package br.com.warehouse.shortenlinks.features.shorten.presentation.components

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import br.com.warehouse.shortenlinks.core.presentation.model.SubmitLoadingState
import br.com.warehouse.shortenlinks.features.shorten.domain.model.Shorten
import androidx.compose.ui.tooling.preview.Preview
import br.com.warehouse.shortenlinks.ui.theme.ShortenLinksTheme

@Composable
fun ResultSection(state: SubmitLoadingState<Shorten>) {
    when (state) {
        is SubmitLoadingState.Loading -> {
            CircularProgressIndicator()
        }
        is SubmitLoadingState.Success -> {
            Text("Shortened URL: ${state.data.links.shortUrl}")
        }
        is SubmitLoadingState.Error -> {
            Text(
                text = "Error: ${state.exception.message}",
                color = MaterialTheme.colorScheme.error
            )
        }
        is SubmitLoadingState.Idle -> {
            // Do nothing while idle
        }
    }
}

@Preview(showBackground = true, name = "Loading State")
@Composable
private fun ResultSectionLoadingPreview() {
    ShortenLinksTheme {
        ResultSection(state = SubmitLoadingState.Loading)
    }
}

@Preview(showBackground = true, name = "Success State")
@Composable
private fun ResultSectionSuccessPreview() {
    ShortenLinksTheme {
        ResultSection(
            state = SubmitLoadingState.Success(
                Shorten(
                    alias = "alias123",
                    links = br.com.warehouse.shortenlinks.features.shorten.data.model.Links(
                        self = "https://original.url",
                        shortUrl = "https://short.url/123"
                    )
                )
            )
        )
    }
}

@Preview(showBackground = true, name = "Error State")
@Composable
private fun ResultSectionErrorPreview() {
    ShortenLinksTheme {
        ResultSection(
            state = SubmitLoadingState.Error(
                Throwable("This is a long error message to see how it wraps.")
            )
        )
    }
}
