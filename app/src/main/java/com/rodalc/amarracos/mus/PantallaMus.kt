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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
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
import androidx.compose.ui.window.Dialog
import com.rodalc.amarracos.main.ToastRateLimiter
import com.rodalc.amarracos.storage.DataStoreManager
import kotlinx.coroutines.async

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

    /*
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val activity = context as ComponentActivity
        val lifecycleObserver = object : DefaultLifecycleObserver {
            @SuppressLint("SourceLockedOrientationActivity")
            override fun onCreate(owner: LifecycleOwner) {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }

            override fun onDestroy(owner: LifecycleOwner) {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            }
        }

        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }
     */

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
        RecuperarDatos(context = context) {
            showConfig = it
            canLoad = false
        }
    } else {
        if (showConfig) {
            PantallaConfiguracion(context) { showConfig = it }
        } else {
            PlantillaMus(landscape)
        }
    }

}

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

@Composable
fun PlantillaMus(landscape: Boolean) {
    val viewModel = MusViewModel()
    val buenos by viewModel.buenos.collectAsState()
    val malos by viewModel.malos.collectAsState()
    val puntos by viewModel.puntos.collectAsState()

    var rondaEnvites by rememberSaveable { mutableStateOf(true) }

    val finRonda = { gannBuenos: Boolean ->
        viewModel.updateEnvites(Envites())
        rondaEnvites = true
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

    if (landscape) {
        Row(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
            ) {
                BotonesPareja(buenos = true, landscape, viewModel) { finRonda(true) }
            }
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
            ) {
                EnvitesYDeshacer(viewModel, landscape, rondaEnvites) {
                    rondaEnvites = !rondaEnvites
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
            ) {
                BotonesPareja(buenos = false, landscape, viewModel) { finRonda(false) }
            }
        }
    } else {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                BotonesPareja(buenos = true, landscape, viewModel) { finRonda(true) }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                EnvitesYDeshacer(viewModel, landscape, rondaEnvites) {
                    rondaEnvites = !rondaEnvites
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                BotonesPareja(buenos = false, landscape, viewModel) { finRonda(false) }
            }
        }
    }
}

@Composable
fun BotonesPareja(
    buenos: Boolean,
    landscape: Boolean,
    viewModel: MusViewModel,
    onOrdago: () -> Unit
) {
    val context = LocalContext.current
    val pareja by if (buenos) viewModel.buenos.collectAsState() else viewModel.malos.collectAsState()

    if (!landscape) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.weight(0.5f)
            ) {
                Text(
                    text = "${pareja.nombre}: ${pareja.victorias}",
                    fontSize = 25.sp
                )
                Spacer(modifier = Modifier.width(30.dp))
                OutlinedButton(onClick = {
                    Mus.pushState()
                    onOrdago()
                    Mus.saveState(context)
                }
                ) { Text("Órdago") }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(0.5f)
            ) {
                IconButton(
                    onClick = {
                        if (buenos) {
                            viewModel.updateBuenos(pareja.copy(puntos = pareja.puntos - 1))
                        } else {
                            viewModel.updateMalos(pareja.copy(puntos = pareja.puntos - 1))
                        }
                        Mus.saveState(context)
                    },
                    enabled = pareja.puntos > 0
                ) {
                    Icon(Icons.Rounded.Remove, contentDescription = "Remove")
                }
                Text(
                    text = pareja.puntos.toString(),
                    fontSize = 80.sp,
                    modifier = Modifier.clickable(
                        onClick = {
                            if (pareja.puntos + 5 >= Mus.getPuntos()) Mus.pushState()
                            if (buenos) {
                                viewModel.updateBuenos(pareja.copy(puntos = pareja.puntos + 5))
                            } else {
                                viewModel.updateMalos(pareja.copy(puntos = pareja.puntos + 5))
                            }
                            Mus.saveState(context)
                        }
                    )
                )
                IconButton(onClick = {
                    if (pareja.puntos + 1 >= Mus.getPuntos()) Mus.pushState()
                    if (buenos) {
                        viewModel.updateBuenos(pareja.copy(puntos = pareja.puntos + 1))
                    } else {
                        viewModel.updateMalos(pareja.copy(puntos = pareja.puntos + 1))
                    }
                    Mus.saveState(context)
                }) {
                    Icon(Icons.Rounded.Add, contentDescription = "Add")
                }
            }
        }
    } else {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            Spacer(modifier = Modifier.weight(5f))
            Text(
                text = "${pareja.nombre}: ${pareja.victorias}",
                fontSize = 25.sp
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = pareja.puntos.toString(),
                fontSize = 80.sp,
                modifier = Modifier.clickable(
                    onClick = {
                        if (pareja.puntos + 5 >= Mus.getPuntos()) Mus.pushState()
                        if (buenos) {
                            viewModel.updateBuenos(pareja.copy(puntos = pareja.puntos + 5))
                        } else {
                            viewModel.updateMalos(pareja.copy(puntos = pareja.puntos + 5))
                        }
                        Mus.saveState(context)
                    }
                )
            )
            Row {
                IconButton(
                    onClick = {
                        if (buenos) {
                            viewModel.updateBuenos(pareja.copy(puntos = pareja.puntos - 1))
                        } else {
                            viewModel.updateMalos(pareja.copy(puntos = pareja.puntos - 1))
                        }
                        Mus.saveState(context)
                    },
                    enabled = pareja.puntos > 0
                ) {
                    Icon(Icons.Rounded.Remove, contentDescription = "Remove")
                }
                IconButton(onClick = {
                    if (pareja.puntos + 1 >= Mus.getPuntos()) Mus.pushState()
                    if (buenos) {
                        viewModel.updateBuenos(pareja.copy(puntos = pareja.puntos + 1))
                    } else {
                        viewModel.updateMalos(pareja.copy(puntos = pareja.puntos + 1))
                    }
                    Mus.saveState(context)
                }) {
                    Icon(Icons.Rounded.Add, contentDescription = "Add")
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            OutlinedButton(onClick = {
                Mus.pushState()
                onOrdago()
                Mus.saveState(context)
            }
            ) { Text("Órdago") }
            Spacer(modifier = Modifier.weight(5f))
        }
    }
}

