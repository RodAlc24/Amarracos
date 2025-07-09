@file:OptIn(ExperimentalMaterial3Api::class)

package com.rodalc.amarracos.ui.elements

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.rodalc.amarracos.R
import com.rodalc.amarracos.ui.theme.AmarracosTheme

/**
 * Title bar for screens.
 *
 * @param title The title of the screen.
 * @param showUpButton Whether to show the up button.
 * @param onUpButtonClick The action to perform when the up button is clicked.
 * @param actions The actions to perform on the right side of the title bar.
 */
@Composable
fun TitleTopBar(
    title: String,
    showUpButton: Boolean = false,
    onUpButtonClick: () -> Unit = {},
    actions: @Composable () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleLarge,
            )
        },
        navigationIcon = {
            if (showUpButton) {
                IconButton(onClick = { onUpButtonClick() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.desc_up_button),
                    )
                }
            }
        },
        actions = { actions() }
    )
}

@Preview
@Composable
fun PreviewTitleTopBar() {
    AmarracosTheme {
        TitleTopBar(
            title = "Amarracos",
            showUpButton = true,
            onUpButtonClick = {},
            actions = {}
        )
    }
}