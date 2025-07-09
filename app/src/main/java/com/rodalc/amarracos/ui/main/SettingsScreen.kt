package com.rodalc.amarracos.ui.main

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.rodalc.amarracos.R
import com.rodalc.amarracos.storage.DataStoreManager
import com.rodalc.amarracos.ui.elements.TitleTopBar
import com.rodalc.amarracos.ui.theme.AmarracosTheme
import kotlinx.coroutines.async

/**
 * This screen contains the configuration of the app.
 *
 * @param modifier The modifier.
 * @param navigateUp The function to execute when the up button is clicked.
 */
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit = {},
) {
    val context = LocalContext.current
    val gitHub = stringResource(R.string.url_gh)
    val mailTo = "mailto:" + stringResource(R.string.mail)
    val googlePlay = stringResource(R.string.google_play)
    val webGooglePlay = stringResource(R.string.url_google_play)

    val screenState by DataStoreManager.readDataStore(
        context,
        DataStoreManager.Key.KEEP_SCREEN_ON
    )
        .collectAsState(initial = true)

    val coreutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier,
        topBar = {
            TitleTopBar(
                title = stringResource(R.string.title_config),
                showUpButton = true,
                onUpButtonClick = navigateUp
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
                SettingsElement(
                    icon = Icons.Outlined.Bolt,
                    title = stringResource(R.string.text_keep_screen_on),
                    clickable = true,
                    onClick = {
                        coreutineScope.async {
                            DataStoreManager.setDataStore(
                                context,
                                DataStoreManager.Key.KEEP_SCREEN_ON,
                                !screenState
                            )
                        }
                    }
                ) {
                    Switch(
                        modifier = modifier.padding(start = 8.dp),
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
                    )
                }
            }
            item {
                SettingsElement(
                    icon = Icons.Outlined.Link,
                    title = stringResource(R.string.text_gh_repo),
                    clickable = true,
                    onClick = {
                        context.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                gitHub.toUri()
                            )
                        )
                    }
                ) {
                    Icon(
                        modifier = modifier.padding(start = 8.dp),
                        imageVector = Icons.AutoMirrored.Outlined.OpenInNew,
                        contentDescription = null
                    )
                }
            }
            item {
                SettingsElement(
                    icon = Icons.Outlined.StarOutline,
                    title = stringResource(R.string.text_rate_app),
                    clickable = true,
                    onClick = {
                        val uri: Uri = googlePlay.toUri()
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
                        } catch (_: ActivityNotFoundException) {
                            context.startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    webGooglePlay.toUri()
                                )
                            )
                        }
                    }
                ) {
                    Icon(
                        modifier = modifier.padding(start = 8.dp),
                        imageVector = Icons.AutoMirrored.Outlined.OpenInNew,
                        contentDescription = null
                    )
                }
            }
            item {
                SettingsElement(
                    icon = Icons.Outlined.Mail,
                    title = stringResource(
                        R.string.text_contact_format,
                        stringResource(R.string.mail)
                    ),
                    clickable = true,
                    onClick = {
                        context.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                mailTo.toUri()
                            )
                        )
                    }
                ) {
                    Icon(
                        modifier = modifier.padding(start = 8.dp),
                        imageVector = Icons.AutoMirrored.Outlined.OpenInNew,
                        contentDescription = null
                    )
                }
            }
            item {
                SettingsElement(
                    icon = Icons.Outlined.Info,
                    title = stringResource(
                        R.string.text_version_format,
                        stringResource(R.string.versionCode)
                    )
                )
            }
        }
    }
}

/**
 * Defines a Row with a configuration option.
 *
 * @param icon The icon of the option.
 * @param title The title of the option.
 * @param modifier The modifier.
 * @param clickable If the option is clickable.
 * @param onClick The function to execute when the option is clicked.
 * @param function The composable function to execute at the end of the row.
 */
@Composable
private fun SettingsElement(
    icon: ImageVector,
    title: String,
    modifier: Modifier = Modifier,
    clickable: Boolean = false,
    onClick: () -> Unit = {},
    function: @Composable () -> Unit = {}
) {
    Card(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clickable(enabled = clickable, onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .sizeIn(72.dp),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = icon.name,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = title,
                modifier = Modifier
                    .weight(1f),
            )
            function()
        }
    }
}

@Preview
@Composable
fun PreviewSettingsElement() {
    AmarracosTheme {
        SettingsElement(
            icon = Icons.Outlined.Bolt,
            title = "Mantener la pantalla encendida durante las partidas"
        )
    }
}

@Preview
@Composable
fun PreviewSettingsScreen() {
    AmarracosTheme {
        SettingsScreen()
    }
}