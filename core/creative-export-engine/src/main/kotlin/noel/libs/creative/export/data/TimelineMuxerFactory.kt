@file:androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)

package noel.libs.creative.export.data

import androidx.media3.common.C
import androidx.media3.common.Format
import androidx.media3.muxer.BufferInfo
import androidx.media3.muxer.Muxer
import androidx.media3.transformer.InAppMp4Muxer
import java.nio.ByteBuffer

/** Drop encoder padding beyond the composition and explicitly end every track on its clock. */
internal class TimelineMuxerFactory(
    private val durationUs: Long,
    private val backend: Muxer.Factory = InAppMp4Muxer.Factory(),
) : Muxer.Factory by backend {
    override fun create(path: String): Muxer {
        val output = backend.create(path)
        return object : Muxer by output {
            private val tracks = mutableListOf<Int>()

            override fun addTrack(format: Format): Int =
                output.addTrack(format).also { tracks += it }

            override fun writeSampleData(trackId: Int, buffer: ByteBuffer, info: BufferInfo) {
                if (
                    info.presentationTimeUs < durationUs &&
                        info.flags and C.BUFFER_FLAG_END_OF_STREAM == 0
                )
                    output.writeSampleData(trackId, buffer, info)
            }

            override fun close() {
                try {
                    tracks.forEach {
                        output.writeSampleData(
                            it,
                            ByteBuffer.allocateDirect(0),
                            BufferInfo(durationUs, 0, C.BUFFER_FLAG_END_OF_STREAM),
                        )
                    }
                } finally {
                    output.close()
                }
            }
        }
    }
}
