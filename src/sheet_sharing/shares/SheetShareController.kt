package sheet_sharing.shares

import config.body
import config.respondNullable
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import org.koin.core.KoinComponent
import org.koin.core.inject

class SheetShareController : KoinComponent {

    private val sheetShareService by inject<SheetShareService>()

    suspend fun createShares(call: ApplicationCall) {
        val shares = call.body<NewShares>()
        call.respond(
            HttpStatusCode.Created,
            sheetShareService.createShares(shares)
        )
    }

    suspend fun getAllShares(call: ApplicationCall) {
        call.respond(sheetShareService.getAllShares())
    }

    suspend fun getShareById(call: ApplicationCall) {
        val shareId = call.parameters["id"]!!
        call.respondNullable(sheetShareService.getShareById(shareId))
    }

    suspend fun getRecipientShares(call: ApplicationCall) {
        val recipientEmail = call.parameters["recipient"]!!
        call.respond(sheetShareService.getRecipientShares(recipientEmail))
    }


}