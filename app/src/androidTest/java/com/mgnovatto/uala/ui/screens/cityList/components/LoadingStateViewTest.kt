package com.mgnovatto.uala.ui.screens.cityList.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.mgnovatto.uala.ui.theme.UalaChallengeTheme
import org.junit.Rule
import org.junit.Test

/**
 * UI tests for the [LoadingStateView] Composable.
 */
class LoadingStateViewTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenLoadingStateViewIsDisplayed_thenTextAndIndicatorAreShown() {
        // When: The LoadingStateView is set as the content.
        composeTestRule.setContent {
            UalaChallengeTheme {
                LoadingStateView()
            }
        }

        // Then: Verify that the loading text is displayed.
        composeTestRule.onNodeWithTag("loading_text").assertIsDisplayed()

        // And verify that the progress indicator is displayed using a robust testTag.
        composeTestRule.onNodeWithTag("loading_indicator").assertIsDisplayed()
    }
}
