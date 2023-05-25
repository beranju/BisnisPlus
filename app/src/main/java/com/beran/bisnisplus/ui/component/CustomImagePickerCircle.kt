package com.beran.bisnisplus.ui.component

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.beran.bisnisplus.R

@Composable
fun CustomImagePickerCircle(
    imageUri: Uri?,
    onPickPhoto: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(context).data(imageUri).build()
    )

    Box(
        modifier = modifier
            .width(150.dp)
            .height(170.dp)
    ) {
        Image(
            painter = if (imageUri != null) painter else painterResource(id = R.drawable.img_empty_profile),
            contentDescription = "set photo",
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center,
            modifier = Modifier
                .clip(CircleShape)
                .size(150.dp)
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.onBackground,
                    shape = CircleShape
                )
        )
        IconButton(
            onClick = onPickPhoto,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
        ) {
            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = "add icon",
                tint = Color.White
            )
        }
    }
}