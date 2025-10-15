package br.com.warehouse.shortenlinks.features.shorten.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import br.com.warehouse.shortenlinks.MainActivity
import br.com.warehouse.shortenlinks.features.shorten.data.repository.FakeShortenRepository
import br.com.warehouse.shortenlinks.features.shorten.domain.repository.ShortenRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class ShortenScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var repository: ShortenRepository

    @Before
    fun setUp() {
        hiltRule.inject()
        (repository as FakeShortenRepository).setShouldReturnError(false)
    }

    @Test
    fun givenInitialState_whenScreenIsLoaded_thenShowsInputFieldAndEmptyHistory() {
        composeTestRule.onNodeWithText("Enter URL to shorten").assertIsDisplayed()
        composeTestRule.onNodeWithText("Recently shortened URLs").assertIsDisplayed()
    }

    @Test
    fun givenValidUrl_whenShortenClicked_thenShowsShortenedUrlInHistory() {
        // Arrange
        val url = "https://www.google.com"

        // Act
        composeTestRule.onNodeWithText("Enter URL to shorten").performTextInput(url)
        composeTestRule.onNodeWithText("Shorten").performClick()
        composeTestRule.waitForIdle()

        // Assert
        composeTestRule.onNodeWithText("http://short.url/fakeAlias1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Original URL").performClick()
        composeTestRule.onNode(hasText(url) and hasAnyAncestor(hasContentDescription("History item card"))).assertIsDisplayed()
    }

    @Test
    fun givenInvalidUrlFormat_whenShortenClicked_thenShowsValidationError() {
        // Arrange
        val url = "invalid-url"

        // Act
        composeTestRule.onNodeWithText("Enter URL to shorten").performTextInput(url)
        composeTestRule.onNodeWithText("Shorten").performClick()

        // Assert
        composeTestRule.onNodeWithText("Invalid URL").assertIsDisplayed()
    }

    @Test
    fun givenApiError_whenShortenClicked_thenShowsApiError() {
        // Arrange
        val url = "https://www.google.com"
        (repository as FakeShortenRepository).setShouldReturnError(true)

        // Act
        composeTestRule.onNodeWithText("Enter URL to shorten").performTextInput(url)
        composeTestRule.onNodeWithText("Shorten").performClick()

        // Assert
        composeTestRule.onNodeWithText("Invalid URL").assertIsDisplayed()
    }

    //    @Test
//    fun givenDuplicateUrl_whenShortenClicked_thenDoesNotAddDuplicateToHistory() {
//        // Arrange
//        val url = "https://www.google.com"
//
//        // Act
//        composeTestRule.onNodeWithText("Enter URL").performTextInput(url)
//        composeTestRule.onNodeWithContentDescription("Shorten").performClick()
//        composeTestRule.waitForIdle()
//        composeTestRule.onNodeWithContentDescription("Shorten").performClick()
//
//        // Assert
//        composeTestRule.onAllNodesWithContentDescription("History item card").assertCountEquals(1)
//    }

    //    @Test
//    fun givenLongHistory_whenScroll_thenScrolls() {
//        // Arrange
//        for (i in 1..5) {
//            composeTestRule.onNodeWithText("Enter URL").performTextInput("https://url$i.com")
//            composeTestRule.onNodeWithContentDescription("Shorten").performClick()
//            composeTestRule.waitForIdle()
//        }
//
//        // Act & Assert
//        composeTestRule.onNode(hasScrollAction()).performScrollToIndex(4)
//        composeTestRule.onNodeWithText("http://short.url/fakeAlias1").assertIsDisplayed()
//    }
}