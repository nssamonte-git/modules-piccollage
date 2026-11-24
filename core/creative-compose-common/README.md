# core-creative-compose-common

Two small layout composables shared by the creative `-compose` modules: `CreativePanel`, a titled column, and `CreativeStatus`, a one-line status text. It is a Compose UI module.

## When to use it

- You build your own creative screen and want it to match the look of the library panels (for example `DocumentBrowser`).
- You do not need it to use the other `-compose` modules. They depend on it with `implementation`, so it is not on your compile classpath through them; add it yourself if you call these functions.

## Add it

```kotlin
implementation("noel.libs:core-creative-compose-common:0.2.1")
```

Brings in `core-creative-model`, `kotlinx-coroutines-core`, and Compose UI and foundation-layout (through the Compose BOM) via `api`. Material 3 is used internally.

## Quick start

```kotlin
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import noel.libs.creative.ui.CreativePanel
import noel.libs.creative.ui.CreativeStatus

@Composable
fun SaveSection(status: String, onSave: () -> Unit) {
    CreativePanel("Projects") {
        Button(onClick = onSave) { Text("Save") }
        CreativeStatus(status)
    }
}
```

## Main types

| Type | What it does |
|---|---|
| `CreativePanel(title, modifier, content)` | A `Column` with 16 dp padding and 12 dp spacing, the title in `titleLarge`, then `content` (a `ColumnScope` lambda). |
| `CreativeStatus(message, modifier)` | Shows `message` in `bodyMedium`; shows nothing when it is empty. |

## What your app provides

A `MaterialTheme` around the UI; text styles come from `MaterialTheme.typography`. All state and strings come from the caller.

## Limits and behaviour

No state, no strings of its own, no effects. Padding and spacing are fixed.

## Tests

This module has no unit tests.

```bash
./gradlew :core-creative-compose-common:testDebugUnitTest
```

## See also

- [core-creative-documents-compose](../creative-documents-compose/README.md) (uses `CreativePanel`)
- Guide: [Adopt individual capabilities](../../docs/creative-libraries.md#adopt-individual-capabilities)
