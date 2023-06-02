package com.beran.bisnisplus.ui.screen.home.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.beran.bisnisplus.R
import com.beran.core.domain.model.UserModel

@Composable
fun HomeProfileCard(
    modifier: Modifier = Modifier,
    user: UserModel? = null,
    onNavigateToProfile: (() -> Unit)? = null,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.clickable {
            if (onNavigateToProfile != null) {
                onNavigateToProfile()
            }
        }) {
        AsyncImage(
            model = user?.photoUrl,
            contentDescription = "Photo profile",
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.img_empty_profile),
            error = painterResource(id = R.drawable.img_empty_profile),
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = user?.name.orEmpty(),
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleSmall
            )
            if (user?.phoneNumber == null || user.bisnisId == null) {
                Button(onClick = {
                    if (onNavigateToProfile != null) {
                        onNavigateToProfile()
                    }
                }) {
                    Text(text = "Lengkapi profile")
                }
            } else {
                Text(text = user.phoneNumber.orEmpty(), style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}