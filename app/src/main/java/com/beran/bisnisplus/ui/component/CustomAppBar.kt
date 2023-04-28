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
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
    onLeadingClick: () -> Unit,
    leadingIcon: ImageVector = Icons.Outlined.Menu,
    showTrailingIcon: Boolean = false
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .height(85.dp)
            .padding(horizontal = 16.dp)
    ) {
        Icon(imageVector = leadingIcon, contentDescription = "Menu", modifier = Modifier.clickable { onLeadingClick() })
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = titleAppBar,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.weight(1f)
        )
        if (showTrailingIcon) Icon(
            imageVector = Icons.Outlined.AccountCircle,
            contentDescription = "Avatar icon"
        )
    }

}

@Preview(showBackground = true)
@Composable
fun CustomAppBarPrev() {
    BisnisPlusTheme {
        CustomAppBar(titleAppBar = "Bisnis Plus", onLeadingClick = {}, showTrailingIcon = true)
    }
}