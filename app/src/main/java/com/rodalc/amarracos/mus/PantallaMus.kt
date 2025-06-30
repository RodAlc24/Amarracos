package com.rodalc.amarracos.mus

import android.content.res.Configuration
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Undo
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.rodalc.amarracos.storage.DataStoreManager
import com.rodalc.amarracos.ui.elements.PopUp

/**
 * Punto de entrada para el mus.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMus() {
    val context = LocalContext.current
    val landscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

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

    PlantillaMus(landscape)
}

/**
 * Se encarga de representar todos los elementos necesarios para un partida de mus.
 *
 * @param landscape Si el dispositivo está en horizontal
 */
@Composable
fun PlantillaMus(landscape: Boolean) {
    val viewModel = MusViewModel()
    val buenos by viewModel.buenos.collectAsState()
    val malos by viewModel.malos.collectAsState()
    val canUndo by viewModel.canUndo.collectAsState()
    val puntos = Mus.getPuntos()

    val context = LocalContext.current

    val finRonda = { gannBuenos: Boolean ->
        viewModel.updateEnvites(Envites())
        if (gannBuenos) {
            viewModel.updateBuenos(buenos.copy(puntos = 0, victorias = buenos.victorias + 1))
            viewModel.updateMalos(malos.copy(puntos = 0))
        } else {
            viewModel.updateMalos(malos.copy(puntos = 0, victorias = malos.victorias + 1))
            viewModel.updateBuenos(buenos.copy(puntos = 0))
        }
    }

    if (buenos.puntos >= puntos) {
        finRonda(true)
    } else if (malos.puntos >= puntos) {
        finRonda(false)
    }

    val undo = @Composable {
        val tint =
            if (canUndo) ButtonDefaults.textButtonColors().contentColor else ButtonDefaults.textButtonColors().disabledContentColor
        TextButton(
            onClick = {
                Mus.popState()
                viewModel.update()
                Mus.saveState(context)
            }, enabled = canUndo,
            modifier = Modifier.size(50.dp)
        ) {
            Icon(
                Icons.AutoMirrored.Rounded.Undo,
                "Deshacer",
                tint = tint
            )
        }
    }

    val ordago = @Composable {
        var show by rememberSaveable { mutableStateOf(false) }
        OutlinedButton(onClick = {
            show = true
        }) {
            Text(text = "Órdago")
        }
        if (show) PopUp(
            title = "Ganador:",
            optionA = buenos.nombre,
            optionB = malos.nombre,
            onClickA = {
                Mus.pushState()
                finRonda(true)
                Mus.saveState(context)
                show = false
            },
            onClickB = {
                Mus.pushState()
                finRonda(false)
                Mus.saveState(context)
                show = false
            },
            onDismiss = { show = false }
        )
    }

    val elementos = @Composable { modifier1: Modifier, modifier2: Modifier ->
        Box(modifier = modifier1) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BotonesPareja(buenos = true, landscape, viewModel, Modifier.fillMaxSize(0.8f))
                if (landscape) undo()
            }
        }
        Box(modifier = modifier2) {
            EnvitesYDeshacer(viewModel, landscape)
        }
        Box(modifier = modifier1) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BotonesPareja(buenos = false, landscape, viewModel, Modifier.fillMaxSize(0.8f))
                if (landscape) ordago()
            }
        }
    }

    if (landscape) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
        ) {
            elementos(
                Modifier
                    .fillMaxHeight()
                    .weight(1f),
                Modifier
                    .fillMaxHeight()
                    .weight(1.3f)
            )
        }
    } else {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f)
            ) {
                elementos(
                    Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    Modifier
                        .fillMaxHeight()
                        .weight(1.3f)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .fillMaxHeight()
            ) {
                undo()
                ordago()
            }
        }
    }
}

