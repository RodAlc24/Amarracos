package com.rodalc.amarracos.mus

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rodalc.amarracos.main.ToastRateLimiter

@Preview(
    device = "spec:width=411dp,height=891dp,orientation=landscape",
    showSystemUi = true,
    showBackground = true
)
@Composable
fun PantallaMus() {
    var showConfig by rememberSaveable { mutableStateOf(true) }
    val context = LocalContext.current
    if (showConfig) {
        PantallaConfiguracion(context) { showConfig = it }
    } else {
        PlantillaMus()
    }
}

@Composable
fun PantallaConfiguracion(
    context: Context,
    show: (Boolean) -> Unit
) {
    var buenos by rememberSaveable { mutableStateOf("") }
    var malos by rememberSaveable { mutableStateOf("") }
    var puntos30 by rememberSaveable { mutableStateOf(true) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()

    ) {
        Spacer(modifier = Modifier.weight(0.2f))
        Text(text = "Mus", fontSize = 30.sp)
        Spacer(modifier = Modifier.weight(0.2f))
        TextField(
            modifier = Modifier.fillMaxWidth(0.7f),
            value = buenos,
            onValueChange = {
                if (it.length <= 10) {
                    buenos = it
                } else ToastRateLimiter.showToast(context, "¡Pon un nombre más corto!")
            },
            maxLines = 1,
            label = { Text(text = "Buenos") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(0.7f),
            value = malos,
            onValueChange = {
                if (it.length <= 10) {
                    malos = it
                } else ToastRateLimiter.showToast(context, "¡Pon un nombre más corto!")
            },
            maxLines = 1,
            label = { Text(text = "Malos") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
        )
        Spacer(modifier = Modifier.weight(0.2f))
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Puntos")
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "30:")
            RadioButton(selected = puntos30, onClick = { puntos30 = true })
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "40:")
            RadioButton(selected = !puntos30, onClick = { puntos30 = false })
        }
        Spacer(modifier = Modifier.weight(0.2f))
        Button(
            onClick = {
                Mus.getBuenos().nombre = if (buenos == "") "Buenos" else buenos
                Mus.getMalos().nombre = if (malos == "") "Malos" else malos
                Mus.setPuntos(if (puntos30) 30 else 40)
                show(false)
            },
            modifier = Modifier.padding(20.dp)
        ) {
            Icon(Icons.Rounded.Done, contentDescription = "Done")
        }
    }
}

@Composable
fun PlantillaMus() {
    // TODO: If landscape ... else ...
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.weight(1f))
        ColumnaParejaLandscape(pareja = Mus.getBuenos())
        Spacer(modifier = Modifier.weight(1f))
        ColumnaEnvites()
        Spacer(modifier = Modifier.weight(1f))
        ColumnaParejaLandscape(pareja = Mus.getMalos())
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun ColumnaParejaLandscape(pareja: Pareja) {
    var puntos by rememberSaveable { mutableIntStateOf(pareja.puntos) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxHeight()
            .padding(10.dp)
    ) {
        Spacer(modifier = Modifier.weight(5f))
        Text(
            text = "${pareja.nombre}: ${pareja.victorias}",
            fontSize = 25.sp
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = pareja.puntos.toString(),
            fontSize = 80.sp
        )
        Row {
            TextButton(onClick = {
                puntos -= 1
                pareja.puntos = puntos
            }) {
                Icon(Icons.Rounded.Remove, contentDescription = "Remove")
            }
            TextButton(onClick = {
                puntos += 1
                pareja.puntos = puntos
            }) {
                Icon(Icons.Rounded.Add, contentDescription = "Add")
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = { /*TODO*/ }) {
            Text("Órdago")
        }
        Spacer(modifier = Modifier.weight(5f))
    }
}

@Composable
fun ColumnaEnvites() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(.35f)
    ) {
        FilaEnvite(envite = Mus.getEnvites().grande) { Mus.getEnvites().grande = it }
        FilaEnvite(envite = Mus.getEnvites().chica) { Mus.getEnvites().chica = it }
        FilaEnvite(envite = Mus.getEnvites().pares) { Mus.getEnvites().pares = it }
        FilaEnvite(envite = Mus.getEnvites().juego) { Mus.getEnvites().juego = it }
        Button(onClick = { /*TODO*/ }) {
            Icon(Icons.Rounded.Done, contentDescription = "Done")
        }

    }
}

@Composable
fun FilaEnvite(envite: Int, setValue: (Int) -> Unit) {
    var temp by remember { mutableIntStateOf(envite) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(1f)
    ) {
        TextButton(
            onClick = {
                temp -= 1
                setValue(temp)
            },
            enabled = temp > 0
        ) {
            Icon(Icons.Rounded.Remove, contentDescription = "Remove")
        }
        Text(
            text = temp.toString(),
            fontSize = 20.sp,
            modifier = Modifier
                .clickable(onClick = {
                    temp += 2
                    setValue(temp)
                })
                .padding(10.dp)
        )
        TextButton(onClick = {
            temp += 1
            setValue(temp)
        }) {
            Icon(Icons.Rounded.Add, contentDescription = "Add")
        }
    }
}