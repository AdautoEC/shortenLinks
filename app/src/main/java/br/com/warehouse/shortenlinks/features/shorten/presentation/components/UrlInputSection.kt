package br.com.warehouse.shortenlinks.features.shorten.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import br.com.warehouse.shortenlinks.ui.theme.ShortenLinksTheme
import br.com.warehouse.shortenlinks.ui.theme.spacing

@Composable
fun UrlInputSection(
    url: String,
    onUrlChange: (String) -> Unit,
    onShortenClick: () -> Unit,
    onClearClick: () -> Unit,
    modifier: Modifier = Modifier,
    urlError: String? = null,
    isLoading: Boolean = false
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = url,
            onValueChange = onUrlChange,
            label = { Text("Enter URL to shorten") },
            modifier = Modifier.fillMaxWidth(),
            isError = urlError != null,
            supportingText = {
                if (urlError != null) {
                    Text(urlError)
                }
            },
            trailingIcon = {
                if (url.isNotEmpty()) {
                    IconButton(onClick = onClearClick) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear")
                    }
                }
            },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
        Button(
            onClick = onShortenClick,
            enabled = !isLoading && url.isNotBlank()
        ) {
            Text("Shorten")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun UrlInputSectionPreview() {
    ShortenLinksTheme {
        UrlInputSection(url = "https://www.google.com", onUrlChange = {}, onShortenClick = {}, onClearClick = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun UrlInputSectionErrorPreview() {
    ShortenLinksTheme {
        UrlInputSection(url = "invalid url", onUrlChange = {}, onShortenClick = {}, onClearClick = {}, urlError = "Invalid URL")
    }
}