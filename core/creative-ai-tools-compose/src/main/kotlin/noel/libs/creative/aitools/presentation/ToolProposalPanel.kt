package noel.libs.creative.aitools.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import noel.libs.creative.aitools.domain.*
import noel.libs.creative.model.*
import noel.libs.creative.ui.CreativePanel

@Composable
fun ToolProposalPanel(
    call: ToolCall?,
    onAccept: () -> Unit,
    onReject: () -> Unit,
    modifier: Modifier = Modifier,
    status: String = "",
) {
    CreativePanel("Suggested edit", modifier) {
        if (call == null) Text("No proposed edit")
        else {
            Text(call.name)
            call.arguments.forEach { (k, v) -> Text("$k: $v") }
            Row {
                Button(onClick = onAccept) { Text("Apply") }
                TextButton(onClick = onReject) { Text("Dismiss") }
            }
        }
        Text(status)
    }
}
