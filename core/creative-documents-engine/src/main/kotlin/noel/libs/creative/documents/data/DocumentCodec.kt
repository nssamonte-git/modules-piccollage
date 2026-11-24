package noel.libs.creative.documents.data

import java.io.*
import noel.libs.creative.model.*

/** Bounded binary v1; no object deserialization, reflection, or executable payloads. */
class DocumentCodec {
    class FutureVersion(val version: Int) : IOException("Unsupported document version $version")

    private fun DataInputStream.count(max: Int): Int = readInt().also { require(it in 0..max) }

    private fun DataOutputStream.string(s: String) {
        val b = s.toByteArray(Charsets.UTF_8)
        require(b.size <= 1_000_000)
        writeInt(b.size)
        write(b)
    }

    private fun DataInputStream.string(): String {
        val b = ByteArray(count(1_000_000))
        readFully(b)
        return b.toString(Charsets.UTF_8)
    }

    private fun DataOutputStream.points(p: List<Point>) {
        writeInt(p.size)
        p.forEach {
            writeDouble(it.x)
            writeDouble(it.y)
        }
    }

    private fun DataInputStream.points() =
        List(count(100_000)) {
            Point(readDouble(), readDouble()).also { require(it.x.isFinite() && it.y.isFinite()) }
        }

    fun encode(d: CreativeDocument): ByteArray {
        d.validate()
        val bytes = ByteArrayOutputStream()
        DataOutputStream(bytes).use { o ->
            o.writeInt(0x4e43444f)
            o.writeInt(1)
            o.string(d.id)
            o.writeLong(d.revision)
            o.string(d.title)
            o.writeInt(d.assets.size)
            d.assets.forEach {
                o.string(it.id)
                o.string(it.mediaType)
                o.writeInt(it.width)
                o.writeInt(it.height)
                o.string(it.contentRevision)
                o.writeInt(it.orientation)
            }
            o.writeInt(d.pages.size)
            d.pages.forEach { p ->
                o.string(p.id)
                o.writeInt(p.width)
                o.writeInt(p.height)
                o.writeInt(p.background)
                o.writeInt(p.layers.size)
                p.layers.forEach { l ->
                    o.string(l.id)
                    with(l.transform) {
                        o.writeDouble(x)
                        o.writeDouble(y)
                        o.writeDouble(scaleX)
                        o.writeDouble(scaleY)
                        o.writeDouble(rotation)
                    }
                    o.writeDouble(l.width)
                    o.writeDouble(l.height)
                    o.writeBoolean(l.visible)
                    o.writeBoolean(l.locked)
                    o.writeFloat(l.opacity)
                    o.writeInt(l.effects.size)
                    l.effects.forEach {
                        o.string(it.kind.name)
                        o.writeFloat(it.amount)
                    }
                    o.writeBoolean(l.mask != null)
                    l.mask?.let { m ->
                        o.writeBoolean(m.inverted)
                        o.string(m.baseAssetId ?: "")
                        o.writeInt(m.strokes.size)
                        m.strokes.forEach {
                            o.points(it.points)
                            o.writeDouble(it.width)
                            o.writeBoolean(it.restore)
                        }
                    }
                    when (val c = l.content) {
                        is Content.Image -> {
                            o.writeByte(0)
                            o.string(c.assetId)
                            o.writeDouble(c.crop.left)
                            o.writeDouble(c.crop.top)
                            o.writeDouble(c.crop.right)
                            o.writeDouble(c.crop.bottom)
                        }
                        is Content.Text -> {
                            o.writeByte(1)
                            o.string(c.text)
                            with(c.format) {
                                o.string(fontId)
                                o.writeFloat(size)
                                o.writeInt(color)
                                o.string(alignment)
                                o.writeFloat(lineSpacing)
                                o.writeFloat(outlineWidth)
                                o.writeInt(outlineColor)
                                o.writeFloat(shadowRadius)
                                o.writeInt(background)
                            }
                            o.writeInt(c.styles.size)
                            c.styles.forEach {
                                o.writeInt(it.start)
                                o.writeInt(it.end)
                                o.writeInt(it.color)
                                o.writeFloat(it.size)
                                o.writeBoolean(it.bold)
                            }
                        }
                        is Content.Drawing -> {
                            o.writeByte(2)
                            o.writeInt(c.strokes.size)
                            c.strokes.forEach {
                                o.points(it.points)
                                o.writeDouble(it.width)
                                o.writeInt(it.color)
                                o.writeInt(it.pressures.size)
                                it.pressures.forEach(o::writeFloat)
                            }
                        }
                    }
                }
            }
        }
        return bytes.toByteArray().also { require(it.size <= 16 * 1024 * 1024) }
    }

