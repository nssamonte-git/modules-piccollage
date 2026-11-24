# core-creative-templates-compose

A Compose template picker for [core-creative-templates-engine](../creative-templates-engine/README.md). `TemplateGallery` shows a search field and one button per matching `Recipe`, and calls back when the user picks one. It does not apply the recipe itself. This is a Compose UI module.

## When to use it

- You want a simple searchable list of recipes without writing the UI.
- Use the engine module alone if you build your own picker; `FilterRecipes` gives you the same filtering.

## Add it

```kotlin
implementation("noel.libs:core-creative-templates-compose:0.2.1")
```

It brings in `core-creative-templates-engine`, `core-creative-model`, `kotlinx-coroutines-core`, and the Compose BOM, `compose-ui` and `compose-foundation-layout` through `api`. Material 3 and `core-creative-compose-common` are internal (`implementation`).

## Quick start

Based on the sample app, inside a composable:

```kotlin
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import noel.libs.creative.model.Outcome
import noel.libs.creative.templates.domain.ApplyRecipe
import noel.libs.creative.templates.presentation.TemplateGallery

val scope = rememberCoroutineScope()

TemplateGallery(
    recipes = recipes,
    photoCount = 0,
    onApply = { recipe ->
        scope.launch {
            val result = ApplyRecipe(null)("demo", session, recipe, emptyMap())
            status = if (result is Outcome.Failure) result.error.message else "Done"
        }
    },
)
```

## Main types

| Type | What it does |
|---|---|
| `TemplateGallery(recipes, photoCount, onApply, modifier)` | Panel titled "Templates" with a "Find a template" text field. Lists recipes that pass `FilterRecipes` for the typed query and `photoCount`; each is a button labelled with the recipe `id`. Shows "No matching templates" when the list is empty. |

Package: `noel.libs.creative.templates.presentation`.

## What your app provides

- The recipe list (for example from your own `TemplateCatalog`).
- `photoCount`: the number of distinct image slots a recipe must have to be shown. Pass the number of photos the user selected, or `0` for text-only recipes.
- `onApply`: build the slot map and call `ApplyRecipe` in a coroutine. Show its `Outcome` to the user.
- A `MaterialTheme` around it; text uses Material 3 styles.

## Limits and behaviour

- The search text is held in `remember` state inside the composable, so it is not saved across configuration changes or process death.
- Buttons show the recipe `id`, not a preview image.
- All recipes are filtered on each recomposition; there is no paging.

## Tests

This module has no unit tests. The filtering it uses is tested in `core-creative-templates-engine`.

## See also

- [core-creative-templates-engine](../creative-templates-engine/README.md) for `Recipe`, `ApplyRecipe` and the failure rules.
- [core-creative-layout-compose](../creative-layout-compose/README.md) for the layout suggestion panel.
- Guide: [Templates and layout](../../docs/creative-libraries.md#templates-and-layout).
- Sample: the "Templates" tab in [`MainActivity.kt`](../../sample/src/main/kotlin/noel/libs/sample/MainActivity.kt).
