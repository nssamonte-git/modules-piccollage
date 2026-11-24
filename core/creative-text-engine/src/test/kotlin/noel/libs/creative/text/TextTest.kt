package noel.libs.creative.text

import noel.libs.creative.model.*
import noel.libs.creative.text.domain.*
import org.junit.Assert.*
import org.junit.Test

class TextTest {
    @Test
    fun familyEmojiIsOneGrapheme() {
        val text = "A👨‍👩‍👧‍👦e\u0301"
        assertEquals(3, Graphemes.boundaries(text).size - 1)
        val range = Graphemes.utf16Range(text, 1, 2)
        assertEquals("👨‍👩‍👧‍👦", text.substring(range.first, range.second))
    }

    @Test
    fun invalidRangeRejected() {
        assertTrue(
            ValidateText()(Content.Text("a", styles = listOf(TextStyle(0, 2)))) is Outcome.Failure
        )
    }

    @Test
    fun semanticTextPreserved() {
        val c = Content.Text("Hello")
        assertEquals("Hello", (ValidateText()(c) as Outcome.Success).value.text)
    }
}
