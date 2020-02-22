package config

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.routing.Routing
import io.ktor.routing.delete
import io.ktor.routing.get
import io.ktor.routing.patch
import io.ktor.routing.post

fun Routing.get(path: String, func: suspend (ApplicationCall) -> Unit) = get(path) { func(call) }
fun Routing.post(path: String, func: suspend (ApplicationCall) -> Unit) = post(path) { func(call) }
fun Routing.patch(path: String, func: suspend (ApplicationCall) -> Unit) = patch(path) { func(call) }
fun Routing.delete(path: String, func: suspend (ApplicationCall) -> Unit) = delete(path) { func(call) }

fun Routing.route() {

}