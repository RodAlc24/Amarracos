package com.rodalc.amarracos.pocha

import android.app.Activity
import android.content.pm.ActivityInfo
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.abs

@Preview(showBackground = true, widthDp = 640, heightDp = 360)
@Composable
fun PantallaPocha() {
    val activity = (LocalContext.current as Activity)
    activity.requestedOrientation =
        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT // TODO: Support landscape

    PochaNombres()

}

@Composable
fun PochaNombres() {
    var state by remember { mutableIntStateOf(0) }
    var jugadores by remember { mutableStateOf(listOf(Jugador(), Jugador())) }
    var oros by remember {mutableStateOf(false)}

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (state == 0) {
            Text(text = "Número de jugadores: ${jugadores.size}")
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
        } else if (state == 1) {
            Text(text = "Ronda de apuestas")
        } else {
            Text(text = "Ronda de resultados")
        }
        Spacer(modifier = Modifier.height(10.dp))
        if (state == 0) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                for (jugador in jugadores) {
                    val nombreState = remember { mutableStateOf(jugador.nombre) }
                    TextField(
                        value = nombreState.value,
                        onValueChange = { nuevoNombre ->
                            nombreState.value = nuevoNombre
                            jugador.nombre = nuevoNombre // Update the Jugador object's name
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        } else if (state == 1) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                for (jugador in jugadores) {
                    val apuestaState = remember { mutableIntStateOf(jugador.apuesta) }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "${jugador.nombre}: ${jugador.punots}")
                        Spacer(modifier = Modifier.weight(1f))
                        Button(
                            onClick = {
                                apuestaState.intValue -= 1
                                jugador.apuesta = apuestaState.intValue
                            },
                            enabled = apuestaState.intValue > 0
                        ) {
                            Text("-")
                        }
                        Text(
                            text = jugador.apuesta.toString(),
                            modifier = Modifier.padding(10.dp)
                        )
                        Button(onClick = {
                            apuestaState.intValue += 1
                            jugador.apuesta = apuestaState.intValue
                        }
                        ) {
                            Text("+")
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                for (jugador in jugadores) {
                    val victoriaState = remember { mutableIntStateOf(jugador.victoria) }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "${jugador.nombre}: ${jugador.punots} (${jugador.apuesta})")
                        Spacer(modifier = Modifier.weight(1f))
                        Button(
                            onClick = {
                                victoriaState.intValue -= 1
                                jugador.victoria = victoriaState.intValue
                            },
                            enabled = victoriaState.intValue > 0
                        ) {
                            Text("-")
                        }
                        Text(
                            text = jugador.victoria.toString(),
                            modifier = Modifier.padding(10.dp)
                        )
                        Button(onClick = {
                            victoriaState.intValue += 1
                            jugador.victoria = victoriaState.intValue
                        }
                        ) {
                            Text("+")
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        if (state == 0) {
            Button(onClick = { state = 1 }) {
                Text("Aceptar")
            }
        } else if (state == 1) {
            Button(onClick = { state = 2 }) {
                Text("Aceptar")
            }
        } else {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "Duplica: ")
                Spacer(modifier = Modifier.width(10.dp))
                Switch(checked = oros, onCheckedChange = {oros = it})
            }
            Button(onClick = {
                for ( jugador in jugadores) {
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
                oros = false
                state = 1 }
            ) {
                Text("Aceptar")
            }
        }
    }
}