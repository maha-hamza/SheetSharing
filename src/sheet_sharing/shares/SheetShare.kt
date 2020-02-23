package sheet_sharing.shares

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import java.time.Instant

object Shares : Table() {
    val id = varchar(name = "id", length = 50).primaryKey()
    val sheet = enumerationByName("sheet", 10, SheetEnum::class)
    val selection = varchar(name = "selection", length = 100)
    val createdAt = datetime("created_at")
}

data class Share(
    val id: String,
    val sheet: SheetEnum,
    val selection: String,
    val createdAt: Instant,
    val recipients: List<String>?
)

data class NewShares(
    val recipients: List<String>,
    val selections: List<String>
)

data class RecipientShareResponse(
    val recipient: String,
    val shares: List<Share>
)

fun toShare(row: ResultRow): Share =
    Share(
        id = row[Shares.id],
        sheet = row[Shares.sheet],
        selection = row[Shares.selection],
        createdAt = row[Shares.createdAt].let { Instant.ofEpochMilli(it.millis) },
        recipients = null
    )