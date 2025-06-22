@file:OptIn(ExperimentalMaterial3Api::class)

package com.rodalc.amarracos.pocha

import android.content.Context
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rodalc.amarracos.generico.OptionsMenu
import com.rodalc.amarracos.generico.SortMenu
import com.rodalc.amarracos.main.ToastRateLimiter
import com.rodalc.amarracos.storage.DataStoreManager
import com.rodalc.amarracos.ui.theme.Playfair

/**
 * Gestiona toda la pantalla para el juego de la pocha.
 *
 * @param navController El controlador de navegación
 */
@Composable
fun PantallaPocha(navController: NavController) {
    val context = LocalContext.current
    var state by rememberSaveable { mutableStateOf(Ronda.NOMBRES) }
    var duplica by rememberSaveable { mutableStateOf(Pocha.getDuplica()) }
    var canLoad by rememberSaveable { mutableStateOf(Pocha.canLoadState(context)) }
    var jugadores by remember { mutableStateOf(listOf(JugadorPocha(1), JugadorPocha(2))) }
    var recuperar by rememberSaveable { mutableStateOf(canLoad) }

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
        Ronda.NOMBRES -> {
            Plantilla(
                options = false,
                navController = navController,
                header = {
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        RadioButton(
                            selected = recuperar,
                            onClick = { recuperar = true },
                            enabled = canLoad
                        )
                        Text(
                            text = "Recuperar partida guardada",
                            modifier = Modifier.clickable(onClick = { recuperar = true })
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        RadioButton(selected = !recuperar, onClick = { recuperar = false })
                        Text(
                            text = "Nueva partida",
                            modifier = Modifier.clickable(onClick = { recuperar = false })
                        )
                    }
                    if (!recuperar) {
                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            IconButton(
                                onClick = { jugadores = jugadores.dropLast(1).toMutableList() },
                                enabled = jugadores.size > 2
                            ) {
                                Icon(
                                    Icons.Rounded.Remove,
                                    "Quitar jugador",
                                    tint = if (jugadores.size > 2) ButtonDefaults.textButtonColors().contentColor else ButtonDefaults.textButtonColors().disabledContentColor
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Box(
                                modifier = Modifier.width(30.dp),
                                contentAlignment = Alignment.Center
                            ) { Text(text = jugadores.size.toString()) }
                            Spacer(modifier = Modifier.width(10.dp))
                            IconButton(
                                onClick = {
                                    jugadores =
                                        (jugadores + JugadorPocha(jugadores.size + 1)).toMutableList()
                                },
                                enabled = jugadores.size < 100
                            ) {
                                Icon(
                                    Icons.Rounded.Add,
                                    "Añadir jugador",
                                    tint = if (jugadores.size < 100) ButtonDefaults.textButtonColors().contentColor else ButtonDefaults.textButtonColors().disabledContentColor
                                )
                            }
                        }
                    }
                },
                lineJugador = {
                    if (!recuperar) {
                        FilaJugadorNombres(
                            jugador = it,
                            numJugadores = jugadores.size,
                            context = context
                        )
                    }
                },
                floatingIcon = { Icon(Icons.Rounded.Done, contentDescription = "Hecho") },
                nextRound = {
                    if (recuperar) {
                        Pocha.loadState(context)
                        jugadores = Pocha.getJugadores()
                    } else {
                        Pocha.discardBackup(context)
                        Pocha.setJugadores(jugadores)
                        Pocha.saveState(context)
                    }
                    state = Ronda.APUESTAS
                },
                jugadores = jugadores
            )
        }

        Ronda.APUESTAS -> {
            Plantilla(
                navController = navController,
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
                            colors = SwitchDefaults.colors(
                                uncheckedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                                uncheckedBorderColor = MaterialTheme.colorScheme.primary,
                                uncheckedThumbColor = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                },
                lineJugador = { jugador ->
                    FilaJugador(
                        jugador = jugador,
                        ronda = Ronda.APUESTAS
                    )
                },
                nextRound = {
                    state = Ronda.CONTEO
                },
                floatingIcon = { Icon(Icons.Rounded.Add, contentDescription = "Nueva ronda") },
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
                navController = navController,
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
                            colors = SwitchDefaults.colors(
                                uncheckedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                                uncheckedBorderColor = MaterialTheme.colorScheme.primary,
                                uncheckedThumbColor = MaterialTheme.colorScheme.primary
                            )
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
                        state = Ronda.APUESTAS
                    } else ToastRateLimiter.showToast(
                        context,
                        "Las apuestas no pueden coincidir con el número de rondas jugadas"
                    )
                },
                floatingIcon = { Icon(Icons.Rounded.Done, contentDescription = "Hecho") },
                undo = { state = Ronda.APUESTAS },
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
    jugador: JugadorPocha,
    ronda: Ronda
) {
    var valorState by rememberSaveable { mutableIntStateOf(if (ronda == Ronda.APUESTAS) jugador.apuesta else jugador.victoria) }
    val content = ButtonDefaults.textButtonColors().contentColor
    val contentDisabled = ButtonDefaults.textButtonColors().disabledContentColor
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
            text = if (ronda == Ronda.APUESTAS) jugador.puntos.toString() else jugador.apuesta.toString(),
            modifier = Modifier.padding(10.dp)
        )
        IconButton(
            onClick = {
                valorState -= 1
                if (ronda == Ronda.APUESTAS) {
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
            if (ronda == Ronda.APUESTAS) {
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
                if (ronda == Ronda.APUESTAS) {
                    jugador.apuesta = valorState
                } else {
                    jugador.victoria = valorState
                }
            }, enabled = valorState < 99
        ) { Icon(Icons.Rounded.Add, contentDescription = "Añadir uno a $jugador", tint = tintB) }
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
fun FilaJugadorNombres(jugador: JugadorPocha, numJugadores: Int, context: Context) {
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
        keyboardOptions = KeyboardOptions(imeAction = if (jugador.id == numJugadores) ImeAction.Done else ImeAction.Next),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
        )
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
    options: Boolean = true,
    navController: NavController,
    header: @Composable () -> Unit,
    lineJugador: @Composable (JugadorPocha) -> Unit,
    nextRound: () -> Unit,
    floatingIcon: @Composable () -> Unit,
    undo: () -> Unit = {},
    undoEnabled: Boolean = false,
    jugadores: List<JugadorPocha>
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        "Pocha",
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
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                actions = {
                    if (options) {
                        SortMenu()
                        OptionsMenu(undoEnabled = undoEnabled, undo = undo)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { nextRound() }) {
                floatingIcon()
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            header()
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
                item {
                    Spacer(modifier = Modifier.height(70.dp))
                }
            }
        }
    }
}
