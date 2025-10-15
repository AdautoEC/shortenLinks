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

/**
 * A composable that provides a text field for URL input and a button to trigger the shortening process.
 *
 * @param url The current value of the URL input field.
 * @param onUrlChange A callback that is invoked when the URL input value changes.
 * @param onShortenClick A callback that is invoked when the shorten button is clicked.
 * @param onClearClick A callback that is invoked when the clear button is clicked.
 * @param modifier The modifier to be applied to the composable.
 * @param urlError An optional error message to be displayed below the text field.
 * @param isLoading A boolean to indicate if the shortening process is in progress.
 */
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