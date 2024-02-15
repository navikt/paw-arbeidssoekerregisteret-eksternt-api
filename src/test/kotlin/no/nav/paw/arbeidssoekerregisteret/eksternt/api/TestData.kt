package no.nav.paw.arbeidssoekerregisteret.eksternt.api

import no.nav.paw.arbeidssoekerregisteret.eksternt.api.domain.toIdentitetsnummer

object TestData {
    val foedselsnummer = "18908396568".toIdentitetsnummer()
    val maskinportenScope = "nav:arbeid:arbeidssokerregisteret.read"
}
