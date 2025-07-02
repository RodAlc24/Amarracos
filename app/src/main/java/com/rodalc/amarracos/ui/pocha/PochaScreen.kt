package com.rodalc.amarracos.ui.pocha

import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rodalc.amarracos.data.generico.GenericoViewModel
import com.rodalc.amarracos.data.generico.JugadorGenericoUiState
import com.rodalc.amarracos.storage.DataStoreManager
import com.rodalc.amarracos.ui.elements.TitleTopBar
import com.rodalc.amarracos.ui.theme.AmarracosTheme

@Composable
fun PochaScreen(
    modifier: Modifier = Modifier,
    pochaViewModel: GenericoViewModel = viewModel(),
    onUpButtonClick: () -> Unit = {}
) {
    val uiState by pochaViewModel.uiState.collectAsState()
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

    Scaffold(
        modifier = modifier,
        topBar = {
            TitleTopBar(
                title = "Pocha",
                showUpButton = true,
                onUpButtonClick = onUpButtonClick,
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { pochaViewModel.changeRound() }) {
                Icon(Icons.Rounded.Done, contentDescription = "Hecho")
            }

        }
    ) { innerPadding ->
        LazyVerticalGrid(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 8.dp)
                .fillMaxHeight(),
            columns = GridCells.Adaptive(minSize = 400.dp)
        ) {
            stickyHeader {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Card(
                        modifier = Modifier
//                        .fillMaxWidth(0.9f)
                            .padding(8.dp)
                            .clickable(onClick = { pochaViewModel.setDuplica(!uiState.duplica) }),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(text = "Duplicar puntuación: ")
                            Spacer(modifier = Modifier.width(10.dp))
                            Switch(
                                checked = uiState.duplica,
                                onCheckedChange = { pochaViewModel.setDuplica(it) })
                        }
                    }
                }
            }
            items(uiState.jugadores) { jugador ->
                PlayerCard(
                    name = jugador.nombre,
                    newPoints = jugador.apuesta,
                    extraPoints = jugador.victoria,
                    totalPoints = jugador.puntos,
                    roundApuestas = uiState.rondaApuestas
                ) { newPoints ->
                    pochaViewModel.updatePoints(
                        jugadorId = jugador.id,
                        newPoints = newPoints,
                        pointType = if (uiState.rondaApuestas) GenericoViewModel.PointType.APUESTA else GenericoViewModel.PointType.VICTORIA
                    )
                }
            }
            stickyHeader {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Preview
@Composable
fun PreviewPochaScreen() {
    val pochaViewModel: GenericoViewModel = viewModel()
    pochaViewModel.startGame(
        jugadores = listOf(
            JugadorGenericoUiState(nombre = "Jugador 1"),
            JugadorGenericoUiState(nombre = "Jugador 2"),
            JugadorGenericoUiState(nombre = "Jugador 3")
        )
    )

    AmarracosTheme {
        PochaScreen()
    }
}