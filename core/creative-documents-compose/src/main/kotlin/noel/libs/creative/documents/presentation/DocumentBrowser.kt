package noel.libs.creative.documents.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import noel.libs.creative.model.*
import noel.libs.creative.ui.CreativePanel

@Composable
fun DocumentBrowser(
    documents: List<CreativeDocument>,
    onOpen: (String) -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
    busy: Boolean = false,
    status: String = "",
) {
    CreativePanel("Projects", modifier) {
        Button(onClick = onSave, enabled = !busy) { Text("Save current project") }
        documents.forEach { d ->
            OutlinedButton(onClick = { onOpen(d.id) }, enabled = !busy) {
                Text("${d.title} · ${d.pages.size} pages · revision ${d.revision}")
            }
        }
        if (documents.isEmpty()) Text("No saved projects")
        Text(status)
    }
}
