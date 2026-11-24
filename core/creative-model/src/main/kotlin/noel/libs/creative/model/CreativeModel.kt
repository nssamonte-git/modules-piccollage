package noel.libs.creative.model

import kotlin.math.*
import kotlinx.coroutines.flow.StateFlow

sealed interface Outcome<out T> {
    data class Success<T>(val value: T) : Outcome<T>

    data class Failure(val error: CreativeError) : Outcome<Nothing>
}

data class CreativeError(val code: Code, val message: String) {
    enum class Code {
        INVALID_INPUT,
        STALE_REVISION,
        MISSING_ASSET,
        UNAVAILABLE,
        IO,
        CANCELED,
        CONFLICT,
        UNSUPPORTED,
        LIMIT,
        REMOTE,
    }
}

fun failure(code: CreativeError.Code, message: String) =
    Outcome.Failure(CreativeError(code, message))

data class Point(val x: Double, val y: Double)

data class Transform(
    val x: Double = 0.0,
    val y: Double = 0.0,
    val scaleX: Double = 1.0,
    val scaleY: Double = 1.0,
    val rotation: Double = 0.0,
) {
    fun valid() =
        listOf(x, y, scaleX, scaleY, rotation).all { it.isFinite() } &&
            abs(scaleX) > 1e-6 &&
            abs(scaleY) > 1e-6

    fun map(p: Point): Point {
        val a = rotation * PI / 180
        return Point(
            x + p.x * scaleX * cos(a) - p.y * scaleY * sin(a),
            y + p.x * scaleX * sin(a) + p.y * scaleY * cos(a),
        )
    }

    fun inverse(p: Point): Point {
        val a = -rotation * PI / 180
        val dx = p.x - x
        val dy = p.y - y
        return Point((dx * cos(a) - dy * sin(a)) / scaleX, (dx * sin(a) + dy * cos(a)) / scaleY)
    }
}

data class TextStyle(
    val start: Int,
    val end: Int,
    val color: Int = 0xff111111.toInt(),
    val size: Float = 32f,
    val bold: Boolean = false,
)

data class TextFormat(
    val fontId: String = "sans-serif",
    val size: Float = 32f,
    val color: Int = 0xff111111.toInt(),
    val alignment: String = "left",
    val lineSpacing: Float = 1f,
    val outlineWidth: Float = 0f,
    val outlineColor: Int = 0xffffffff.toInt(),
    val shadowRadius: Float = 0f,
    val background: Int = 0,
)

data class Stroke(
    val points: List<Point>,
    val width: Double = 4.0,
    val color: Int = 0xff111111.toInt(),
    val pressures: List<Float> = emptyList(),
)

data class Effect(val kind: Kind, val amount: Float = 0f) {
    enum class Kind {
        BRIGHTNESS,
        CONTRAST,
        SATURATION,
        GRAYSCALE,
        BLUR,
    }
}

data class MaskStroke(val points: List<Point>, val width: Double, val restore: Boolean = false)

data class Mask(
    val strokes: List<MaskStroke> = emptyList(),
    val inverted: Boolean = false,
    val baseAssetId: String? = null,
)

data class Crop(
    val left: Double = 0.0,
    val top: Double = 0.0,
    val right: Double = 1.0,
    val bottom: Double = 1.0,
) {
    init {
        require(
            listOf(left, top, right, bottom).all { it.isFinite() } &&
                left >= 0 &&
                top >= 0 &&
                right <= 1 &&
                bottom <= 1 &&
                right > left &&
                bottom > top
        )
    }
}

sealed interface Content {
    data class Image(val assetId: String, val crop: Crop = Crop()) : Content

    data class Text(
        val text: String,
        val format: TextFormat = TextFormat(),
        val styles: List<TextStyle> = emptyList(),
    ) : Content

    data class Drawing(val strokes: List<Stroke>) : Content
}

data class Layer(
    val id: String,
    val transform: Transform = Transform(),
    val width: Double = 100.0,
    val height: Double = 100.0,
    val content: Content,
    val visible: Boolean = true,
    val locked: Boolean = false,
    val opacity: Float = 1f,
    val effects: List<Effect> = emptyList(),
    val mask: Mask? = null,
)

data class Page(
    val id: String,
    val width: Int,
    val height: Int,
    val background: Int = 0,
    val layers: List<Layer> = emptyList(),
)

