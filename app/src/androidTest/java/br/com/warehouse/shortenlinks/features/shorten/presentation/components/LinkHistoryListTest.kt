package br.com.warehouse.shortenlinks.features.shorten.presentation.components

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithText
import br.com.warehouse.shortenlinks.features.shorten.domain.model.Links
import br.com.warehouse.shortenlinks.features.shorten.domain.model.Shorten
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class LinkHistoryListTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenHistoryIsNotEmpty_thenDisplayHistoryItems() {
        val history = listOf(
            Shorten("alias1", Links("self1", "short1")),
            Shorten("alias2", Links("self2", "short2"))
        )

        composeTestRule.setContent {
            LinkHistoryList(history = history, isLoading = false)
        }

        composeTestRule.onNodeWithText("short1").assertExists()
        composeTestRule.onNodeWithText("short2").assertExists()
    }

    @Test
    fun whenHistoryIsEmptyAndLoading_thenDisplayShimmerItems() {
        composeTestRule.setContent {
            LinkHistoryList(history = emptyList(), isLoading = true)
        }

        composeTestRule.onAllNodesWithTag("ShimmerLinkHistoryItem").fetchSemanticsNodes().size.let { count ->
            Assert.assertEquals(5, count)
        }
    }

    @Test
    fun whenHistoryIsNotEmptyAndLoading_thenDisplayHistoryItems() {
        val history = listOf(
            Shorten("alias1", Links("self1", "short1")),
        )

        composeTestRule.setContent {
            LinkHistoryList(history = history, isLoading = true)
        }

        composeTestRule.onNodeWithText("short1").assertExists()
        composeTestRule.onAllNodesWithTag("ShimmerLinkHistoryItem").fetchSemanticsNodes().size.let { count ->
            Assert.assertEquals(0, count)
        }
    }
}
