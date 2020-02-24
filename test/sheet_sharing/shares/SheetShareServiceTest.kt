package sheet_sharing.shares

import abstracts.AbstractDBTest
import abstracts.insertRecipientsSharing
import abstracts.insertShare
import config.lazyInject
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.joda.time.DateTime
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.koin.core.KoinComponent
import java.time.Instant

class SheetShareServiceTest : AbstractDBTest(), KoinComponent {

    private val service by lazyInject<SheetShareService>()

    @Nested
    inner class AllShares {

        @Test
        fun `Should return all shares associated with recipients successfully`() {
            val share1Id = insertShare(createdAt = DateTime.now())
            insertRecipientsSharing(shareId = share1Id, email = "test1@me.com")
            insertRecipientsSharing(shareId = share1Id, email = "test2@me.com")

            val share2Id = insertShare(
                selection = "ASSUMPTIONS!A1",
                sheet = SheetEnum.ASSUMPTIONS,
                createdAt = DateTime.now()
            )
            insertRecipientsSharing(shareId = share2Id, email = "test3@me.com")

            val result = service.getAllShares()

            assertThat(result)
                .usingRecursiveFieldByFieldElementComparator()
                .usingElementComparatorIgnoringFields("createdAt")
                .isEqualTo(
                    listOf(
                        Share(
                            id = share1Id,
                            createdAt = Instant.now(),
                            sheet = SheetEnum.HRRREPORT,
                            recipients = listOf("test1@me.com", "test2@me.com"),
                            selection = "HRRREPORT!A1"
                        ),
                        Share(
                            id = share2Id,
                            createdAt = Instant.now(),
                            sheet = SheetEnum.ASSUMPTIONS,
                            recipients = listOf("test3@me.com"),
                            selection = "ASSUMPTIONS!A1"
                        )
                    )
                )
        }
    }

    @Nested
    inner class SharesById {

        @Test
        fun `Should return Share by id successfully`() {
            val share1Id = insertShare(createdAt = DateTime.now())
            insertRecipientsSharing(shareId = share1Id, email = "test1@me.com")
            insertRecipientsSharing(shareId = share1Id, email = "test2@me.com")

            val share2Id = insertShare(
                selection = "ASSUMPTIONS!A1",
                sheet = SheetEnum.ASSUMPTIONS,
                createdAt = DateTime.now()
            )
            insertRecipientsSharing(shareId = share2Id, email = "test3@me.com")

            val result = service.getShareById(share1Id)

            assertThat(result)
                .isEqualToIgnoringGivenFields(
                    Share(
                        id = share1Id,
                        createdAt = Instant.now(),
                        sheet = SheetEnum.HRRREPORT,
                        recipients = listOf("test1@me.com", "test2@me.com"),
                        selection = "HRRREPORT!A1"
                    ), Shares::createdAt.name
                )
        }

        @Test
        fun `Should return null if share not found`() {
            val result = service.getShareById("any-id")

            assertThat(result)
                .isNull()
        }
    }

    @Nested
    inner class RecipientsShares {

        @Test
        fun `Should Return shares of specific Recipient Successfully`() {
            val share1Id = insertShare(createdAt = DateTime.now())
            insertRecipientsSharing(shareId = share1Id, email = "test1@me.com")
            insertRecipientsSharing(shareId = share1Id, email = "test2@me.com")

            val share2Id = insertShare(
                selection = "ASSUMPTIONS!A1",
                sheet = SheetEnum.ASSUMPTIONS,
                createdAt = DateTime.now()
            )
            insertRecipientsSharing(shareId = share2Id, email = "test1@me.com")

            val result = service.getRecipientShares("test1@me.com")

            assertThat(result.recipient).isEqualTo("test1@me.com")
            assertThat(result.shares)
                .usingRecursiveFieldByFieldElementComparator()
                .usingElementComparatorIgnoringFields("createdAt")
                .containsExactlyInAnyOrder(
                    Share(
                        id = share2Id,
                        createdAt = Instant.now(),
                        sheet = SheetEnum.ASSUMPTIONS,
                        recipients = null,
                        selection = "ASSUMPTIONS!A1"
                    ),
                    Share(
                        id = share1Id,
                        createdAt = Instant.now(),
                        sheet = SheetEnum.HRRREPORT,
                        recipients = null,
                        selection = "HRRREPORT!A1"
                    )
                )
        }

        @Test
        fun `Should fail retrieving recipients' shares (recipient not found)`() {
            val result = assertThrows<RecipientNotFoundException> { service.getRecipientShares("any@email.com") }
            assertThat(result.message).isEqualTo("any@email.com, has no shares yet")
        }
    }

    @Nested
    inner class SharesCreation {

        @Test
        fun `Share creation fails (invalid email format)`() {
            val shares = NewShares(
                recipients = listOf("test@me", "test@test.com"),
                selections = listOf("sel1")
            )
            val result = assertThrows<InvalidEmailFormatException> { service.createShares(shares) }
            assertThat(result.message).isEqualTo("(1) email address(es) in invalid format")
        }

        @Test
        fun `Share creation fails (invalid selection format)`() {
            val shares = NewShares(
                recipients = listOf("test1@me.com", "test2@test.com"),
                selections = listOf("'sel1")
            )
            val result = assertThrows<InvalidSelectionFormatException> { service.createShares(shares) }
            assertThat(result.message).isEqualTo("(1) Selection(s) in invalid format")
        }

        @Test
        fun `Share creation fails (selection doesn't match actual sheet)`() {
            val shares = NewShares(
                recipients = listOf("test1@me.com", "test2@test.com"),
                selections = listOf("sel1", "sel2")
            )
            val result = assertThrows<SelectionDoesntMatchActualSheetException> { service.createShares(shares) }
            assertThat(result.message).isEqualTo("(2) Selection(s) didn't represent a valid sheet")
        }

        @Test
        fun `Should Create Shares Successfully`() {
            val shares = NewShares(
                recipients = listOf("test1@me.com", "test2@test.com", "test3@test.com"),
                selections = listOf("HRR REPORT!B4", "ASSUMPTIONS")
            )
            val result = service.createShares(shares).sortedBy { it.sheet }

            assertThat(result)
                .usingElementComparatorIgnoringFields("createdAt", "id")
                .isEqualTo(
                    listOf(
                        Share(
                            id = "",
                            createdAt = Instant.now(),
                            sheet = SheetEnum.HRRREPORT,
                            recipients = listOf("test1@me.com", "test2@test.com", "test3@test.com"),
                            selection = "HRR REPORT!B4"
                        ),
                        Share(
                            id = "",
                            createdAt = Instant.now(),
                            sheet = SheetEnum.ASSUMPTIONS,
                            recipients = listOf("test1@me.com", "test2@test.com", "test3@test.com"),
                            selection = "ASSUMPTIONS"
                        )
                    )
                )
        }

        @Test
        fun `Creating shares fails(Unique Index Exception)`() {
            val shares = NewShares(
                recipients = listOf("test@test.com", "test@test.com"),
                selections = listOf("HRR REPORT!B4")
            )
            assertThrows<ExposedSQLException> { service.createShares(shares) }
        }
    }

}