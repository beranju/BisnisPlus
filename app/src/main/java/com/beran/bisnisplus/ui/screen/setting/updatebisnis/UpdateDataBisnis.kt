package com.beran.bisnisplus.ui.screen.setting.updatebisnis

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.NavigateBefore
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.beran.bisnisplus.ui.screen.setting.common.SettingState
import com.beran.bisnisplus.ui.theme.BisnisPlusTheme
import com.beran.bisnisplus.utils.Utils
import com.beran.core.domain.model.BusinessModel
import com.beran.core.domain.model.UserModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi


@Composable
fun UpdateDataBisnis(
    state: SettingState,
    fetchCurrentUser: () -> Unit,
    onNavigateBack: () -> Unit,
    onCreateBisnisData: (UserModel, BusinessModel) -> Unit
) {
    var loading by remember {
        mutableStateOf(state.isLoading)
    }
    var error by remember {
        mutableStateOf<String?>(state.error)
    }
    var isSuccess by remember {
        mutableStateOf(state.isSuccess)
    }
    LaunchedEffect(key1 = state.isSuccess) {
        if (state.isSuccess) {
            onNavigateBack()
        }
        isSuccess = state.isSuccess
    }
    LaunchedEffect(key1 = state.error?.isNotEmpty()) {
        error = state.error
    }
    LaunchedEffect(key1 = state.isLoading) {
        if (state.isLoading) {
            fetchCurrentUser()
        }
        loading = state.isLoading
    }
    Column(modifier = Modifier.fillMaxSize()) {
        CustomAppBar(
            titleAppBar = "Lengkapi data anda",
            leadingIcon = Icons.Default.NavigateBefore,
            onLeadingClick = { onNavigateBack() })
        UpdateDataBisnisContent(
            user = state.user,
            isLoading = loading,
            errorText = error,
            onChangeError = { error = it },
            onCreateBisnisData = onCreateBisnisData
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun UpdateDataBisnisContent(
    user: UserModel?,
    isLoading: Boolean,
    errorText: String?,
    onCreateBisnisData: (UserModel, BusinessModel) -> Unit,
    onChangeError: (String) -> Unit
) {
    val scrollState = rememberScrollState()
//    val permissionState =
//        rememberPermissionState(permission = Manifest.permission.READ_EXTERNAL_STORAGE)
//    var imageUri by remember {
//        mutableStateOf<Uri?>(null)
//    }
//    val imagePickerLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.PickVisualMedia(), onResult = { uri ->
//            imageUri = uri
//
//        })
    var bisnisName by remember {
        mutableStateOf("")
    }
    var bisnisCategory by remember {
        mutableStateOf("")
    }
    var commodity by remember {
        mutableStateOf("")
    }
    var phoneNumber by remember {
        mutableStateOf("")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = stringResource(R.string.txt_sign_data_desc),
                style = MaterialTheme.typography.bodyMedium
            )
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
            Spacer(modifier = Modifier.height(30.dp))
//            Box(
//                contentAlignment = Alignment.Center,
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                CustomImagePickerCircle(imageUri = imageUri, onPickPhoto = {
//                    if (permissionState.hasPermission || permissionState.hasPermission) {
//                        imagePickerLauncher.launch(
//                            PickVisualMediaRequest(
//                                ActivityResultContracts.PickVisualMedia.ImageOnly
//                            )
//                        )
//                    } else {
//                        permissionState.launchPermissionRequest()
//                    }
//                })
//            }
//            Spacer(modifier = Modifier.height(20.dp))
            CustomDataFormField(
                labelName = stringResource(R.string.txt_nama_usaha),
                textHint = stringResource(R.string.txt_nama_usaha_hint),
                value = bisnisName,
                onChangeValue = { newValue ->
                    bisnisName = newValue
                }
            )
            Spacer(modifier = Modifier.height(20.dp))
            CustomDataFormField(
                labelName = stringResource(R.string.txt_jenis_usaha),
                textHint = stringResource(R.string.txt_jenis_usaha_hint),
                value = bisnisCategory,
                onChangeValue = { newValue ->
                    bisnisCategory = newValue
                }
            )
            Spacer(modifier = Modifier.height(20.dp))
            CustomDataFormField(
                labelName = stringResource(R.string.txt_komoditas),
                textHint = stringResource(R.string.txt_komoditas_hint),
                value = commodity,
                onChangeValue = { newValue ->
                    commodity = newValue
                }
            )
            Text(
                text = stringResource(R.string.txt_komoditas_exp),
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(20.dp))
            CustomDataFormField(
                labelName = stringResource(R.string.txt_phoneNumber),
                textHint = stringResource(R.string.txt_phone_hint),
                value = phoneNumber,
                onChangeValue = { newValue ->
                    phoneNumber = newValue
                }
            )
            Spacer(modifier = Modifier.height(40.dp))
            Box(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = {
                        if (phoneNumber.isEmpty()) onChangeError("Nomor telepon tidak boleh kosong")
                        if (bisnisName.isEmpty()) onChangeError("Nama bisnis tidak boleh kosong")
                        if (bisnisCategory.isEmpty()) onChangeError("Kategori bisnis tidak boleh kosong")
                        if (commodity.isEmpty()) onChangeError("Komoditas bisnis tidak boleh kosong")
                        if (errorText == null) {
                            val bisnisId = Utils.generateUUid()
                            val createdAt = System.currentTimeMillis()
                            val userId = user?.uid
                            val updatedAt = System.currentTimeMillis()
                            val userModel = user?.copy(
                                phoneNumber = phoneNumber,
                                bisnisId = bisnisId,
                                updatedAt = updatedAt
                            ) as UserModel
                            val businessModel = BusinessModel(
                                bisnisId = bisnisId,
                                userId = userId,
                                bisnisName = bisnisName,
                                bisnisCategory = bisnisCategory,
                                commodity = commodity,
                                createdAt = createdAt
                            )
                            onCreateBisnisData(userModel, businessModel)
                        }
                    }, enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = R.string.txt_selanjutnya),
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
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignDataBisnisPrev() {
    BisnisPlusTheme {
        UpdateDataBisnis(
            state = SettingState(),
            onCreateBisnisData = { _, _ -> },
            fetchCurrentUser = {},
            onNavigateBack = {}
        )
    }
}