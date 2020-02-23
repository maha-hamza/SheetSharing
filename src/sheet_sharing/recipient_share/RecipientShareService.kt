package sheet_sharing.recipient_share

import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.KoinComponent

class RecipientShareService : KoinComponent {


    fun getRecipientsEmailsByShare(shareId: String): List<String> {
        return transaction {
            RecipientShares.selectAll()
                .andWhere { RecipientShares.shareId eq shareId }
                .map { it[RecipientShares.recipientEmail] }
        }
    }

    fun isRecipientExists(email: String): Boolean {
        return transaction {
            RecipientShares
                .select { RecipientShares.recipientEmail eq email }
                .count() != 0
        }
    }
}







