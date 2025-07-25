package com.rodalc.amarracos.ui.overview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.rodalc.amarracos.R
import com.rodalc.amarracos.data.generico.JugadorGenericoUiState
import com.rodalc.amarracos.ui.elements.TitleTopBar
import kotlinx.coroutines.runBlocking

/**
 * Composable function that displays the overview screen.
 * This screen shows a line chart with the historical points of each player.
 *
 * @param jugadores The list of players with their historical points.
 * @param onUpButtonClick The callback to be invoked when the up button is clicked.
 */
@Composable
fun OverviewScreen(
    jugadores: List<JugadorGenericoUiState>,
    onUpButtonClick: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            TitleTopBar(
                title = stringResource(R.string.title_results),
                showUpButton = true,
                onUpButtonClick = onUpButtonClick,
            )
        },
    ) { padding ->
        val modelProducer = remember { CartesianChartModelProducer() }
        runBlocking {
            modelProducer.runTransaction {
                lineSeries {
                    jugadores.forEach { jugador ->
                        series(
                            jugador.historicoPuntos.keys,
                            jugador.historicoPuntos.values
                        )
                    }
                }
                extras { extraStore ->
                    extraStore[LegendLabelKey] =
                        jugadores.map { jugador -> jugador.nombre }.toSet()
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.TopCenter
        ) {
            Card(modifier = Modifier.fillMaxWidth(0.9f)) {
                Chart(modelProducer, modifier = Modifier.padding(16.dp))
            }
        }
    }
}

