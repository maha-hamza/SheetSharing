package sheet_sharing.recipient_share

import abstracts.AbstractDBTest
import abstracts.insertRecipientsSharing
import abstracts.insertShare
import config.lazyInject
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.koin.core.KoinComponent
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RecipientShareServiceTest : AbstractDBTest(), KoinComponent {

    private val service by lazyInject<RecipientShareService>()

    @Test
    fun `Should retrieve recipients emails successfully`() {
        val shareId = insertShare()
        insertRecipientsSharing(shareId = shareId, email = "test1@me.com")
        insertRecipientsSharing(shareId = shareId, email = "test2@me.com")

        val shareId2 = insertShare()
        insertRecipientsSharing(shareId = shareId2, email = "test3@me.com")

        val result = service.getRecipientsEmailsByShare(shareId)
        assertThat(result).containsExactlyInAnyOrder("test1@me.com", "test2@me.com")
    }

    @Test
    fun `Should Return email Empty list if share not found`() {
        val result = service.getRecipientsEmailsByShare("anyid")
        assertThat(result).isEmpty()
    }

    @Test
    fun `Should return true if recipient existences`() {
        val shareId = insertShare()
        insertRecipientsSharing(shareId = shareId, email = "test@me.com")

        assertTrue { service.isRecipientExists("test@me.com") }
    }

    @Test
    fun `Should return false if no recipient found`() {
        assertFalse { service.isRecipientExists("test@me.com") }
    }

}