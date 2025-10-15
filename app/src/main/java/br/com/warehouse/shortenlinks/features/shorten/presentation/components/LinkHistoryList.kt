package br.com.warehouse.shortenlinks.features.shorten.presentation.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.com.warehouse.shortenlinks.features.shorten.domain.model.Shorten

/**
 * A composable that displays a list of shortened links.
 *
 * It shows a shimmer loading animation when the list is empty and loading.
 * Otherwise, it displays the list of [LinkHistoryItem]s.
 *
 * @param history The list of shortened links to display.
 * @param isLoading A boolean to indicate if the list is currently loading.
 * @param modifier The modifier to be applied to the composable.
 */
@Composable
fun LinkHistoryList(
    history: List<Shorten>,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    if (isLoading && history.isEmpty()) {
        LazyColumn(modifier = modifier) {
            items(5) {
                ShimmerLinkHistoryItem()
            }
        }
    } else {
        LazyColumn(modifier = modifier) {
            items(history, key = { it.alias }) { shorten ->
                LinkHistoryItem(shorten = shorten)
            }
        }
    }
}
