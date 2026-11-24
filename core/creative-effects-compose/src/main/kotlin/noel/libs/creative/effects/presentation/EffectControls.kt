package noel.libs.creative.effects.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import noel.libs.creative.model.*
import noel.libs.creative.ui.CreativePanel

@Composable
fun EffectControls(
    effects: List<Effect>,
    onChange: (List<Effect>) -> Unit,
    modifier: Modifier = Modifier,
) {
    CreativePanel("Image effects", modifier) {
        Effect.Kind.entries.forEach { kind ->
            val range =
                when (kind) {
                    Effect.Kind.BRIGHTNESS -> -1f..1f
                    Effect.Kind.CONTRAST,
                    Effect.Kind.SATURATION -> 0f..2f
                    Effect.Kind.GRAYSCALE -> 0f..1f
                    Effect.Kind.BLUR -> 0f..8f
                }
            val default =
                if (kind == Effect.Kind.CONTRAST || kind == Effect.Kind.SATURATION) 1f else 0f
            var value by
                remember(kind, effects) {
                    mutableFloatStateOf(effects.firstOrNull { it.kind == kind }?.amount ?: default)
                }
            Text(kind.name.lowercase().replaceFirstChar { it.uppercase() })
            Slider(
                value,
                { value = it },
                valueRange = range,
                onValueChangeFinished = {
                    onChange(effects.filterNot { it.kind == kind } + Effect(kind, value))
                },
            )
        }
        TextButton(onClick = { onChange(emptyList()) }) { Text("Reset effects") }
    }
}
