@file:OptIn(ExperimentalMaterial3Api::class)

package com.rodalc.amarracos.ui.mainScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rodalc.amarracos.comun.Jugador
import com.rodalc.amarracos.data.mus.MusViewModel
import com.rodalc.amarracos.generico.Generico
import com.rodalc.amarracos.pocha.Pocha
import com.rodalc.amarracos.ui.elements.TitleTopBar
import com.rodalc.amarracos.ui.tabs.GenericoTabScreen
import com.rodalc.amarracos.ui.tabs.MusTabScreen
import com.rodalc.amarracos.ui.theme.AmarracosTheme

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    musViewModel: MusViewModel = viewModel(),
    navigate: (String) -> Unit = {},
) {
    val navController = rememberNavController()
    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TitleTopBar(
                title = "Amarracos",
                actions = {
                    IconButton(
                        onClick = { navigate(Screens.SCREEN_CONFIG.name) }
                    ) { Icon(Icons.Outlined.Settings, "Configuración") }
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PrimaryTabRow(
                selectedTabIndex = selectedIndex,
            ) {
                Tabs.entries.forEachIndexed { index, destination ->
                    Tab(
                        selected = index == selectedIndex,
                        onClick = {
                            navController.popBackStack()
                            navController.navigate(route = destination.name)
                            selectedIndex = index
                        },
                        text = {
                            Text(
                                text = destination.label,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    )
                }
            }
            val context = LocalContext.current
            NavHost(
                navController = navController,
                startDestination = Tabs.TAB_MUS.name,
                contentAlignment = Alignment.TopCenter
            ) {
                composable(route = Tabs.TAB_MUS.name) {
                    MusTabScreen(
                        canLoad = musViewModel.canLoadState(context),
                        labelBuenos = "Buenos", //TODO
                        labelMalos = "Malos", //TODO
                        onStartClick = { nombreBuenos, nombreMalos, puntos ->
                            musViewModel.startGame(
                                nombreBuenos = nombreBuenos,
                                nombreMalos = nombreMalos,
                                puntos30 = puntos,
                                context = context
                            )
                            navigate(Screens.SCREEN_MUS.name)
                        },
                        onLoadClick = {
                            musViewModel.loadState(context)
                            navigate(Screens.SCREEN_MUS.name)
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                    )
                }
                composable(route = Tabs.TAB_POCHA.name) {
                    var jugadores by rememberSaveable {
                        mutableStateOf(
                            listOf(
                                Jugador(1),
                                Jugador(2)
                            )
                        )
                    }
                    GenericoTabScreen(
                        jugadores = jugadores,
                        canLoad = Pocha.canLoadState(context),
                        addJugador = { jugadores = jugadores + Jugador(jugadores.size + 1) },
                        removeJugador = { jugadores = jugadores.dropLast(1) },
                        onStartClick = {
                            Pocha.discardBackup(context)
                            Pocha.setJugadores(jugadores)
                            Pocha.saveState(context)
                            navigate(Screens.SCREEN_POCHA.name)
                        },
                        onLoadClick = {
                            Pocha.loadState(context)
                            navigate(Screens.SCREEN_POCHA.name)
                        },
                        modifier = Modifier.fillMaxWidth(0.8f)
                    )
                }
                composable(route = Tabs.TAB_GENERICO.name) {
                    var jugadores by rememberSaveable {
                        mutableStateOf(
                            listOf(
                                Jugador(1),
                                Jugador(2)
                            )
                        )
                    }
                    GenericoTabScreen(
                        jugadores = jugadores,
                        canLoad = Generico.canLoadState(context),
                        addJugador = { jugadores = jugadores + Jugador(jugadores.size + 1) },
                        removeJugador = { jugadores = jugadores.dropLast(1) },
                        onStartClick = {
                            Generico.discardBackup(context)
                            Generico.setJugadores(jugadores)
                            Generico.saveState(context)
                            navigate(Screens.SCREEN_GENERICO.name)
                        },
                        onLoadClick = {
                            Generico.loadState(context)
                            navigate(Screens.SCREEN_GENERICO.name)
                        },
                        modifier = Modifier.fillMaxWidth(0.8f)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewMainScreen() {
    AmarracosTheme {
        MainScreen()
    }
}