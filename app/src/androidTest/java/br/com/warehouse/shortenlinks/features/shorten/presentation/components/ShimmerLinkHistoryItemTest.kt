package br.com.warehouse.shortenlinks.features.shorten.presentation.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import br.com.warehouse.shortenlinks.ui.theme.ShortenLinksTheme
import org.junit.Rule
import org.junit.Test

class ShimmerLinkHistoryItemTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun givenShimmerLinkHistoryItem_whenRendered_thenIsDisplayed() {
        // Act
        composeTestRule.setContent {
            ShortenLinksTheme {
                ShimmerLinkHistoryItem()
            }
        }

        // Assert
        composeTestRule.onNodeWithTag("ShimmerLinkHistoryItem").assertIsDisplayed()
    }
}
