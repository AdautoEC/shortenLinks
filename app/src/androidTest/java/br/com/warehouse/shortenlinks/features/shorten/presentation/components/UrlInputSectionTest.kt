package br.com.warehouse.shortenlinks.features.shorten.presentation.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import br.com.warehouse.shortenlinks.ui.theme.ShortenLinksTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class UrlInputSectionTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun givenUrlInputSection_whenRendered_thenDisplaysCorrectly() {
        // Act
        composeTestRule.setContent {
            ShortenLinksTheme {
                UrlInputSection(url = "", onUrlChange = {}, onShortenClick = {}, onClearClick = {})
            }
        }

        // Assert
        composeTestRule.onNodeWithText("Enter URL to shorten").assertIsDisplayed()
        composeTestRule.onNodeWithText("Shorten").assertIsDisplayed()
    }

    @Test
    fun givenUrlInputSection_whenTextEntered_thenCallsOnUrlChange() {
        // Arrange
        var changedUrl = ""
        val onUrlChange = { url: String -> changedUrl = url }

        // Act
        composeTestRule.setContent {
            ShortenLinksTheme {
                UrlInputSection(url = "", onUrlChange = onUrlChange, onShortenClick = {}, onClearClick = {})
            }
        }
        composeTestRule.onNodeWithText("Enter URL to shorten").performTextInput("https://google.com")

        // Assert
        assertEquals("https://google.com", changedUrl)
    }

    @Test
    fun givenUrlInputSection_whenShortenButtonClicked_thenCallsOnShortenClick() {
        // Arrange
        var clicked = false
        val onShortenClick = { clicked = true }

        // Act
        composeTestRule.setContent {
            ShortenLinksTheme {
                UrlInputSection(url = "https://google.com", onUrlChange = {}, onShortenClick = onShortenClick, onClearClick = {})
            }
        }
        composeTestRule.onNodeWithText("Shorten").performClick()

        // Assert
        assertEquals(true, clicked)
    }

    @Test
    fun givenUrlInputSection_whenClearButtonClicked_thenCallsOnClearClick() {
        // Arrange
        var clicked = false
        val onClearClick = { clicked = true }

        // Act
        composeTestRule.setContent {
            ShortenLinksTheme {
                UrlInputSection(url = "https://google.com", onUrlChange = {}, onShortenClick = {}, onClearClick = onClearClick)
            }
        }
        composeTestRule.onNodeWithContentDescription("Clear").performClick()

        // Assert
        assertEquals(true, clicked)
    }
}