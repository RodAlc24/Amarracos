package com.rodalc.amarracos.pocha

import android.content.Context
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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

@Composable
fun RecuperarDatos(context: Context, onClick: (Ronda) -> Unit) {
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
                            Pocha.load(context)
                            onClick(Ronda.APUESTAS)
                        }) {
                            Text(text = "Sí")
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Button(onClick = {
                            Pocha.discardBackup(context)
                            onClick(Ronda.NOMBRES)
                        }) {
                            Text(text = "No")
                        }
                    }
                }
            }
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
    var valorState by rememberSaveable { mutableIntStateOf(valor) }
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
                valorState -= 1
                modificar(valorState)
            },
            enabled = valorState > 0
        ) {
            Text("-")
        }
        Text(
            text = valorState.toString(),
            modifier = Modifier.padding(10.dp)
        )
        Button(onClick = {
            valorState += 1
            modificar(valorState)
        }
        ) {
            Text("+")
        }
    }
}

/**
 * Gestiona toda la pantalla para el conteo de la pocha.
 */
@Preview(
    showBackground = true,
    device = "spec:width=411dp,height=891dp,orientation=landscape"
)
@Composable
fun PantallaPocha() {
    val context = LocalContext.current
    var state by rememberSaveable { mutableStateOf(Ronda.NOMBRES) }
    var duplica by rememberSaveable { mutableStateOf(false) }
    var canLoad by rememberSaveable { mutableStateOf(Pocha.canLoad(context)) }
    var jugadores by remember { mutableStateOf(Pocha.getJugadores()) }

    if (canLoad) {
        RecuperarDatos(context) {
            state = it
            canLoad = false
            jugadores = Pocha.getJugadores()
        }
    } else {
        when (state) {
            Ronda.NOMBRES -> {
                Plantilla(
                    header = {
                        Text(text = "Jugadores ${jugadores.size}")
                        Spacer(modifier = Modifier.height(10.dp))
                        Row {
                            Button(
                                onClick = {
                                    jugadores = jugadores.dropLast(1).toMutableList()
                                },
                                enabled = jugadores.size > 2
                            ) {
                                Text(text = "-")
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Button(onClick = {
                                jugadores = (jugadores + Jugador(jugadores.size)).toMutableList()
                            }) {
                                Text(text = "+")
                            }
                        }
                    },
                    lineJugador = {
                        FilaNombre(
                            jugador = it,
                            numJugadores = jugadores.size,
                            context = context
                        )
                    },
                    nextRound = {
                        Pocha.setJugadores(jugadores)
                        Pocha.save(context)
                        state = Ronda.APUESTAS
                    },
                    undo = {},
                    undoEnabled = false,
                    jugadores = jugadores
                )
            }

            Ronda.APUESTAS -> {
                Plantilla(
                    header = {
                        Text(text = "Ronda de apuestas")
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(text = "Duplicar puntuación")
                            Spacer(modifier = Modifier.weight(1f))
                            Switch(checked = duplica, onCheckedChange = { duplica = it })
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    },
                    lineJugador = { jugador ->
                        FilaJugador(
                            texto = jugador.toString(),
                            puntos = jugador.puntos.toString(),
                            valor = jugador.apuesta
                        ) {
                            jugador.apuesta = it
                        }
                    },
                    nextRound = {
                        state = Ronda.CONTEO
                    },
                    undo = {
                        Pocha.popState()
                        jugadores = Pocha.getJugadores()
                        state = Ronda.CONTEO
                    },
                    undoEnabled = Pocha.canUndo(),
                    jugadores = jugadores
                )
            }

            Ronda.CONTEO -> {
                Plantilla(
                    header = {
                        Text(text = "Ronda de resultados")
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(text = "Duplicar puntuación")
                            Spacer(modifier = Modifier.weight(1f))
                            Switch(checked = duplica, onCheckedChange = { duplica = it })
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    },
                    lineJugador = { jugador ->
                        FilaJugador(
                            texto = "$jugador (${jugador.apuesta})",
                            puntos = jugador.puntos.toString(),
                            valor = jugador.victoria
                        ) {
                            jugador.victoria = it
                        }
                    },
                    nextRound = {
                        Pocha.pushState()
                        Pocha.actualizarPuntuacion(duplica)
                        duplica = false
                        Pocha.save(context)
                        state = Ronda.APUESTAS
                    },
                    undo = { state = Ronda.APUESTAS },
                    undoEnabled = true,
                    jugadores = jugadores
                )

            }
        }
    }

}

@Composable
fun FilaNombre(jugador: Jugador, numJugadores: Int, context: Context) {
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
            imeAction = if (jugador.id + 1 == numJugadores) ImeAction.Done else ImeAction.Next
        )
    )
}

@Composable
fun Plantilla(
    header: @Composable () -> Unit,
    lineJugador: @Composable (Jugador) -> Unit,
    nextRound: () -> Unit,
    undo: () -> Unit,
    undoEnabled: Boolean,
    jugadores: List<Jugador>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        header()
        Spacer(modifier = Modifier.height(10.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(jugadores) { jugador ->
                lineJugador(jugador)
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                modifier = Modifier.padding(10.dp),
                enabled = (undoEnabled),
                onClick = { undo() }) {
                Text(text = "Volver")
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                modifier = Modifier.padding(10.dp),
                onClick = { nextRound() }
            ) {
                Text("Aceptar")
            }
        }
    }
}