    fun decode(bytes: ByteArray): CreativeDocument {
        require(bytes.size <= 16 * 1024 * 1024)
        return DataInputStream(ByteArrayInputStream(bytes)).use { i ->
            require(i.readInt() == 0x4e43444f)
            val version = i.readInt()
            if (version > 1) throw FutureVersion(version)
            require(version in 0..1)
            val id = i.string()
            val revision = i.readLong()
            val title = if (version == 0) "Untitled" else i.string()
            val assets =
                List(i.count(4096)) {
                    Asset(i.string(), i.string(), i.readInt(), i.readInt(), i.string(), i.readInt())
                }
            val pages =
                List(i.count(256)) {
                    val pid = i.string()
                    val w = i.readInt()
                    val h = i.readInt()
                    val bg = i.readInt()
                    val layers =
                        List(i.count(1024)) {
                            val lid = i.string()
                            val t =
                                Transform(
                                    i.readDouble(),
                                    i.readDouble(),
                                    i.readDouble(),
                                    i.readDouble(),
                                    i.readDouble(),
                                )
                            val lw = i.readDouble()
                            val lh = i.readDouble()
                            val visible = i.readBoolean()
                            val locked = i.readBoolean()
                            val opacity = i.readFloat()
                            val effects =
                                List(i.count(32)) {
                                    Effect(Effect.Kind.valueOf(i.string()), i.readFloat())
                                }
                            val mask =
                                if (i.readBoolean()) {
                                    val inv = i.readBoolean()
                                    val base = i.string().ifEmpty { null }
                                    Mask(
                                        List(i.count(10_000)) {
                                            MaskStroke(i.points(), i.readDouble(), i.readBoolean())
                                        },
                                        inv,
                                        base,
                                    )
                                } else null
                            val content =
                                when (i.readByte().toInt()) {
                                    0 ->
                                        Content.Image(
                                            i.string(),
                                            if (version == 0) Crop()
                                            else
                                                Crop(
                                                    i.readDouble(),
                                                    i.readDouble(),
                                                    i.readDouble(),
                                                    i.readDouble(),
                                                ),
                                        )
                                    1 -> {
                                        val text = i.string()
                                        val f =
                                            TextFormat(
                                                i.string(),
                                                i.readFloat(),
                                                i.readInt(),
                                                i.string(),
                                                i.readFloat(),
                                                i.readFloat(),
                                                i.readInt(),
                                                i.readFloat(),
                                                i.readInt(),
                                            )
                                        Content.Text(
                                            text,
                                            f,
                                            List(i.count(100_000)) {
                                                TextStyle(
                                                    i.readInt(),
                                                    i.readInt(),
                                                    i.readInt(),
                                                    i.readFloat(),
                                                    i.readBoolean(),
                                                )
                                            },
                                        )
                                    }
                                    2 ->
                                        Content.Drawing(
                                            List(i.count(10_000)) {
                                                Stroke(
                                                    i.points(),
                                                    i.readDouble(),
                                                    i.readInt(),
                                                    List(i.count(100_000)) { i.readFloat() },
                                                )
                                            }
                                        )
                                    else -> throw IOException("Unknown layer kind")
                                }
                            Layer(lid, t, lw, lh, content, visible, locked, opacity, effects, mask)
                        }
                    Page(pid, w, h, bg, layers)
                }
            require(i.available() == 0)
            CreativeDocument(id, revision, pages, assets, title = title).also { it.validate() }
        }
    }
}
