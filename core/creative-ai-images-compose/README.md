# core-creative-ai-images-compose

A Compose panel for AI image editing: a prompt field, a Generate button, Cancel while a job runs, and "Use result" when a result is ready. It holds only the prompt text; your callbacks start, cancel and accept jobs with `ImageJobRunner` from [core-creative-ai-images-engine](../creative-ai-images-engine/README.md). This is a Compose UI module.

## When to use it

- You want a basic prompt-and-accept screen on top of the image job engine.
- Build your own UI if you need operation choice, style or mask inputs, a result preview, or localized labels.

## Add it

```kotlin
implementation("noel.libs:core-creative-ai-images-compose:0.2.0")
```

Brings in through `api`: `core-creative-ai-images-engine`, `core-creative-model`, `kotlinx-coroutines-core`, and Compose UI and foundation layout from the Compose BOM. Material 3 and `core-creative-compose-common` are internal.

## Quick start

Shortened from the sample's "AI Images" tab:

```kotlin
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import noel.libs.creative.aiimages.domain.ImageJob
import noel.libs.creative.aiimages.presentation.ImageJobPanel
import noel.libs.creative.model.Outcome

var imageJob by remember { mutableStateOf<ImageJob?>(null) }
var busy by remember { mutableStateOf(false) }
var hasResult by remember { mutableStateOf(false) }

ImageJobPanel(
    busy = busy,
    hasResult = hasResult,
    onGenerate = { prompt ->
        val job = buildJob(prompt) // your ImageJob for the selected image layer
        imageJob = job
        scope.launch {
            busy = true
            try {
                hasResult = imageRunner.start(job) is Outcome.Success
            } finally {
                busy = false
            }
        }
    },
    onCancel = { imageJob?.let { imageRunner.cancel(it) }; hasResult = false },
    onAccept = {
        imageJob?.let { imageRunner.accept(it, session, pageId, layerId) }
        hasResult = false
    },
    status = status,
)
```

## Main types

| Type | What it does |
|---|---|
| `ImageJobPanel(busy, hasResult, onGenerate, onCancel, onAccept, modifier, status = "")` | Prompt field and job buttons; `onGenerate` receives the prompt text. |

## What your app provides

- The `busy` and `hasResult` flags, the `ImageJob` built from the prompt, and the `ImageJobRunner` calls.
- Threading: `start` is `suspend`; `cancel` and `accept` are synchronous and write the journal. With a file-backed journal, run them from a worker context.
- A `MaterialTheme` around the panel.

## Limits and behaviour

- Generate is disabled while `busy`. Cancel is shown only while `busy`. "Use result" is shown only when `hasResult`.
- The prompt is kept with `remember`, so it is lost on configuration change.
- The panel does not reference engine types; it passes the prompt string and nothing else.
- `status` is shown as plain text. Labels are fixed English strings.

## Tests

This module has no unit tests.

## See also

- [core-creative-ai-images-engine](../creative-ai-images-engine/README.md)
- Guide: [AI and cloud boundaries](../../docs/creative-libraries.md#ai-and-cloud-boundaries)
- Sample: `sample/src/main/kotlin/noel/libs/sample/MainActivity.kt` ("AI Images" tab)
