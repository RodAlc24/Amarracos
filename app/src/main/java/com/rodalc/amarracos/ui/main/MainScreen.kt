@file:OptIn(ExperimentalMaterial3Api::class)

package com.rodalc.amarracos.ui.main

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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rodalc.amarracos.R
import com.rodalc.amarracos.data.generico.GenericoViewModel
import com.rodalc.amarracos.data.mus.MusDefaultConfig
import com.rodalc.amarracos.data.mus.MusViewModel
import com.rodalc.amarracos.ui.elements.TitleTopBar
import com.rodalc.amarracos.ui.tabs.GenericoTabScreen
import com.rodalc.amarracos.ui.tabs.MusTabScreen
import com.rodalc.amarracos.ui.theme.AmarracosTheme

/**
 * Composable function for the main screen of the application.
 * This screen displays a tab layout for different game modes (Mus, Pocha, Generic).
 * It uses a [Scaffold] to provide a top app bar with a title and a settings icon.
 * The main content area uses a [PrimaryTabRow] to switch between different game tabs.
 * Each tab hosts a specific game screen ([MusTabScreen] or [GenericoTabScreen])
 * and handles starting new games or loading existing ones.
 *
 * @param modifier Optional [Modifier] for the root composable.
 * @param musViewModel The [MusViewModel] used for the Mus game tab.
 * @param genericoViewModel The [GenericoViewModel] used for the Generic game tab.
 * @param pochaViewModel The [GenericoViewModel] used for the Pocha game tab.
 * @param navigate A lambda function to handle navigation to other screens. It takes a route string as a parameter.
 */
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    musViewModel: MusViewModel = viewModel(),
    genericoViewModel: GenericoViewModel = viewModel(),
    pochaViewModel: GenericoViewModel = viewModel(),
    navigate: (String) -> Unit = {},
) {
    val navController = rememberNavController()
    val context = LocalContext.current
    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TitleTopBar(
                title = stringResource(id = R.string.app_name),
                actions = {
                    IconButton(
                        onClick = { navigate(Screens.SCREEN_CONFIG.name) }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = stringResource(id = R.string.desc_config)
                        )
                    }
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
                                text = stringResource(id = destination.title),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    )
                }
            }
            NavHost(
                navController = navController,
                startDestination = Tabs.TAB_MUS.name,
                contentAlignment = Alignment.TopCenter
            ) {
                composable(route = Tabs.TAB_MUS.name) {
                    MusTabScreen(
                        canLoad = musViewModel.canLoadState(context),
                        labelBuenos = MusDefaultConfig.load(context).nameBuenos,
                        labelMalos = MusDefaultConfig.load(context).nameMalos,
                        puntos30 = MusDefaultConfig.load(context).puntos30,
                        onStartClick = { nombreBuenos, nombreMalos, puntos ->
                            MusDefaultConfig.save(
                                context = context,
                                defaultMus = MusDefaultConfig.DefaultMus(
                                    nameBuenos = nombreBuenos,
                                    nameMalos = nombreMalos,
                                    puntos30 = puntos
                                )
                            )
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
                    GenericoTabScreen(
                        canLoad = pochaViewModel.canLoadState(context = context, isPocha = true),
                        onStartClick = {
                            pochaViewModel.startGame(
                                jugadores = it,
                                context = context,
                                isPocha = true
                            )
                            navigate(Screens.SCREEN_POCHA.name)
                        },
                        onLoadClick = {
                            pochaViewModel.loadState(context = context, isPocha = true)
                            navigate(Screens.SCREEN_POCHA.name)
                        },
                        modifier = Modifier.fillMaxWidth(0.8f)
                    )
                }
                composable(route = Tabs.TAB_GENERICO.name) {
                    GenericoTabScreen(
                        canLoad = genericoViewModel.canLoadState(
                            context = context,
                            isPocha = false
                        ),
                        onStartClick = {
                            genericoViewModel.startGame(jugadores = it, context = context)
                            navigate(Screens.SCREEN_GENERICO.name)
                        },
                        onLoadClick = {
                            genericoViewModel.loadState(context = context, isPocha = false)
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