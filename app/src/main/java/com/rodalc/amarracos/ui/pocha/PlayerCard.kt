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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rodalc.amarracos.ui.theme.AmarracosTheme

@Composable
fun PlayerCard(
    modifier: Modifier = Modifier,
    name: String = "",
    newPoints: Int = 0,
    totalPoints: Int = 0,
    extraPoints: Int? = null,
    roundApuestas: Boolean = true,
    incrementPoints: (Int) -> Unit = {},
) {
    val color = if (roundApuestas || extraPoints == newPoints) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.error
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
            Row(
                modifier = Modifier.fillMaxWidth(0.9f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Apuestas: ",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f) // Allocate space for the text
                        .clipToBounds()
                )
                TextButton(
                    onClick = { incrementPoints(-1) },
                    enabled = newPoints > 0 && roundApuestas
                ) { Icon(Icons.Rounded.Remove, contentDescription = "Quitar 1 a $name") }
                Box(
                    modifier = Modifier.width(40.dp),
                    contentAlignment = Alignment.Center
                ) { Text(text = newPoints.toString(), color = color) }
                TextButton(
                    onClick = { incrementPoints(1) },
                    enabled = newPoints < 99 && roundApuestas
                ) { Icon(Icons.Rounded.Add, contentDescription = "Añadir uno a $name") }

            }
            if (extraPoints != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(0.9f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Victorias: ",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .weight(1f) // Allocate space for the text
                            .clipToBounds()
                    )
                    TextButton(
                        onClick = { incrementPoints(-1) },
                        enabled = extraPoints > 0 && !roundApuestas
                    ) { Icon(Icons.Rounded.Remove, contentDescription = "Quitar 1 a $name") }
                    Box(
                        modifier = Modifier.width(40.dp),
                        contentAlignment = Alignment.Center
                    ) { Text(text = extraPoints.toString(), color = color) }
                    TextButton(
                        onClick = { incrementPoints(1) },
                        enabled = extraPoints < 99 && !roundApuestas
                    ) { Icon(Icons.Rounded.Add, contentDescription = "Añadir uno a $name") }

                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewPlayerRow() {
    AmarracosTheme {
        PlayerCard(
            name = "Jugador",
            newPoints = 14,
            totalPoints = 10,
            extraPoints = 2,
            incrementPoints = {})
    }
}

@Preview
@Composable
fun PreviewExpandedPlayerRow() {
    AmarracosTheme {
        PlayerCard(
            name = "Jugador",
            newPoints = 14,
            totalPoints = 10,
            extraPoints = 2,
            incrementPoints = {})
    }
}
