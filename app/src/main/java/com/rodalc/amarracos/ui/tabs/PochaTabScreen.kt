package com.rodalc.amarracos.ui.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rodalc.amarracos.comun.Jugador
import com.rodalc.amarracos.storage.DataStoreManager
import com.rodalc.amarracos.ui.theme.AmarracosTheme
import com.rodalc.amarracos.utils.ToastRateLimiter
import kotlinx.coroutines.async

@Composable
fun PochaTabScreen(
    jugadores: List<Jugador>,
    modifier: Modifier = Modifier,
    canLoad: Boolean = false,
    addJugador: () -> Unit = {},
    removeJugador: () -> Unit = {},
    onStartClick: () -> Unit = {},
    onLoadClick: () -> Unit = {},
) {
    val context = LocalContext.current

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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = "Jugadores:")
                    Spacer(Modifier.weight(1f))
                    IconButton(onClick = { removeJugador() }, enabled = jugadores.size > 2) {
                        Icon(Icons.Filled.Remove, contentDescription = "Quitar jugador")
                    }
                    Box(
                        modifier = Modifier.width(30.dp),
                        contentAlignment = Alignment.Center
                    ) { Text(text = jugadores.size.toString()) }
                    IconButton(onClick = { addJugador() }, enabled = jugadores.size < 100) {
                        Icon(Icons.Filled.Add, contentDescription = "Añadir jugador")
                    }

                }
                for (index in jugadores.indices) {
                    var nombre by rememberSaveable { mutableStateOf(jugadores[index].nombre) }
                    OutlinedTextField(
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth(0.9f),
                        value = nombre,
                        onValueChange = {
                            if (it.length <= 10) {
                                jugadores[index].nombre = it
                                nombre = it
                            } else ToastRateLimiter.showToast(context, "¡Pon un nombre más corto!")
                        },
                        maxLines = 1,
                        placeholder = { Text(text = "Jugador ${jugadores[index].id}") },
                        keyboardOptions = KeyboardOptions(imeAction = if (index == jugadores.size - 1) ImeAction.Done else ImeAction.Next),
                    )
                }
                Spacer(Modifier.height(20.dp))
                Button(
                    onClick = {
                        onStartClick()
                    },
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

@Preview(showSystemUi = false, showBackground = false)
@Composable
fun PreviewPochaTabScreen() {
    AmarracosTheme(darkTheme = true) {
        PochaTabScreen(jugadores = listOf(Jugador(1), Jugador(2), Jugador(3)))
    }
}