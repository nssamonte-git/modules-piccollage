package noel.libs.creative.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import noel.libs.creative.model.*

@Composable
fun CreativePanel(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(title, style = MaterialTheme.typography.titleLarge)
        content()
    }
}

@Composable
fun CreativeStatus(message: String, modifier: Modifier = Modifier) {
    if (message.isNotEmpty()) Text(message, modifier, style = MaterialTheme.typography.bodyMedium)
}
