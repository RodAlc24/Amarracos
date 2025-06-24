@file:OptIn(ExperimentalMaterial3Api::class)

package com.rodalc.amarracos.pocha

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.rodalc.amarracos.generico.Jugador
import com.rodalc.amarracos.resulltados.LegendLabelKey
import com.rodalc.amarracos.resulltados.Puntuaciones
import com.rodalc.amarracos.ui.theme.Playfair
import kotlinx.coroutines.runBlocking

@Composable
fun PantallaResultadosPocha(
    navController: NavController
) {
    val jugadores = Pocha.getJugadores()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        "Resultados",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontFamily = Playfair,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to game",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
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
                    extraStore[LegendLabelKey] = jugadores.map { jugador -> jugador.toString() }.toSet()
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(modifier = Modifier.fillMaxWidth(0.8f)) {
            Puntuaciones(modelProducer)
            }
        }
    }
}