@Composable
fun EnvitesYDeshacer(
    viewModel: MusViewModel,
    landscape: Boolean,
    rondaEnvites: Boolean,
    changeRondaEmbites: () -> Unit
) {
    val context = LocalContext.current
    val envites by viewModel.envites.collectAsState()
    var canUndo by rememberSaveable { mutableStateOf(Mus.canUndo()) }
    canUndo = Mus.canUndo()

    if (!rondaEnvites && envites.vacio()) changeRondaEmbites()

    if (!landscape) {
        Row {
            BotonesEnvite(rondaEnvites, landscape, envites.grande, viewModel) {
                viewModel.updateEnvites(envites.copy(grande = it))
            }
            BotonesEnvite(rondaEnvites, landscape, envites.chica, viewModel) {
                viewModel.updateEnvites(envites.copy(chica = it))
            }
            BotonesEnvite(rondaEnvites, landscape, envites.pares, viewModel) {
                viewModel.updateEnvites(envites.copy(pares = it))
            }
            BotonesEnvite(rondaEnvites, landscape, envites.juego, viewModel) {
                viewModel.updateEnvites(envites.copy(juego = it))
            }
            if (rondaEnvites && !envites.vacio()) {
                Button(
                    onClick = { changeRondaEmbites() },
                ) { Icon(Icons.Rounded.Done, contentDescription = "Done") }
            } else {
                Button(
                    onClick = {
                        if (canUndo) {
                            if (rondaEnvites) changeRondaEmbites()
                            Mus.popState()
                            viewModel.update()
                            Mus.saveState(context)
                        } else {
                            changeRondaEmbites()
                        }
                    }, enabled = canUndo || !envites.vacio()
                ) { Icon(Icons.AutoMirrored.Rounded.Undo, contentDescription = "Undo") }
            }
        }
    } else {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            BotonesEnvite(rondaEnvites, landscape, envites.grande, viewModel) {
                viewModel.updateEnvites(envites.copy(grande = it))
            }
            BotonesEnvite(rondaEnvites, landscape, envites.chica, viewModel) {
                viewModel.updateEnvites(envites.copy(chica = it))
            }
            BotonesEnvite(rondaEnvites, landscape, envites.pares, viewModel) {
                viewModel.updateEnvites(envites.copy(pares = it))
            }
            BotonesEnvite(rondaEnvites, landscape, envites.juego, viewModel) {
                viewModel.updateEnvites(envites.copy(juego = it))
            }
            if (rondaEnvites && !envites.vacio()) {
                Button(
                    onClick = { changeRondaEmbites() },
                ) { Icon(Icons.Rounded.Done, contentDescription = "Done") }
            } else {
                Button(
                    onClick = {
                        if (canUndo) {
                            if (rondaEnvites) changeRondaEmbites()
                            Mus.popState()
                            viewModel.update()
                            Mus.saveState(context)
                        } else {
                            changeRondaEmbites()
                        }
                    }, enabled = canUndo || !envites.vacio()
                ) { Icon(Icons.AutoMirrored.Rounded.Undo, contentDescription = "Undo") }
            }
        }
    }
}

