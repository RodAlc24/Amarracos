package com.rodalc.amarracos.mus

import android.content.Context
import android.content.res.Configuration
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.Undo
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rodalc.amarracos.main.PopUp
import com.rodalc.amarracos.main.ToastRateLimiter
import com.rodalc.amarracos.storage.DataStoreManager
import kotlinx.coroutines.async

/**
 * Punto de entrada para el mus.
 */
@Preview(
    device = "spec:width=411dp,height=891dp,orientation=landscape",
    showSystemUi = true,
    showBackground = true
)
@Composable
fun PantallaMus() {
    var showConfig by rememberSaveable { mutableStateOf(true) }
    val context = LocalContext.current
    var canLoad by rememberSaveable { mutableStateOf(Mus.canLoadState(context)) }
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

    if (canLoad) {
        PopUp(
            title = "¿Recuperar la última partida?",
            optionA = "No",
            optionB = "Sí",
            onClickA = {
                Mus.discardBackup(context)
                Mus.reset()
                showConfig = true
                canLoad = false
            },
            onClickB = {
                Mus.loadState((context))
                showConfig = false
                canLoad = false
            },
            preferredOptionB = true
        )
    } else {
        if (showConfig) {
            PantallaConfiguracion(context) { showConfig = it }
        } else {
            PlantillaMus(landscape)
        }
    }
}

/**
 * Pantalla para poner los nombres y el límite de puntos.
 * Solo debería llamarse una vez, al empezar la partida.
 *
 * @param context El contexto de la aplicación
 * @param show Si se muestra o no esta pantalla
 */
@Composable
fun PantallaConfiguracion(
    context: Context,
    show: (Boolean) -> Unit
) {
    var buenos by rememberSaveable { mutableStateOf("") }
    var malos by rememberSaveable { mutableStateOf("") }
    val puntos30 by DataStoreManager.readDataStore(context, DataStoreManager.Key.MUS_A_30)
        .collectAsState(initial = true)
    val coreutineScope = rememberCoroutineScope()

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()

    ) {
        Spacer(modifier = Modifier.weight(0.2f))
        Text(text = "Mus", fontSize = 30.sp)
        Spacer(modifier = Modifier.weight(0.2f))
        TextField(
            modifier = Modifier.fillMaxWidth(0.7f),
            value = buenos,
            onValueChange = {
                if (it.length <= 10) {
                    buenos = it
                } else ToastRateLimiter.showToast(context, "¡Pon un nombre más corto!")
            },
            maxLines = 1,
            label = { Text(text = "Buenos") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(0.7f),
            value = malos,
            onValueChange = {
                if (it.length <= 10) {
                    malos = it
                } else ToastRateLimiter.showToast(context, "¡Pon un nombre más corto!")
            },
            maxLines = 1,
            label = { Text(text = "Malos") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
        )
        Spacer(modifier = Modifier.weight(0.2f))
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Puntos")
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "30:")
            RadioButton(selected = puntos30, onClick = {
                coreutineScope.async {
                    DataStoreManager.setDataStore(
                        context,
                        DataStoreManager.Key.MUS_A_30,
                        true
                    )
                }
            })
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "40:")
            RadioButton(selected = !puntos30, onClick = {
                coreutineScope.async {
                    DataStoreManager.setDataStore(
                        context,
                        DataStoreManager.Key.MUS_A_30,
                        false
                    )
                }
            })
        }
        Spacer(modifier = Modifier.weight(0.2f))
        OutlinedButton(
            onClick = {
                Mus.getBuenos().nombre = if (buenos == "") "Buenos" else buenos
                Mus.getMalos().nombre = if (malos == "") "Malos" else malos
                Mus.setPuntos(if (puntos30) 30 else 40)
                Mus.saveState(context)
                show(false)
            },
            modifier = Modifier.padding(20.dp)
        ) {
            Icon(Icons.Rounded.Done, contentDescription = "Done")
        }
    }
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
        IconButton(
            onClick = {
                Mus.popState()
                viewModel.update()
                Mus.saveState(context)
            }, enabled = canUndo
        ) {
            Icon(
                Icons.AutoMirrored.Rounded.Undo,
                "Deshacer",
                tint = if (canUndo) MaterialTheme.colorScheme.primary else ButtonDefaults.textButtonColors().disabledContentColor
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

/**
 * Conjunto de botones y puntos para cada pareja
 *
 * @param buenos Si es la primera pareja
 * @param landscape Si el dispositivo está en horizontal
 * @param viewModel El VM del mus
 */
@Composable
fun BotonesPareja(
    buenos: Boolean,
    landscape: Boolean,
    viewModel: MusViewModel,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val pareja by if (buenos) viewModel.buenos.collectAsState() else viewModel.malos.collectAsState()

    val nombreVictorias = @Composable {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "${pareja.nombre}:", fontSize = 25.sp)
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.width(40.dp)
            ) {
                Text(text = pareja.victorias.toString(), fontSize = 25.sp)
            }
        }
    }

    val puntos = @Composable {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.width(110.dp)
        ) {
            Text(
                text = pareja.puntos.toString(),
                fontSize = 80.sp,
                modifier = Modifier.clickable(
                    onClick = {
                        Mus.pushState()
                        if (buenos) {
                            viewModel.updateBuenos(pareja.copy(puntos = pareja.puntos + 5))
                        } else {
                            viewModel.updateMalos(pareja.copy(puntos = pareja.puntos + 5))
                        }
                        Mus.saveState(context)
                    }
                )
            )
        }
    }

    val reducirPuntos = @Composable {
        TextButton(
            onClick = {
                Mus.pushState()
                if (buenos) {
                    viewModel.updateBuenos(pareja.copy(puntos = pareja.puntos - 1))
                } else {
                    viewModel.updateMalos(pareja.copy(puntos = pareja.puntos - 1))
                }
                Mus.saveState(context)
            },
            enabled = pareja.puntos > 0
        ) { Icon(Icons.Rounded.Remove, contentDescription = "Remove", Modifier.size(40.dp)) }
    }

    val aumentarPuntos = @Composable {
        TextButton(onClick = {
            Mus.pushState()
            if (buenos) {
                viewModel.updateBuenos(pareja.copy(puntos = pareja.puntos + 1))
            } else {
                viewModel.updateMalos(pareja.copy(puntos = pareja.puntos + 1))
            }
            Mus.saveState(context)
        }) { Icon(Icons.Rounded.Add, contentDescription = "Add", Modifier.size(40.dp)) }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(10.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))
        nombreVictorias()
        Spacer(modifier = Modifier.weight(1f))
        if (landscape) puntos()
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
        ) {
            reducirPuntos()
            if (!landscape) puntos()
            aumentarPuntos()
        }
    }
}

