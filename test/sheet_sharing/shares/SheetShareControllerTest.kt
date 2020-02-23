package sheet_sharing.shares

import abstracts.*
import abstracts.ProjectionAssertions.Companion.assertThat
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import org.junit.jupiter.api.Test
import java.time.Instant

class SheetShareControllerTest : AbstractDBTest() {

    @Test
    fun `Should Retrieve All Shares`() {
        val share = insertShare()
        insertRecipientsSharing(share, "email1@me.com")
        insertRecipientsSharing(share, "email2@me.com")

        handle("/api/shares") {
            assertThat(response)
                .status(HttpStatusCode.OK)
                .contentType(JsonUtf8)
                .listBody<Share>()
                .usingRecursiveFieldByFieldElementComparator()
                .usingElementComparatorIgnoringFields("createdAt")
                .isEqualTo(
                    listOf(
                        Share(
                            id = share,
                            selection = "HRRREPORT!A1",
                            sheet = SheetEnum.HRRREPORT,
                            recipients = listOf("email1@me.com", "email2@me.com"),
                            createdAt = Instant.now()

                        )
                    )
                )
        }
    }

    @Test
    fun `Should Retrieve share by id`() {
        val share = insertShare()
        insertRecipientsSharing(share, "email@me.com")
        insertShare(sheet = SheetEnum.ASSUMPTIONS, selection = "ASSUMPTIONS")

        handle("/api/shares/$share") {
            assertThat(response)
                .status(HttpStatusCode.OK)
                .contentType(JsonUtf8)
                .body<Share>()
                .isEqualToIgnoringGivenFields(
                    Share(
                        id = share,
                        selection = "HRRREPORT!A1",
                        sheet = SheetEnum.HRRREPORT,
                        recipients = listOf("email@me.com"),
                        createdAt = Instant.now()

                    ), Share::createdAt.name
                )
        }
    }

    @Test
    fun `Should retrieve recipient shares`() {
        val share = insertShare()
        insertRecipientsSharing(share, "email@me.com")
        val share2 = insertShare(sheet = SheetEnum.ASSUMPTIONS, selection = "ASSUMPTIONS")
        insertRecipientsSharing(share2, "email@me.com")

        handle("/api/shares/recipient/email@me.com") {
            assertThat(response)
                .status(HttpStatusCode.OK)
        }
    }

    @Test
    fun `Should fail retrieving recipient shares(not found)`() {
        handle("/api/shares/recipient/email@me.com") {
            assertThat(response)
                .status(HttpStatusCode.NotFound)
                .contentType(PlainUtf8)
                .body<String>()
                .isEqualTo("email@me.com, has no shares yet")
        }
    }

    @Test
    fun `Should create shares successfully`() {
        val shares = NewShares(
            recipients = listOf("test1@me.com", "test2@test.com", "test3@test.com"),
            selections = listOf("HRR REPORT!B4", "ASSUMPTIONS")
        )
        handle(
            uri = "/api/shares",
            method = HttpMethod.Post,
            body = shares
        ) {
            assertThat(response)
                .status(HttpStatusCode.Created)
                .contentType(JsonUtf8)
                .listBody<Share>()
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
    }
}