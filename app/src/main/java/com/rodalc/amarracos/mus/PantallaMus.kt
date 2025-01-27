package com.rodalc.amarracos.mus

import android.content.Context
import android.content.res.Configuration
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.Undo
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.rodalc.amarracos.main.PopUp
import com.rodalc.amarracos.main.ToastRateLimiter
import com.rodalc.amarracos.main.repeatingClickable
import com.rodalc.amarracos.storage.DataStoreManager
import com.rodalc.amarracos.ui.theme.Playfair
import kotlinx.coroutines.async

/**
 * Punto de entrada para el mus.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMus(navigation: NavController) {
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

    if (showConfig) {
        PantallaConfiguracion(canLoad, navigation, context) { showConfig = it }
    } else {
        PlantillaMus(landscape)
    }
}

/**
 * Pantalla para poner los nombres y el límite de puntos.
 * Solo debería llamarse una vez, al empezar la partida.
 *
 * @param context El contexto de la aplicación
 * @param show Si se muestra o no esta pantalla
 */
@ExperimentalMaterial3Api
@Composable
fun PantallaConfiguracion(
    canLoad: Boolean,
    navController: NavController,
    context: Context,
    show: (Boolean) -> Unit
) {
    var buenos by rememberSaveable { mutableStateOf("") }
    var malos by rememberSaveable { mutableStateOf("") }
    var recuperar by rememberSaveable { mutableStateOf(canLoad) }

    MusDefaultConfigManager.loadState(context)
    val labelBuenos = MusDefaultConfigManager.getBuenos()
    val labelMalos = MusDefaultConfigManager.getMalos()

    val puntos30 by DataStoreManager.readDataStore(context, DataStoreManager.Key.MUS_A_30)
        .collectAsState(initial = true)
    val coreutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.primaryContainer,
                ),
                title = {
                    Text(
                        "Mus",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontFamily = Playfair,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("pantallaInicial") }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to main menu",
                            tint = MaterialTheme.colorScheme.primaryContainer
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (recuperar) {
                    Mus.loadState((context))
                    show(false)
                } else {
                    Mus.discardBackup(context)
                    Mus.reset()
                    Mus.getBuenos().nombre = if (buenos == "") labelBuenos else buenos
                    Mus.getMalos().nombre = if (malos == "") labelMalos else malos
                    MusDefaultConfigManager.setBuenos(Mus.getBuenos().nombre)
                    MusDefaultConfigManager.setMalos(Mus.getMalos().nombre)
                    MusDefaultConfigManager.saveState(context)
                    Mus.setPuntos(if (puntos30) 30 else 40)
                    Mus.saveState(context)
                    show(false)
                }
            }) {
                Icon(Icons.Rounded.Done, contentDescription = "Done")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(0.8f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RadioButton(
                        selected = recuperar,
                        onClick = { recuperar = true },
                        enabled = canLoad
                    )
                    Text(text = "Recuperar partida guardada")
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RadioButton(selected = !recuperar, onClick = { recuperar = false })
                    Text(text = "Nueva partida")
                }
                Spacer(modifier = Modifier.height(5.dp))
                TextField(
                    modifier = Modifier.fillMaxWidth(0.7f),
                    value = buenos,
                    onValueChange = {
                        if (it.length <= 10) {
                            buenos = it
                        } else ToastRateLimiter.showToast(context, "¡Pon un nombre más corto!")
                    },
                    maxLines = 1,
                    placeholder = { Text(text = labelBuenos) },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    enabled = !recuperar
                )
                Spacer(modifier = Modifier.height(10.dp))
                TextField(
                    modifier = Modifier.fillMaxWidth(0.7f),
                    value = malos,
                    onValueChange = {
                        if (it.length <= 10) {
                            malos = it
                        } else ToastRateLimiter.showToast(context, "¡Pon un nombre más corto!")
                    },
                    maxLines = 1,
                    placeholder = { Text(text = labelMalos) },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    enabled = !recuperar
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = "Puntos para ganar:")
                Spacer(modifier = Modifier.height(5.dp))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(10.dp))
                    RadioButton(selected = puntos30, onClick = {
                        coreutineScope.async {
                            DataStoreManager.setDataStore(
                                context,
                                DataStoreManager.Key.MUS_A_30,
                                true
                            )
                        }
                    }, enabled = !recuperar)
                    Text(text = "30")
                    Spacer(modifier = Modifier.width(10.dp))
                    RadioButton(selected = !puntos30, onClick = {
                        coreutineScope.async {
                            DataStoreManager.setDataStore(
                                context,
                                DataStoreManager.Key.MUS_A_30,
                                false
                            )
                        }
                    }, enabled = !recuperar)
                    Text(text = "40")
                }
            }
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
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(vertical = 10.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.width(40.dp)
                ) {
                    Text(
                        text = pareja.victorias.toString(),
                        fontSize = 25.sp,
                        maxLines = 1,
                    )
                }
                Text(
                    text = ":${pareja.nombre}",
                    fontSize = 25.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Right
                )
            }
        }
    }

    val puntos = @Composable {
        TextButton(
            onClick = {
                Mus.pushState()
                if (buenos) {
                    viewModel.updateBuenos(pareja.copy(puntos = pareja.puntos + 1))
                } else {
                    viewModel.updateMalos(pareja.copy(puntos = pareja.puntos + 1))
                }
                Mus.saveState(context)
            },
            modifier = Modifier
                .width(110.dp)
        ) {
            Text(
                text = pareja.puntos.toString(),
                fontSize = 60.sp,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }

    val reducirPuntos = @Composable {
        val enabled = pareja.puntos > 0
        val tint =
            if (enabled) ButtonDefaults.textButtonColors().contentColor else ButtonDefaults.textButtonColors().disabledContentColor
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
            enabled = enabled,
            modifier = Modifier
                .padding(5.dp)
                .size(60.dp)
        ) { Icon(Icons.Rounded.Remove, "Remove", Modifier.size(40.dp), tint = tint) }
    }

    val aumentarPuntos = @Composable {
        val tint = ButtonDefaults.textButtonColors().contentColor
        TextButton(
            onClick = {
                Mus.pushState()
                if (buenos) {
                    viewModel.updateBuenos(pareja.copy(puntos = pareja.puntos + 1))
                } else {
                    viewModel.updateMalos(pareja.copy(puntos = pareja.puntos + 1))
                }
                Mus.saveState(context)
            },
            modifier = Modifier
                .padding(5.dp)
                .size(60.dp)
        ) { Icon(Icons.Rounded.Add, "Add", Modifier.size(40.dp), tint = tint) }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        nombreVictorias()
        if (landscape) puntos()
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(10.dp)
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
        val enabled = if (botonSuma) envite < 99 else envite > 0
        val tint =
            if (enabled) ButtonDefaults.textButtonColors().contentColor else ButtonDefaults.textButtonColors().disabledContentColor

        TextButton(
            modifier = Modifier.repeatingClickable(
                interactionSource = remember { MutableInteractionSource() },
                onClick = {
                    Mus.pushState()
                    updateEnvite(envite + if (botonSuma) 1 else -1)
                    Mus.saveState(context)
                },
                enabled = enabled,
            ),
            onClick = {},
        ) {
            if (botonSuma) {
                Icon(Icons.Rounded.Add, contentDescription = "Aumentar envite", tint = tint)
            } else {
                Icon(Icons.Rounded.Remove, contentDescription = "Reducir envite", tint = tint)
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
            TextButton(
                onClick = {
                    Mus.pushState()
                    updateEnvite(envite + 2)
                    Mus.saveState(context)
                },
                enabled = envite < 98
            ) {
                Text(
                    text = envite.toString(),
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
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
