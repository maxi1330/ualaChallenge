package com.mgnovatto.uala.ui.screens.cityList.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mgnovatto.uala.R


/**
 * A composable that displays a loading indicator and a loading message while the initial data is being downloaded.
 *
 * @param modifier Modifier to be applied to the root layout.
 */
@Composable
fun LoadingStateView(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize().testTag("LoadingStateView"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(stringResource(R.string.downloading_initial_data))
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator(
                modifier = Modifier.testTag("loading_indicator")
            )
        }
    }
}