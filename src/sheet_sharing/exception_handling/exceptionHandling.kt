package sheet_sharing.exception_handling

import io.ktor.application.call
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import sheet_sharing.shares.*

val exceptionHandling: StatusPages.Configuration.() -> Unit = {

    exception<BodyDeserializationException> { cause ->
        call.respond(
            HttpStatusCode.BadRequest,
            cause.message!!
        )
    }

    exception<InvalidSelectionFormatException> { cause ->
        call.respond(
            HttpStatusCode.BadRequest,
            cause.message!!
        )
    }

    exception<RecipientNotFoundException> { cause ->
        call.respond(
            HttpStatusCode.NotFound,
            cause.message!!
        )
    }

    exception<InvalidEmailFormatException> { cause ->
        call.respond(
            HttpStatusCode.BadRequest,
            cause.message!!
        )
    }

    exception<SelectionDoesntMatchActualSheetException> { cause ->
        call.respond(
            HttpStatusCode.BadRequest,
            cause.message!!
        )
    }
}