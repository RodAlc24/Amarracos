package com.rodalc.amarracos.ui.pocha

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rodalc.amarracos.ui.theme.AmarracosTheme

/**
 * A Composable function that displays a player card with their name, total points, and current points.
 * It also allows incrementing/decrementing points based on the game type (generic or Pocha).
 *
 * @param modifier Modifier for styling the card.
 * @param name The name of the player.
 * @param totalPoints The total points accumulated by the player.
 * @param newPoints The points scored in the current round.
 * @param extraPoints Optional points for Pocha game (victories). If null, it's a generic game.
 * @param roundApuestas Boolean indicating if it's the betting round in Pocha.
 * @param incrementPoints Lambda function to handle point changes.
 */
@Composable
fun PlayerCard(
    modifier: Modifier = Modifier,
    name: String = "",
    totalPoints: Int = 0,
    newPoints: Int = 0,
    extraPoints: Int? = null,
    roundApuestas: Boolean = true,
    incrementPoints: (Int) -> Unit = {},
) {
    val color =
        if (extraPoints == null || roundApuestas || extraPoints == newPoints) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.error
    Card(
        modifier = modifier
            .fillMaxWidth(0.9f)
            .padding(8.dp)
            .clickable(onClick = { incrementPoints(1) }),
    ) {
        Column(
            modifier = Modifier
                .padding(start = 10.dp, top = 20.dp, bottom = 10.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(0.9f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$name: ",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        //                     .weight(1f) // Allocate space for the text
                        .clipToBounds(),
                    fontSize = 20.sp,
                    color = color
                )
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = totalPoints.toString(),
                    fontSize = 20.sp,
                    color = color
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
            if (extraPoints == null) { // Generico
                RowPoints(
                    title = "Puntos",
                    points = newPoints,
                    onClickIncrement = { incrementPoints(it) },
                    playerName = name,
                    removeEnabled = newPoints > -999,
                    addEnabled = newPoints < 999
                )
            } else { // Pocha
                RowPoints(
                    title = "Apuestas",
                    points = newPoints,
                    onClickIncrement = { incrementPoints(it) },
                    playerName = name,
                    removeEnabled = newPoints > 0 && roundApuestas,
                    addEnabled = newPoints < 99 && roundApuestas,
                    textColor = color
                )
                RowPoints(
                    title = "Victorias",
                    points = extraPoints,
                    onClickIncrement = { incrementPoints(it) },
                    playerName = name,
                    removeEnabled = extraPoints > 0 && !roundApuestas,
                    addEnabled = extraPoints < 99 && !roundApuestas,
                    textColor = color
                )
            }
        }
    }
}


/**
 * A composable function that displays a row with a title, points, and buttons to increment/decrement the points.
 *
 * @param title The title to display for the points.
 * @param points The current points value.
 * @param onClickIncrement A lambda function to be called when the increment/decrement buttons are clicked.
 * It receives an integer representing the amount to increment/decrement by (e.g., 1 for increment, -1 for decrement).
 * @param modifier Optional [Modifier] for this composable.
 * @param playerName The name of the player, used in content descriptions for accessibility. Defaults to "Jugador".
 * @param removeEnabled A boolean indicating whether the remove button should be enabled. Defaults to true.
 * @param addEnabled A boolean indicating whether the add button should be enabled. Defaults to true.
 * @param textColor The color of the points text. Defaults to `MaterialTheme.colorScheme.onSurface`.
 */
@Composable
fun RowPoints(
    title: String,
    points: Int,
    onClickIncrement: (Int) -> Unit,
    modifier: Modifier = Modifier,
    playerName: String = "Jugador",
    removeEnabled: Boolean = true,
    addEnabled: Boolean = true,
    textColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = modifier.fillMaxWidth(0.9f),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$title: ",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f) // Allocate space for the text
                .clipToBounds()
        )
        TextButton(
            onClick = { onClickIncrement(-1) },
            enabled = removeEnabled
        ) { Icon(Icons.Rounded.Remove, contentDescription = "Quitar 1 a $playerName") }
        Box(
            modifier = Modifier.width(40.dp),
            contentAlignment = Alignment.Center
        ) { Text(text = points.toString(), color = textColor) }
        TextButton(
            onClick = { onClickIncrement(1) },
            enabled = addEnabled
        ) { Icon(Icons.Rounded.Add, contentDescription = "Añadir uno a $playerName") }
    }
}

@Preview
@Composable
fun PreviewPlayerRow() {
    AmarracosTheme {
        PlayerCard(
            modifier = Modifier.width(350.dp),
            name = "mmmmmmmmmm",
            newPoints = 14,
            totalPoints = 10,
            extraPoints = 2,
            incrementPoints = {})
    }
}

