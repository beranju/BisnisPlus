package com.beran.bisnisplus.ui.screen.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.beran.bisnisplus.R
import com.beran.bisnisplus.ui.component.CustomDataFormField
import com.beran.bisnisplus.ui.theme.BisnisPlusTheme

@Composable
fun SignDataBisnis() {
    SignDataBisnisContent()
}

@Composable
fun SignDataBisnisContent() {
    var name by remember {
        mutableStateOf("")
    }
    var jenis by remember {
        mutableStateOf("")
    }
    var komoditas by remember {
        mutableStateOf("")
    }
    var nomor by remember {
        mutableStateOf("")
    }

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
            Spacer(modifier = Modifier.height(30.dp))
            CustomDataFormField(
                labelName = stringResource(R.string.txt_nama_usaha),
                textHint = stringResource(R.string.txt_nama_usaha_hint),
                value = name,
                onChangeValue = { newValue ->
                    name = newValue
                }
            )
            Spacer(modifier = Modifier.height(20.dp))
            CustomDataFormField(
                labelName = stringResource(R.string.txt_jenis_usaha),
                textHint = stringResource(R.string.txt_jenis_usaha_hint),
                value = jenis,
                onChangeValue = { newValue ->
                    jenis = newValue
                }
            )
            Spacer(modifier = Modifier.height(20.dp))
            CustomDataFormField(
                labelName = stringResource(R.string.txt_komoditas),
                textHint = stringResource(R.string.txt_komoditas_hint),
                value = komoditas,
                onChangeValue = { newValue ->
                    komoditas = newValue
                }
            )
            Text(text = stringResource(R.string.txt_komoditas_exp), style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(25.dp))
            CustomDataFormField(
                labelName = stringResource(R.string.txt_no_telepon),
                textHint = stringResource(R.string.txt_noi_telp_hint),
                value = nomor,
                onChangeValue = { newValue ->
                    nomor = newValue
                }
            )
            Spacer(modifier = Modifier.height(40.dp))
            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                Text(text = stringResource(id = R.string.txt_selanjutnya), style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignDataBisnisPrev() {
    BisnisPlusTheme {
        SignDataBisnis()
    }
}