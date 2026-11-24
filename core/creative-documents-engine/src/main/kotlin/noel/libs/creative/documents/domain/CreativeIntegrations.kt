package noel.libs.creative.documents.domain

import noel.libs.creative.model.*

data class UploadPermit(
    val accountId: String,
    val assetId: String,
    val contentRevision: String,
    val expiresAtMillis: Long,
)

class UploadPreflight {
    operator fun invoke(
        accountId: String,
        asset: Asset,
        permit: UploadPermit,
        nowMillis: Long,
    ): Outcome<Unit> =
        if (
            accountId == permit.accountId &&
                asset.id == permit.assetId &&
                asset.contentRevision == permit.contentRevision &&
                nowMillis < permit.expiresAtMillis
        )
            Outcome.Success(Unit)
        else
            failure(
                CreativeError.Code.CONFLICT,
                "Upload permit expired or does not match this asset",
            )
}

class MediaEligibility(
    private val maxSelection: Int = 100,
    private val maxPixels: Long = 16_000_000,
) {
    operator fun invoke(assets: List<Asset>, allowedTypes: Set<String>): Outcome<Unit> =
        if (
            assets.size <= maxSelection &&
                assets.all {
                    it.mediaType in allowedTypes &&
                        it.width > 0 &&
                        it.height > 0 &&
                        it.width.toLong() * it.height <= maxPixels
                }
        )
            Outcome.Success(Unit)
        else failure(CreativeError.Code.LIMIT, "Selected media exceeds this feature's limits")
}

interface PaidContentGateway : Entitlements {
    suspend fun restore(accountId: String): Outcome<Unit>
}

class RestorePurchases(private val gateway: PaidContentGateway?) {
    suspend operator fun invoke(accountId: String): Outcome<Unit> =
        gateway?.restore(accountId)
            ?: failure(CreativeError.Code.UNAVAILABLE, "Host purchase adapter required")
}

class RemindUnfinishedProject(private val reminders: ReminderPort?) {
    suspend operator fun invoke(accountId: String, documentId: String): Outcome<Unit> =
        reminders?.schedule(accountId, documentId)
            ?: failure(CreativeError.Code.UNAVAILABLE, "Host reminder adapter required")
}

enum class DeletionScope {
    LOCAL,
    REMOTE,
    BOTH,
}

interface LocalDraftDeletion {
    suspend fun delete(accountId: String, documentId: String): Outcome<Unit>
}

class DeleteProject(private val local: LocalDraftDeletion, private val remote: BackupPort?) {
    suspend operator fun invoke(
        accountId: String,
        documentId: String,
        scope: DeletionScope,
    ): Outcome<Unit> {
        if (scope != DeletionScope.LOCAL) {
            val adapter =
                remote
                    ?: return failure(
                        CreativeError.Code.UNAVAILABLE,
                        "Host cloud-deletion adapter required",
                    )
            val result = adapter.deleteRemote(accountId, documentId)
            if (result is Outcome.Failure) return result
        }
        return if (scope != DeletionScope.REMOTE) local.delete(accountId, documentId)
        else Outcome.Success(Unit)
    }
}
