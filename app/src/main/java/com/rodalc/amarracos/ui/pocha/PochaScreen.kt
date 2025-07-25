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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rodalc.amarracos.R
import com.rodalc.amarracos.data.generico.GenericoViewModel
import com.rodalc.amarracos.storage.DataStoreManager
import com.rodalc.amarracos.ui.elements.OptionsMenu
import com.rodalc.amarracos.ui.elements.SortMenu
import com.rodalc.amarracos.ui.elements.TitleTopBar
import com.rodalc.amarracos.ui.theme.AmarracosTheme
import com.rodalc.amarracos.utils.ToastRateLimiter

/**
 * Composable function that represents the screen for the Pocha game.
 * It displays the players, their scores, and allows the user to input bets and victories.
 *
 * @param modifier Modifier to be applied to the layout.
 * @param pochaViewModel ViewModel for managing the game state.
 * @param isPocha Boolean indicating whether the game is Pocha or a generic game.
 * @param onUpButtonClick Callback function for when the up button is clicked.
 * @param showResults Callback function for when the show results button is clicked.
 */
@Composable
fun PochaScreen(
    modifier: Modifier = Modifier,
    pochaViewModel: GenericoViewModel = viewModel(),
    isPocha: Boolean = true,
    onUpButtonClick: () -> Unit = {},
    showResults: () -> Unit = {},
) {
    val uiState by pochaViewModel.uiState.collectAsState()
    val context = LocalContext.current
    val canUndo = pochaViewModel.canUndo.collectAsState()

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
                title = stringResource(if (isPocha) R.string.title_pocha else R.string.title_generico),
                showUpButton = true,
                onUpButtonClick = onUpButtonClick,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    SortMenu(sortBy = {
                        pochaViewModel.sortPlayersBy(
                            sortType = it,
                            context = context
                        )
                    })
                    OptionsMenu(
                        undoEnabled = canUndo.value,
                        undo = { pochaViewModel.undo(context) },
                        showResults = showResults
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (!isPocha || uiState.rondaApuestas || !pochaViewModel.apuestasEqualVictorias()) {
                    pochaViewModel.changeRound(isPocha, context)
                } else {
                    ToastRateLimiter.showToast(
                        context = context,
                        message = context.getString(R.string.toast_error_bets)
                    )
                }
            }) {
                Icon(Icons.Rounded.Done, contentDescription = stringResource(R.string.desc_done))
            }

        }
    ) { innerPadding ->
        LazyVerticalGrid(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 8.dp)
                .fillMaxHeight(),
            columns = GridCells.Adaptive(minSize = 350.dp)
        ) {
            if (isPocha) {
                stickyHeader {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Card(
                            modifier = Modifier
//                        .fillMaxWidth(0.9f)
                                .padding(8.dp)
                                .clickable(onClick = {
                                    pochaViewModel.setDuplica(
                                        duplica = !uiState.duplica,
                                        context = context
                                    )
                                }),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(text = stringResource(R.string.text_double_points))
                                Spacer(modifier = Modifier.width(10.dp))
                                Switch(
                                    checked = uiState.duplica,
                                    onCheckedChange = {
                                        pochaViewModel.setDuplica(
                                            duplica = it,
                                            context = context
                                        )
                                    })
                            }
                        }
                    }
                }
            }
            items(uiState.jugadores) { jugador ->
                val pointType = if (isPocha) {
                    if (uiState.rondaApuestas)
                        GenericoViewModel.PointType.APUESTA
                    else
                        GenericoViewModel.PointType.VICTORIA
                } else {
                    GenericoViewModel.PointType.INCREMENTO
                }

                val isLastPlayer = uiState.jugadores.last() == jugador

                PlayerCard(
                    name = jugador.nombre,
                    isLastPlayer = isLastPlayer,
                    newPoints = if (isPocha) jugador.apuesta else jugador.incremento,
                    extraPoints = if (isPocha) jugador.victoria else null,
                    totalPoints = jugador.puntos,
                    roundApuestas = uiState.rondaApuestas
                ) { newPoints ->
                    pochaViewModel.updatePoints(
                        jugadorId = jugador.id,
                        newPoints = newPoints,
                        pointType = pointType,
                        context = context
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
    AmarracosTheme {
        PochaScreen()
    }
}