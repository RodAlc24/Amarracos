package com.rodalc.amarracos.main

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.navigation.NavController
import com.rodalc.amarracos.R
import com.rodalc.amarracos.storage.DataStoreManager
import com.rodalc.amarracos.ui.elements.TitleTopBar
import kotlinx.coroutines.async

/**
 * Pantalla con los ajustes generales de la aplicación y los créditos.
 */
@Composable
fun PantallaAjustes(navController: NavController) {
    val context = LocalContext.current
    val gitHub = "https://github.com/RodAlc24/Amarracos"
    val mus = "https://www.nhfournier.es/como-jugar/mus/"
    val pocha = "https://www.nhfournier.es/como-jugar/pocha/"
    val mail = "weibull.apps@gmail.com"
    val googlePlay = "market://details?id=com.rodalc.amarracos"
    val webGooglePlay = "https://play.google.com/store/apps/details?id=com.rodalc.amarracos"

    val screenState by DataStoreManager.readDataStore(context, DataStoreManager.Key.KEEP_SCREEN_ON)
        .collectAsState(initial = true)

    val coreutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TitleTopBar(
                title = "Ajustes",
                backButtonAction = { navController.popBackStack() },
            )
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Elemento(
                    icon = Icons.Outlined.Bolt,
                    title = "Mantener la pantalla encendida durante las partidas"
                ) {
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
                        },
                        colors = SwitchDefaults.colors(
                            uncheckedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                            uncheckedBorderColor = MaterialTheme.colorScheme.primary,
                            uncheckedThumbColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }
            item {
                Elemento(
                    icon = Icons.AutoMirrored.Outlined.HelpOutline,
                    title = "Cómo jugar al mus"
                ) {
                    IconButton(onClick = {
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(mus)))
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.OpenInNew,
                            contentDescription = "Abrir en internet"
                        )
                    }
                }
            }
            item {
                Elemento(
                    icon = Icons.AutoMirrored.Outlined.HelpOutline,
                    title = "Cómo jugar a la pocha"
                ) {
                    IconButton(onClick = {
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(pocha)))
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.OpenInNew,
                            contentDescription = "Abrir en internet"
                        )
                    }
                }
            }
            item {
                Elemento(icon = Icons.Outlined.Link, title = "Repositorio de GitHub") {
                    IconButton(onClick = {
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(gitHub)))
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.OpenInNew,
                            contentDescription = "Abrir en internet"
                        )
                    }
                }
            }
            item {
                Elemento(
                    icon = Icons.Outlined.StarOutline,
                    title = "Calificar Amarracos en Google Play"
                ) {
                    IconButton(onClick = {
                        val uri: Uri = Uri.parse(googlePlay)
                        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
                        // To count with Play market backstack, After pressing back button,
                        // to taken back to our application, we need to add following flags to intent.
                        goToMarket.addFlags(
                            Intent.FLAG_ACTIVITY_NO_HISTORY or
                                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK
                        )
                        try {
                            context.startActivity(goToMarket)
                        } catch (e: ActivityNotFoundException) {
                            context.startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(webGooglePlay)
                                )
                            )
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.OpenInNew,
                            contentDescription = "Abrir en internet"
                        )
                    }
                }
            }
            item {
                Elemento(
                    icon = Icons.Outlined.Mail,
                    title = "Contacto: $mail"
                ) {
                    IconButton(onClick = {
                        context.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("mailto:$mail")
                            )
                        )
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.OpenInNew,
                            contentDescription = "Abrir en el correo"
                        )
                    }
                }
            }
            item {
                Elemento(
                    icon = Icons.Outlined.Info,
                    title = "Versión: ${stringResource(R.string.versionCode)}"
                )
            }
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