package no.nav.paw.arbeidssoekerregisteret.eksternt.api.routes

import io.ktor.http.*
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import no.nav.paw.arbeidssoekerregisteret.eksternt.api.domain.request.EksternRequest
import no.nav.paw.arbeidssoekerregisteret.eksternt.api.services.ArbeidssoekerService
import no.nav.paw.arbeidssoekerregisteret.eksternt.api.utils.logger
import java.time.LocalDate
import java.time.format.DateTimeParseException

fun Route.arbeidssoekerRoutes(arbeidssoekerService: ArbeidssoekerService) {
    route("/api/v1") {
        authenticate("maskinporten") {
            route("/arbeidssoekerperioder") {
                post {
                    // Henter arbeidssøkerperiode for bruker
                    logger.info("Henter arbeidssøkerperioder for bruker")

                    val eksternRequest =
                        try {
                            call.receive<EksternRequest>()
                        } catch (e: BadRequestException) {
                            return@post call.respond(HttpStatusCode.BadRequest, "Ugyldig request body")
                        }
                    val fraStartetDato =
                        try {
                            LocalDate.parse(eksternRequest.fraStartetDato)
                        } catch (e: DateTimeParseException) {
                            return@post call.respond(HttpStatusCode.BadRequest, "Ugyldig dato 'fraStartetDato' må være satt med yyyy-mm-dd")
                        }

                    val identitetsnummer = eksternRequest.getIdentitetsnummer()

                    val arbeidssoekerperioder = arbeidssoekerService.hentArbeidssoekerperioder(identitetsnummer, fraStartetDato)

                    logger.info("Hentet arbeidssøkerperioder for bruker")

                    return@post call.respond(HttpStatusCode.OK, arbeidssoekerperioder)
                }
            }
        }
    }
}