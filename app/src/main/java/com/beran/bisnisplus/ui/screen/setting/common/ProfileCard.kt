package com.beran.bisnisplus.ui.screen.setting.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.beran.bisnisplus.R
import com.beran.core.domain.model.UserModel

@Composable
fun ProfileCard(
    user: UserModel?,
    onNavigateToEditProfile: () -> Unit,
    onNavigateToCompleteProfile: () -> Unit,
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
           if (user?.phoneNumber == null || user.bisnisId == null) {
               Button(
                   onClick = onNavigateToCompleteProfile,
                   modifier = Modifier.align(Alignment.End)
               ) {
                   Text(text = "Lengkapi data")
               }
            }else{
               Button(
                   onClick = onNavigateToEditProfile,
                   modifier = Modifier.align(Alignment.End)
               ) {
                   Text(text = "Edit")
               }
           }
        }
    }
}
