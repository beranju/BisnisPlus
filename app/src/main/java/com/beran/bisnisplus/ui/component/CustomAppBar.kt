package com.beran.bisnisplus.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.beran.bisnisplus.ui.theme.BisnisPlusTheme

@Composable
fun CustomAppBar(
    titleAppBar: String,
    onLeadingClick: (() -> Unit)? = null,
    onTrailingClick: (() -> Unit)? = null,
    leadingIcon: ImageVector? = null,
    showTrailingIcon: Boolean = false
) {
    Surface(shadowElevation = 2.dp, color = MaterialTheme.colorScheme.background) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(horizontal = 16.dp)
        ) {
            if (leadingIcon != null) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = "Menu",
                    modifier = Modifier.clickable {
                        if (onLeadingClick != null) {
                            onLeadingClick()
                        }
                    })
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = titleAppBar,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.weight(1f)
            )
            if (showTrailingIcon) Icon(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = "Notification icon",
                modifier = Modifier.clickable {
                    if (onTrailingClick != null) {
                        onTrailingClick()
                    }
                }
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun CustomAppBarPrev() {
    BisnisPlusTheme {
        CustomAppBar(
            titleAppBar = "Bisnis Plus",
            onLeadingClick = {},
            onTrailingClick = {},
            showTrailingIcon = true
        )
    }
}