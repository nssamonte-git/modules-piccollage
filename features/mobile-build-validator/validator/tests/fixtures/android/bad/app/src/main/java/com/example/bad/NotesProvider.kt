package com.example.bad

class NotesProvider : ContentProvider() {
    override fun openFile(uri: Uri, mode: String): ParcelFileDescriptor? {
        val file = File(context!!.filesDir, uri.lastPathSegment!!)
        return ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
    }
}
