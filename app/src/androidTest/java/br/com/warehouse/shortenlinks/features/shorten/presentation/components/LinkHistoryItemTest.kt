package br.com.warehouse.shortenlinks.features.shorten.presentation.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import br.com.warehouse.shortenlinks.features.shorten.data.model.Links
import br.com.warehouse.shortenlinks.features.shorten.domain.model.Shorten
import br.com.warehouse.shortenlinks.ui.theme.ShortenLinksTheme
import org.junit.Rule
import org.junit.Test

class LinkHistoryItemTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun givenLinkHistoryItem_whenRendered_thenShowsShortenedUrl() {
        // Arrange
        val shorten = Shorten(
            alias = "alias",
            links = Links(
                self = "https://long.url",
                shortUrl = "https://short.url"
            )
        )

        // Act
        composeTestRule.setContent {
            ShortenLinksTheme {
                LinkHistoryItem(shorten = shorten)
            }
        }

        // Assert
        composeTestRule.onNodeWithText("https://short.url").assertIsDisplayed()
        composeTestRule.onNodeWithText("https://long.url").assertDoesNotExist()
    }

    @Test
    fun givenLinkHistoryItem_whenExpanded_thenShowsOriginalUrl() {
        // Arrange
        val shorten = Shorten(
            alias = "alias",
            links = Links(
                self = "https://long.url",
                shortUrl = "https://short.url"
            )
        )

        // Act
        composeTestRule.setContent {
            ShortenLinksTheme {
                LinkHistoryItem(shorten = shorten)
            }
        }
        composeTestRule.onNodeWithText("Original URL").performClick()

        // Assert
        composeTestRule.onNodeWithText("https://short.url").assertIsDisplayed()
        composeTestRule.onNodeWithText("https://long.url").assertIsDisplayed()
    }
}
