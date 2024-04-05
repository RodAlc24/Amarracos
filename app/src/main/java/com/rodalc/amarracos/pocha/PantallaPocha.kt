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

    when (state) {
        Ronda.NOMBRES -> PochaInicio(jugadores = jugadores) {
            jugadores = it
            state = Ronda.APUESTAS
        }

        Ronda.APUESTAS -> PochaApuesta(jugadores = jugadores) {
            jugadores = it
            state = Ronda.CONTEO
        }

        Ronda.CONTEO -> PochaResultados(jugadores = jugadores) {
            jugadores = it
            state = Ronda.APUESTAS
        }
    }
}

@Composable
fun PochaInicio(jugadores: List<Jugador>, salir: (List<Jugador>) -> Unit) {
    var stateJugadores by remember { mutableStateOf(jugadores) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "Número de jugadores: ${stateJugadores.size}")
        Row {
            Button(
                onClick = {
                    stateJugadores = stateJugadores.dropLast(1)
                },
                enabled = stateJugadores.size > 2
            ) {
                Text(text = "-")
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(onClick = {
                stateJugadores += Jugador()
            }) {
                Text(text = "+")
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            for (jugador in stateJugadores) {
                val nombreState = remember { mutableStateOf(jugador.nombre) }
                TextField(
                    value = nombreState.value,
                    onValueChange = { nuevoNombre ->
                        nombreState.value = nuevoNombre
                        jugador.nombre = nuevoNombre
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {
            salir(stateJugadores)
        }) {
            Text("Aceptar")
        }
    }
}

@Composable
fun PochaApuesta(jugadores: List<Jugador>, salir: (List<Jugador>) -> Unit) {
    val stateJugadores by remember { mutableStateOf(jugadores) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "Ronda de apuestas")
        Spacer(modifier = Modifier.height(10.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            for (jugador in stateJugadores) {
                var apuestaState by remember { mutableIntStateOf(jugador.apuesta) }
                FilaJugador(
                    texto = "${jugador.nombre}: ${jugador.punots}",
                    valor = apuestaState
                ) {
                    apuestaState = it
                    jugador.apuesta = apuestaState
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {
            salir(stateJugadores)
        }) {
            Text("Aceptar")
        }
    }
}

@Composable
fun PochaResultados(jugadores: List<Jugador>, salir: (List<Jugador>) -> Unit) {
    val stateJugadores by remember { mutableStateOf(jugadores) }
    var oros by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "Ronda de resultados")

        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            for (jugador in stateJugadores) {
                var victoriaState by remember { mutableIntStateOf(jugador.victoria) }
                FilaJugador(
                    texto = "${jugador.nombre}: ${jugador.punots} (${jugador.apuesta})",
                    valor = victoriaState
                ) {
                    victoriaState = it
                    jugador.victoria = victoriaState
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Duplica: ")
            Spacer(modifier = Modifier.width(10.dp))
            Switch(checked = oros, onCheckedChange = { oros = it })
        }
        Button(onClick = {
            for (jugador in stateJugadores) {
                var incremento = if (jugador.apuesta == jugador.victoria) {
                    10 + 5 * jugador.apuesta
                } else {
                    -5 * abs(jugador.apuesta - jugador.victoria)
                }
                if (oros) incremento *= 2

                jugador.punots += incremento
                jugador.apuesta = 0
                jugador.victoria = 0
            }
            salir(stateJugadores)
        }
        ) {
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