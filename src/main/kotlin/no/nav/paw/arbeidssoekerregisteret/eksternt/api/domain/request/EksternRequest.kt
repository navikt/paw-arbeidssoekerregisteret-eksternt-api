package no.nav.paw.arbeidssoekerregisteret.eksternt.api.domain.request

import no.nav.paw.arbeidssoekerregisteret.eksternt.api.domain.Identitetsnummer
import no.nav.paw.arbeidssoekerregisteret.eksternt.api.domain.toIdentitetsnummer

data class EksternRequest(val identitetsnummer: String, val fraStartetDato: String) {
    fun getIdentitetsnummer(): Identitetsnummer = identitetsnummer.toIdentitetsnummer()
}
