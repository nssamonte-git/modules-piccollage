package noel.libs.creative.export.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import noel.libs.creative.export.domain.*
import noel.libs.creative.model.*
import noel.libs.creative.ui.CreativePanel

@Composable
fun ExportControls(
    state: ExportState,
    onPng: () -> Unit,
    onJpeg: () -> Unit,
    onMp4: () -> Unit,
    onCancel: () -> Unit,
    onShare: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CreativePanel("Export", modifier) {
        val running = state is ExportState.Running
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = onPng, enabled = !running) { Text("PNG") }
            Button(onClick = onJpeg, enabled = !running) { Text("JPEG") }
            Button(onClick = onMp4, enabled = !running) { Text("MP4") }
        }
        when (state) {
            is ExportState.Running -> {
                LinearProgressIndicator(progress = { state.progress / 100f })
                TextButton(onClick = onCancel) { Text("Cancel") }
            }
            is ExportState.Completed -> {
                Text("Export ready")
                OutlinedButton(onClick = onShare) { Text("Share") }
            }
            is ExportState.Failed -> Text(state.message)
            ExportState.Canceled -> Text("Export canceled")
            ExportState.Idle -> Text("Choose an output format")
        }
    }
}
