# core-creative-effects-compose

A Compose panel of effect sliders for [core-creative-effects-engine](../creative-effects-engine/README.md). `EffectControls` shows one slider per `Effect.Kind` and a reset button, and reports the new effect list when the user releases a slider. Your app commits the list, usually with `SetEffects`. This is a Compose UI module.

## When to use it

- You want basic adjustment sliders for the selected layer without writing the UI.
- Use the engine module alone if you need presets, a different control layout, or live preview while dragging.

## Add it

```kotlin
implementation("noel.libs:core-creative-effects-compose:0.2.0")
```

It brings in `core-creative-effects-engine` (and through it `core-creative-renderer`), `core-creative-model`, `kotlinx-coroutines-core`, and the Compose BOM, `compose-ui` and `compose-foundation-layout` through `api`. Material 3 and `core-creative-compose-common` are internal (`implementation`).

## Quick start

Based on the sample app, inside a composable:

```kotlin
import noel.libs.creative.effects.domain.SetEffects
import noel.libs.creative.effects.presentation.EffectControls
import noel.libs.creative.model.Outcome

EffectControls(
    effects = layer.effects,
    onChange = { effects ->
        val result = SetEffects()(session, page.id, layer.id, effects)
        status = if (result is Outcome.Failure) result.error.message else "Done"
    },
)
```

## Main types

| Type | What it does |
|---|---|
| `EffectControls(effects, onChange, modifier)` | Panel titled "Image effects" with a labelled slider for each `Effect.Kind` and a "Reset effects" button. |

Package: `noel.libs.creative.effects.presentation`.

## What your app provides

- The current `effects` of the selected layer, read from the document so the sliders follow undo and redo.
- `onChange`: commit the list (for example `SetEffects`) and show any failure.
- Rendering of the result. The panel shows no preview; re-render the page with `DocumentRenderer` after the commit.
- A `MaterialTheme` around it; it uses Material 3 sliders and buttons.

## Limits and behaviour

- Slider ranges match the engine's limits: brightness -1 to 1, contrast and saturation 0 to 2, grayscale 0 to 1, blur 0 to 8. A kind that is not in the list starts at its neutral value (1 for contrast and saturation, 0 for the others).
- `onChange` is called once when a slider is released, not while it moves. The new list is the old list without that kind, with the new value added at the end. Because effects run in list order, the last adjusted effect runs last.
- "Reset effects" calls `onChange` with an empty list.
- The panel does not validate or commit anything itself.

## Tests

This module has no unit tests. Effect validation and the pixel pipeline are tested in `core-creative-effects-engine`.

## See also

- [core-creative-effects-engine](../creative-effects-engine/README.md) for `SetEffects`, the renderer adapters and the limits.
- [core-creative-masks-compose](../creative-masks-compose/README.md) for the mask editor.
- Guide: [Rendering, masks and text](../../docs/creative-libraries.md#rendering-masks-and-text).
- Sample: the "Effects" tab in [`MainActivity.kt`](../../sample/src/main/kotlin/noel/libs/sample/MainActivity.kt).