data class Asset(
    val id: String,
    val mediaType: String,
    val width: Int,
    val height: Int,
    val contentRevision: String = "1",
    val orientation: Int = 0,
)

data class CreativeDocument(
    val id: String,
    val revision: Long = 0,
    val pages: List<Page>,
    val assets: List<Asset> = emptyList(),
    val schemaVersion: Int = 1,
    val title: String = "Untitled",
) {
    fun validate() {
        require(id.isNotBlank() && revision >= 0 && schemaVersion == 1)
        require(
            pages.isNotEmpty() &&
                pages.size <= 256 &&
                pages.map { it.id }.distinct().size == pages.size
        )
        require(assets.size <= 4096 && assets.map { it.id }.distinct().size == assets.size)
        val ids = pages.flatMap { it.layers }.map { it.id }
        require(ids.distinct().size == ids.size)
        assets.forEach {
            require(
                it.id.isNotBlank() &&
                    it.width > 0 &&
                    it.height > 0 &&
                    it.width.toLong() * it.height <= 64_000_000
            )
        }
        pages.forEach { p ->
            require(
                p.id.isNotBlank() &&
                    p.width > 0 &&
                    p.height > 0 &&
                    p.width.toLong() * p.height <= 16_000_000 &&
                    p.layers.size <= 1024
            )
            p.layers.forEach { l ->
                require(
                    l.id.isNotBlank() &&
                        l.transform.valid() &&
                        l.width.isFinite() &&
                        l.height.isFinite() &&
                        l.width > 0 &&
                        l.height > 0 &&
                        l.opacity in 0f..1f
                )
                require(l.effects.size <= 32 && l.effects.all { it.amount.isFinite() })
                l.mask?.let { m ->
                    require(m.strokes.size <= 10_000)
                    m.strokes.forEach { stroke ->
                        require(
                            stroke.width.isFinite() &&
                                stroke.width > 0 &&
                                stroke.width <= 8192 &&
                                stroke.points.size <= 100_000 &&
                                stroke.points.all { it.x.isFinite() && it.y.isFinite() }
                        )
                    }
                }
                // Same bounds as the draft file format, so every valid document can be reopened.
                if (l.content is Content.Drawing) require(l.content.strokes.size <= 10_000)
                if (l.content is Content.Drawing)
                    l.content.strokes.forEach { stroke ->
                        require(
                            stroke.width.isFinite() &&
                                stroke.width > 0 &&
                                stroke.points.size <= 100_000 &&
                                stroke.points.all { it.x.isFinite() && it.y.isFinite() } &&
                                (stroke.pressures.isEmpty() ||
                                    stroke.pressures.size == stroke.points.size) &&
                                stroke.pressures.all { it in 0f..1f }
                        )
                    }
                if (l.content is Content.Image) require(assets.any { it.id == l.content.assetId })
                if (l.content is Content.Text)
                    require(
                        l.content.text.length <= 100_000 &&
                            l.content.styles.size <= 100_000 &&
                            l.content.format.size.isFinite() &&
                            l.content.format.size in 1f..4096f
                    )
            }
        }
    }

    fun immutableCopy() =
        copy(
            pages =
                pages.map { p ->
                    p.copy(
                        layers =
                            p.layers.map { l ->
                                l.copy(
                                    effects = l.effects.toList(),
                                    mask =
                                        l.mask?.copy(
                                            strokes =
                                                l.mask.strokes.map {
                                                    it.copy(points = it.points.toList())
                                                }
                                        ),
                                    content =
                                        when (val c = l.content) {
                                            is Content.Text -> c.copy(styles = c.styles.toList())
                                            is Content.Drawing ->
                                                c.copy(
                                                    strokes =
                                                        c.strokes.map {
                                                            it.copy(
                                                                points = it.points.toList(),
                                                                pressures = it.pressures.toList(),
                                                            )
                                                        }
                                                )
                                            is Content.Image -> c
                                        },
                                )
                            }
                    )
                },
            assets = assets.toList(),
        )
}

sealed interface Edit {
    data class AddLayer(val pageId: String, val layer: Layer) : Edit

    data class ReplaceLayer(val pageId: String, val layer: Layer) : Edit

    data class TransformLayer(val pageId: String, val layerId: String, val transform: Transform) :
        Edit

    data class RemoveLayer(val pageId: String, val layerId: String) : Edit

