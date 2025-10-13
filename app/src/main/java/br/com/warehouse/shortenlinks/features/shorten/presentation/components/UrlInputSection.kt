package br.com.warehouse.shortenlinks.features.shorten.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = url,
            onValueChange = onUrlChange,
            label = { Text("Enter URL") },
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
        Button(onClick = onShortenClick) {
            Text("Shorten")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun UrlInputSectionPreview() {
    ShortenLinksTheme {
        UrlInputSection(url = "https://www.google.com", onUrlChange = {}, onShortenClick = {})
    }
}