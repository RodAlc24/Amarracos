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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rodalc.amarracos.data.mus.MusViewModel
import com.rodalc.amarracos.storage.DataStoreManager
import com.rodalc.amarracos.ui.elements.TitleTopBar
import com.rodalc.amarracos.ui.theme.AmarracosTheme

@Composable
fun PantallaMus(
    modifier: Modifier = Modifier,
    musViewModel: MusViewModel = viewModel(),
    onUpButtonClick: () -> Unit = {}
) {
    val landscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    val uiState = musViewModel.uiState.collectAsState()
    val context = LocalContext.current

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
        )
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
        )
    }

    Scaffold(
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
            }
        }
    }
}


@Preview
@Composable
fun PantallaMusPreview() {
    AmarracosTheme {
        PantallaMus()
    }
}