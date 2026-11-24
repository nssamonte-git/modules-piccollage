package noel.libs.creative.model

import org.junit.Assert.*
import org.junit.Test

class ValidationTest {
    @Test
    fun lockedLayerCannotBeReplaced() {
        val layer = Layer("l", content = Content.Text("A"), locked = true)
        val d = CreativeDocument("d", pages = listOf(Page("p", 100, 100, layers = listOf(layer))))
        try {
            applyEdits(d, listOf(Edit.ReplaceLayer("p", layer.copy(content = Content.Text("B")))))
            fail("Locked layer replaced")
        } catch (_: IllegalArgumentException) {}
    }

    @Test
    fun invalidMaskRejected() {
        val l =
            Layer(
                "l",
                content = Content.Text("A"),
                mask = Mask(listOf(MaskStroke(listOf(Point(Double.NaN, 1.0)), -2.0))),
            )
        try {
            CreativeDocument("d", pages = listOf(Page("p", 100, 100, layers = listOf(l))))
                .validate()
            fail()
        } catch (_: IllegalArgumentException) {}
    }
}
