package br.com.warehouse.shortenlinks.features.shorten.presentation.components

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test
import io.mockk.mockk
import io.mockk.verify

class ErrorDialogTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenDialogIsDisplayed_thenTitleAndMessageAreCorrect() {
        val title = "Error Title"
        val message = "Error Message"

        composeTestRule.setContent {
            ErrorDialog(title = title, message = message, onDismiss = {})
        }

        composeTestRule.onNodeWithText(title).assertExists()
        composeTestRule.onNodeWithText(message).assertExists()
    }

    @Test
    fun whenDismissButtonIsClicked_thenOnDismissIsCalled() {
        val onDismiss = mockk<() -> Unit>(relaxed = true)

        composeTestRule.setContent {
            ErrorDialog(title = "Title", message = "Message", onDismiss = onDismiss)
        }

        composeTestRule.onNodeWithText("OK").performClick()

        verify { onDismiss() }
    }
}
