package com.rodalc.amarracos.main

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.rodalc.amarracos.R

// PANTALLA AUXILIAR, VA A SER APISONADA
@Composable
fun PantallaVacia() {
    val url = "https://github.com/RodAlc24/Amarracos"
    val context = LocalContext.current
    Column(
        modifier = Modifier.padding(10.dp)
    ) {
        Text(text = "Amarracos está bajo la licencia MIT. Tanto la licencia como el código se encuentran publicados en github:")
        Text(text = url,
            modifier = Modifier.clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            })
    }
}

@Composable
fun GitHubRepoLink(url: String, repoName: String) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .padding(8.dp)
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
    ) {
        Image(
            painter = painterResource(id = R.drawable.github_logo), // Ensure you have this drawable in your resources
            contentDescription = "GitHub Logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = repoName,
            color = Color.Blue,
            fontSize = 18.sp
        )
    }
}