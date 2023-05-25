package com.beran.bisnisplus.ui.screen.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NavigateBefore
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.beran.bisnisplus.R
import com.beran.bisnisplus.ui.component.CustomAppBar
import com.beran.bisnisplus.ui.component.CustomDataFormField
import com.beran.bisnisplus.ui.component.CustomImagePickerCircle
import com.beran.bisnisplus.ui.theme.BisnisPlusTheme

@Composable
fun EditProfileUserScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember {
        mutableStateOf("")
    }
    var noHp by remember {
        mutableStateOf("")
    }
    var email by remember {
        mutableStateOf("")
    }
    var pekerjaan by remember {
        mutableStateOf("")
    }

    Scaffold(topBar = {
        CustomAppBar(
            titleAppBar = "Edit Profile Anda",
            leadingIcon = Icons.Outlined.NavigateBefore,
            onLeadingClick = onNavigateBack
        )
    }) { innerPadding ->
        Box(modifier = modifier.padding(innerPadding)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
            ) {
                CustomImagePickerCircle()
                CustomDataFormField(
                    labelName = "Nama",
                    textHint = "Masukkan nama kamu",
                    value = name,
                    onChangeValue = { newValue -> name = newValue }
                )
                CustomDataFormField(
                    labelName = "No Hp",
                    textHint = "Masukkan no hp kamu",
                    value = noHp,
                    onChangeValue = { newValue -> noHp = newValue }
                )
                CustomDataFormField(
                    labelName = "Email",
                    textHint = "Masukkan email kamu",
                    value = email,
                    onChangeValue = { newValue -> email = newValue }
                )
                CustomDataFormField(
                    labelName = "Pekerjaan",
                    textHint = "Masukkan pekerjaan kamu",
                    value = pekerjaan,
                    onChangeValue = { newValue -> pekerjaan = newValue }
                )
                Button(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = R.string.txt_simpan),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun EditProfileUserScreenPrev() {
    BisnisPlusTheme {
        EditProfileUserScreen(onNavigateBack = {})
    }
}