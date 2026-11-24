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

    @Test
    fun drawingStrokeAndTextStyleCountsMatchTheDraftFormat() {
        fun doc(content: Content) =
            CreativeDocument("d", pages = listOf(Page("p", 100, 100, layers = listOf(Layer("l", content = content)))))
        val stroke = Stroke(listOf(Point(1.0, 1.0)))
        doc(Content.Drawing(List(10_000) { stroke })).validate()
        try {
            doc(Content.Drawing(List(10_001) { stroke })).validate()
            fail("Drawing with more strokes than a draft can store was accepted")
        } catch (_: IllegalArgumentException) {}
        try {
            doc(Content.Text("A", styles = List(100_001) { TextStyle(0, 1) })).validate()
            fail("Text with more styles than a draft can store was accepted")
        } catch (_: IllegalArgumentException) {}
    }
}
