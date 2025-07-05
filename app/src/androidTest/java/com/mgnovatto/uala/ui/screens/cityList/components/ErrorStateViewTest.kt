package com.mgnovatto.uala.ui.screens.cityList.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.mgnovatto.uala.ui.theme.UalaChallengeTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

/**
 * UI tests for the [ErrorStateView] Composable.
 */
class ErrorStateViewTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun givenAnErrorMessage_whenErrorStateViewIsDisplayed_thenMessageAndButtonAreShown() {
        // Given
        val errorMessage = "Error de prueba"

        // When
        composeTestRule.setContent {
            UalaChallengeTheme {
                ErrorStateView(message = errorMessage, onRetry = {})
            }
        }

        // Then
        // Verify that the error message is displayed
        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
        // Verify that the retry button is displayed
        composeTestRule.onNodeWithText("Reintentar").assertIsDisplayed()
    }

    @Test
    fun whenRetryButtonIsClicked_thenOnRetryCallbackIsCalled() {
        // Given
        var wasRetryClicked = false
        val errorMessage = "Error de prueba"

        composeTestRule.setContent {
            UalaChallengeTheme {
                ErrorStateView(
                    message = errorMessage,
                    onRetry = { wasRetryClicked = true }
                )
            }
        }

        // When
        // Find the button by its text and perform a click
        composeTestRule.onNodeWithText("Reintentar").performClick()

        // Then
        // Verify that our flag was set to true, meaning the callback was invoked
        assertTrue("The onRetry callback should have been called", wasRetryClicked)
    }
}
