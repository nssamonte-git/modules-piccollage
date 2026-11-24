# core-creative-text-compose

A Compose panel for editing a text layer. `GraphicTextEditor` has a text field, a font size slider and an "Apply text" button, and hands the edited `Content.Text` to your app. Your app validates and commits it, usually with `SetText` from [core-creative-text-engine](../creative-text-engine/README.md). This is a Compose UI module.

## When to use it

- You want a simple editor for the text and size of a text layer.
- Use the engine module alone if you need per-range styling controls, font pickers or in-place editing on the canvas.

## Add it

```kotlin
implementation("noel.libs:core-creative-text-compose:0.2.0")
```

It brings in `core-creative-text-engine`, `core-creative-model`, `kotlinx-coroutines-core`, and the Compose BOM, `compose-ui` and `compose-foundation-layout` through `api`. Material 3 and `core-creative-compose-common` are internal (`implementation`).

## Quick start

Based on the sample app, inside a composable:

```kotlin
import noel.libs.creative.model.Content
import noel.libs.creative.model.Outcome
import noel.libs.creative.text.domain.SetText
import noel.libs.creative.text.presentation.GraphicTextEditor

GraphicTextEditor(
    content = layer.content as Content.Text,
    onApply = { text ->
        val result = SetText()(session, page.id, layer.id, text)
        status = if (result is Outcome.Failure) result.error.message else "Done"
    },
)
```

## Main types

| Type | What it does |
|---|---|
| `GraphicTextEditor(content, onApply, modifier)` | Panel titled "Graphic text" with a full-width "Text" field, a "Font size" slider and an "Apply text" button. |

Package: `noel.libs.creative.text.presentation`.

## What your app provides

- The layer's current `Content.Text`, read from the document so the editor resets after undo, redo or another edit.
- `onApply`: validate and commit, for example with `SetText`, and show any failure. The panel does not validate.
- A `MaterialTheme` around it; it uses Material 3 controls.

## Limits and behaviour

- Edits stay local until "Apply text" is pressed. The draft text and size are reset whenever `content` changes.
- The font size slider ranges from 8 to 128. Only `format.size` is changed; every other `TextFormat` field is kept.
- If the text was changed, all `TextStyle` ranges are dropped, because their grapheme positions may no longer be valid. If only the size changed, styles are kept.
- "Apply text" is disabled while the keyboard has an active composition (for example an unfinished IME word), so partial input is not committed.
- There are no controls for font, colour, alignment or style ranges.

## Tests

This module has no unit tests. Text validation and grapheme handling are tested in `core-creative-text-engine`.

## See also

- [core-creative-text-engine](../creative-text-engine/README.md) for `ValidateText`, `SetText`, `Graphemes` and the limits.
- [core-creative-masks-compose](../creative-masks-compose/README.md) and [core-creative-effects-compose](../creative-effects-compose/README.md) for the other layer panels.
- Guide: [Rendering, masks and text](../../docs/creative-libraries.md#rendering-masks-and-text).
- Sample: the "Text" tab in [`MainActivity.kt`](../../sample/src/main/kotlin/noel/libs/sample/MainActivity.kt).
