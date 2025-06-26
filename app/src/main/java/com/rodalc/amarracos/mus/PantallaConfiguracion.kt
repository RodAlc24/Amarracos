package com.rodalc.amarracos.mus

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rodalc.amarracos.main.ToastRateLimiter
import com.rodalc.amarracos.storage.DataStoreManager
import com.rodalc.amarracos.ui.elements.TitleTopBar
import kotlinx.coroutines.async

/**
 * Pantalla para poner los nombres y el límite de puntos.
 * Solo debería llamarse una vez, al empezar la partida.
 *
 * @param canLoad Si se puede cargar una partida guardada
 * @param navController El controlador de navegación
 * @param context El contexto de la aplicación
 * @param show Si se muestra o no esta pantalla
 */
@Composable
fun PantallaConfiguracion(
    canLoad: Boolean,
    navController: NavController,
    context: Context,
    show: (Boolean) -> Unit
) {
    var buenos by rememberSaveable { mutableStateOf("") }
    var malos by rememberSaveable { mutableStateOf("") }
    var recuperar by rememberSaveable { mutableStateOf(canLoad) }

    MusDefaultConfigManager.loadState(context)
    val labelBuenos = MusDefaultConfigManager.getBuenos()
    val labelMalos = MusDefaultConfigManager.getMalos()

    val puntos30 by DataStoreManager.readDataStore(context, DataStoreManager.Key.MUS_A_30)
        .collectAsState(initial = true)
    val coreutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TitleTopBar(
                title = "Mus",
                backButtonAction = { navController.popBackStack() },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (recuperar) {
                    Mus.loadState((context))
                    show(false)
                } else {
                    Mus.discardBackup(context)
                    Mus.reset()
                    Mus.getBuenos().nombre = if (buenos == "") labelBuenos else buenos
                    Mus.getMalos().nombre = if (malos == "") labelMalos else malos
                    MusDefaultConfigManager.setBuenos(Mus.getBuenos().nombre)
                    MusDefaultConfigManager.setMalos(Mus.getMalos().nombre)
                    MusDefaultConfigManager.saveState(context)
                    Mus.setPuntos(if (puntos30) 30 else 40)
                    Mus.saveState(context)
                    show(false)
                }
            }) { Icon(Icons.Rounded.Done, contentDescription = "Done") }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .clickable(onClick = { recuperar = true })
                ) {
                    RadioButton(
                        selected = recuperar,
                        onClick = { recuperar = true },
                        enabled = canLoad
                    )
                    Text(text = "Recuperar partida guardada")
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .clickable(onClick = { recuperar = false })
                ) {
                    RadioButton(selected = !recuperar, onClick = { recuperar = false })
                    Text(text = "Nueva partida")
                }
                if (!recuperar) {
                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(30.dp))
                    TextField(
                        modifier = Modifier.fillMaxWidth(0.7f),
                        value = buenos,
                        onValueChange = {
                            if (it.length <= 10) {
                                buenos = it
                            } else ToastRateLimiter.showToast(context, "¡Pon un nombre más corto!")
                        },
                        maxLines = 1,
                        placeholder = { Text(text = labelBuenos) },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    TextField(
                        modifier = Modifier.fillMaxWidth(0.7f),
                        value = malos,
                        onValueChange = {
                            if (it.length <= 10) {
                                malos = it
                            } else ToastRateLimiter.showToast(context, "¡Pon un nombre más corto!")
                        },
                        maxLines = 1,
                        placeholder = { Text(text = labelMalos) },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        )
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(text = "Puntos para ganar:")
                    Spacer(modifier = Modifier.height(5.dp))
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(10.dp))
                        RadioButton(selected = puntos30, onClick = {
                            coreutineScope.async {
                                DataStoreManager.setDataStore(
                                    context,
                                    DataStoreManager.Key.MUS_A_30,
                                    true
                                )
                            }
                        })
                        Text(text = "30")
                        Spacer(modifier = Modifier.width(10.dp))
                        RadioButton(selected = !puntos30, onClick = {
                            coreutineScope.async {
                                DataStoreManager.setDataStore(
                                    context,
                                    DataStoreManager.Key.MUS_A_30,
                                    false
                                )
                            }
                        })
                        Text(text = "40")
                    }
                }
            }
        }
    }
}

