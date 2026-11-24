package noel.libs.creative.templates

import noel.libs.creative.model.*
import noel.libs.creative.templates.domain.*
import org.junit.Assert.*
import org.junit.Test

class TemplateTest {
    @Test
    fun missingSlotDoesNotPlanPartialMutation() {
        val recipe =
            Recipe("r", steps = listOf(RecipeStep.Image("cover", "p", "l", 0.0, 0.0, 50.0, 50.0)))
        assertTrue(
            PlanRecipe()(
                recipe,
                mapOf(),
                CreativeDocument("d", pages = listOf(Page("p", 100, 100))),
            )
                is Outcome.Failure
        )
    }

    @Test
    fun unknownRecipeVersionRejected() {
        assertTrue(
            PlanRecipe()(
                Recipe("r", version = 99, steps = emptyList()),
                emptyMap(),
                CreativeDocument("d", pages = listOf(Page("p", 100, 100))),
            )
                is Outcome.Failure
        )
    }

    @Test
    fun filtering() {
        val r = Recipe("r", category = "birthday", steps = emptyList())
        assertEquals(1, FilterRecipes()(listOf(r), "birth", 0).size)
    }
}
