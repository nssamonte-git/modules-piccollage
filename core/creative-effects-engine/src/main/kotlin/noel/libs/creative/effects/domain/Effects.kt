package noel.libs.creative.effects.domain

import kotlin.math.*
import noel.libs.creative.model.*

data class EffectNode(val id: String, val inputs: List<String>, val effect: Effect)

class ValidateEffectGraph {
    operator fun invoke(nodes: List<EffectNode>): Outcome<List<Effect>> =
        try {
            require(nodes.size <= 32 && nodes.map { it.id }.distinct().size == nodes.size)
            require(nodes.all { it.inputs.size <= 1 })
            require(nodes.isEmpty() || nodes.count { it.inputs.isEmpty() } == 1)
            val referenced = nodes.flatMap { it.inputs }
            require(referenced.distinct().size == referenced.size)
            val map = nodes.associateBy { it.id }
            val active = mutableSetOf<String>()
            val done = mutableSetOf<String>()
            val ordered = mutableListOf<Effect>()
            fun visit(id: String) {
                require(id !in active)
                if (id in done) return
                val node = map[id] ?: throw IllegalArgumentException("Missing input")
                active += id
                node.inputs.forEach(::visit)
                active -= id
                done += id
                validateEffect(node.effect)
                ordered += node.effect
            }
            nodes.forEach { visit(it.id) }
            Outcome.Success(ordered)
        } catch (e: IllegalArgumentException) {
            failure(CreativeError.Code.INVALID_INPUT, "Invalid effect graph")
        }
}

fun validateEffect(e: Effect) {
    require(e.amount.isFinite())
    require(
        when (e.kind) {
            Effect.Kind.BRIGHTNESS -> e.amount in -1f..1f
            Effect.Kind.CONTRAST,
            Effect.Kind.SATURATION -> e.amount in 0f..2f
            Effect.Kind.GRAYSCALE -> e.amount in 0f..1f
            Effect.Kind.BLUR -> e.amount in 0f..8f
        }
    )
}

/** Straight-alpha ARGB reference pipeline. Source pixels are never changed. */
object PixelEffects {
    fun apply(source: IntArray, width: Int, height: Int, effects: List<Effect>): IntArray {
        require(
            width > 0 &&
                height > 0 &&
                width.toLong() * height <= 16_000_000 &&
                source.size == width * height &&
                effects.size <= 32
        )
        var pixels = source.copyOf()
        for (e in effects) {
            validateEffect(e)
            if (e.kind == Effect.Kind.BLUR) {
                val radius = e.amount.roundToInt()
                if (radius == 0) continue
                val input = pixels
                pixels =
                    IntArray(input.size) { index ->
                        val x = index % width
                        val y = index / width
                        var a = 0.0
                        var red = 0.0
                        var green = 0.0
                        var blue = 0.0
                        var n = 0
                        for (dy in -radius..radius) for (dx in -radius..radius) {
                            val p =
                                input[
                                    (y + dy).coerceIn(0, height - 1) * width +
                                        (x + dx).coerceIn(0, width - 1)]
                            val alpha = (p ushr 24) / 255.0
                            a += alpha
                            red += ((p ushr 16) and 255) * alpha
                            green += ((p ushr 8) and 255) * alpha
                            blue += (p and 255) * alpha
                            n++
                        }
                        val aa = (a / n * 255).roundToInt().coerceIn(0, 255)
                        if (a == 0.0) 0
                        else
                            (aa shl 24) or
                                ((red / a).roundToInt().coerceIn(0, 255) shl 16) or
                                ((green / a).roundToInt().coerceIn(0, 255) shl 8) or
                                (blue / a).roundToInt().coerceIn(0, 255)
                    }
                continue
            }
            pixels =
                IntArray(pixels.size) { i ->
                    val p = pixels[i]
                    var red = ((p ushr 16) and 255).toFloat()
                    var green = ((p ushr 8) and 255).toFloat()
                    var blue = (p and 255).toFloat()
                    val luminance = 0.2126f * red + 0.7152f * green + 0.0722f * blue
                    when (e.kind) {
                        Effect.Kind.BRIGHTNESS -> {
                            red += e.amount * 255
                            green += e.amount * 255
                            blue += e.amount * 255
                        }
                        Effect.Kind.CONTRAST -> {
                            red = (red - 127.5f) * e.amount + 127.5f
                            green = (green - 127.5f) * e.amount + 127.5f
                            blue = (blue - 127.5f) * e.amount + 127.5f
                        }
                        Effect.Kind.SATURATION -> {
                            red = luminance + (red - luminance) * e.amount
                            green = luminance + (green - luminance) * e.amount
                            blue = luminance + (blue - luminance) * e.amount
                        }
                        Effect.Kind.GRAYSCALE -> {
                            red += (luminance - red) * e.amount
                            green += (luminance - green) * e.amount
                            blue += (luminance - blue) * e.amount
                        }
                        else -> {}
                    }
                    (p and -0x1000000) or
                        (red.roundToInt().coerceIn(0, 255) shl 16) or
                        (green.roundToInt().coerceIn(0, 255) shl 8) or
                        blue.roundToInt().coerceIn(0, 255)
                }
        }
        return pixels
    }
}

class SetEffects {
    operator fun invoke(
        session: EditSession,
        pageId: String,
        layerId: String,
        effects: List<Effect>,
    ): Outcome<CreativeDocument> {
        val d = session.document.value
        val layer =
            d.pages
                .firstOrNull { it.id == pageId }
                ?.layers
                ?.firstOrNull { it.id == layerId && !it.locked }
                ?: return failure(CreativeError.Code.INVALID_INPUT, "Missing editable layer")
        return try {
            require(effects.size <= 32)
            effects.forEach(::validateEffect)
            session.commit(
                EditRequest(
                    d.revision,
                    listOf(Edit.ReplaceLayer(pageId, layer.copy(effects = effects))),
                )
            )
        } catch (_: IllegalArgumentException) {
            failure(CreativeError.Code.INVALID_INPUT, "Invalid effects")
        }
    }
}
