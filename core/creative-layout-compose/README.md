# core-creative-layout-compose

A small Compose panel for [core-creative-layout-engine](../creative-layout-engine/README.md). `LayoutSuggestions` shows a "Suggest a layout" button, the current placements, an "Apply layout" button and a status line. Your app runs `SuggestLayout` and commits the result; the panel only displays state and calls back. This is a Compose UI module.

## When to use it

- You want a quick suggest-then-apply control for photo placement.
- Use the engine module alone if you show placements on the canvas or build your own controls.

## Add it

```kotlin
implementation("noel.libs:core-creative-layout-compose:0.2.0")
```

It brings in `core-creative-layout-engine`, `core-creative-model`, `kotlinx-coroutines-core`, and the Compose BOM, `compose-ui` and `compose-foundation-layout` through `api`. Material 3 and `core-creative-compose-common` are internal (`implementation`).

## Quick start

Based on the sample app, inside a composable:

```kotlin
import androidx.compose.runtime.*
import noel.libs.creative.layout.domain.LayoutPhoto
import noel.libs.creative.layout.domain.Placement
import noel.libs.creative.layout.domain.Slot
import noel.libs.creative.layout.domain.SuggestLayout
import noel.libs.creative.layout.presentation.LayoutSuggestions
import noel.libs.creative.model.Outcome

var placements by remember { mutableStateOf<List<Placement>>(emptyList()) }
var status by remember { mutableStateOf("") }

LayoutSuggestions(
    placements = placements,
    onSuggest = {
        val result =
            SuggestLayout()(
                listOf(LayoutPhoto(document.assets.first())),
                listOf(Slot("main", 20.0, 80.0, 600.0, 360.0)),
            )
        if (result is Outcome.Success) placements = result.value
        status = if (result is Outcome.Failure) result.error.message else "Done"
    },
    onApply = { items -> status = applyPlacements(items) }, // your Edit.ReplaceLayer commit
    status = status,
)
```

See the engine README for how the sample turns a `Placement` into an `Edit.ReplaceLayer`.

## Main types

| Type | What it does |
|---|---|
| `LayoutSuggestions(placements, onSuggest, onApply, modifier, status)` | Panel titled "Layout". Lists each placement as one line of text: the asset ID, an arrow, and the slot ID. "Apply layout" is enabled only when `placements` is not empty and passes the list to `onApply`. `status` defaults to an empty string. |

Package: `noel.libs.creative.layout.presentation`.

## What your app provides

- The placements state, and the call to `SuggestLayout` in `onSuggest`.
- The commit in `onApply`, including converting each `Roi` crop to `Crop` for `Content.Image`.
- The status text, for example the failure message from `SuggestLayout` or the commit.
- A `MaterialTheme` around it; text uses Material 3 styles.

## Limits and behaviour

- The panel holds no state of its own; it shows exactly what you pass.
- It shows no image previews or crop outlines, only asset and slot IDs.
- `onSuggest` runs on the main thread when the button is pressed; move large suggestion runs to a worker yourself.

## Tests

This module has no unit tests. The assignment and crop logic is tested in `core-creative-layout-engine`.

## See also

- [core-creative-layout-engine](../creative-layout-engine/README.md) for `SuggestLayout`, `Placement` and the failure rules.
- [core-creative-templates-compose](../creative-templates-compose/README.md) for the template picker.
- Guide: [Templates and layout](../../docs/creative-libraries.md#templates-and-layout).
- Sample: the "Layout" tab in [`MainActivity.kt`](../../sample/src/main/kotlin/noel/libs/sample/MainActivity.kt).
