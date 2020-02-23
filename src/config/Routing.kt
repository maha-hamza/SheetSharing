package config

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.routing.Routing
import io.ktor.routing.delete
import io.ktor.routing.get
import io.ktor.routing.patch
import io.ktor.routing.post
import sheet_sharing.shares.SheetShareController

fun Routing.get(path: String, func: suspend (ApplicationCall) -> Unit) = get(path) { func(call) }
fun Routing.post(path: String, func: suspend (ApplicationCall) -> Unit) = post(path) { func(call) }
fun Routing.patch(path: String, func: suspend (ApplicationCall) -> Unit) = patch(path) { func(call) }
fun Routing.delete(path: String, func: suspend (ApplicationCall) -> Unit) = delete(path) { func(call) }

fun Routing.route() {

    val sheetShareController = inject<SheetShareController>()

    get   ("/api/shares",                         sheetShareController::getAllShares)
    get   ("/api/shares/recipient/{recipient}", sheetShareController::getRecipientShares)
    get   ("/api/shares/{id}",                    sheetShareController::getAllShares)
    post  ("/api/shares",                         sheetShareController::createShares)
}