/**
 * Conjunto de botones y un texto para gestionar un envite.
 *
 * @param rondaEnvites Si es la ronda de envites (true) o la de conteo (false)
 * @param landscape Si el dispositivo está en horizontal
 * @param envite La cantidad del envite
 * @param viewModel El VM del mus
 * @param updateEnvite Se ejecuta al modificar el valor del envite (e.g. para aumentarlo)
 */
@Composable
fun BotonesEnvite(
    rondaEnvites: Boolean,
    landscape: Boolean,
    envite: Int,
    viewModel: MusViewModel,
    updateEnvite: (Int) -> Unit
) {
    val context = LocalContext.current
    val buenos by viewModel.buenos.collectAsState()
    val malos by viewModel.malos.collectAsState()

    val botonGenerico = @Composable { primerBoton: Boolean ->
        val botonSuma = primerBoton != landscape
        TextButton(
            onClick = {
                if (rondaEnvites) {
                    Mus.deleteStack()
                    updateEnvite(envite + if (primerBoton == landscape) -1 else 1)
                } else {
                    Mus.pushState()
                    if (primerBoton) {
                        viewModel.updateBuenos(buenos.copy(puntos = buenos.puntos + envite))
                    } else {
                        viewModel.updateMalos(malos.copy(puntos = malos.puntos + envite))
                    }
                    updateEnvite(0)
                }
                Mus.saveState(context)
            },
            enabled = if (rondaEnvites) if (botonSuma) envite < 99 else envite > 0 else envite != 0
        ) {
            if (rondaEnvites) {
                if (botonSuma) {
                    Icon(Icons.Rounded.Add, contentDescription = "Aumentar envite")
                } else {
                    Icon(Icons.Rounded.Remove, contentDescription = "Reducir envite")
                }
            } else {
                if (landscape) {
                    if (primerBoton) {
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Buenos ganan"
                        )
                    } else {
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowForward,
                            contentDescription = "Malos ganan"
                        )
                    }
                } else {
                    if (primerBoton) {
                        Icon(Icons.Rounded.ArrowUpward, contentDescription = "Buenos ganan")
                    } else {
                        Icon(Icons.Rounded.ArrowDownward, contentDescription = "Malos ganan")
                    }
                }
            }
        }
    }

    val botonesYTexto = @Composable {
        botonGenerico(true)
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
                            Mus.deleteStack()
                            updateEnvite(envite + 5)
                            Mus.saveState(context)
                        },
                        enabled = rondaEnvites
                    )
            )
        }
        botonGenerico(false)
    }

    val modifier = Modifier.padding(10.dp)

    if (landscape) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = modifier.fillMaxWidth()
        ) {
            botonesYTexto()
        }
    } else {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.fillMaxHeight()
        ) {
            botonesYTexto()
        }
    }
}

/**
 * Dyalog mostrado al principio para preguntar si se quieren recuperar los datos de la partida anterior.
 *
 * @param context El contexto actual
 * @param showConfig Si se debe mostrar la pantalla de configuración o no
 */
@Composable
fun RecuperarDatos(context: Context, showConfig: (Boolean) -> Unit) {
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
                        OutlinedButton(onClick = {
                            Mus.discardBackup(context)
                            Mus.reset()
                            showConfig(true)
                        }) { Text(text = "No") }
                        Spacer(modifier = Modifier.width(20.dp))
                        Button(onClick = {
                            Mus.loadState((context))
                            showConfig(false)
                        }) { Text(text = "Sí") }
                    }
                }
            }
        }
    }
}
