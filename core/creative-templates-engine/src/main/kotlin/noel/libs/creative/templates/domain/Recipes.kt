package noel.libs.creative.templates.domain

import noel.libs.creative.model.*

sealed interface RecipeStep {
    data class Image(
        val slotId: String,
        val pageId: String,
        val layerId: String,
        val x: Double,
        val y: Double,
        val width: Double,
        val height: Double,
    ) : RecipeStep

    data class Text(
        val pageId: String,
        val layerId: String,
        val text: String,
        val x: Double,
        val y: Double,
        val width: Double = 200.0,
        val height: Double = 60.0,
    ) : RecipeStep
}

data class Recipe(
    val id: String,
    val version: Int = 1,
    val category: String = "",
    val premiumCapability: String? = null,
    val steps: List<RecipeStep>,
)

class PlanRecipe {
    operator fun invoke(
        recipe: Recipe,
        slots: Map<String, String>,
        document: CreativeDocument,
    ): Outcome<EditRequest> {
        if (recipe.version != 1)
            return failure(CreativeError.Code.UNSUPPORTED, "Unknown recipe version")
        return try {
            require(recipe.steps.size <= 256)
            val edits =
                recipe.steps.map { step ->
                    when (step) {
                        is RecipeStep.Image -> {
                            val id =
                                slots[step.slotId]
                                    ?: return failure(
                                        CreativeError.Code.MISSING_ASSET,
                                        "Select media for ${step.slotId}",
                                    )
                            require(
                                document.assets.any {
                                    it.id == id && it.mediaType.startsWith("image/")
                                }
                            )
                            Edit.AddLayer(
                                step.pageId,
                                Layer(
                                    step.layerId,
                                    Transform(step.x, step.y),
                                    step.width,
                                    step.height,
                                    Content.Image(id),
                                ),
                            )
                        }
                        is RecipeStep.Text ->
                            Edit.AddLayer(
                                step.pageId,
                                Layer(
                                    step.layerId,
                                    Transform(step.x, step.y),
                                    step.width,
                                    step.height,
                                    Content.Text(step.text),
                                ),
                            )
                    }
                }
            applyEdits(document, edits)
            Outcome.Success(EditRequest(document.revision, edits))
        } catch (e: IllegalArgumentException) {
            failure(CreativeError.Code.INVALID_INPUT, "Recipe is incompatible with this document")
        }
    }
}

class ApplyRecipe(private val entitlements: Entitlements?) {
    suspend operator fun invoke(
        accountId: String,
        session: EditSession,
        recipe: Recipe,
        slots: Map<String, String>,
    ): Outcome<CreativeDocument> {
        if (
            recipe.premiumCapability != null &&
                entitlements?.allowed(accountId, recipe.premiumCapability) != true
        )
            return failure(CreativeError.Code.UNAVAILABLE, "Content access required")
        return when (val plan = PlanRecipe()(recipe, slots, session.document.value)) {
            is Outcome.Success -> session.commit(plan.value)
            is Outcome.Failure -> plan
        }
    }
}

class FilterRecipes {
    operator fun invoke(recipes: List<Recipe>, query: String, photoCount: Int): List<Recipe> =
        recipes.filter { r ->
            (r.id.contains(query, true) || r.category.contains(query, true)) &&
                r.steps.filterIsInstance<RecipeStep.Image>().map { it.slotId }.distinct().size ==
                    photoCount
        }
}

interface TemplateCatalog {
    suspend fun search(query: String, photoCount: Int): Outcome<List<Recipe>>
}
