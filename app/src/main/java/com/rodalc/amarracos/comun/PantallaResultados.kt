package com.rodalc.amarracos.comun

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.rodalc.amarracos.generico.Generico
import com.rodalc.amarracos.pocha.Pocha
import com.rodalc.amarracos.resultados.LegendLabelKey
import com.rodalc.amarracos.resultados.Puntuaciones
import com.rodalc.amarracos.ui.elements.TitleTopBar
import kotlinx.coroutines.runBlocking

@Composable
fun PantallaResultados(
    pocha: Boolean,
    navController: NavController
) {
    val jugadores = if (pocha == true) Pocha.getJugadores() else Generico.getJugadores()
    Scaffold(
        topBar = {
            TitleTopBar(
                title = "Resultados",
                backButtonAction = { navController.popBackStack() },
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
                        jugadores.map { jugador -> jugador.toString() }.toSet()
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

