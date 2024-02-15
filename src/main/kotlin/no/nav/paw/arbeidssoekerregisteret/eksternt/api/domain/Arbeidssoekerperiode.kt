package no.nav.paw.arbeidssoekerregisteret.eksternt.api.domain

import java.time.LocalDateTime
import java.util.UUID

data class ArbeidssoekerperiodeResponse(
    val periodeId: UUID,
    val startet: LocalDateTime,
    val avsluttet: LocalDateTime? = null
)

data class Arbeidssoekerperiode(
    val identitetsnummer: Identitetsnummer,
    val periodeId: UUID,
    val startet: LocalDateTime,
    val avsluttet: LocalDateTime? = null
)

fun Arbeidssoekerperiode.toArbeidssoekerperiodeResponse() =
    ArbeidssoekerperiodeResponse(
        periodeId = periodeId,
        startet = startet,
        avsluttet = avsluttet
    )
