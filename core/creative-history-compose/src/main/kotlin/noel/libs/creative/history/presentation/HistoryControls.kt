package noel.libs.creative.history.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import noel.libs.creative.model.*

@Composable
fun HistoryControls(session: EditSession, modifier: Modifier = Modifier) {
    val state by session.history.collectAsState()
    Row(modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedButton(onClick = { session.undo() }, enabled = state.canUndo) { Text("Undo") }
        OutlinedButton(onClick = { session.redo() }, enabled = state.canRedo) { Text("Redo") }
    }
}
