package com.beran.bisnisplus.ui.screen.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.beran.bisnisplus.R
import com.beran.bisnisplus.ui.component.CustomDataFormField
import com.beran.bisnisplus.ui.screen.auth.dataBisnis.BisnisState
import com.beran.bisnisplus.ui.screen.auth.dataBisnis.BisnisViewModel
import com.beran.bisnisplus.ui.theme.BisnisPlusTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun SignDataBisnis(
    viewmodel: BisnisViewModel,
    onNavigateToSignIn: () -> Unit,
    onCreateBisnisData: (bisnisName: String, bisnisCategory: String, commodity: String) -> Unit
) {
    val state = viewmodel.uiState.collectAsStateWithLifecycle().value
    SignDataBisnisContent(
        state = state,
        onNavigateToSignIn = onNavigateToSignIn,
        onCreateBisnisData = onCreateBisnisData
    )
}

@Composable
fun SignDataBisnisContent(
    state: BisnisState,
    onNavigateToSignIn: () -> Unit,
    onCreateBisnisData: (bisnisName: String, bisnisCategory: String, commodity: String) -> Unit
) {
    var bisnisName by remember {
        mutableStateOf("")
    }
    var bisnisCategory by remember {
        mutableStateOf("")
    }
    var commodity by remember {
        mutableStateOf("")
    }
    var isLoading by remember {
        mutableStateOf(false)
    }
    var errorText by remember {
        mutableStateOf<String?>(null)
    }

    DisposableEffect(key1 = state, effect = {
        when (state) {
            is BisnisState.Loading -> isLoading = true
            is BisnisState.Error -> errorText = state.message
            is BisnisState.Success -> onNavigateToSignIn()
            else -> {}
        }
        onDispose {

        }
    })

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
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
            Spacer(modifier = Modifier.height(40.dp))
            Box(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = {
                        if (bisnisName.isNotEmpty() && bisnisCategory.isNotEmpty() && commodity.isNotEmpty())
                            onCreateBisnisData(bisnisName, bisnisCategory, commodity)
                    }, enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.primary)
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
        SignDataBisnis(
            viewmodel = koinViewModel(),
            onCreateBisnisData = { _, _, _ -> },
            onNavigateToSignIn = {})
    }
}