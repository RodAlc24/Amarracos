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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rodalc.amarracos.R
import com.rodalc.amarracos.data.generico.JugadorGenericoUiState
import com.rodalc.amarracos.data.tabs.TabViewModel
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
    isPocha: Boolean,
    modifier: Modifier = Modifier,
    canLoad: Boolean = false,
    tabViewmodel: TabViewModel = viewModel(),
    onStartClick: (List<JugadorGenericoUiState>) -> Unit = {},
    onLoadClick: () -> Unit = {},
) {
    val context = LocalContext.current
    val uiState by tabViewmodel.uiState.collectAsState()

    val jugadores = if (isPocha) uiState.jugadoresPocha else uiState.jugadoresGenerico
    val defaultPlayerName = stringResource(R.string.default_player_name_format)

    AbstractTabScreen(
        modifier = modifier,
        canLoad = canLoad,
        onStartClick = {
            onStartClick(
                jugadores.map { jugador ->
                    JugadorGenericoUiState(
                        id = jugador.id,
                        nombre = jugador.nombre.ifBlank { defaultPlayerName.format(jugador.id) }
                    )
                }
            )
        },
        onLoadClick = onLoadClick,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = stringResource(R.string.text_players))
            Spacer(Modifier.weight(1f))
            IconButton(
                onClick = { tabViewmodel.removeLast(isPocha) },
                enabled = jugadores.size > 2,
                colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(
                    Icons.Filled.Remove,
                    contentDescription = stringResource(R.string.desc_remove_player)
                )
            }
            Box(
                modifier = Modifier.width(30.dp),
                contentAlignment = Alignment.Center
            ) { Text(text = jugadores.size.toString()) }
            IconButton(
                onClick = { tabViewmodel.addPlayer(isPocha) },
                enabled = jugadores.size < 100,
                colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = stringResource(R.string.desc_add_player)
                )
            }
        }
        for (jugador in jugadores) {
            OutlinedTextField(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(0.9f),
                value = jugador.nombre,
                onValueChange = {
                    if (it.length <= 10) {
                        tabViewmodel.changeName(isPocha, jugador.id, it)
                    } else ToastRateLimiter.showToast(
                        context,
                        context.getString(R.string.toast_name_too_long)
                    )
                },
                maxLines = 1,
                placeholder = { Text(text = defaultPlayerName.format(jugador.id)) },
                keyboardOptions = KeyboardOptions(imeAction = if (jugador.id == jugadores.size) ImeAction.Done else ImeAction.Next),
            )
        }
    }
}

@Preview(showSystemUi = false, showBackground = false)
@Composable
fun PreviewPochaTabScreen() {
    AmarracosTheme(darkTheme = true) {
        GenericoTabScreen(isPocha = true)
    }
}