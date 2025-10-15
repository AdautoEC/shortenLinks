package br.com.warehouse.shortenlinks.features.shorten.presentation

import android.content.pm.ActivityInfo
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import br.com.warehouse.shortenlinks.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class ShortenScreenRotationTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun whenDeviceIsRotated_thenUrlInputSectionIsVisible() {
        composeTestRule.onNodeWithText("Enter URL to shorten").assertExists()

        // Rotate to landscape
        composeTestRule.activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        composeTestRule.onNodeWithText("Enter URL to shorten").assertExists()

        // Rotate back to portrait
        composeTestRule.activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        composeTestRule.onNodeWithText("Enter URL to shorten").assertExists()
    }
}
