package noel.libs.creative.export.data

import android.app.Activity
import android.content.*
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.*
import android.print.*
import java.io.FileOutputStream
import noel.libs.creative.model.*

class AndroidDelivery {
    fun share(context: Context, uri: Uri, mimeType: String): Outcome<Unit> =
        try {
            require(uri.scheme == "content")
            val intent =
                Intent(Intent.ACTION_SEND)
                    .setType(mimeType)
                    .putExtra(Intent.EXTRA_STREAM, uri)
                    .apply {
                        clipData = ClipData.newRawUri("Export", uri)
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
            val chooser = Intent.createChooser(intent, "Share export")
            if (context !is Activity) chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(chooser)
            Outcome.Success(Unit)
        } catch (_: ActivityNotFoundException) {
            failure(CreativeError.Code.UNAVAILABLE, "No sharing application available")
        } catch (_: Exception) {
            failure(CreativeError.Code.INVALID_INPUT, "Cannot share this output")
        }

    fun printImage(
        activity: Activity,
        bitmap: Bitmap,
        name: String = "Creative image",
    ): Outcome<Unit> =
        try {
            val manager =
                activity.getSystemService(Context.PRINT_SERVICE) as? PrintManager
                    ?: return failure(CreativeError.Code.UNAVAILABLE, "Printing unavailable")
            manager.print(
                name,
                object : PrintDocumentAdapter() {
                    private var attributes: PrintAttributes? = null

                    override fun onLayout(
                        oldAttributes: PrintAttributes?,
                        newAttributes: PrintAttributes,
                        cancellationSignal: CancellationSignal,
                        callback: LayoutResultCallback,
                        extras: Bundle?,
                    ) {
                        if (cancellationSignal.isCanceled) {
                            callback.onLayoutCancelled()
                            return
                        }
                        attributes = newAttributes
                        callback.onLayoutFinished(
                            PrintDocumentInfo.Builder(name)
                                .setContentType(PrintDocumentInfo.CONTENT_TYPE_PHOTO)
                                .setPageCount(1)
                                .build(),
                            true,
                        )
                    }

                    override fun onWrite(
                        pages: Array<out PageRange>,
                        destination: ParcelFileDescriptor,
                        cancellationSignal: CancellationSignal,
                        callback: WriteResultCallback,
                    ) {
                        if (cancellationSignal.isCanceled) {
                            callback.onWriteCancelled()
                            return
                        }
                        val pdf = PdfDocument()
                        try {
                            val media = attributes?.mediaSize
                            val w = ((media?.widthMils ?: 8500) * 72 / 1000)
                            val h = ((media?.heightMils ?: 11000) * 72 / 1000)
                            val page = pdf.startPage(PdfDocument.PageInfo.Builder(w, h, 1).create())
                            val scale =
                                minOf(w.toFloat() / bitmap.width, h.toFloat() / bitmap.height)
                            page.canvas.drawBitmap(
                                bitmap,
                                null,
                                RectF(
                                    (w - bitmap.width * scale) / 2,
                                    (h - bitmap.height * scale) / 2,
                                    (w + bitmap.width * scale) / 2,
                                    (h + bitmap.height * scale) / 2,
                                ),
                                Paint(Paint.FILTER_BITMAP_FLAG),
                            )
                            pdf.finishPage(page)
                            if (cancellationSignal.isCanceled) {
                                callback.onWriteCancelled()
                                return
                            }
                            FileOutputStream(destination.fileDescriptor).use { pdf.writeTo(it) }
                            callback.onWriteFinished(arrayOf(PageRange.ALL_PAGES))
                        } catch (_: Exception) {
                            callback.onWriteFailed("Could not print image")
                        } finally {
                            pdf.close()
                        }
                    }
                },
                null,
            )
            Outcome.Success(Unit)
        } catch (_: Exception) {
            failure(CreativeError.Code.UNAVAILABLE, "Printing unavailable")
        }
}
