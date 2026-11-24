package noel.libs.creative.documents.domain

import java.util.UUID
import noel.libs.creative.model.*

class SaveDraft(private val repository: DocumentRepository) {
    suspend operator fun invoke(accountId: String, document: CreativeDocument) =
        repository.save(accountId, document)
}

class OpenDraft(private val repository: DocumentRepository) {
    suspend operator fun invoke(accountId: String, id: String) = repository.load(accountId, id)
}

class DuplicatePage {
    operator fun invoke(session: EditSession, pageId: String): Outcome<CreativeDocument> {
        val d = session.document.value
        val p =
            d.pages.firstOrNull { it.id == pageId }
                ?: return failure(CreativeError.Code.INVALID_INPUT, "Missing page")
        val duplicate =
            p.copy(
                id = UUID.randomUUID().toString(),
                layers = p.layers.map { it.copy(id = UUID.randomUUID().toString()) },
            )
        return session.commit(EditRequest(d.revision, listOf(Edit.AddPage(duplicate))))
    }
}

class BackupDraft(private val backup: BackupPort?) {
    suspend operator fun invoke(accountId: String, d: CreativeDocument): Outcome<Unit> =
        backup?.upload(accountId, d)
            ?: failure(CreativeError.Code.UNAVAILABLE, "Host backup adapter required")
}

data class Album(val id: String, val accountId: String, val documentIds: Set<String>)

class AlbumMembership {
    fun add(album: Album, accountId: String, id: String): Outcome<Album> =
        if (accountId != album.accountId) failure(CreativeError.Code.CONFLICT, "Account mismatch")
        else Outcome.Success(album.copy(documentIds = album.documentIds + id))
}
