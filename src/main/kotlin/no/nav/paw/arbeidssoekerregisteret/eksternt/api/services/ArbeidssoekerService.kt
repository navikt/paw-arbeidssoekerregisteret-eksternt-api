package no.nav.paw.arbeidssoekerregisteret.eksternt.api.services

import no.nav.paw.arbeidssoekerregisteret.eksternt.api.domain.ArbeidssoekerperiodeResponse
import no.nav.paw.arbeidssoekerregisteret.eksternt.api.domain.Identitetsnummer
import no.nav.paw.arbeidssoekerregisteret.eksternt.api.domain.toArbeidssoekerperiodeResponse
import no.nav.paw.arbeidssoekerregisteret.eksternt.api.repositories.ArbeidssoekerperiodeRepository
import no.nav.paw.arbeidssokerregisteret.api.v1.Periode
import java.time.LocalDate

class ArbeidssoekerService(private val arbeidssoekerperiodeRepository: ArbeidssoekerperiodeRepository) {
    fun storeBatch(arbeidssoekerperioder: Iterable<Periode>) {
        arbeidssoekerperiodeRepository.storeBatch(arbeidssoekerperioder)
    }

    fun hentArbeidssoekerperioder(
        identitetsnummer: Identitetsnummer,
        fraStartetDato: LocalDate?
    ): List<ArbeidssoekerperiodeResponse?> = arbeidssoekerperiodeRepository.hentArbeidssoekerperioder(identitetsnummer, fraStartetDato).map { it?.toArbeidssoekerperiodeResponse() }
}
