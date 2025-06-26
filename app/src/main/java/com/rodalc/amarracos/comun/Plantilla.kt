package com.rodalc.amarracos.comun

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rodalc.amarracos.ui.elements.TitleTopBar

/**
 * Plantilla para la pantalla de la pocha y genérico.
 *
 * @param title El título de la pantalla
 * @param options Si se mostrarán las opciones
 * @param navController El controlador de navegación
 * @param header El encabezado de la pantalla (un título...)
 * @param lineJugador La fila en la que se representará cada uno de los jugadores
 * @param nextRound Se llamará al cambiar de ronda
 * @param floatingIcon El icono del botón flotante
 * @param undo Se llamará pulsando el botón "volver"
 * @param undoEnabled Indica si se activa el botón "volver"
 * @param jugadores La lista con los jugadores de la partida
 */
@Composable
fun Plantilla(
    title: String,
    options: Boolean = true,
    navController: NavController,
    pantallaResultadosId: String,
    header: @Composable () -> Unit = {},
    lineJugador: @Composable (Jugador) -> Unit,
    nextRound: () -> Unit,
    floatingIcon: @Composable () -> Unit,
    undo: () -> Unit = {},
    undoEnabled: Boolean = false,
    jugadores: List<Jugador>,
) {
    Scaffold(
        topBar = {
            TitleTopBar(
                title = title,
                backButtonAction = { navController.popBackStack() },
                actions = {
                    if (options) {
                        SortMenu()
                        OptionsMenu(
                            undoEnabled = undoEnabled,
                            undo = undo,
                            showResults = { navController.navigate(pantallaResultadosId) }
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { nextRound() }) {
                floatingIcon()
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            header()
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(top = 11.dp, bottom = 10.dp)
            ) {
                items(jugadores) { jugador ->
                    lineJugador(jugador)
                    Spacer(modifier = Modifier.height(10.dp))
                }
                item {
                    Spacer(modifier = Modifier.height(70.dp))
                }
            }
        }
    }
}
