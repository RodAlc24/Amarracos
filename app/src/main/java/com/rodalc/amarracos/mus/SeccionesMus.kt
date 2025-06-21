package com.rodalc.amarracos.mus

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rodalc.amarracos.main.repeatingClickable

/**
 * Conjunto de botones y puntos para cada pareja
 *
 * @param buenos Si es la primera pareja
 * @param landscape Si el dispositivo está en horizontal
 * @param viewModel El VM del mus
 * @param modifier El modificador
 */
@Composable
fun BotonesPareja(
    buenos: Boolean,
    landscape: Boolean,
    viewModel: MusViewModel,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val pareja by if (buenos) viewModel.buenos.collectAsState() else viewModel.malos.collectAsState()

    val nombreVictorias = @Composable {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(vertical = 10.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.width(40.dp)
                ) {
                    Text(
                        text = pareja.victorias.toString(),
                        fontSize = 25.sp,
                        maxLines = 1,
                    )
                }
                Text(
                    text = ":${pareja.nombre}",
                    fontSize = 25.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Right
                )
            }
        }
    }

    val puntos = @Composable {
        TextButton(
            onClick = {
                Mus.pushState()
                if (buenos) {
                    viewModel.updateBuenos(pareja.copy(puntos = pareja.puntos + 1))
                } else {
                    viewModel.updateMalos(pareja.copy(puntos = pareja.puntos + 1))
                }
                Mus.saveState(context)
            },
            modifier = Modifier
                .width(110.dp)
        ) {
            Text(
                text = pareja.puntos.toString(),
                fontSize = 60.sp,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }

    val reducirPuntos = @Composable {
        val enabled = pareja.puntos > 0
        val tint =
            if (enabled) ButtonDefaults.textButtonColors().contentColor else ButtonDefaults.textButtonColors().disabledContentColor
        TextButton(
            onClick = {
                Mus.pushState()
                if (buenos) {
                    viewModel.updateBuenos(pareja.copy(puntos = pareja.puntos - 1))
                } else {
                    viewModel.updateMalos(pareja.copy(puntos = pareja.puntos - 1))
                }
                Mus.saveState(context)
            },
            enabled = enabled,
            modifier = Modifier
                .padding(5.dp)
                .size(60.dp)
        ) { Icon(Icons.Rounded.Remove, "Remove", Modifier.size(40.dp), tint = tint) }
    }

    val aumentarPuntos = @Composable {
        val tint = ButtonDefaults.textButtonColors().contentColor
        TextButton(
            onClick = {
                Mus.pushState()
                if (buenos) {
                    viewModel.updateBuenos(pareja.copy(puntos = pareja.puntos + 1))
                } else {
                    viewModel.updateMalos(pareja.copy(puntos = pareja.puntos + 1))
                }
                Mus.saveState(context)
            },
            modifier = Modifier
                .padding(5.dp)
                .size(60.dp)
        ) { Icon(Icons.Rounded.Add, "Add", Modifier.size(40.dp), tint = tint) }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        nombreVictorias()
        if (landscape) puntos()
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(10.dp)
        ) {
            reducirPuntos()
            if (!landscape) puntos()
            aumentarPuntos()
        }
    }
}

/**
 * Conjunto de botones y textos con los envites actuales.
 *
 * @param viewModel El VM del mus
 * @param landscape Si el dispositivo está en horizontal
 */
@Composable
fun EnvitesYDeshacer(
    viewModel: MusViewModel,
    landscape: Boolean,
) {
    val envites by viewModel.envites.collectAsState()

    val botonesEnvites = @Composable {
        BotonesEnvite(landscape, envites.grande, viewModel) {
            viewModel.updateEnvites(envites.copy(grande = it))
        }
        BotonesEnvite(landscape, envites.chica, viewModel) {
            viewModel.updateEnvites(envites.copy(chica = it))
        }
        BotonesEnvite(landscape, envites.pares, viewModel) {
            viewModel.updateEnvites(envites.copy(pares = it))
        }
        BotonesEnvite(landscape, envites.juego, viewModel) {
            viewModel.updateEnvites(envites.copy(juego = it))
        }
    }

    if (landscape) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            botonesEnvites()
        }
    } else {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            botonesEnvites()
        }
    }
}

/**
 * Conjunto de botones y un texto para gestionar un envite.
 *
 * @param landscape Si el dispositivo está en horizontal
 * @param envite La cantidad del envite
 * @param viewModel El VM del mus
 * @param updateEnvite Se ejecuta al modificar el valor del envite (e.g. para aumentarlo)
 */
@Composable
fun BotonesEnvite(
    landscape: Boolean,
    envite: Int,
    viewModel: MusViewModel,
    updateEnvite: (Int) -> Unit
) {
    val context = LocalContext.current
    val buenos by viewModel.buenos.collectAsState()
    val malos by viewModel.malos.collectAsState()

    val botonPuntuacion = @Composable { botonSuma: Boolean ->
        val enabled = if (botonSuma) envite < 99 else envite > 0
        val tint =
            if (enabled) ButtonDefaults.textButtonColors().contentColor else ButtonDefaults.textButtonColors().disabledContentColor

        TextButton(
            modifier = Modifier.repeatingClickable(
                interactionSource = remember { MutableInteractionSource() },
                onClick = {
                    Mus.pushState()
                    updateEnvite(envite + if (botonSuma) 1 else -1)
                    Mus.saveState(context)
                },
                enabled = enabled,
            ),
            onClick = {
                Mus.pushState()
                updateEnvite(envite + if (botonSuma) 1 else -1)
                Mus.saveState(context)
            },
            enabled = enabled
        ) {
            if (botonSuma) {
                Icon(Icons.Rounded.Add, contentDescription = "Aumentar envite", tint = tint)
            } else {
                Icon(Icons.Rounded.Remove, contentDescription = "Reducir envite", tint = tint)
            }
        }
    }

    val botonConteo = @Composable { botonBuenos: Boolean ->
        FilledIconButton(
            onClick = {
                Mus.pushState()
                if (botonBuenos) {
                    viewModel.updateBuenos(buenos.copy(puntos = buenos.puntos + envite))
                } else {
                    viewModel.updateMalos(malos.copy(puntos = malos.puntos + envite))
                }
                updateEnvite(0)
                Mus.saveState(context)
            }, enabled = envite != 0
        ) {
            if (landscape) {
                if (botonBuenos) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Buenos ganan")
                } else {
                    Icon(Icons.AutoMirrored.Rounded.ArrowForward, "Malos ganan")
                }
            } else {
                if (botonBuenos) {
                    Icon(Icons.Rounded.ArrowUpward, "Buenos ganan")
                } else {
                    Icon(Icons.Rounded.ArrowDownward, "Malos ganan")
                }
            }
        }
    }

    val texto = @Composable {
        Box(
            modifier = Modifier.width(50.dp),
            contentAlignment = Alignment.Center
        ) {
            TextButton(
                onClick = {
                    Mus.pushState()
                    updateEnvite(envite + 2)
                    Mus.saveState(context)
                },
                enabled = envite < 98
            ) {
                Text(
                    text = envite.toString(),
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }

    val modifier = Modifier.padding(10.dp)

    if (landscape) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = modifier.fillMaxWidth()
        ) {
            botonConteo(true)
            botonPuntuacion(false)
            texto()
            botonPuntuacion(true)
            botonConteo(false)
        }
    } else {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.fillMaxHeight()
        ) {
            botonConteo(true)
            botonPuntuacion(true)
            texto()
            botonPuntuacion(false)
            botonConteo(false)
        }
    }
}
