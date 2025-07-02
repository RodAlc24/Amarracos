package com.rodalc.amarracos.ui.tabs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rodalc.amarracos.data.generico.JugadorGenericoUiState
import com.rodalc.amarracos.ui.theme.AmarracosTheme
import com.rodalc.amarracos.utils.ToastRateLimiter

/**
 * Composable function for the Generico and Pocha tab screen.
 * This screen allows users to configure and start a new Generico or Pocha game or load a saved one.
 *
 * @param modifier A [Modifier] for this composable. Defaults to [Modifier].
 * @param canLoad A boolean indicating whether a saved game can be loaded. Defaults to false.
 * @param onStartClick A lambda function to be invoked when the "Empezar nueva partida" (Start new game) button is clicked. Defaults to an empty lambda.
 * @param onLoadClick A lambda function to be invoked when the "Cargar partida guardada" (Load saved game) button is clicked. Defaults to an empty lambda.
 */
@Composable
fun GenericoTabScreen(
    modifier: Modifier = Modifier,
    canLoad: Boolean = false,
    onStartClick: (List<JugadorGenericoUiState>) -> Unit = {},
    onLoadClick: () -> Unit = {},
) {
    val context = LocalContext.current

    var jugadores by rememberSaveable { mutableStateOf(listOf(Pair(1, ""), Pair(2, "")))}

    AbstractTabScreen(
        modifier = modifier,
        canLoad = canLoad,
        onStartClick = {
            onStartClick(
                jugadores.map { JugadorGenericoUiState(id = it.first, nombre = it.second.ifBlank { "Jugador ${it.first}" }) }
            )
        },
        onLoadClick = onLoadClick,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = "Jugadores:")
            Spacer(Modifier.weight(1f))
            IconButton(
                onClick = { jugadores = jugadores.dropLast(1) },
                enabled = jugadores.size > 2,
                colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(Icons.Filled.Remove, contentDescription = "Quitar jugador")
            }
            Box(
                modifier = Modifier.width(30.dp),
                contentAlignment = Alignment.Center
            ) { Text(text = jugadores.size.toString()) }
            IconButton(
                onClick = {
                    jugadores = jugadores + Pair(jugadores.size + 1, "")
                },
                enabled = jugadores.size < 100,
                colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Añadir jugador")
            }
        }
        for (index in jugadores.indices) {
            var nombre by rememberSaveable { mutableStateOf(jugadores[index].second) }
            OutlinedTextField(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(0.9f),
                value = nombre,
                onValueChange = {
                    if (it.length <= 10) {
                        jugadores = jugadores.toMutableList()
                            .apply { this[index] = this[index].copy(second = it) }
                        nombre = it
                    } else ToastRateLimiter.showToast(context, "¡Pon un nombre más corto!")
                },
                maxLines = 1,
                placeholder = { Text(text = "Jugador ${jugadores[index].first}") },
                keyboardOptions = KeyboardOptions(imeAction = if (index == jugadores.size - 1) ImeAction.Done else ImeAction.Next),
            )
        }
    }
}

@Preview(showSystemUi = false, showBackground = false)
@Composable
fun PreviewPochaTabScreen() {
    AmarracosTheme(darkTheme = true) {
        GenericoTabScreen()
    }
}