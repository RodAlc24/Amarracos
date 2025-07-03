package com.rodalc.amarracos.ui.mus

import android.content.res.Configuration
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rodalc.amarracos.data.mus.MusViewModel
import com.rodalc.amarracos.storage.DataStoreManager
import com.rodalc.amarracos.ui.elements.PopUp
import com.rodalc.amarracos.ui.elements.TitleTopBar
import com.rodalc.amarracos.ui.theme.AmarracosTheme

/**
 * Composable function for the Mus game screen.
 *
 * This function displays the game interface, including the scores for both teams,
 * buttons for bets, and options for undoing actions or calling an "órdago".
 * It adapts its layout based on the device orientation (portrait or landscape).
 *
 * The screen also includes a feature to keep the screen on if the corresponding setting is enabled.
 * An "órdago" popup is shown when the "Órdago" button is clicked, allowing the user to select the winner of the game.
 *
 * @param modifier Modifier for customizing the layout and appearance of the screen.
 * @param musViewModel ViewModel for managing the game state and logic.
 * @param onUpButtonClick Lambda function to be executed when the up button in the top bar is clicked.
 */
@Composable
fun MusScreen(
    modifier: Modifier = Modifier,
    musViewModel: MusViewModel = viewModel(),
    onUpButtonClick: () -> Unit = {}
) {
    val landscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    val uiState = musViewModel.uiState.collectAsState()
    val canUndo = musViewModel.canUndo.collectAsState()
    val context = LocalContext.current
    var ordago by rememberSaveable { mutableStateOf(false) }

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

    val content = @Composable { landscape: Boolean ->
        Pareja(
            name = uiState.value.nombreBuenos,
            juegos = uiState.value.juegosBuenos,
            puntos = uiState.value.puntosBuenos,
            increment = {
                musViewModel.incrementarPuntos(
                    team = MusViewModel.Teams.BUENOS,
                    increment = it,
                    context = context
                )
            },
            landscape = landscape
        ) {
            if (landscape) {
                ButtonUndo(
                    enabled = canUndo.value,
                    onClick = { musViewModel.undo(context) }
                )
            }
        }
        Envites(
            viewModel = musViewModel,
            modifier = if (landscape) Modifier.fillMaxHeight() else Modifier.fillMaxWidth(),
            landscape = landscape
        )
        Pareja(
            name = uiState.value.nombreMalos,
            juegos = uiState.value.juegosMalos,
            puntos = uiState.value.puntosMalos,
            increment = {
                musViewModel.incrementarPuntos(
                    team = MusViewModel.Teams.MALOS,
                    increment = it,
                    context = context
                )
            },
            landscape = landscape
        ) {
            if (landscape) {
                ButtonOrdago(
                    onClick = { ordago = true }
                )
            }
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TitleTopBar(
                title = "Mus",
                showUpButton = true,
                onUpButtonClick = onUpButtonClick,
            )
        }
    ) { innerPadding ->
        if (landscape) {
            Row(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                content(true)
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                content(false)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ButtonUndo(
                        enabled = canUndo.value,
                        onClick = { musViewModel.undo(context) }
                    )
                    ButtonOrdago(
                        onClick = { ordago = true }
                    )
                }
            }
        }
    }

    if (ordago) {
        PopUp(
            title = "Órdago",
            optionA = uiState.value.nombreBuenos,
            optionB = uiState.value.nombreMalos,
            onClickA = {
                musViewModel.ganadorJuego(winner = MusViewModel.Teams.BUENOS, context = context)
                ordago = false
            },
            onClickB = {
                musViewModel.ganadorJuego(winner = MusViewModel.Teams.MALOS, context = context)
                ordago = false
            },
            onDismiss = { ordago = false }
        )
    }
}


@Preview(device = "id:pixel_5")
@Composable
fun MusScreenPortraitPreview() {
    AmarracosTheme {
        MusScreen()
    }
}

@Preview(device = "spec:parent=pixel_5,orientation=landscape")
@Composable
fun MusScreenLandscapePreview() {
    AmarracosTheme {
        MusScreen()
    }
}
