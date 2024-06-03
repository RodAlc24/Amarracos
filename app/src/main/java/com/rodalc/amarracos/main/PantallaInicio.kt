package com.rodalc.amarracos.main

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
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
    var showCreditos by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                Modifier
                    .padding(10.dp)
                    .clickable(onClick = { showCreditos = true })
            ) {
                Icon(Icons.Outlined.Info, contentDescription = "Info")
            }
        }
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
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { navController.navigate("pantallaMus") }) {
                    Text("Mus")
                }
                Button(onClick = { navController.navigate("pantallaPocha") }) {
                    Text("Pocha")
                }
            }
        }
    }

    if (showCreditos) {
        Creditos { showCreditos = it }
    }
}

/**
 * Mostrar el popup de los créditos
 *
 * @param state Cambia el estado (visible/oculto) del popup
 */
@Composable
fun Creditos(state: (Boolean) -> Unit) {
    val url = "https://github.com/RodAlc24/Amarracos"
    val context = LocalContext.current
    Dialog(onDismissRequest = { state(false) }) {
        Box(modifier = Modifier) {
            Surface(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(5.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text(text = "Amarracos está bajo la licencia MIT. Tanto la licencia como el código se encuentran publicados en GitHub:")
                    Text(text = url,
                        modifier = Modifier.clickable {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        }
                    )
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
