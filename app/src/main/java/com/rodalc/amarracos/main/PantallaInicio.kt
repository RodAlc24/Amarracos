package com.rodalc.amarracos.main

import android.graphics.fonts.Font
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
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
        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
        ) {
            Text(
                text = "Amarracos",
                fontFamily = Playfair,
                fontSize = 60.sp,
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.weight(1f))

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp), // Ajusta el padding según sea necesario
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { navController.navigate("pantallaMus") }) {
                    Text("Mus")
                }
                Button(onClick = { navController.navigate("pantallaPocha") }) {
                    Text("Pocha")
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp), // Ajusta el padding según sea necesario
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { navController.navigate("pantallaCreditos") }) {
                    Text("Créditos")
                }
                Button(enabled = false, onClick = { navController.navigate("pantallaAjustes") }) {
                    Text("Ajustes")
                }
            }

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
