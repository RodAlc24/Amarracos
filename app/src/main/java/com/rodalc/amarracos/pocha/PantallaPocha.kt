package com.rodalc.amarracos.pocha

import android.content.Context
import android.view.WindowManager
import androidx.activity.ComponentActivity
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.rodalc.amarracos.main.ToastRateLimiter
import com.rodalc.amarracos.storage.DataStoreManager

/**
 * Gestiona toda la pantalla para el juego de la pocha.
 */
@Preview(
    showBackground = true,
    device = "spec:width=411dp,height=891dp,orientation=landscape"
)
@Composable
fun PantallaPocha() {
    val context = LocalContext.current
    var state by rememberSaveable { mutableStateOf(Ronda.NOMBRES) }
    var duplica by rememberSaveable { mutableStateOf(Pocha.getDuplica()) }
    var canLoad by rememberSaveable { mutableStateOf(Pocha.canLoadState(context)) }
    var jugadores by remember { mutableStateOf(Pocha.getJugadores()) }

    // Keep screen on. Only if user has selected it
    val screenState by DataStoreManager.readDataStore(context, DataStoreManager.Keys.KEEP_SCREEN_ON)
        .collectAsState(initial = true)
    if (screenState) {
        val activity = context as? ComponentActivity

        SideEffect {
            activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }

        DisposableEffect(Unit) {
            onDispose {
                activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
        }
    }

    if (canLoad) {
        RecuperarDatos(context) {
            state = it
            jugadores = Pocha.getJugadores()
            canLoad = false
        }
    } else {
        when (state) {
            Ronda.NOMBRES -> {
                Plantilla(
                    header = {
                        Text(
                            text = "Jugadores ${jugadores.size}",
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row {
                            Button(
                                onClick = { jugadores = jugadores.dropLast(1).toMutableList() },
                                enabled = jugadores.size > 2
                            ) { Text(text = "-") }
                            Spacer(modifier = Modifier.width(10.dp))
                            Button(onClick = {
                                jugadores = (jugadores + Jugador(jugadores.size)).toMutableList()
                            }) { Text(text = "+") }
                        }
                    },
                    lineJugador = {
                        FilaJugadorNombres(
                            jugador = it,
                            numJugadores = jugadores.size,
                            context = context
                        )
                    },
                    nextRound = {
                        Pocha.setJugadores(jugadores)
                        Pocha.saveState(context)
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
                        Text(
                            text = "Ronda de apuestas",
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth(0.9f)
                        ) {
                            Text(text = "Duplicar puntuación")
                            Spacer(modifier = Modifier.weight(1f))
                            Switch(checked = duplica, onCheckedChange = { duplica = it })
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
                        duplica = Pocha.getDuplica()
                        state = Ronda.CONTEO
                    },
                    undoEnabled = Pocha.canUndo(),
                    jugadores = Pocha.getJugadores()
                )
            }

            Ronda.CONTEO -> {
                Plantilla(
                    header = {
                        Text(
                            text = "Ronda de resultados",
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth(0.9f)
                        ) {
                            Text(text = "Duplicar puntuación")
                            Spacer(modifier = Modifier.weight(1f))
                            Switch(checked = duplica, onCheckedChange = { duplica = it })
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
                        Pocha.setDuplica(duplica)
                        Pocha.pushState()
                        Pocha.actualizarPuntuacion(duplica)
                        duplica = false
                        Pocha.saveState(context)
                        state = Ronda.APUESTAS
                    },
                    undo = { state = Ronda.APUESTAS },
                    undoEnabled = true,
                    jugadores = Pocha.getJugadores()
                )
            }
        }
    }
}

/**
 * Crea una fila con la información de un jugador, además de unos botones de - y + para aumentar y disminuir el valor correspondiente.
 *
 * @param texto El texto que se muestra a la izquierda, suele ser el nombre.
 * @param puntos Los puntos (totales) del jugador.
 * @param valor La cantidad que se quiere modificar, ya sean las apuestas o las victorias.
 * @param modificar Recibe como parámetro el nuevo valor de la variable recibida.
 */
@Composable
fun FilaJugador(texto: String, puntos: String, valor: Int, modificar: (Int) -> Unit) {
    var valorState by rememberSaveable { mutableIntStateOf(valor) }
    Row(
        modifier = Modifier.fillMaxWidth(0.9f),
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
        ) { Icon(Icons.Rounded.Remove, contentDescription = "Remove") }
        Text(
            text = valorState.toString(),
            modifier = Modifier.padding(10.dp)
        )
        Button(onClick = {
            valorState += 1
            modificar(valorState)
        }
        ) { Icon(Icons.Rounded.Add, contentDescription = "Add") }
    }
}


/**
 * Fila para poner los nombres de cada jugador.
 *
 * @param jugador El jugador en cuestión
 * @param numJugadores El número total de jugadores, útil para saber cuál es el último
 * @param context El contexto actual
 */
@Composable
fun FilaJugadorNombres(jugador: Jugador, numJugadores: Int, context: Context) {
    var nombreState by rememberSaveable { mutableStateOf(jugador.nombre) }
    TextField(
        modifier = Modifier.fillMaxWidth(0.8f),
        value = nombreState,
        onValueChange = {
            if (it.length <= 20) {
                nombreState = it
                jugador.nombre = nombreState
            } else ToastRateLimiter.showToast(context, "¡Pon un nombre más corto!")
        },
        maxLines = 1,
        label = { Text("Jugador ${jugador.id}") },
        keyboardOptions = KeyboardOptions(imeAction = if (jugador.id + 1 == numJugadores) ImeAction.Done else ImeAction.Next)
    )
}

/**
 * Plantilla para la pantalla de la pocha.
 *
 * @param header El encabezado de la pantalla (un título...)
 * @param lineJugador La fila en la que se representará cada uno de los jugadores
 * @param nextRound Se llamará al cambiar de ronda
 * @param undo Se llamará pulsando el botón "volver"
 * @param undoEnabled Indica si se activa el botón "volver"
 * @param jugadores La lista con los jugadores de la partida
 */
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
        Spacer(modifier = Modifier.height(20.dp))
        header()
        Spacer(modifier = Modifier.height(10.dp))
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
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

/**
 * Dyalog mostrado al principio para preguntar si se quieren recuperar los datos de la partida anterior.
 *
 * @param context El contexto actual
 * @param atExit Se invoca al finalizar (al elegir una opción)
 */
@Composable
fun RecuperarDatos(context: Context, atExit: (Ronda) -> Unit) {
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
                            Pocha.loadState(context)
                            atExit(Ronda.APUESTAS)
                        }) { Text(text = "Sí") }
                        Spacer(modifier = Modifier.width(10.dp))
                        Button(onClick = {
                            Pocha.discardBackup(context)
                            atExit(Ronda.NOMBRES)
                        }) { Text(text = "No") }
                    }
                }
            }
        }
    }
}
