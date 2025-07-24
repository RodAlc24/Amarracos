package com.rodalc.amarracos.ui.overview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.rodalc.amarracos.R
import com.rodalc.amarracos.data.generico.JugadorGenericoUiState
import com.rodalc.amarracos.ui.elements.TitleTopBar
import com.rodalc.amarracos.ui.theme.AmarracosTheme
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
    potgName: String,
    potgPoints: Int,
    onUpButtonClick: () -> Unit = {},
) {
    val winner = jugadores.maxByOrNull { it.puntos }?.nombre ?: ""
    val points = jugadores.maxByOrNull { it.puntos }?.puntos ?: 0

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

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(8.dp)
                ) {
                    Chart(modelProducer, modifier = Modifier.padding(16.dp))
                }
            }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(
                            Icons.Outlined.EmojiEvents,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(8.dp)
                        )
                        Text(text = "Ganador: $winner ($points)")
                    }
                }
            }
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(
                            Icons.AutoMirrored.Outlined.TrendingUp,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(8.dp)
                        )
                        Text(text = "Mejor jugada: $potgName ($potgPoints)")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewOverviewScreen() {
    AmarracosTheme {
        val jugadores = listOf(
            JugadorGenericoUiState(nombre = "Jugador 1"),
            JugadorGenericoUiState(nombre = "Jugador 2")
        )
        OverviewScreen(
            jugadores = jugadores,
            potgName = "Jugador 1",
            potgPoints = 100
        )
    }
}
