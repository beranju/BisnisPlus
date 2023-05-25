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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Badge
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.beran.bisnisplus.R
import com.beran.bisnisplus.ui.component.CustomTextField
import com.beran.bisnisplus.ui.screen.auth.signUp.SignUpState
import com.beran.bisnisplus.ui.screen.auth.signUp.SignUpViewModel
import com.beran.bisnisplus.ui.theme.BisnisPlusTheme
import com.beran.bisnisplus.utils.Utils
import com.beran.bisnisplus.utils.isValidEmail
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel,
    oneTapLogin : () -> Unit,
    navigateToSignDataBisnis: () -> Unit,
    navigateToSignIn: () -> Unit
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    var name by remember {
        mutableStateOf("")
    }
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
    var isLoading by remember {
        mutableStateOf(false)
    }
    var errorText by remember {
        mutableStateOf<String?>(null)
    }

    DisposableEffect(key1 = state.value, effect = {
        when (state.value) {
            is SignUpState.Success -> navigateToSignDataBisnis()
            is SignUpState.Loading -> isLoading = true
            is SignUpState.Error -> {
                errorText = "Ada yang salah, coba lagi!"
                Timber.d("onError: ${(state.value as SignUpState.Error).message}")
            }

            is SignUpState.Initial -> {}
        }
        onDispose { }
    })

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.verticalScroll(rememberScrollState())
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
                labelText = stringResource(R.string.txt_name),
                hintText = stringResource(R.string.txt_name_hint),
                icon = Icons.Outlined.Badge,
                value = name,
                onChangeValue = { newValue -> name = newValue },
                errorText = null
            )
            Spacer(modifier = Modifier.height(20.dp))
            CustomTextField(
                labelText = stringResource(R.string.txt_email),
                hintText = stringResource(R.string.txt_email_hint),
                icon = Icons.Outlined.Mail,
                value = email,
                onChangeValue = { newValue ->
                    email = newValue
                    // ** validation of email
                    emailError = if (!newValue.isValidEmail()) "Email tidak valid" else null
                },
                keyBoardType = KeyboardType.Email,
                errorText = emailError.orEmpty(),
                isError = !emailError.isNullOrEmpty()
            )
            Spacer(modifier = Modifier.height(20.dp))
            CustomTextField(
                labelText = stringResource(R.string.txt_password),
                hintText = stringResource(R.string.txt_password_hint),
                icon = Icons.Outlined.Lock,
                value = password,
                onChangeValue = { newValue ->
                    password = newValue
                    passwordError = if (newValue.length < 8) "Password minimal 8 karakter" else null
                },
                keyBoardType = KeyboardType.Password,
                visualTransformation = PasswordVisualTransformation(),
                errorText = passwordError.orEmpty(),
                isError = !passwordError.isNullOrEmpty()
            )
            Spacer(modifier = Modifier.height(25.dp))
            Box(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = {
                        if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                            viewModel.signUp(name, email, password)
                        } else {
                            errorText = "Field tidak boleh ada yang kosong"
                        }
                    }, enabled = !isLoading, modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = R.string.txt_daftar),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                if (isLoading) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .align(Alignment.CenterEnd)
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .size(25.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = stringResource(R.string.atau_daftar_dengan),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Image(
                painter = painterResource(id = R.drawable.icon_google),
                contentDescription = "Icon Google",
                modifier = Modifier.size(45.dp).clickable {
                    oneTapLogin()
                }
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
                TextButton(onClick = navigateToSignIn) {
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
        SignUpScreen(
            viewModel = koinViewModel(),
            navigateToSignDataBisnis = {}, navigateToSignIn = {}, oneTapLogin = {})
    }

}