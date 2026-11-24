# core-creative-templates-engine

Typed template recipes for a `CreativeDocument`. A `Recipe` is a list of steps that add image and text layers to pages. The engine checks the whole recipe against the document before anything is changed, then applies it as one undoable commit, with an optional entitlement check for premium recipes. This is an engine module: it has no Compose dependency.

## When to use it

- You offer ready-made layouts ("templates") that fill a page with photos and text.
- You need premium templates that are checked against the user's entitlements at the moment they are applied.
- You want to search a local list of recipes by name, category and number of photos.
- For a ready-made template picker, also add [core-creative-templates-compose](../creative-templates-compose/README.md).
- For placing photos into slots with subject-aware crops, use [core-creative-layout-engine](../creative-layout-engine/README.md).

## Add it

```kotlin
implementation("noel.libs:core-creative-templates-engine:0.2.1")
```

It brings in `core-creative-model` and `kotlinx-coroutines-core` through `api`. You also need an `EditSession`; `HistorySession` from `core-creative-history-engine` is the usual one.

## Quick start

```kotlin
import kotlinx.coroutines.launch
import noel.libs.creative.history.domain.HistorySession
import noel.libs.creative.model.Outcome
import noel.libs.creative.templates.domain.ApplyRecipe
import noel.libs.creative.templates.domain.Recipe
import noel.libs.creative.templates.domain.RecipeStep

val session = HistorySession(document)
val pageId = document.pages.first().id

val recipe =
    Recipe(
        id = "photo-and-title",
        category = "birthday",
        steps =
            listOf(
                RecipeStep.Image("cover", pageId, "cover-layer", 20.0, 20.0, 600.0, 400.0),
                RecipeStep.Text(pageId, "title-layer", "Your story", 50.0, 440.0, 400.0, 70.0),
            ),
    )

// ApplyRecipe is a suspend function; call it from a coroutine.
scope.launch {
    // "cover" is the slot ID; the value is an image asset already in document.assets.
    val slots = mapOf("cover" to photoAssetId)
    when (val result = ApplyRecipe(entitlements)("account-1", session, recipe, slots)) {
        is Outcome.Success -> showDocument(result.value)
        is Outcome.Failure -> showError(result.error.message)
    }
}
```

Pass `null` for `entitlements` if you have no premium recipes (the sample does this). Use `PlanRecipe()(recipe, slots, document)` to get the `EditRequest` without committing it.

## Main types

| Type | What it does |
|---|---|
| `Recipe` | `id`, `version` (default 1), `category`, optional `premiumCapability`, and the list of steps. |
| `RecipeStep.Image` | Adds an image layer for a named slot at `x`, `y`, `width`, `height` on a page. |
| `RecipeStep.Text` | Adds a text layer; `width` and `height` default to 200 and 60. |
| `PlanRecipe` | Resolves slots and checks the recipe against a document. Returns `Outcome<EditRequest>` and changes nothing. |
| `ApplyRecipe` | Checks the entitlement, plans the recipe and commits it to an `EditSession`. |
| `FilterRecipes` | Filters a list by a query (matched case-insensitively against `id` or `category`) and by the number of distinct image slots. |
| `TemplateCatalog` | Interface for a host-provided recipe source: `suspend fun search(query, photoCount): Outcome<List<Recipe>>`. |

All types are in `noel.libs.creative.templates.domain`.

## What your app provides

- An `EditSession` for the document (for example `HistorySession`).
- The slot map: slot ID to asset ID. Each asset must already be in `document.assets` with a `mediaType` that starts with `image/`. Add new photos first with `Edit.AddAsset`.
- An `Entitlements` implementation if any recipe sets `premiumCapability`. `ApplyRecipe` calls `allowed(accountId, capability)` every time a premium recipe is applied.
- A `TemplateCatalog` implementation if recipes come from a server or bundle. The library ships no catalog and no recipes.
- A coroutine scope for `ApplyRecipe`. `PlanRecipe` and `FilterRecipes` are plain synchronous calls.

## Limits and behaviour

- Only `version = 1` is accepted. Other versions fail with `UNSUPPORTED`.
- A step whose slot has no entry in the map fails with `MISSING_ASSET` ("Select media for <slot>").
- More than 256 steps, a missing page, a slot that points to a non-image or unknown asset, or a layer ID that already exists in the document fail with `INVALID_INPUT` ("Recipe is incompatible with this document"). Layer IDs in a recipe must be unique in the document, so applying the same recipe twice needs new layer IDs (the sample adds the document revision to the ID).
- A premium recipe fails with `UNAVAILABLE` ("Content access required") when the entitlement check returns false or `entitlements` is `null`.
- Nothing is changed on failure. On success the whole recipe is one commit at the current document revision, so one undo removes it.
- Steps only describe layers with positions, sizes, text and slot IDs. No script, shader, class name or resource path is read from recipe data.
- `FilterRecipes` with an empty query matches every recipe that has the requested number of image slots.

## Tests

`TemplateTest` covers: a missing slot fails without planning a partial change, an unknown recipe version is rejected, and filtering by category text.

```bash
./gradlew :core-creative-templates-engine:testDebugUnitTest
```

## See also

- [core-creative-templates-compose](../creative-templates-compose/README.md) for the `TemplateGallery` picker.
- [core-creative-layout-engine](../creative-layout-engine/README.md) for slot assignment and crops.
- [core-creative-history-engine](../creative-history-engine/README.md) for `HistorySession` and undo.
- Guide: [Templates and layout](../../docs/creative-libraries.md#templates-and-layout).
- Sample: the "Templates" tab in [`MainActivity.kt`](../../sample/src/main/kotlin/noel/libs/sample/MainActivity.kt).
