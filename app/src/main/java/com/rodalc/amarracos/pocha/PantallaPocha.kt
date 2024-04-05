package com.rodalc.amarracos.pocha

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.abs

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun PantallaPocha() {
    var state by remember { mutableStateOf(Ronda.NOMBRES) }
    var jugadores by remember { mutableStateOf(listOf(Jugador(), Jugador())) }
    var oros by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val texto = when (state) {
            Ronda.NOMBRES -> "Número de jugadores: ${jugadores.size}"
            Ronda.APUESTAS -> "Ronda de apuestas"
            Ronda.CONTEO -> "Ronda de resultados"
        }
        Text(text = texto)
        if (state == Ronda.NOMBRES) {
            Row {
                Button(
                    onClick = {
                        jugadores = jugadores.dropLast(1)
                    },
                    enabled = jugadores.size > 2
                ) {
                    Text(text = "-")
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(onClick = {
                    jugadores += Jugador()
                }) {
                    Text(text = "+")
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            for (jugador in jugadores) {
                if (state == Ronda.NOMBRES) {
                    val nombreState = remember { mutableStateOf(jugador.nombre) }
                    TextField(
                        value = nombreState.value,
                        onValueChange = { nuevoNombre ->
                            nombreState.value = nuevoNombre
                            jugador.nombre = nuevoNombre
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                    )
                } else {
                    var valorState by remember { mutableIntStateOf(0) }
                    valorState = if (state == Ronda.APUESTAS) jugador.apuesta else jugador.victoria
                    val textoFila =
                        if (state == Ronda.APUESTAS) "${jugador.nombre}: ${jugador.punots}" else "${jugador.nombre}: ${jugador.punots} (${jugador.apuesta})"

                    FilaJugador(
                        texto = textoFila,
                        valor = valorState
                    ) {
                        valorState = it
                        if (state == Ronda.APUESTAS)
                            jugador.apuesta = valorState
                        else
                            jugador.victoria = valorState
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
        if (state == Ronda.CONTEO) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "Duplica: ")
                Spacer(modifier = Modifier.width(10.dp))
                Switch(checked = oros, onCheckedChange = { oros = it })
            }
        }
        Button(onClick = {
            state = when (state) {
                Ronda.NOMBRES -> Ronda.APUESTAS
                Ronda.APUESTAS -> Ronda.CONTEO
                Ronda.CONTEO -> {
                    for (jugador in jugadores) {
                        val incremento = if (jugador.apuesta == jugador.victoria) {
                            10 + 5 * jugador.apuesta
                        } else {
                            -5 * abs(jugador.apuesta - jugador.victoria)
                        }
                        jugador.punots += if (oros) 2 * incremento else incremento
                        jugador.apuesta = 0
                        jugador.victoria = 0
                    }
                    Ronda.APUESTAS
                }
            }
        }) {
            Text("Aceptar")
        }
    }
}

@Composable
fun FilaJugador(texto: String, valor: Int, modificar: (Int) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = texto)
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {
                modificar(valor - 1)
            },
            enabled = valor > 0
        ) {
            Text("-")
        }
        Text(
            text = valor.toString(),
            modifier = Modifier.padding(10.dp)
        )
        Button(onClick = {
            modificar(valor + 1)
        }
        ) {
            Text("+")
        }
    }
}