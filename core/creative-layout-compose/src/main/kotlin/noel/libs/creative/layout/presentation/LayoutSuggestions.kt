package noel.libs.creative.layout.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import noel.libs.creative.layout.domain.*
import noel.libs.creative.model.*
import noel.libs.creative.ui.CreativePanel

@Composable
fun LayoutSuggestions(
    placements: List<Placement>,
    onSuggest: () -> Unit,
    onApply: (List<Placement>) -> Unit,
    modifier: Modifier = Modifier,
    status: String = "",
) {
    CreativePanel("Layout", modifier) {
        Button(onClick = onSuggest) { Text("Suggest a layout") }
        placements.forEach { p -> Text("${p.assetId} → ${p.slot.id}") }
        OutlinedButton(onClick = { onApply(placements) }, enabled = placements.isNotEmpty()) {
            Text("Apply layout")
        }
        Text(status)
    }
}
