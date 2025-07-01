package com.rodalc.amarracos.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rodalc.amarracos.comun.PantallaResultados
import com.rodalc.amarracos.data.mus.MusViewModel
import com.rodalc.amarracos.generico.PantallaGenerico
import com.rodalc.amarracos.pocha.PantallaPocha
import com.rodalc.amarracos.ui.main.MainScreen
import com.rodalc.amarracos.ui.main.Screens
import com.rodalc.amarracos.ui.main.SettingsScreen
import com.rodalc.amarracos.ui.mus.MusScreen
import com.rodalc.amarracos.ui.theme.AmarracosTheme

/**
 * Main Composable of the application that manages navigation between different screens.
 * It uses a [NavHost] to define routes and their corresponding screens.
 *
 * The available screens are:
 * - [Screens.SCREEN_START]: Main screen ([MainScreen]).
 * - [Screens.SCREEN_MUS]: Screen for the Mus game ([MusScreen]).
 * - [Screens.SCREEN_POCHA]: Screen for the Pocha game ([PantallaPocha]).
 * - [Screens.SCREEN_GENERICO]: Screen for a generic game ([PantallaGenerico]).
 * - [Screens.SCREEN_CONFIG]: Configuration screen ([SettingsScreen]).
 * - [Screens.SCREEN_RES_POCHA]: Results screen for Pocha ([PantallaResultados]).
 * - [Screens.SCREEN_RES_GEN]: Results screen for the generic game ([PantallaResultados]).
 */
@Composable
fun AmarracosScreen() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen =
        Screens.valueOf(backStackEntry?.destination?.route ?: Screens.SCREEN_START.name)

    val musViewModel: MusViewModel = viewModel()

    NavHost(navController = navController, startDestination = Screens.SCREEN_START.name) {
        composable(Screens.SCREEN_START.name) {
            MainScreen(
                navigate = { navController.navigate(it) },
                musViewModel = musViewModel
            )
        }
        composable(Screens.SCREEN_MUS.name) {
            MusScreen(
                musViewModel = musViewModel,
                onUpButtonClick = { navController.navigateUp() },
                )
        }
        composable(Screens.SCREEN_POCHA.name) { PantallaPocha(navController) }
        composable(Screens.SCREEN_GENERICO.name) { PantallaGenerico(navController) }
        composable(Screens.SCREEN_CONFIG.name) { SettingsScreen(navigateUp = { navController.navigateUp() }) }
        composable(Screens.SCREEN_RES_POCHA.name) {
            PantallaResultados(
                pocha = true,
                navController
            )
        }
        composable(Screens.SCREEN_RES_GEN.name) {
            PantallaResultados(
                pocha = false,
                navController
            )
        }
    }
}

/**
 * Función auxiliar para Preview
 */
@Preview(
    showBackground = true,
    device = "id:pixel", backgroundColor = 0xFFFFFFFF
)
@Composable
fun PreviewAmarracosApp() {
    AmarracosTheme {
        AmarracosScreen()
    }
}
