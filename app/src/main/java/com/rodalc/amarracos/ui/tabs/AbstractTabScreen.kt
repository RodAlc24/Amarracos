package com.rodalc.amarracos.ui.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * A composable function that represents an abstract tab screen.
 *
 * This function is used as a template for creating different tab screens in the application.
 * It provides a common layout structure with a card containing the main content and two buttons:
 * "Empezar nueva partida" (Start new game) and "Cargar partida guardada" (Load saved game).
 *
 * @param modifier The modifier to be applied to the root Column. Defaults to Modifier.
 * @param canLoad A boolean indicating whether the "Load saved game" button should be enabled. Defaults to false.
 * @param onStartClick A lambda function to be executed when the "Start new game" button is clicked. Defaults to an empty lambda.
 * @param onLoadClick A lambda function to be executed when the "Load saved game" button is clicked. Defaults to an empty lambda.
 * @param content A composable lambda that defines the main content to be displayed within the card.
 */
@Composable
fun AbstractTabScreen(
    modifier: Modifier = Modifier,
    canLoad: Boolean = false,
    onStartClick: () -> Unit = {},
    onLoadClick: () -> Unit = {},
    content: @Composable () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.height(50.dp))
        Card {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                content()
                Spacer(Modifier.height(20.dp))
                Button(
                    onClick = { onStartClick() },
                    modifier = Modifier.fillMaxWidth(0.9f),
                ) { Text("Empezar nueva partida") }
            }
        }
        Spacer(Modifier.height(20.dp))
        OutlinedButton(
            onClick = { onLoadClick() },
            enabled = canLoad,
            modifier = Modifier.fillMaxWidth()
        ) { Text("Cargar partida guardada") }
    }
}
