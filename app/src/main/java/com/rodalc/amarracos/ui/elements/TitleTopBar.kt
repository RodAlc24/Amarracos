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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.rodalc.amarracos.ui.theme.AmarracosTheme

/**
 * Barra de título para las pantallas.
 *
 * @param title El título de la pantalla
 * @param upButtonOnClick La acción a realizar al pulsar el botón de retroceso
 * @param actions Las acciones a realizar a la derecha de la barra de título
 */
@Composable
fun TitleTopBar(
    title: String,
    showUpButton: Boolean = false,
    upButtonOnClick: () -> Unit = {},
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
                IconButton(onClick = { upButtonOnClick() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Navegar a la pantalla anterior",
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
            upButtonOnClick = {},
            actions = {}
        )
    }
}