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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow

/**
 * Barra de título para las pantallas.
 *
 * @param title El título de la pantalla
 * @param backButtonAction La acción a realizar al pulsar el botón de retroceso
 * @param actions Las acciones a realizar a la derecha de la barra de título
 */
@Composable
fun TitleTopBar(
    title: String,
    backButtonAction: (() -> Unit)? = null,
    actions: @Composable () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = MaterialTheme.typography.titleLarge.fontSize
            )
        },
        navigationIcon = {
            if (backButtonAction != null) {
                IconButton(onClick = { backButtonAction() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Navegar a la pantalla anterior",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        },
        actions = { actions() }
    )
}
