package com.rodalc.amarracos.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.rodalc.amarracos.ui.theme.AmarracosTheme
import com.rodalc.amarracos.ui.theme.Playfair

/**
 * Primera pantalla de la aplicación, contiene el menú para navegar por ella.
 *
 * @param navController El controlador usado para cambiar entre pantallas.
 */
@Composable
fun PantallaInicio(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            IconButton(
                onClick = { navController.navigate(("pantallaAjustes")) },
            ) {
                Icon(Icons.Outlined.Settings, contentDescription = "Settings")
            }
        }
        Spacer(modifier = Modifier.weight(0.1f))
        Text(
            text = "Amarracos",
            fontFamily = Playfair,
            fontSize = 60.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.weight(0.6f))
        GameButton(icon = Icons.AutoMirrored.Rounded.ArrowForward, text = "Mus") {
            navController.navigate("pantallaMus")
        }
        Spacer(modifier = Modifier.height(10.dp))
        GameButton(icon = Icons.AutoMirrored.Rounded.ArrowForward, text = "Pocha") {
            navController.navigate("pantallaPocha")
        }
        Spacer(modifier = Modifier.weight(0.1f))
    }
}

@Composable
fun GameButton(icon: ImageVector, text: String, onClick: () -> Unit) {
    FilledTonalButton(onClick = onClick) {
        Row {
            Text(text, fontSize = 20.sp, modifier = Modifier.fillMaxWidth(0.2f))
            Icon(imageVector = icon, contentDescription = icon.name)
        }
    }
}


/**
 * Función auxiliar para Preview
 */
@Preview(
    showBackground = true,
    device = "id:pixel", backgroundColor = 0xFFFFFFFF
)
@Composable
fun InicioPreview() {
    val navController = rememberNavController()
    AmarracosTheme {
        PantallaInicio(navController)
    }
}
