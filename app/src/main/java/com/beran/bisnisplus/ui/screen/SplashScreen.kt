package com.beran.bisnisplus.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.beran.bisnisplus.R
import com.beran.bisnisplus.ui.theme.BisnisPlusTheme
import com.beran.bisnisplus.ui.theme.fontFamily

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxHeight()
        ) {
            Spacer(Modifier.height(50.dp))
            Image(
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = "App logo"
            )
            Text(
                text = "Catatan keuangan\nusaha kamu",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
        }

    }

}

@Preview(showBackground = true)
@Composable
fun SplashScreenPrev() {
    BisnisPlusTheme {
        SplashScreen()
    }
}