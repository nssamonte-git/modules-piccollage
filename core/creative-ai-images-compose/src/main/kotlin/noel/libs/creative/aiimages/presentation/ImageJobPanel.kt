package noel.libs.creative.aiimages.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import noel.libs.creative.model.*
import noel.libs.creative.ui.CreativePanel

@Composable
fun ImageJobPanel(
    busy: Boolean,
    hasResult: Boolean,
    onGenerate: (String) -> Unit,
    onCancel: () -> Unit,
    onAccept: () -> Unit,
    modifier: Modifier = Modifier,
    status: String = "",
) {
    var prompt by remember { mutableStateOf("") }
    CreativePanel("AI image editing", modifier) {
        OutlinedTextField(
            prompt,
            { prompt = it },
            label = { Text("Describe the edit") },
            modifier = Modifier.fillMaxWidth(),
        )
        Button(onClick = { onGenerate(prompt) }, enabled = !busy) { Text("Generate") }
        if (busy) TextButton(onClick = onCancel) { Text("Cancel") }
        if (hasResult) OutlinedButton(onClick = onAccept) { Text("Use result") }
        Text(status)
    }
}
