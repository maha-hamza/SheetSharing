package abstracts

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import sheet_sharing.recipient_share.RecipientShares
import sheet_sharing.shares.Shares
import sheet_sharing.shares.SheetEnum
import java.time.Instant
import java.util.*


fun insertShare(
    sheet: SheetEnum = SheetEnum.HRRREPORT,
    selection: String = "HRRREPORT!A1",
    createdAt: DateTime = DateTime(Instant.now().toEpochMilli())
): String {
    return transaction {
        Shares.insert {
            it[id] = UUID.randomUUID().toString()
            it[Shares.sheet] = sheet
            it[Shares.selection] = selection
            it[Shares.createdAt] = createdAt
        } get Shares.id
    }
}

fun insertRecipientsSharing(
    shareId: String,
    email: String
): String {
    return transaction {
        RecipientShares.insert {
            it[id] = UUID.randomUUID().toString()
            it[RecipientShares.shareId] = shareId
            it[recipientEmail] = email
        } get RecipientShares.id
    }
}