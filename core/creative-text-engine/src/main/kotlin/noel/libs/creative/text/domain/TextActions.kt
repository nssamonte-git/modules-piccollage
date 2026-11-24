package noel.libs.creative.text.domain

import java.util.regex.Pattern
import noel.libs.creative.model.*

/** Java's extended-grapheme matcher includes ZWJ emoji and combining marks. */
object Graphemes {
    private val grapheme = Pattern.compile("\\X")

    fun boundaries(text: String): List<Int> {
        val result = mutableListOf(0)
        val matcher = grapheme.matcher(text)
        while (matcher.find()) result += matcher.end()
        return result
    }

    fun utf16Range(text: String, start: Int, end: Int): Pair<Int, Int> {
        val b = boundaries(text)
        require(start >= 0 && end >= start && end < b.size)
        return b[start] to b[end]
    }
}

class ValidateText {
    operator fun invoke(text: Content.Text): Outcome<Content.Text> =
        try {
            require(
                text.text.length <= 100_000 &&
                    text.format.size in 1f..4096f &&
                    text.format.lineSpacing in 0.5f..4f &&
                    text.format.outlineWidth in 0f..64f &&
                    text.format.shadowRadius in 0f..64f &&
                    text.format.alignment in listOf("left", "center", "right")
            )
            val count = Graphemes.boundaries(text.text).size - 1
            require(text.styles.size <= 4096)
            text.styles.forEach {
                require(
                    it.start >= 0 && it.end >= it.start && it.end <= count && it.size in 1f..4096f
                )
            }
            Outcome.Success(text)
        } catch (_: IllegalArgumentException) {
            failure(CreativeError.Code.INVALID_INPUT, "Invalid text or style range")
        }
}

class SetText {
    operator fun invoke(
        session: EditSession,
        pageId: String,
        layerId: String,
        text: Content.Text,
    ): Outcome<CreativeDocument> {
        val checked = ValidateText()(text)
        if (checked is Outcome.Failure) return checked
        val d = session.document.value
        val l =
            d.pages
                .firstOrNull { it.id == pageId }
                ?.layers
                ?.firstOrNull { it.id == layerId && !it.locked }
                ?: return failure(CreativeError.Code.INVALID_INPUT, "Missing editable layer")
        return session.commit(
            EditRequest(d.revision, listOf(Edit.ReplaceLayer(pageId, l.copy(content = text))))
        )
    }
}
