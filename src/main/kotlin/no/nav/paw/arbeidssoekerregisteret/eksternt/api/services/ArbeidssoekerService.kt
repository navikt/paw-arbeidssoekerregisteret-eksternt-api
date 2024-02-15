package no.nav.paw.arbeidssoekerregisteret.eksternt.api.services

import no.nav.paw.arbeidssoekerregisteret.eksternt.api.domain.Arbeidssoekerperiode
import no.nav.paw.arbeidssoekerregisteret.eksternt.api.domain.ArbeidssoekerperiodeResponse
import no.nav.paw.arbeidssoekerregisteret.eksternt.api.domain.Identitetsnummer
import no.nav.paw.arbeidssoekerregisteret.eksternt.api.domain.toArbeidssoekerperiodeResponse
import no.nav.paw.arbeidssoekerregisteret.eksternt.api.repositories.ArbeidssoekerperiodeRepository
import no.nav.paw.arbeidssokerregisteret.api.v1.Periode
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

class ArbeidssoekerService(private val arbeidssoekerperiodeRepository: ArbeidssoekerperiodeRepository) {
    fun opprettEllerOppdaterArbeidssoekerperiode(periodeMelding: Periode) {
        val eksisterendePeriode = arbeidssoekerperiodeRepository.finnesArbeidssoekerperiode(periodeMelding.id)
        val periode =
            Arbeidssoekerperiode(
                identitetsnummer = Identitetsnummer(periodeMelding.identitetsnummer),
                periodeId = periodeMelding.id,
                startet = LocalDateTime.ofInstant(periodeMelding.startet.tidspunkt, ZoneId.systemDefault()),
                avsluttet = periodeMelding.avsluttet?.tidspunkt?.let { LocalDateTime.ofInstant(it, ZoneId.systemDefault()) }
            )
        if (eksisterendePeriode) {
            arbeidssoekerperiodeRepository.oppdaterArbeidssoekerperiode(periode)
        } else {
            arbeidssoekerperiodeRepository.opprettArbeidssoekerperiode(periode)
        }
    }

    fun hentArbeidssoekerperioder(
        identitetsnummer: Identitetsnummer,
        fraStartetDato: LocalDate?
    ): List<ArbeidssoekerperiodeResponse?> = arbeidssoekerperiodeRepository.hentArbeidssoekerperioder(identitetsnummer, fraStartetDato).map { it?.toArbeidssoekerperiodeResponse() }
}
