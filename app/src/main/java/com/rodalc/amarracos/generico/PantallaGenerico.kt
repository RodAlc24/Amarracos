@file:OptIn(ExperimentalMaterial3Api::class)

package com.rodalc.amarracos.generico

import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rodalc.amarracos.storage.DataStoreManager
import com.rodalc.amarracos.ui.theme.Playfair

/**
 * Gestiona toda la pantalla para el marcador de puntos.
 *
 * @param navController El controlador de navegación
 */
@Composable
fun PantallaGenerico(navController: NavController) {
    val context = LocalContext.current
    var state by rememberSaveable { mutableStateOf(Ronda.NOMBRES) }
    var canLoad by rememberSaveable { mutableStateOf(Generico.canLoadState(context)) }
    var jugadores by rememberSaveable { mutableStateOf(listOf(Jugador(1), Jugador(2))) }
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
                                        (jugadores + Jugador(jugadores.size + 1)).toMutableList()
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
                    if (!recuperar) FilaJugadorNombres(
                        jugador = it,
                        numJugadores = jugadores.size,
                        context = context
                    )
                },
                floatingIcon = { Icon(Icons.Rounded.Done, contentDescription = "Hecho") },
                nextRound = {
                    if (recuperar) {
                        Generico.loadState(context)
                        jugadores = Generico.getJugadores()
                    } else {
                        Generico.discardBackup(context)
                        Generico.setJugadores(jugadores)
                        Generico.saveState(context)
                    }
                    state = Ronda.JUEGO
                },
                jugadores = jugadores
            )
        }

        Ronda.JUEGO -> {
            Plantilla(
                navController = navController,
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
                    jugadores = Generico.getJugadores()
                    state = Ronda.CONTEO
                },
                undoEnabled = Generico.canUndo(),
                jugadores = Generico.getJugadores()
            )
        }

        Ronda.CONTEO -> {
            Plantilla(
                navController = navController,
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
                jugadores = Generico.getJugadores()
            )
        }
    }
}

/**
 * Plantilla para la pantalla de la pocha.
 *
 * @param options Si se mostrarán las opciones
 * @param navController El controlador de navegación
 * @param header El encabezado de la pantalla (un título...)
 * @param lineJugador La fila en la que se representará cada uno de los jugadores
 * @param nextRound Se llamará al cambiar de ronda
 * @param floatingIcon El icono del botón flotante
 * @param undo Se llamará pulsando el botón "volver"
 * @param undoEnabled Indica si se activa el botón "volver"
 * @param jugadores La lista con los jugadores de la partida
 */
@Composable
fun Plantilla(
    options: Boolean = true,
    navController: NavController,
    header: @Composable () -> Unit = {},
    lineJugador: @Composable (Jugador) -> Unit,
    nextRound: () -> Unit,
    floatingIcon: @Composable () -> Unit,
    undo: () -> Unit = {},
    undoEnabled: Boolean = false,
    jugadores: List<Jugador>
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.primaryContainer,
                ),
                title = {
                    Text(
                        "Puntuaciones",
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
                    .weight(1f),
                contentPadding = PaddingValues(10.dp)
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
