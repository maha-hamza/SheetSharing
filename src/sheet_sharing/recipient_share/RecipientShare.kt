package sheet_sharing.recipient_share

import org.jetbrains.exposed.sql.Table
import sheet_sharing.shares.Shares

object RecipientShares : Table() {
    val id = varchar(name = "id", length = 50).primaryKey()
    val shareId = varchar(name = "share_id", length = 50) references Shares.id
    val recipientEmail = varchar(name = "recipient_email", length = 100)
}