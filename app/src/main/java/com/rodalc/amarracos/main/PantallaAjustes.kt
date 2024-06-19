package com.rodalc.amarracos.main

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.OpenInNew
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rodalc.amarracos.R
import com.rodalc.amarracos.storage.DataStoreManager
import kotlinx.coroutines.async

/**
 * Pantalla con los ajustes generales de la aplicación y los créditos.
 */
@Composable
fun PantallaAjustes() {
    val context = LocalContext.current
    val url = "https://github.com/RodAlc24/Amarracos"

    val screenState by DataStoreManager.readDataStore(context, DataStoreManager.Key.KEEP_SCREEN_ON)
        .collectAsState(initial = true)

    val coreutineScope = rememberCoroutineScope()

    LazyColumn(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()

    ) {
        item {
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Ajustes e infomación", fontSize = 20.sp)
            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider()
        }
        item {
            Elemento(icon = Icons.Rounded.Bolt, title = "Mantener la pantalla encendida") {
                Switch(
                    checked = screenState,
                    onCheckedChange = {
                        coreutineScope.async {
                            DataStoreManager.setDataStore(
                                context,
                                DataStoreManager.Key.KEEP_SCREEN_ON,
                                it
                            )
                        }
                    })
            }
        }
        item {
            Elemento(icon = Icons.AutoMirrored.Rounded.OpenInNew, title = "Repositorio de GitHub") {
                OutlinedButton(onClick = {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                }) {
                    Text(text = "Abrir")
                }
            }
        }
        item {
            Elemento(
                icon = Icons.Rounded.Info,
                title = "Versión: ${stringResource(R.string.versionCode)}"
            )
        }
    }
}


@Composable
fun Elemento(
    icon: ImageVector,
    title: String,
    function: @Composable () -> Unit = {}
) {
    Spacer(modifier = Modifier.height(15.dp))
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.fillMaxWidth(0.9f)
    ) {
        Icon(imageVector = icon, contentDescription = icon.name)
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = title,
            modifier = Modifier
                .weight(1f),
        )
        function()
    }
    Spacer(modifier = Modifier.height(15.dp))
    HorizontalDivider()
}