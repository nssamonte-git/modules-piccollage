package noel.libs.creative.history

import noel.libs.creative.history.domain.*
import noel.libs.creative.model.*
import org.junit.Assert.*
import org.junit.Test

class HistoryTest {
    private fun document(): CreativeDocument {
        val layer = Layer("l", content = Content.Text("hello"))
        return CreativeDocument("d", pages = listOf(Page("p", 100, 100, layers = listOf(layer))))
    }

    @Test
    fun undoRedoAndBranch() {
        val s = HistorySession(document())
        assertTrue(
            s.commit(EditRequest(0, listOf(Edit.TransformLayer("p", "l", Transform(x = 12.0)))))
                is Outcome.Success
        )
        assertEquals(12.0, s.document.value.pages[0].layers[0].transform.x, 0.0)
        s.undo()
        assertEquals(0.0, s.document.value.pages[0].layers[0].transform.x, 0.0)
        s.redo()
        assertEquals(12.0, s.document.value.pages[0].layers[0].transform.x, 0.0)
        s.undo()
        s.commit(
            EditRequest(
                s.document.value.revision,
                listOf(Edit.TransformLayer("p", "l", Transform(x = 7.0))),
            )
        )
        assertFalse(s.history.value.canRedo)
    }

    @Test
    fun groupFailureAndStaleEditPublishNothing() {
        val s = HistorySession(document())
        val failed =
            s.commit(
                EditRequest(
                    0,
                    listOf(
                        Edit.TransformLayer("p", "l", Transform(x = 8.0)),
                        Edit.RemoveLayer("p", "missing"),
                    ),
                )
            )
        assertTrue(failed is Outcome.Failure)
        assertEquals(0L, s.document.value.revision)
        assertTrue(s.commit(EditRequest(9, emptyList())) is Outcome.Failure)
    }

    @Test
    fun mergedGestureAndCancel() {
        val s = HistorySession(document())
        s.commit(EditRequest(0, listOf(Edit.TransformLayer("p", "l", Transform(x = 3.0))), "drag"))
        s.commit(EditRequest(1, listOf(Edit.TransformLayer("p", "l", Transform(x = 9.0))), "drag"))
        s.undo()
        assertEquals(0.0, s.document.value.pages[0].layers[0].transform.x, 0.0)
    }
}
