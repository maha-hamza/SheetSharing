package sheet_sharing.shares

import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
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

    fun createShares(newShares: NewShares): List<Share> {
        validateSharingsInput(newShares)
        return transaction {
            val shares = Shares.batchInsert(newShares.selections) { selection ->
                this[Shares.id] = UUID.randomUUID().toString()
                this[Shares.createdAt] = DateTime(Instant.now().toEpochMilli())
                this[Shares.selection] = selection
                this[Shares.sheet] = getEquivalentSheetEnum(selection)
            }.map { toShare(it) }

            RecipientShares.batchInsert(newShares.recipients) { recipient ->
                shares.map { it.id }.forEach { shareId ->
                    this[RecipientShares.id] = UUID.randomUUID().toString()
                    this[RecipientShares.recipientEmail] = recipient
                    this[RecipientShares.shareId] = shareId
                }
            }
            shares.map {
                it.copy(recipients = recipientShareService.getRecipientsEmailsByShare(it.id))
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

    fun getAllShares(): List<Share> {
        return transaction {
            Shares
                .selectAll()
                .map(::toShare)
                .map { it.copy(recipients = recipientShareService.getRecipientsEmailsByShare(it.id)) }
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
}







