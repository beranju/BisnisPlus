package com.beran.bisnisplus.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Help
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.beran.bisnisplus.R
import com.beran.bisnisplus.constant.ItemMenu
import com.beran.bisnisplus.ui.component.ErrorView
import com.beran.bisnisplus.ui.screen.setting.common.SettingState
import com.beran.bisnisplus.ui.theme.BisnisPlusTheme
import com.beran.core.domain.model.UserModel

@Composable
fun SettingScreen(
    state: SettingState<UserModel>,
    onNavigateToEditProfile: () -> Unit,
    fetchUserData: () -> Unit,
    signOut: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp)
    )
    {
        when (state) {
            is SettingState.Loading -> fetchUserData()
            is SettingState.Success -> {
                ProfileCard(
                    state.data,
                    onNavigateToEditProfile = onNavigateToEditProfile
                )
            }

            is SettingState.Error -> {
                ErrorView(errorText = state.message)
            }
        }
        Spacer(modifier = Modifier.height(50.dp))
        OptionMenu(
            signOut = signOut
        )
    }
}

@Composable
private fun OptionMenu(signOut: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        MenuItem(title = ItemMenu.Setting.string, icon = Icons.Outlined.Settings, onClick = { })
        MenuItem(title = ItemMenu.About.string, icon = Icons.Outlined.Info, onClick = { })
        MenuItem(title = ItemMenu.Help.string, icon = Icons.Outlined.Help, onClick = { })
        MenuItem(title = ItemMenu.SignOut.string, icon = Icons.Outlined.Logout, onClick = signOut)
    }
}

@Composable
private fun MenuItem(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp), modifier = modifier
            .padding(bottom = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.clickable { onClick() })
        }
        Divider()
    }
}

@Composable
private fun ProfileCard(
    user: UserModel?,
    onNavigateToEditProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(8.dp)
    ) {
        AsyncImage(
            model = user?.photoUrl,
            placeholder = painterResource(id = R.drawable.img_empty_profile),
            error = painterResource(id = R.drawable.img_empty_profile),
            contentDescription = "photo profile",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = user?.name.orEmpty(), style = MaterialTheme.typography.titleSmall)
            Text(
                text = user?.phoneNumber.orEmpty(),
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp)
            )
            Button(
                onClick = onNavigateToEditProfile,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(text = "Edit")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SettingScreenPrev() {
    BisnisPlusTheme {
        SettingScreen(
            state = SettingState.Loading,
            onNavigateToEditProfile = {},
            signOut = {},
            fetchUserData = {})
    }
}