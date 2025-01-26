package com.rodalc.amarracos.generico

import android.content.Context
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import com.rodalc.amarracos.main.PopUp
import com.rodalc.amarracos.main.ToastRateLimiter
import com.rodalc.amarracos.main.repeatingClickable
import com.rodalc.amarracos.storage.DataStoreManager

/**
 * Gestiona toda la pantalla para el marcador de puntos.
 */
@Preview(
    showBackground = true,
    device = "spec:width=411dp,height=891dp,orientation=landscape"
)
@Composable
fun PantallaGenerico() {
    val context = LocalContext.current
    var state by rememberSaveable { mutableStateOf(Ronda.NOMBRES) }
    var canLoad by rememberSaveable { mutableStateOf(Generico.canLoadState(context)) }
    var jugadores by remember { mutableStateOf(Generico.getJugadores()) }

    // Keep screen on. Only if user has selected it
    val screenState by DataStoreManager.readDataStore(context, DataStoreManager.Key.KEEP_SCREEN_ON)
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
        PopUp(
            title = "¿Recuperar la última partida?",
            optionA = "No",
            optionB = "Sí",
            onClickA = {
                Generico.discardBackup(context)
                state = Ronda.NOMBRES
                jugadores = Generico.getJugadores()
                canLoad = false
            },
            onClickB = {
                Generico.loadState(context)
                state = Ronda.JUEGO
                jugadores = Generico.getJugadores()
                canLoad = false
            },
            preferredOptionB = true
        )
    } else {
        when (state) {
            Ronda.NOMBRES -> {
                Plantilla(
                    header = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth(0.9f)

                        ) {
                            Spacer(modifier = Modifier.weight(0.1f))
                            Text(
                                text = "Jugadores:",
                                fontSize = 20.sp
                            )
                            Spacer(modifier = Modifier.weight(0.9f))
                            IconButton(
                                onClick = { jugadores = jugadores.dropLast(1).toMutableList() },
                                enabled = jugadores.size > 2
                            ) { Icon(Icons.Rounded.Remove, "Quitar jugador") }
                            Spacer(modifier = Modifier.width(10.dp))
                            Box(
                                modifier = Modifier.width(30.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = jugadores.size.toString(),
                                    fontSize = 20.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            IconButton(
                                onClick = {
                                    jugadores =
                                        (jugadores + Jugador(jugadores.size + 1)).toMutableList()
                                },
                                enabled = jugadores.size < 100
                            ) { Icon(Icons.Rounded.Add, "Añadir jugador") }
                            Spacer(modifier = Modifier.weight(0.1f))
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
                        Generico.setJugadores(jugadores)
                        Generico.saveState(context)
                        state = Ronda.JUEGO
                    },
                    undo = {},
                    undoEnabled = false,
                    jugadores = jugadores
                )
            }

            Ronda.JUEGO -> {
                Plantilla(
                    header = {
                        Text(
                            text = "Puntos totales:",
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    },
                    lineJugador = { jugador ->
                        FilaJugador(
                            jugador = jugador,
                            ronda = Ronda.JUEGO
                        )
                    },
                    nextRound = {
                        state = Ronda.CONTEO
                    },
                    undo = {
                        Generico.popState()
                        jugadores = Generico.getJugadores()
                        state = Ronda.CONTEO
                    },
                    undoEnabled = Generico.canUndo(),
                    jugadores = Generico.getJugadores()
                )
            }

            Ronda.CONTEO -> {
                Plantilla(
                    header = {
                        Text(
                            text = "Puntos ganados en esta ronda: ",
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    },
                    lineJugador = { jugador ->
                        FilaJugador(
                            jugador = jugador,
                            ronda = Ronda.CONTEO
                        )
                    },
                    nextRound = {
                        Generico.pushState()
                        Generico.actualizarPuntuacion()
                        Generico.saveState(context)
                        state = Ronda.JUEGO
                    },
                    undo = { state = Ronda.JUEGO },
                    undoEnabled = true,
                    jugadores = Generico.getJugadores()
                )
            }
        }
    }
}

/**
 * Crea una fila con la información de un jugador, además de unos botones de - y + para aumentar y disminuir el valor correspondiente.
 *
 * @param jugador El jugador que se va a emplear
 * @param ronda La ronda actual
 */
@Composable
fun FilaJugador(
    jugador: Jugador,
    ronda: Ronda
) {
    var valorState by rememberSaveable { mutableIntStateOf(jugador.incremento) }
    val content = ButtonDefaults.textButtonColors().contentColor
    val contentDisabled = ButtonDefaults.textButtonColors().disabledContentColor
    val tintA = if (valorState > -99) content else contentDisabled
    val tintB = if (valorState < 99) content else contentDisabled
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = Modifier.fillMaxWidth(0.9f).height(50.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = jugador.toString(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f) // Allocate space for the text
                .clipToBounds()
        )

        if (ronda == Ronda.JUEGO) {
            Text(
                text = jugador.puntos.toString(),
                fontSize = 20.sp,
                modifier = Modifier.padding(10.dp)
            )
        } else if (ronda == Ronda.CONTEO) {
            IconButton(
                modifier = Modifier.repeatingClickable(
                    interactionSource = interactionSource,
                    onClick = {
                        valorState -= 1
                        jugador.incremento = valorState
                    },
                ),
                onClick = {},
            ) {
                Icon(
                    Icons.Rounded.Remove,
                    contentDescription = "Quitar 1 a $jugador",
                    tint = tintA
                )
            }
            Box(
                modifier = Modifier.width(55.dp).height(50.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = valorState.toString(),
                    fontSize = 20.sp
                )
            }
            IconButton(
                modifier = Modifier.repeatingClickable(
                    interactionSource = interactionSource,
                    onClick = {
                        valorState += 1
                        jugador.incremento = valorState
                    },
                ),
                onClick = {},

            ) {
                Icon(
                    Icons.Rounded.Add,
                    contentDescription = "Añadir uno a $jugador",
                    tint = tintB
                )
            }
        }
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
        keyboardOptions = KeyboardOptions(imeAction = if (jugador.id == numJugadores) ImeAction.Done else ImeAction.Next)
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
            OutlinedButton(
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
