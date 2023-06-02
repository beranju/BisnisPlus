package com.beran.bisnisplus.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.beran.bisnisplus.ui.screen.setting.common.OptionMenu
import com.beran.bisnisplus.ui.screen.setting.common.ProfileCard
import com.beran.bisnisplus.ui.screen.setting.common.ProfileCardShimmer
import com.beran.bisnisplus.ui.screen.setting.common.SettingState
import com.beran.bisnisplus.ui.theme.BisnisPlusTheme

@Composable
fun SettingScreen(
    state: SettingState,
    onNavigateToEditProfile: () -> Unit,
    onNavigateToCompleteProfile: () -> Unit,
    fetchUserData: () -> Unit,
    signOut: () -> Unit,
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
    LaunchedEffect(key1 = state.isLoading) {
        if (state.isLoading) {
            fetchUserData()
        }
        loading = state.isLoading
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp)
    )
    {
        if (loading) {
            ProfileCardShimmer()
        } else {
            ProfileCard(
                state.user,
                onNavigateToEditProfile = onNavigateToEditProfile,
                onNavigateToCompleteProfile = onNavigateToCompleteProfile,
            )
        }
        Spacer(modifier = Modifier.height(50.dp))
        OptionMenu(
            signOut = signOut
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SettingScreenPrev() {
    BisnisPlusTheme {
        SettingScreen(
            state = SettingState(),
            onNavigateToEditProfile = {},
            onNavigateToCompleteProfile = {},
            signOut = {},
            fetchUserData = {})
    }
}