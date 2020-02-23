package sheet_sharing.shares

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.koin.core.KoinComponent
import org.koin.core.inject
import sheet_sharing.recipient_share.RecipientShareService
import sheet_sharing.recipient_share.RecipientShares
import java.time.Instant
import java.util.*

class SheetShareService : KoinComponent {

    private val recipientShareService by inject<RecipientShareService>()

    /**
     * Concurrency Handling should be included if needed here
     * I Would Use Locking due to different DB operations (Will not relay on DB Isolation Levels in that case)
     */
    fun createShares(newShares: NewShares): List<Share> {
        validateSharingsInput(newShares)
        return transaction {
            val shares = Shares.batchInsert(newShares.selections) { selection ->
                this[Shares.id] = UUID.randomUUID().toString()
                this[Shares.createdAt] = DateTime(Instant.now().toEpochMilli())
                this[Shares.selection] = selection
                this[Shares.sheet] = getEquivalentSheetEnum(selection)
            }.map { toShare(it) }

//TODO revisit
            newShares.recipients.forEach { rec ->
                shares.map { it.id }.forEach { share ->
                    RecipientShares.insert {
                        it[id] = UUID.randomUUID().toString()
                        it[recipientEmail] = rec
                        it[shareId] = share
                    }
                }
            }
            /**
             * Notify Recipients that they have access to new parts
             */
            shares.map {
                it.copy(recipients = recipientShareService.getRecipientsEmailsByShare(it.id))
            }
            /**
             * Send Notification to Users that the action is done (return with the result)
             * Low Priority because response is getting back to the client , but would be good idea in case of hierarchy
             * i.e. Admin , Editor ... etc other than action initiator
             */
        }
    }

    fun getAllShares(): List<Share> {
        return transaction {
            Shares
                .selectAll()
                .map(::toShare)
                .map {
                    it.copy(recipients = recipientShareService.getRecipientsEmailsByShare(it.id))
                }
        }
    }

    fun getShareById(id: String): Share? {
        return transaction {
            Shares
                .select { Shares.id eq id }
                .map(::toShare)
                .map { it.copy(recipients = recipientShareService.getRecipientsEmailsByShare(it.id)) }
                .firstOrNull()
        }
    }

    fun getRecipientShares(email: String): RecipientShareResponse {
        return transaction {
            when (recipientShareService.isRecipientExists(email)) {
                true -> {
                    val shares = RecipientShares.leftJoin(Shares)
                        .slice(Shares.id, Shares.selection, Shares.sheet, Shares.createdAt)
                        .selectAll()
                        .andWhere { RecipientShares.recipientEmail eq email }
                        .map(::toShare)
                    RecipientShareResponse(recipient = email, shares = shares)
                }
                false -> throw RecipientNotFoundException("$email, has no shares yet")
            }
        }
    }

    private fun validateSharingsInput(shares: NewShares) {
        val emailsValidation = shares.recipients.map { validateEmail(it) }.filter { !it }
        if (emailsValidation.isNotEmpty())
            throw InvalidEmailFormatException("One or More email address(es) in invalid format")

        val selectionsValidation = shares.selections.map { validateSelection(it) }.filter { !it }
        if (selectionsValidation.isNotEmpty())
            throw InvalidSelectionFormatException("One or More Selection(s) in invalid format")

        shares.selections.map { getEquivalentSheetEnum(it) }
    }
}







