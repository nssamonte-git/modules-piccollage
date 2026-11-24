# core-creative-ai-tools-compose

A Compose panel that shows one proposed AI edit (a `ToolCall`) with Apply and Dismiss buttons, so the user approves each edit before it runs. It does not execute anything itself; your Apply callback passes the call to `ToolRunner` from [core-creative-ai-tools-engine](../creative-ai-tools-engine/README.md). This is a Compose UI module.

## When to use it

- You want a simple confirm step between an assistant's suggestion and the edit.
- Build your own UI if you need to describe the edit in friendlier words, show several proposals, or localize labels.

## Add it

```kotlin
implementation("noel.libs:core-creative-ai-tools-compose:0.2.0")
```

Brings in through `api`: `core-creative-ai-tools-engine`, `core-creative-model`, `kotlinx-coroutines-core`, and Compose UI and foundation layout from the Compose BOM. Material 3 and `core-creative-compose-common` are internal.

## Quick start

From the sample's "AI Tools" tab:

```kotlin
import androidx.compose.runtime.*
import noel.libs.creative.aitools.domain.ToolCall
import noel.libs.creative.aitools.domain.ToolRunner
import noel.libs.creative.aitools.presentation.ToolProposalPanel

var proposal by remember { mutableStateOf<ToolCall?>(null) }
val toolRunner = remember { ToolRunner(journal) }

ToolProposalPanel(
    call = proposal,
    onAccept = {
        proposal?.let {
            status = describe(toolRunner.execute(it, session)) // your formatting
            proposal = null
        }
    },
    onReject = { proposal = null },
    status = status,
)
```

## Main types

| Type | What it does |
|---|---|
| `ToolProposalPanel(call, onAccept, onReject, modifier, status = "")` | Shows the tool name and each argument as `key: value`, with Apply and Dismiss. |

## What your app provides

- The `ToolCall` to show (or `null`), and the `ToolRunner` and `EditSession` used on Apply.
- Threading: `ToolRunner.execute` is synchronous and writes the journal. With a file-backed journal, run it off the main thread from your Apply callback.
- A `MaterialTheme` around the panel.

## Limits and behaviour

- With `call == null` it shows "No proposed edit".
- Arguments are shown raw, as the model sent them.
- `status` is shown under the buttons as plain text.
- Labels are fixed English strings.

## Tests

This module has no unit tests.

## See also

- [core-creative-ai-tools-engine](../creative-ai-tools-engine/README.md)
- Guide: [AI and cloud boundaries](../../docs/creative-libraries.md#ai-and-cloud-boundaries)
- Sample: `sample/src/main/kotlin/noel/libs/sample/MainActivity.kt` ("AI Tools" tab)
