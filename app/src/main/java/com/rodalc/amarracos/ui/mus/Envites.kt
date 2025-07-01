package com.rodalc.amarracos.ui.mus

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rodalc.amarracos.data.mus.MusViewModel
import com.rodalc.amarracos.ui.theme.AmarracosTheme
import com.rodalc.amarracos.utils.repeatingClickable

/**
 * Composable function that displays a single "envite" (bet) in a Mus game.
 * It shows the current value of the envite and provides buttons to increment/decrement the value
 * and assign the envite to a team.
 *
 * @param value The current value of the envite.
 * @param modifier The modifier to be applied to the layout.
 * @param increment A lambda function to increment or decrement the envite value.
 *                  It takes an integer as input (positive for increment, negative for decrement).
 * @param updateEnvite A lambda function to update the team that won the envite.
 *                     It takes a [MusViewModel.Teams] enum as input.
 * @param landscape A boolean indicating whether the layout should be optimized for landscape orientation.
 *                  If true, the elements are arranged in a Row.
 *                  If false, the elements are arranged in a Column.
 */
@Composable
private fun Envite(
    value: Int,
    modifier: Modifier = Modifier,
    increment: (Int) -> Unit = {},
    updateEnvite: (MusViewModel.Teams) -> Unit = { _ -> },
    landscape: Boolean = true,
) {
    val buttonRemove = @Composable {
        IconButton(
            onClick = { increment(-1) },
            enabled = value > 0,
            modifier = Modifier
                .padding(8.dp)
                .repeatingClickable(
                    interactionSource = remember { MutableInteractionSource() },
                    enabled = value > 0,
                    onClick = { increment(-1) }
                ),
            colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.primary)
        ) {
            Icon(Icons.Rounded.Remove, contentDescription = "Incrementar envite")
        }
    }

    val buttonAdd = @Composable {
        IconButton(
            onClick = { increment(1) },
            enabled = value < 100,
            modifier = Modifier
                .padding(8.dp)
                .repeatingClickable(
                    interactionSource = remember { MutableInteractionSource() },
                    enabled = value < 100,
                    onClick = { increment(1) }
                ),
            colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.primary)
        ) {
            Icon(Icons.Rounded.Add, contentDescription = "Decrementar envite")
        }
    }

    val buttonBuenos = @Composable {
        FilledIconButton(
            onClick = { updateEnvite(MusViewModel.Teams.BUENOS) },
            enabled = value > 0
        ) {
            Icon(
                imageVector = if (landscape) Icons.AutoMirrored.Rounded.ArrowBack else Icons.Rounded.ArrowUpward,
                contentDescription = "Buenos ganan el envite"
            )
        }
    }

    val buttonMalos = @Composable {
        FilledIconButton(
            onClick = { updateEnvite(MusViewModel.Teams.MALOS) },
            enabled = value > 0
        ) {
            Icon(
                imageVector = if (landscape) Icons.AutoMirrored.Rounded.ArrowForward else Icons.Rounded.ArrowDownward,
                contentDescription = "Malos ganan el envite"
            )
        }
    }

    val enviteText = @Composable {
        Box(
            modifier = Modifier.width(50.dp),
            contentAlignment = Alignment.Center
        ) {
            TextButton(
                onClick = { increment(2) },
                enabled = value < 98
            ) {
                Text(
                    text = value.toString(),
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }

    if (landscape) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            buttonBuenos()
            buttonRemove()
            enviteText()
            buttonAdd()
            buttonMalos()
        }

    } else {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            buttonBuenos()
            buttonAdd()
            enviteText()
            buttonRemove()
            buttonMalos()
        }
    }
}

/**
 * Composable function that displays the "envites" (bets) in a Mus game.
 * It shows the current value of each envite (Grande, Chica, Pares, Juego)
 * and provides buttons to increment/decrement the value and assign the envite to a team.
 * The layout adapts to landscape or portrait orientation.
 *
 * @param landscape A boolean indicating whether the device is in landscape orientation.
 * @param viewModel The [MusViewModel] that holds the game state and logic.
 * @param modifier The modifier to be applied to the layout.
 */
@Composable
fun Envites(
    landscape: Boolean,
    viewModel: MusViewModel,
    modifier: Modifier = Modifier,
) {
    val uiState = viewModel.uiState.collectAsState()
    val listEnvites: List<Pair<Int, MusViewModel.Envites>> = listOf(
        Pair(uiState.value.enviteGrande, MusViewModel.Envites.GRANDE),
        Pair(uiState.value.enviteChica, MusViewModel.Envites.CHICA),
        Pair(uiState.value.envitePares, MusViewModel.Envites.PARES),
        Pair(uiState.value.enviteJuego, MusViewModel.Envites.JUEGO),
    )

    val content = @Composable {
        listEnvites.forEach { item ->
            Envite(
                value = item.first,
                landscape = landscape,
                increment = {
                    viewModel.incrementEnvite(
                        envite = item.second,
                        increment = it
                    )
                },
                updateEnvite = {
                    viewModel.updateEnvite(
                        envite = item.second,
                        team = it
                    )
                }
            )
        }
    }

    if (landscape) {
        Column(modifier = modifier) {
            content()
        }
    } else {
        Row(modifier = modifier) {
            content()
        }
    }
}


@Preview
@Composable
fun EnviteRoWPreview() {
    AmarracosTheme() {
        Envite(landscape = true, value = 15)
    }
}

@Preview
@Composable
fun EnviteColumnPreview() {
    AmarracosTheme() {
        Envite(15, landscape = false)
    }
}

@Preview
@Composable
fun EnvitesLandscapePreview() {
    val viewModel: MusViewModel = viewModel()
    viewModel.incrementEnvite(MusViewModel.Envites.GRANDE, 1)
    viewModel.incrementEnvite(MusViewModel.Envites.CHICA, 23)
    viewModel.incrementEnvite(MusViewModel.Envites.JUEGO, 15)
    AmarracosTheme {
        Envites(landscape = true, viewModel = viewModel)
    }
}

@Preview
@Composable
fun EnvitesPortraitPreview() {
    val viewModel: MusViewModel = viewModel()
    viewModel.incrementEnvite(MusViewModel.Envites.GRANDE, 1)
    viewModel.incrementEnvite(MusViewModel.Envites.CHICA, 23)
    viewModel.incrementEnvite(MusViewModel.Envites.JUEGO, 15)
    AmarracosTheme {
        Envites(landscape = false, viewModel = viewModel)
    }
}
