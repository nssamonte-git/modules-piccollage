# core-creative-history-compose

`HistoryControls`, a row with Undo and Redo buttons bound to an `EditSession`. The buttons are enabled from the session's `history` state. It is a Compose UI module.

## When to use it

- You want ready-made Undo and Redo buttons for a `HistorySession`.
- If you need your own buttons, icons or translated labels, use `core-creative-history-engine` alone and call `session.undo()` / `session.redo()` with `session.history` for the enabled state.

## Add it

```kotlin
implementation("noel.libs:core-creative-history-compose:0.2.2")
```

Brings in `core-creative-history-engine`, `core-creative-model`, `kotlinx-coroutines-core`, and Compose UI and foundation-layout (through the Compose BOM) via `api`.

## Quick start

```kotlin
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import noel.libs.creative.history.presentation.HistoryControls
import noel.libs.creative.model.EditSession

@Composable
fun EditorToolbar(session: EditSession) {
    HistoryControls(session, Modifier.padding(horizontal = 16.dp))
}
```

This is how the sample places it, above `CreativeCanvas`.

## Main types

| Type | What it does |
|---|---|
| `HistoryControls(session, modifier)` | Two `OutlinedButton`s, "Undo" and "Redo", 8 dp apart. Enabled from `canUndo` / `canRedo`. |

## What your app provides

The `EditSession` (normally a `HistorySession`) and a `MaterialTheme`. When you replace the session (for example after opening another draft), pass the new one; the composable collects whichever session it receives.

## Limits and behaviour

- The labels "Undo" and "Redo" are fixed English strings.
- The `Outcome` returned by `undo()` and `redo()` is ignored; nothing is shown on failure.

## Tests

This module has no unit tests. The sample's device test `sampleActivityLaunches` starts `MainActivity`, which shows `HistoryControls`; it only checks that the activity launches.

```bash
./gradlew :sample:connectedDebugAndroidTest
```

## See also

- [core-creative-history-engine](../creative-history-engine/README.md), [core-creative-canvas-compose](../creative-canvas-compose/README.md)
- Guide: [Editing and composition](../../docs/creative-libraries.md#editing-and-composition)
- Sample: `sample/src/main/kotlin/noel/libs/sample/MainActivity.kt`
