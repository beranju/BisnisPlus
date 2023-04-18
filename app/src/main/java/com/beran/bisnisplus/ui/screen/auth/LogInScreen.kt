package com.beran.bisnisplus.ui.screen.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.beran.bisnisplus.R
import com.beran.bisnisplus.ui.component.CustomTextField

@Composable
fun LogInScreen(onNavigateToHome: () -> Unit) {
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = stringResource(R.string.txt_login),
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 32.sp, fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = stringResource(R.string.sign_in_desc),
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(30.dp))
            CustomTextField(
                labelText = stringResource(id = R.string.txt_email),
                hintText = stringResource(id = R.string.txt_email_hint),
                icon = Icons.Outlined.Mail,
                value = email,
                onChangeValue = { newValue -> email = newValue },
                keyBoardType = KeyboardType.Email
            )
            Spacer(modifier = Modifier.height(20.dp))
            CustomTextField(
                labelText = stringResource(id = R.string.txt_password),
                hintText = stringResource(id = R.string.txt_password_hint),
                icon = Icons.Outlined.Lock,
                value = password,
                onChangeValue = { newValue -> password = newValue },
                visualTransformation = PasswordVisualTransformation(),
                keyBoardType = KeyboardType.Password
            )
            Spacer(modifier = Modifier.height(25.dp))
            Button(
                onClick = onNavigateToHome,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = stringResource(id = R.string.txt_login),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = stringResource(R.string.atau_login_dengan),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Image(
                painter = painterResource(id = R.drawable.icon_google),
                contentDescription = "Icon Google",
                modifier = Modifier.size(45.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.belum_punya_akun),
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp)
                )
                TextButton(onClick = {}) {
                    Text(
                        text = stringResource(id = R.string.txt_login),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}