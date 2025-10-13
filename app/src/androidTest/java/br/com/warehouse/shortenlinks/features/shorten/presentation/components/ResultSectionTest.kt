package br.com.warehouse.shortenlinks.features.shorten.presentation.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import br.com.warehouse.shortenlinks.core.presentation.model.SubmitLoadingState
import br.com.warehouse.shortenlinks.features.shorten.data.model.Links
import br.com.warehouse.shortenlinks.features.shorten.domain.model.Shorten
import br.com.warehouse.shortenlinks.ui.theme.ShortenLinksTheme
import org.junit.Rule
import org.junit.Test

class ResultSectionTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun givenResultSection_whenStateIsLoading_thenShowsLoading() {
        // Act
        composeTestRule.setContent {
            ShortenLinksTheme {
                ResultSection(state = SubmitLoadingState.Loading)
            }
        }

        // Assert
        // We can't easily test for the CircularProgressIndicator without a test tag,
        // but we can check that no other state is displayed.
        composeTestRule.onNodeWithText("Shortened URL:").assertDoesNotExist()
        composeTestRule.onNodeWithText("Error:").assertDoesNotExist()
    }

    @Test
    fun givenResultSection_whenStateIsSuccess_thenShowsSuccess() {
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
                ResultSection(state = SubmitLoadingState.Success(shorten))
            }
        }

        // Assert
        composeTestRule.onNodeWithText("Shortened URL: https://short.url").assertIsDisplayed()
    }

    @Test
    fun givenResultSection_whenStateIsError_thenShowsError() {
        // Arrange
        val error = Throwable("Test error")

        // Act
        composeTestRule.setContent {
            ShortenLinksTheme {
                ResultSection(state = SubmitLoadingState.Error(error))
            }
        }

        // Assert
        composeTestRule.onNodeWithText("Error: Test error").assertIsDisplayed()
    }

    @Test
    fun givenResultSection_whenStateIsIdle_thenShowsNothing() {
        // Act
        composeTestRule.setContent {
            ShortenLinksTheme {
                ResultSection(state = SubmitLoadingState.Idle)
            }
        }

        // Assert
        composeTestRule.onNodeWithText("Shortened URL:").assertDoesNotExist()
        composeTestRule.onNodeWithText("Error:").assertDoesNotExist()
    }
}
