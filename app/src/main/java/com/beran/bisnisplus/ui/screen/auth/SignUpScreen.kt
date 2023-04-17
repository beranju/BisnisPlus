package com.beran.bisnisplus.ui.screen.auth

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
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.beran.bisnisplus.R
import com.beran.bisnisplus.ui.component.CustomTextField
import com.beran.bisnisplus.ui.theme.BisnisPlusTheme

@Composable
fun SignUpScreen(navController: NavHostController) {

    var name by remember {
        mutableStateOf("")
    }
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
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.txt_daftar),
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 32.sp, fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = stringResource(R.string.sign_up_desc),
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))
            CustomTextField(
                labelText = stringResource(R.string.txt_name),
                hintText = stringResource(R.string.txt_name_hint),
                icon = Icons.Outlined.Badge,
                value = name,
                onChangeValue = { newValue -> name = newValue },
            )
            Spacer(modifier = Modifier.height(20.dp))
            CustomTextField(
                labelText = stringResource(R.string.txt_email),
                hintText = stringResource(R.string.txt_email_hint),
                icon = Icons.Outlined.Mail,
                value = email,
                onChangeValue = { newValue -> email = newValue },
                keyBoardType = KeyboardType.Email
            )
            Spacer(modifier = Modifier.height(20.dp))
            CustomTextField(
                labelText = stringResource(R.string.txt_password),
                hintText = stringResource(R.string.txt_password_hint),
                icon = Icons.Outlined.Lock,
                value = password,
                onChangeValue = { newValue -> password = newValue },
                keyBoardType = KeyboardType.Password,
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(25.dp))
            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = stringResource(id = R.string.txt_daftar),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.height(25.dp))
            Text(
                text = stringResource(R.string.atau_daftar_dengan),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Icon(
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
                    text = "Sudah punya akun?",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp)
                )
                TextButton(onClick = { }) {
                    Text(
                        text = "Login",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SignUpScreenPrev() {
    BisnisPlusTheme {
        SignUpScreen(navController = rememberNavController())
    }

}