package sheet_sharing.exception_handling

import io.ktor.application.call
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import sheet_sharing.sharing.BodyDeserializationException

val exceptionHandling: StatusPages.Configuration.() -> Unit = {

    exception<BodyDeserializationException> { cause ->
        call.respond(
            HttpStatusCode.BadRequest,
            cause.message!!
        )
    }
}