/**
 * Conjunto de botones y textos con los envites actuales.
 *
 * @param viewModel El VM del mus
 * @param landscape Si el dispositivo está en horizontal
 */
@Composable
fun EnvitesYDeshacer(
    viewModel: MusViewModel,
    landscape: Boolean,
) {
    val envites by viewModel.envites.collectAsState()

    val botonesEnvites = @Composable {
        BotonesEnvite(landscape, envites.grande, viewModel) {
            viewModel.updateEnvites(envites.copy(grande = it))
        }
        BotonesEnvite(landscape, envites.chica, viewModel) {
            viewModel.updateEnvites(envites.copy(chica = it))
        }
        BotonesEnvite(landscape, envites.pares, viewModel) {
            viewModel.updateEnvites(envites.copy(pares = it))
        }
        BotonesEnvite(landscape, envites.juego, viewModel) {
            viewModel.updateEnvites(envites.copy(juego = it))
        }
    }

    if (landscape) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            botonesEnvites()
        }
    } else {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            botonesEnvites()
        }
    }
}

/**
 * Conjunto de botones y un texto para gestionar un envite.
 *
 * @param landscape Si el dispositivo está en horizontal
 * @param envite La cantidad del envite
 * @param viewModel El VM del mus
 * @param updateEnvite Se ejecuta al modificar el valor del envite (e.g. para aumentarlo)
 */
@Composable
fun BotonesEnvite(
    landscape: Boolean,
    envite: Int,
    viewModel: MusViewModel,
    updateEnvite: (Int) -> Unit
) {
    val context = LocalContext.current
    val buenos by viewModel.buenos.collectAsState()
    val malos by viewModel.malos.collectAsState()

    val botonPuntuacion = @Composable { botonSuma: Boolean ->
        TextButton(
            onClick = {
                Mus.pushState()
                updateEnvite(envite + if (botonSuma) 1 else -1)
                Mus.saveState(context)
            },
            enabled = if (botonSuma) envite < 99 else envite > 0
        ) {
            if (botonSuma) {
                Icon(Icons.Rounded.Add, contentDescription = "Aumentar envite")
            } else {
                Icon(Icons.Rounded.Remove, contentDescription = "Reducir envite")
            }
        }
    }

    val botonConteo = @Composable { botonBuenos: Boolean ->
        FilledIconButton(
            onClick = {
                Mus.pushState()
                if (botonBuenos) {
                    viewModel.updateBuenos(buenos.copy(puntos = buenos.puntos + envite))
                } else {
                    viewModel.updateMalos(malos.copy(puntos = malos.puntos + envite))
                }
                updateEnvite(0)
                Mus.saveState(context)
            }, enabled = envite != 0
        ) {
            if (landscape) {
                if (botonBuenos) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Buenos ganan")
                } else {
                    Icon(Icons.AutoMirrored.Rounded.ArrowForward, "Malos ganan")
                }
            } else {
                if (botonBuenos) {
                    Icon(Icons.Rounded.ArrowUpward, "Buenos ganan")
                } else {
                    Icon(Icons.Rounded.ArrowDownward, "Malos ganan")
                }
            }
        }
    }

    val texto = @Composable {
        Box(
            modifier = Modifier.width(50.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = envite.toString(),
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(10.dp)
                    .clickable(
                        onClick = {
                            Mus.pushState()
                            updateEnvite(envite + 5)
                            Mus.saveState(context)
                        },
                        enabled = envite < 95
                    )
            )
        }
    }

    val modifier = Modifier.padding(10.dp)

    if (landscape) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = modifier.fillMaxWidth()
        ) {
            botonConteo(true)
            botonPuntuacion(false)
            texto()
            botonPuntuacion(true)
            botonConteo(false)
        }
    } else {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.fillMaxHeight()
        ) {
            botonConteo(true)
            botonPuntuacion(true)
            texto()
            botonPuntuacion(false)
            botonConteo(false)
        }
    }
}
