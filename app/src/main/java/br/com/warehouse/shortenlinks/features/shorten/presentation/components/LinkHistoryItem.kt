package br.com.warehouse.shortenlinks.features.shorten.presentation.components

import android.content.ClipData
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.warehouse.shortenlinks.features.shorten.domain.model.Links
import br.com.warehouse.shortenlinks.features.shorten.domain.model.Shorten
import br.com.warehouse.shortenlinks.ui.theme.ShortenLinksTheme

/**
 * A composable that displays a single item in the link history.
 *
 * It shows the shortened URL by default and allows the user to expand it to see the original URL.
 * Both the shortened and original URLs can be copied to the clipboard.
 *
 * @param shorten The [Shorten] object containing the link information to display.
 */
@Composable
fun LinkHistoryItem(shorten: Shorten) {
    var isExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val clipboardManager = remember {
        context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .semantics { contentDescription = "History item card" }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = shorten.links.shortUrl,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = {
                    val clip = ClipData.newPlainText("shortened url", shorten.links.shortUrl)
                    clipboardManager.setPrimaryClip(clip)
                }) {
                    Icon(Icons.Filled.ContentCopy, contentDescription = "Copy shortened URL")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.clickable { isExpanded = !isExpanded },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Original URL", style = MaterialTheme.typography.bodySmall)
                Icon(
                    imageVector = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = if (isExpanded) "Collapse" else "Expand"
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = shorten.links.self,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = {
                        val clip = ClipData.newPlainText("original url", shorten.links.self)
                        clipboardManager.setPrimaryClip(clip)
                    }) {
                        Icon(Icons.Filled.ContentCopy, contentDescription = "Copy original URL")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LinkHistoryItemPreview() {
    ShortenLinksTheme {
        LinkHistoryItem(
            shorten = Shorten(
                alias = "alias",
                links = Links(
                    self = "https://very-long-url.com/some-path/and-some-more/and-even-more/and-so-on",
                    shortUrl = "http://short.url/alias"
                )
            )
        )
    }
}
