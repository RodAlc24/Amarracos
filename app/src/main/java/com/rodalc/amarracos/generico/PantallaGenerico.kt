package com.rodalc.amarracos.generico

import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rodalc.amarracos.comun.FilaJugadorNombres
import com.rodalc.amarracos.comun.Jugador
import com.rodalc.amarracos.comun.Plantilla
import com.rodalc.amarracos.comun.Ronda
import com.rodalc.amarracos.storage.DataStoreManager
import com.rodalc.amarracos.ui.mainScreen.Screens

/**
 * Gestiona toda la pantalla para el marcador de puntos.
 *
 * @param navController El controlador de navegación
 */
@Composable
fun PantallaGenerico(navController: NavController) {
    val context = LocalContext.current
    var state by rememberSaveable { mutableStateOf(Ronda.JUEGO) }


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
                title = "Generico",
                navController = navController,
                pantallaResultadosId = Screens.SCREEN_RES_GEN.name,
                lineJugador = { jugador ->
                    FilaJugador(
                        jugador = jugador,
                        ronda = Ronda.JUEGO
                    )
                },
                nextRound = {
                    state = Ronda.CONTEO
                },
                floatingIcon = { Icon(Icons.Rounded.Add, contentDescription = "Añadir") },
                undo = {
                    Generico.popState()
                    state = Ronda.CONTEO
                },
                undoEnabled = Generico.canUndo(),
                jugadores = Generico.getJugadores(),
            )
        }

        //Ronda.CONTEO -> {
        else -> {
            Plantilla(
                title = "Generico",
                navController = navController,
                pantallaResultadosId = Screens.SCREEN_RES_GEN.name,
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
                floatingIcon = { Icon(Icons.Rounded.Done, contentDescription = "Hecho") },
                undo = { state = Ronda.JUEGO },
                undoEnabled = true,
                jugadores = Generico.getJugadores(),
            )
        }
    }
}
