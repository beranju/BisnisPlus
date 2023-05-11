package com.beran.bisnisplus.ui.screen.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.beran.bisnisplus.R
import com.beran.bisnisplus.ui.component.CustomTextField
import com.beran.bisnisplus.ui.screen.auth.signin.SignInState
import com.beran.bisnisplus.ui.screen.auth.signin.SignInViewModel
import com.beran.bisnisplus.utils.Utils

@Composable
fun LogInScreen(
    viewModel: SignInViewModel,
    onNavigateToHome: () -> Unit,
    oneTapSignIn: () -> Unit,
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    var email by remember {
        mutableStateOf("")
    }
    var emailError by remember {
        mutableStateOf<String?>(null)
    }
    var password by remember {
        mutableStateOf("")
    }
    var passwordError by remember {
        mutableStateOf<String?>(null)
    }
    var errorText by remember {
        mutableStateOf<String?>(null)
    }
    var loading by remember {
        mutableStateOf(false)
    }

    DisposableEffect(key1 = state.value, effect = {
        when (state.value) {
            is SignInState.Loading -> loading = true
            is SignInState.Error -> errorText = (state.value as SignInState.Error).message
            is SignInState.Success -> onNavigateToHome()
            is SignInState.Initial -> {}
        }
        onDispose { }
    })

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
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

            // ** shown this part when error is not empty
            if (errorText != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.errorContainer)
                        .padding(vertical = 4.dp, horizontal = 6.dp)
                ) {
                    Text(
                        text = errorText.orEmpty(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            CustomTextField(
                labelText = stringResource(id = R.string.txt_email),
                hintText = stringResource(id = R.string.txt_email_hint),
                icon = Icons.Outlined.Mail,
                value = email,
                onChangeValue = { newValue ->
                    email = newValue
                    emailError = if (!Utils.isValidEmail(newValue)) "Email tidak valid" else null
                },
                keyBoardType = KeyboardType.Email,
                errorText = emailError.orEmpty(),
                isError = !emailError.isNullOrEmpty()
            )
            Spacer(modifier = Modifier.height(20.dp))
            CustomTextField(
                labelText = stringResource(id = R.string.txt_password),
                hintText = stringResource(id = R.string.txt_password_hint),
                icon = Icons.Outlined.Lock,
                value = password,
                onChangeValue = { newValue ->
                    password = newValue
                    passwordError = if (newValue.length < 8) "Password minimal 8 karakter" else null
                },
                visualTransformation = PasswordVisualTransformation(),
                keyBoardType = KeyboardType.Password,
                errorText = passwordError.orEmpty(),
                isError = !passwordError.isNullOrEmpty()
            )
            Spacer(modifier = Modifier.height(25.dp))
            Box(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = {
                        if (email.isNotEmpty() && password.isNotEmpty() && emailError.isNullOrEmpty() && passwordError.isNullOrEmpty()) {
                            viewModel.signIn(email, password)
                        } else {
                            errorText = "Field tidak boleh ada yang kosong"
                        }
                    }, enabled = !loading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                ) {
                    Text(
                        text = stringResource(id = R.string.txt_login),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                if (loading) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .align(Alignment.CenterEnd)
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier
                                .size(25.dp)
                        )
                    }
                }
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
                modifier = Modifier.size(45.dp).clickable {
                    oneTapSignIn()
                }
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
                        text = stringResource(id = R.string.txt_daftar),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}