    data class ReorderLayer(val pageId: String, val layerId: String, val index: Int) : Edit

    data class AddPage(val page: Page) : Edit

    data class RemovePage(val pageId: String) : Edit

    data class AddAsset(val asset: Asset) : Edit
}

data class EditRequest(
    val expectedRevision: Long,
    val edits: List<Edit>,
    val mergeKey: String? = null,
)

data class HistoryState(val canUndo: Boolean = false, val canRedo: Boolean = false)

interface EditSession {
    val document: StateFlow<CreativeDocument>
    val history: StateFlow<HistoryState>

    fun commit(request: EditRequest): Outcome<CreativeDocument>

    fun undo(): Outcome<CreativeDocument>

    fun redo(): Outcome<CreativeDocument>
}

fun applyEdits(document: CreativeDocument, edits: List<Edit>): CreativeDocument {
    require(edits.size <= 256)
    var d = document
    for (e in edits) d =
        when (e) {
            is Edit.AddAsset -> {
                require(d.assets.none { it.id == e.asset.id })
                d.copy(assets = d.assets + e.asset)
            }
            is Edit.AddPage -> d.copy(pages = d.pages + e.page)
            is Edit.RemovePage -> {
                require(d.pages.any { it.id == e.pageId })
                d.copy(pages = d.pages.filterNot { it.id == e.pageId })
            }
            else -> {
                val pageId =
                    when (e) {
                        is Edit.AddLayer -> e.pageId
                        is Edit.ReplaceLayer -> e.pageId
                        is Edit.TransformLayer -> e.pageId
                        is Edit.RemoveLayer -> e.pageId
                        is Edit.ReorderLayer -> e.pageId
                        else -> error("unreachable")
                    }
                require(d.pages.any { it.id == pageId })
                d.copy(
                    pages =
                        d.pages.map { p ->
                            if (p.id != pageId) p
                            else
                                p.copy(
                                    layers =
                                        when (e) {
                                            is Edit.AddLayer -> p.layers + e.layer
                                            is Edit.ReplaceLayer -> {
                                                require(
                                                    p.layers.any {
                                                        it.id == e.layer.id && !it.locked
                                                    }
                                                )
                                                p.layers.map {
                                                    if (it.id == e.layer.id) e.layer else it
                                                }
                                            }
                                            is Edit.TransformLayer -> {
                                                require(
                                                    p.layers.any {
                                                        it.id == e.layerId && !it.locked
                                                    }
                                                )
                                                p.layers.map {
                                                    if (it.id == e.layerId)
                                                        it.copy(transform = e.transform)
                                                    else it
                                                }
                                            }
                                            is Edit.RemoveLayer -> {
                                                require(
                                                    p.layers.any {
                                                        it.id == e.layerId && !it.locked
                                                    }
                                                )
                                                p.layers.filterNot { it.id == e.layerId }
                                            }
                                            is Edit.ReorderLayer -> {
                                                val target =
                                                    p.layers.first {
                                                        it.id == e.layerId && !it.locked
                                                    }
                                                require(e.index in p.layers.indices)
                                                p.layers
                                                    .filterNot { it.id == e.layerId }
                                                    .toMutableList()
                                                    .apply { add(e.index, target) }
                                            }
                                            else -> error("unreachable")
                                        }
                                )
                        }
                )
            }
        }
    d.validate()
    return d.immutableCopy()
}

interface DocumentRepository {
    suspend fun save(accountId: String, document: CreativeDocument): Outcome<Unit>

    suspend fun load(accountId: String, documentId: String): Outcome<CreativeDocument>
}

interface AssetBytes {
    fun read(assetId: String): Outcome<ByteArray>
}

interface Entitlements {
    suspend fun allowed(accountId: String, capability: String): Boolean
}

interface BackupPort {
    suspend fun upload(accountId: String, document: CreativeDocument): Outcome<Unit>

    suspend fun deleteRemote(accountId: String, documentId: String): Outcome<Unit>
}

interface ReminderPort {
    suspend fun schedule(accountId: String, documentId: String): Outcome<Unit>

    suspend fun cancel(accountId: String, documentId: String)
}

data class Usage(val inputUnits: Long = 0, val outputUnits: Long = 0, val cachedUnits: Long = 0)

interface Telemetry {
    fun record(operationId: String, state: String, usage: Usage = Usage())
}
