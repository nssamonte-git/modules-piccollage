# core-creative-text-engine

Validation and editing for text layers. Text is stored as `Content.Text`: the plain string, a `TextFormat` for the whole layer, and `TextStyle` ranges for parts of it. Style ranges count user-visible characters (extended grapheme clusters), so an emoji sequence or a letter with a combining accent is one character. This module checks text and styles, converts grapheme ranges to UTF-16 indices, and commits text as an undoable edit. This is an engine module: it has no Compose dependency.

## When to use it

- Your editor has text layers with per-range colour, size or bold.
- You need to turn a grapheme range into `String` indices (for spans or selection) without splitting emoji.
- For a ready-made text editor panel, also add [core-creative-text-compose](../creative-text-compose/README.md).
- Drawing text is done by `DocumentRenderer` in [core-creative-renderer](../creative-renderer/README.md), not by this module.

## Add it

```kotlin
implementation("noel.libs:core-creative-text-engine:0.2.0")
```

It brings in `core-creative-model` and `kotlinx-coroutines-core` through `api`. It is usually paired with `core-creative-history-engine` for the `EditSession` and `core-creative-renderer` for drawing.

## Quick start

```kotlin
import noel.libs.creative.model.Content
import noel.libs.creative.model.Outcome
import noel.libs.creative.model.TextFormat
import noel.libs.creative.model.TextStyle
import noel.libs.creative.text.domain.Graphemes
import noel.libs.creative.text.domain.SetText
import noel.libs.creative.text.domain.ValidateText

val raw = "Cafe\u0301 at night" // "e" + combining accent is one grapheme
val text =
    Content.Text(
        raw,
        format = TextFormat(size = 48f, alignment = "center"),
        styles = listOf(TextStyle(start = 0, end = 4, bold = true)), // first 4 graphemes
    )

// Check without committing.
if (ValidateText()(text) is Outcome.Failure) showError("Invalid text")

// Grapheme range 0..4 is UTF-16 range 0..5 here.
val (from, to) = Graphemes.utf16Range(raw, 0, 4)

// Validate and commit to an existing, unlocked text layer.
when (val result = SetText()(session, pageId, layerId, text)) {
    is Outcome.Success -> showDocument(result.value)
    is Outcome.Failure -> showError(result.error.message)
}
```

## Main types

| Type | What it does |
|---|---|
| `ValidateText` | Checks a `Content.Text` and returns `Outcome<Content.Text>`. |
| `SetText` | Runs `ValidateText`, then replaces the layer's content with `Edit.ReplaceLayer`. |
| `Graphemes` | `boundaries(text)` returns the UTF-16 offset of every grapheme boundary, starting with 0. `utf16Range(text, start, end)` converts a grapheme range to a UTF-16 `Pair`. |

Types are in `noel.libs.creative.text.domain`. `Content.Text`, `TextFormat` and `TextStyle` are in `noel.libs.creative.model`.

## What your app provides

- An `EditSession` for `SetText`.
- Conversion between your text field's selection (UTF-16 indices) and grapheme indices for `TextStyle`. Use `Graphemes.boundaries` for that.
- Fonts. `TextFormat.fontId` defaults to `"sans-serif"`. The renderer's default resolver knows `sans-serif`, `serif` and `monospace`. For other fonts, pass a `FontResolver` to `DocumentRenderer` that returns typefaces already on the device and licensed for your use; load remote fonts before rendering or exporting.

All calls in this module are synchronous; none of them render.

## Limits and behaviour

`ValidateText` returns `INVALID_INPUT` ("Invalid text or style range") unless all of these hold:

- Text is at most 100,000 UTF-16 chars.
- `format.size` is 1 to 4096, `lineSpacing` 0.5 to 4, `outlineWidth` 0 to 64, `shadowRadius` 0 to 64.
- `alignment` is `"left"`, `"center"` or `"right"`.
- At most 4096 styles. Each has `0 <= start <= end <= grapheme count` and `size` from 1 to 4096.

Other behaviour:

- `SetText` returns `INVALID_INPUT` ("Missing editable layer") for a missing or locked layer. It does not check that the layer already holds text; it replaces the content with whatever you pass.
- Grapheme splitting uses Java's `\X` regex, which treats ZWJ emoji sequences and combining marks as one grapheme.
- `utf16Range` throws `IllegalArgumentException` for a range outside the text.
- When a font cannot be resolved, the renderer draws with the system default and adds a warning; the requested `fontId` stays saved in the document.

## Tests

`TextTest` covers: a family emoji (ZWJ sequence) and an accented letter each count as one grapheme and map to the right UTF-16 range, a style range past the end is rejected, and plain text passes unchanged.

```bash
./gradlew :core-creative-text-engine:testDebugUnitTest
```

## See also

- [core-creative-text-compose](../creative-text-compose/README.md) for the `GraphicTextEditor` panel.
- [core-creative-history-engine](../creative-history-engine/README.md) for `HistorySession` and undo.
- Guide: [Rendering, masks and text](../../docs/creative-libraries.md#rendering-masks-and-text).
- Sample: the "Text" tab in [`MainActivity.kt`](../../sample/src/main/kotlin/noel/libs/sample/MainActivity.kt).
