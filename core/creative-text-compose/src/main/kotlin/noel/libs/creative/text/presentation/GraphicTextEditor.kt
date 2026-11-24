package noel.libs.creative.text.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import noel.libs.creative.model.*
import noel.libs.creative.ui.CreativePanel

@Composable
fun GraphicTextEditor(
    content: Content.Text,
    onApply: (Content.Text) -> Unit,
    modifier: Modifier = Modifier,
) {
    var draft by remember(content) { mutableStateOf(TextFieldValue(content.text)) }
    var size by remember(content) { mutableFloatStateOf(content.format.size) }
    CreativePanel("Graphic text", modifier) {
        OutlinedTextField(
            draft,
            { draft = it },
            label = { Text("Text") },
            modifier = Modifier.fillMaxWidth(),
        )
        Text("Font size: ${size.toInt()}")
        Slider(size, { size = it }, valueRange = 8f..128f)
        Button(
            onClick = {
                onApply(
                    content.copy(
                        text = draft.text,
                        format = content.format.copy(size = size),
                        styles = if (draft.text == content.text) content.styles else emptyList(),
                    )
                )
            },
            enabled = draft.composition == null,
        ) {
            Text("Apply text")
        }
    }
}
