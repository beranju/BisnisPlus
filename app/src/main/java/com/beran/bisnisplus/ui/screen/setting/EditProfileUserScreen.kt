package com.beran.bisnisplus.ui.screen.setting

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NavigateBefore
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.beran.bisnisplus.R
import com.beran.bisnisplus.ui.component.CustomAppBar
import com.beran.bisnisplus.ui.component.CustomDataFormField
import com.beran.bisnisplus.ui.component.CustomImagePickerCircle
import com.beran.bisnisplus.ui.screen.setting.common.SettingState
import com.beran.bisnisplus.ui.theme.BisnisPlusTheme
import com.beran.core.domain.model.UserModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState

@Composable
fun EditProfileUserScreen(
    state: SettingState,
    onNavigateBack: () -> Unit,
    fetchUserDetail: () -> Unit,
    onUpdateProfile: (UserModel) -> Unit,
    modifier: Modifier = Modifier
) {
    var loading by remember {
        mutableStateOf(state.isLoading)
    }
    var error by remember {
        mutableStateOf<String?>(null)
    }
    LaunchedEffect(key1 = state.error?.isNotEmpty()) {
        error = state.error
    }
    LaunchedEffect(key1 = state.isSuccess) {
        if (state.isSuccess) {
            onNavigateBack()
        }
    }
    LaunchedEffect(key1 = state.isLoading) {
        if (state.isLoading) {
            fetchUserDetail()
        }
        loading = state.isLoading
    }


    Scaffold(topBar = {
        CustomAppBar(
            titleAppBar = "Edit Profile Anda",
            leadingIcon = Icons.Outlined.NavigateBefore,
            onLeadingClick = onNavigateBack
        )
    }) { innerPadding ->
        Box(modifier = modifier.padding(innerPadding)) {
            if (state.user != null) {
                EditProfileUserContent(
                    userModel = state.user,
                    loading = loading,
                    errorText = error,
                    onChangeError = { error = it },
                    onUpdateProfile = onUpdateProfile
                )
            } else {
                Box(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun EditProfileUserContent(
    userModel: UserModel,
    loading: Boolean,
    onUpdateProfile: (UserModel) -> Unit,
    onChangeError: (String?) -> Unit,
    modifier: Modifier = Modifier,
    errorText: String? = null
) {
    var imageUri by remember {
        mutableStateOf<Uri?>(Uri.parse(userModel.photoUrl.toString()))
    }
    val permissionState =
        rememberPermissionState(permission = Manifest.permission.READ_EXTERNAL_STORAGE)
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(), onResult = { uri ->
            imageUri = uri

        })
    var name by remember {
        mutableStateOf<String>(userModel.name.orEmpty())
    }
    var noHp by remember {
        mutableStateOf<String>(userModel.phoneNumber.orEmpty())
    }
    var email by remember {
        mutableStateOf<String>(userModel.email.orEmpty())
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
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
        CustomImagePickerCircle(
            imageUri = imageUri,
            onPickPhoto = {
                if (permissionState.hasPermission || permissionState.hasPermission) {
                    imagePickerLauncher.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                } else {
                    permissionState.launchPermissionRequest()
                }
            }
        )
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
            onChangeValue = { newValue -> email = newValue },
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp)
        ) {
            Button(
                onClick = {
                    if (name.isNotEmpty() && noHp.isNotEmpty() && imageUri != null) {
                        val user = userModel.copy(
                            name = name,
                            email = email,
                            photoUrl = imageUri.toString(),
                            phoneNumber = noHp,
                            updatedAt = System.currentTimeMillis()
                        )
                        onUpdateProfile(user)
                    } else {
                        onChangeError("Kolom data tidak boleh kosong")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.txt_simpan),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.background
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
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(25.dp)
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
        EditProfileUserScreen(
            state = SettingState(),
            onUpdateProfile = {},
            onNavigateBack = {},
            fetchUserDetail = {},
        )
    }
}