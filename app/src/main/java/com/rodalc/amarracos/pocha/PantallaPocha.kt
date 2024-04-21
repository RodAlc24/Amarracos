package com.rodalc.amarracos.pocha

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.rodalc.amarracos.storage.StateSaver
import kotlin.math.abs

/**
 * Gestiona toda la pantalla para el conteo de la pocha.
 */
@Preview(
    showBackground = true,
    device = "spec:width=411dp,height=891dp,orientation=landscape"
)
@Composable
fun PantallaPocha() {
    var state by rememberSaveable { mutableStateOf(Ronda.NOMBRES) }
    var jugadores by rememberSaveable { mutableStateOf(listOf(Jugador(1), Jugador(2))) }
    var duplica by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    var showRecoever by remember { mutableStateOf(StateSaver.fileExist(context)) }

    if (showRecoever) {
        Dialog(onDismissRequest = { }) {
            Box(modifier = Modifier) {
                Surface(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(10.dp),
                    shape = RoundedCornerShape(16.dp),
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "¿Recuperar la última partida?")
                        Row(
                            modifier = Modifier.padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Button(onClick = {
                                jugadores = StateSaver.loadPocha(context)
                                showRecoever = false
                                state = Ronda.APUESTAS

                            }) {
                                Text(text = "Sí")
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Button(onClick = {
                                StateSaver.deleteFile(context)
                                showRecoever = false
                            }) {
                                Text(text = "No")
                            }
                        }
                    }
                }
            }
        }
    }

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
                    jugadores += Jugador(jugadores.size + 1)
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
                    var nombreState by rememberSaveable { mutableStateOf(jugador.nombre) }
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = nombreState,
                        onValueChange = {
                            if (it.length <= 20) {
                                nombreState = it
                                jugador.nombre = nombreState
                            } else ToastRateLimiter.showToast(context, "¡Pon un nombre más corto!")
                        },
                        maxLines = 1,
                        label = { Text("Jugador ${jugador.id}") },
                        keyboardOptions = KeyboardOptions(
                            imeAction = if (jugadores.last() == jugador) ImeAction.Done else ImeAction.Next
                        )
                    )
                } else {
                    var valorState by rememberSaveable { mutableIntStateOf(0) }
                    valorState = if (state == Ronda.APUESTAS) jugador.apuesta else jugador.victoria
                    val textoFila = jugador.nombre
                    val puntos =
                        if (state == Ronda.APUESTAS) "${jugador.punots}" else "(${jugador.apuesta}) ${jugador.punots}"

                    FilaJugador(
                        texto = textoFila,
                        puntos = puntos,
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
                Switch(checked = duplica, onCheckedChange = { duplica = it })
            }
        }
        Button(onClick = {
            state = when (state) {
                Ronda.NOMBRES -> {
                    for (jugador in jugadores) {
                        if (jugador.nombre == "") jugador.nombre = "Jugador ${jugador.id}"
                    }
                    Ronda.APUESTAS
                }

                Ronda.APUESTAS -> Ronda.CONTEO
                Ronda.CONTEO -> {
                    for (jugador in jugadores) {
                        val incremento = if (jugador.apuesta == jugador.victoria) {
                            10 + 5 * jugador.apuesta
                        } else {
                            -5 * abs(jugador.apuesta - jugador.victoria)
                        }
                        jugador.punots += if (duplica) 2 * incremento else incremento
                        jugador.apuesta = 0
                        jugador.victoria = 0
                    }
                    duplica = false
                    StateSaver.savePocha(context, jugadores)
                    Ronda.APUESTAS
                }
            }
        }) {
            Text("Aceptar")
        }
    }
}

/**
 * Crea una fila con la información de un jugador, además de unos botones de - y + para aumentar y disminuir el valor correspondiente.
 *
 * @param texto El texto que se muestra a la izquierda, suele ser el nombre y los puntos.
 * @param valor La cantidad que se quiere modificar, ya sean las apuestas o las victorias.
 * @param modificar Recibe como parámetro el nuevo valor de la variable recibida.
 */
@Composable
fun FilaJugador(texto: String, puntos: String, valor: Int, modificar: (Int) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = texto,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f) // Allocate space for the text
                .clipToBounds()
        )
        Text(
            text = puntos,
            modifier = Modifier.padding(10.dp)
        )
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