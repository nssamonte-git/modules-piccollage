package noel.libs.creative.export.domain
sealed interface Clip {
    val durationUs: Long

    data class Photo(val pageId: String, override val durationUs: Long = 2_000_000) : Clip

    data class Video(
        val assetId: String,
        val trimStartUs: Long,
        val trimEndUs: Long,
        val muted: Boolean = false,
        val overlayPageId: String? = null,
    ) : Clip {
        override val durationUs
            get() = trimEndUs - trimStartUs
    }
}

data class Music(
    val assetId: String,
    val sourceTrimUs: Long = 0,
    val placementUs: Long = 0,
    val loop: Boolean = false,
    val muted: Boolean = false,
) {
    init {
        require(sourceTrimUs >= 0 && placementUs >= 0)
    }

    fun sourceTimeAt(compositionUs: Long): Long? =
        if (muted || compositionUs < placementUs) null
        else sourceTrimUs + compositionUs - placementUs
}

data class LayerMotion(
    val pageId: String,
    val layerId: String,
    val startUs: Long,
    val endUs: Long,
    val from: noel.libs.creative.model.Transform,
    val to: noel.libs.creative.model.Transform,
) {
    init {
        require(startUs >= 0 && endUs > startUs && from.valid() && to.valid())
    }

    fun at(timeUs: Long): noel.libs.creative.model.Transform {
        val t = ((timeUs - startUs).toDouble() / (endUs - startUs)).coerceIn(0.0, 1.0)
        fun lerp(a: Double, b: Double) = a + (b - a) * t
        return noel.libs.creative.model.Transform(
            lerp(from.x, to.x),
            lerp(from.y, to.y),
            lerp(from.scaleX, to.scaleX),
            lerp(from.scaleY, to.scaleY),
            lerp(from.rotation, to.rotation),
        )
    }
}

data class Timeline(
    val clips: List<Clip>,
    val overlapUs: Long = 0,
    val music: Music? = null,
    val motions: List<LayerMotion> = emptyList(),
) {
    init {
        require(
            clips.isNotEmpty() &&
                clips.size <= 128 &&
                overlapUs >= 0 &&
                clips.all { it.durationUs > overlapUs && it.durationUs <= 300_000_000 }
        )
        require(overlapUs == 0L || clips.all { it is Clip.Photo })
        require(clips.filterIsInstance<Clip.Video>().all { it.trimStartUs >= 0 })
        require(motions.isEmpty() || clips.all { it is Clip.Photo })
        require(durationUs <= 300_000_000)
    }

    val startsUs: List<Long>
        get() {
            var start = 0L
            return clips.mapIndexed { i, c ->
                val value = start
                start += c.durationUs
                if (i < clips.lastIndex) start -= overlapUs
                value
            }
        }

    val durationUs: Long
        get() = clips.sumOf { it.durationUs } - overlapUs * (clips.size - 1)
}

data class VideoSettings(
    val width: Int = 1920,
    val height: Int = 1080,
    val frameRate: Int = 30,
    val background: Int = 0xff000000.toInt(),
) {
    init {
        require(
            width > 0 &&
                height > 0 &&
                width % 2 == 0 &&
                height % 2 == 0 &&
                width.toLong() * height <= 16_000_000 &&
                frameRate in 1..60
        )
    }
}

sealed interface ExportState {
    data object Idle : ExportState

    data class Running(val progress: Int) : ExportState

    data class Completed(val path: String, val mimeType: String) : ExportState

    data class Failed(val message: String) : ExportState

    data object Canceled : ExportState
}
