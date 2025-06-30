package com.rodalc.amarracos.pocha

import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rodalc.amarracos.comun.FilaJugadorNombres
import com.rodalc.amarracos.comun.Jugador
import com.rodalc.amarracos.comun.Plantilla
import com.rodalc.amarracos.comun.Ronda
import com.rodalc.amarracos.utils.ToastRateLimiter
import com.rodalc.amarracos.storage.DataStoreManager
import com.rodalc.amarracos.ui.mainScreen.Screens

/**
 * Gestiona toda la pantalla para el juego de la pocha.
 *
 * @param navController El controlador de navegación
 */
@Composable
fun PantallaPocha(navController: NavController) {
    val context = LocalContext.current
    var state by rememberSaveable { mutableStateOf(Ronda.JUEGO) }
    var duplica by rememberSaveable { mutableStateOf(Pocha.getDuplica()) }

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

    when (state) {
        Ronda.JUEGO -> {
            Plantilla(
                title = "Pocha",
                navController = navController,
                pantallaResultadosId = Screens.SCREEN_RES_POCHA.name,
                header = {
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        Text(text = "Duplicar puntuación")
                        Spacer(modifier = Modifier.weight(1f))
                        Switch(
                            checked = duplica,
                            onCheckedChange = { duplica = it },
                        )
                    }
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
                floatingIcon = { Icon(Icons.Rounded.Add, contentDescription = "Nueva ronda") },
                undo = {
                    Pocha.popState()
                    duplica = Pocha.getDuplica()
                    state = Ronda.CONTEO
                },
                undoEnabled = Pocha.canUndo(),
                jugadores = Pocha.getJugadores()
            )
        }

        //Ronda.CONTEO -> {
        else -> {
            Plantilla(
                title = "Pocha",
                navController = navController,
                pantallaResultadosId = Screens.SCREEN_RES_POCHA.name,
                header = {
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        Text(text = "Duplicar puntuación")
                        Spacer(modifier = Modifier.weight(1f))
                        Switch(
                            checked = duplica,
                            onCheckedChange = { duplica = it },
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                },
                lineJugador = { jugador ->
                    FilaJugador(
                        jugador = jugador,
                        ronda = Ronda.CONTEO
                    )
                },
                nextRound = {
                    if (Pocha.canContinue()) {
                        Pocha.setDuplica(duplica)
                        Pocha.pushState()
                        Pocha.actualizarPuntuacion(duplica)
                        duplica = false
                        Pocha.saveState(context)
                        state = Ronda.JUEGO
                    } else ToastRateLimiter.showToast(
                        context,
                        "Las apuestas no pueden coincidir con el número de rondas jugadas"
                    )
                },
                floatingIcon = { Icon(Icons.Rounded.Done, contentDescription = "Hecho") },
                undo = { state = Ronda.JUEGO },
                undoEnabled = true,
                jugadores = Pocha.getJugadores()
            )
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
    var valorState by rememberSaveable { mutableIntStateOf(if (ronda == Ronda.JUEGO) jugador.apuesta else jugador.victoria) }
    val content = ButtonDefaults.textButtonColors().contentColor //TODO
    val contentDisabled = ButtonDefaults.textButtonColors().disabledContentColor //TODO
    val tintA = if (valorState > 0) content else contentDisabled
    val tintB = if (valorState < 99) content else contentDisabled

    Row(
        modifier = Modifier.fillMaxWidth(0.9f),
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
        Text(
            text = if (ronda == Ronda.JUEGO) jugador.puntos.toString() else jugador.apuesta.toString(),
            modifier = Modifier.padding(10.dp)
        )
        IconButton(
            onClick = {
                valorState -= 1
                if (ronda == Ronda.JUEGO) {
                    jugador.apuesta = valorState
                } else {
                    jugador.victoria = valorState
                }
            },
            enabled = valorState > 0
        ) { Icon(Icons.Rounded.Remove, contentDescription = "Quitar 1 a $jugador", tint = tintA) }
        Box(
            modifier = Modifier.width(40.dp),
            contentAlignment = Alignment.Center
        ) {
            if (ronda == Ronda.JUEGO) {
                Text(text = jugador.apuesta.toString())
            } else {
                Text(
                    text = jugador.victoria.toString(),
                    color = if (jugador.apuesta == jugador.victoria) Color.Unspecified else MaterialTheme.colorScheme.error
                )
            }
        }
        IconButton(
            onClick = {
                valorState += 1
                if (ronda == Ronda.JUEGO) {
                    jugador.apuesta = valorState
                } else {
                    jugador.victoria = valorState
                }
            }, enabled = valorState < 99
        ) { Icon(Icons.Rounded.Add, contentDescription = "Añadir uno a $jugador", tint = tintB) }
    }
}