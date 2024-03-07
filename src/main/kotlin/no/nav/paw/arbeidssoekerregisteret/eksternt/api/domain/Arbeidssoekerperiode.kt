package no.nav.paw.arbeidssoekerregisteret.eksternt.api.domain

import no.nav.paw.arbeidssoekerregisteret.eksternt.api.utils.toLocalDateTime
import no.nav.paw.arbeidssokerregisteret.api.v1.Periode
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

fun Periode.toArbeidssoekerperiode() =
    Arbeidssoekerperiode(
        identitetsnummer = Identitetsnummer(identitetsnummer),
        periodeId = id,
        startet = startet.tidspunkt.toLocalDateTime(),
        avsluttet = avsluttet?.tidspunkt?.toLocalDateTime()
    )
