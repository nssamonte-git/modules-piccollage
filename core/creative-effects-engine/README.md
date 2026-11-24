# core-creative-effects-engine

Image effects for layers: brightness, contrast, saturation, grayscale and box blur. Effects are stored on each `Layer` as a list of `Effect` values. This module validates them, commits them as an undoable edit, and provides two `ImageEffects` adapters for the shared `DocumentRenderer`: an OpenGL ES 2 adapter that falls back to the CPU, and a pure CPU adapter. This is an engine module: it has no Compose dependency.

## When to use it

- Your editor lets users adjust a photo or layer with simple filters.
- You render documents with `DocumentRenderer` and some layers have `effects`. Without an `ImageEffects` adapter those layers render as placeholders (or fail with `strict = true`).
- For ready-made sliders, also add [core-creative-effects-compose](../creative-effects-compose/README.md).
- Only the five built-in effects exist. There are no custom shaders, LUTs or image blending.

## Add it

```kotlin
implementation("noel.libs:core-creative-effects-engine:0.2.0")
```

It brings in `core-creative-model`, [`core-creative-renderer`](../creative-renderer/README.md) and `kotlinx-coroutines-core` through `api`. It is usually paired with `core-creative-history-engine` for the `EditSession` and [core-creative-masks-engine](../creative-masks-engine/README.md) for the other renderer adapter.

## Quick start

```kotlin
import noel.libs.creative.effects.data.GpuImageEffects
import noel.libs.creative.effects.domain.SetEffects
import noel.libs.creative.model.Effect
import noel.libs.creative.model.Outcome
import noel.libs.creative.renderer.data.DocumentRenderer

// Once, at your composition root. `assets` is your AssetBytes implementation.
val renderer = DocumentRenderer(assets, effects = GpuImageEffects { backend -> lastBackend = backend })

// Replace a layer's effects as one undoable commit.
val result =
    SetEffects()(
        session,
        pageId,
        layerId,
        listOf(Effect(Effect.Kind.SATURATION, 0.7f), Effect(Effect.Kind.BRIGHTNESS, 0.1f)),
    )
if (result is Outcome.Failure) showError(result.error.message)

// On a worker thread: render the page. You own and must recycle the bitmap.
val page = renderer.render(session.document.value, pageId)
```

`backend` is `"OpenGL ES 2"` when the GPU path ran and `"CPU fallback"` when it did not.

## Main types

| Type | What it does |
|---|---|
| `SetEffects` | Validates an effect list and commits it to one unlocked layer with `Edit.ReplaceLayer`. |
| `validateEffect(effect)` | Throws `IllegalArgumentException` if the amount is not finite or out of range for its kind. |
| `ValidateEffectGraph` | Checks a list of `EffectNode`s and returns the effects as an ordered list (`Outcome<List<Effect>>`). |
| `EffectNode` | `id`, `inputs` (IDs of earlier nodes) and an `Effect`. |
| `PixelEffects` | `apply(source, width, height, effects)`: CPU reference pipeline on straight-alpha ARGB `IntArray` pixels. Returns a new array. |
| `GpuImageEffects` | `ImageEffects` adapter using OpenGL ES 2 with CPU fallback. Optional `backendSelected` callback. |
| `CpuImageEffects` | `ImageEffects` adapter that runs `PixelEffects` on a `Bitmap`. |

Domain types are in `noel.libs.creative.effects.domain`; the adapters are in `noel.libs.creative.effects.data`. `Effect` and `Effect.Kind` are in `noel.libs.creative.model`.

## What your app provides

- An `EditSession` for `SetEffects`, and an `AssetBytes` for `DocumentRenderer`.
- A worker thread for rendering and for direct `apply` calls. They are synchronous. `GpuImageEffects` creates and destroys its own EGL context on every call, so it can run on any worker thread.
- Bitmap cleanup. Both adapters return a new `Bitmap` and leave the source unchanged; the caller recycles both.

## Limits and behaviour

- Allowed amounts: `BRIGHTNESS` -1 to 1 (0 is neutral), `CONTRAST` and `SATURATION` 0 to 2 (1 is neutral), `GRAYSCALE` 0 to 1, `BLUR` 0 to 8 (box blur radius in pixels, rounded).
- At most 32 effects per layer and 16 million pixels per image. `SetEffects` returns `INVALID_INPUT` ("Invalid effects") for a bad list and `INVALID_INPUT` ("Missing editable layer") for a missing or locked layer. The adapters throw `IllegalArgumentException` for invalid input instead of falling back.
- Effects run in list order. Alpha is kept; colour effects change only RGB.
- If the GPU path throws at runtime (no EGL display, shader or framebuffer failure), `GpuImageEffects` reports `"CPU fallback"` and returns the CPU result. The saved effect values are the same either way.
- `ValidateEffectGraph` accepts only a single chain: at most 32 nodes with unique IDs, exactly one node with no input, at most one input per node, each node used as an input at most once, no cycles and no missing inputs. Anything else returns `INVALID_INPUT` ("Invalid effect graph"). It is not an image-merging API.

## Tests

`EffectsTest` covers: neutral brightness, contrast and saturation leave pixels unchanged, grayscale keeps alpha, and a cyclic effect graph is rejected.

```bash
./gradlew :core-creative-effects-engine:testDebugUnitTest
```

The sample's device test `CreativeAndroidTest.gpuMatchesCpuAndPreservesOrientation` checks that the GPU path is selected and that GPU and CPU output agree within 3 per channel. Run it on a device or emulator with:

```bash
./gradlew :sample:connectedDebugAndroidTest
```

## See also

- [core-creative-effects-compose](../creative-effects-compose/README.md) for the `EffectControls` sliders.
- [core-creative-masks-engine](../creative-masks-engine/README.md) for the mask adapter used by the same renderer.
- Guide: [Rendering, masks and text](../../docs/creative-libraries.md#rendering-masks-and-text).
- Sample: `DocumentRenderer(assets, GpuImageEffects(), AndroidMask())` in [`DemoEnvironment.kt`](../../sample/src/main/kotlin/noel/libs/sample/DemoEnvironment.kt), the "Effects" tab in [`MainActivity.kt`](../../sample/src/main/kotlin/noel/libs/sample/MainActivity.kt), and [`CreativeAndroidTest.kt`](../../sample/src/androidTest/kotlin/noel/libs/sample/CreativeAndroidTest.kt).
