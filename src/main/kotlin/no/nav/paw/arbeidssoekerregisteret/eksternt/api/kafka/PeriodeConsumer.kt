package no.nav.paw.arbeidssoekerregisteret.eksternt.api.kafka

import no.nav.paw.arbeidssoekerregisteret.eksternt.api.services.ArbeidssoekerService
import no.nav.paw.arbeidssoekerregisteret.eksternt.api.utils.logger
import no.nav.paw.arbeidssokerregisteret.api.v1.Periode
import org.apache.kafka.clients.consumer.KafkaConsumer
import java.time.Duration

class PeriodeConsumer(
    private val topic: String,
    private val consumer: KafkaConsumer<String, Periode>,
    private val arbeidssoekerService: ArbeidssoekerService
) {
    fun start() {
        logger.info("Lytter på topic $topic")
        consumer.subscribe(listOf(topic))

        while (true) {
            consumer.poll(Duration.ofMillis(500)).forEach { post ->
                try {
                    logger.info("Mottok melding fra $topic med offset ${post.offset()} partition ${post.partition()}")
                    val arbeidssoekerperiode = post.value()
                    arbeidssoekerService.opprettEllerOppdaterArbeidssoekerperiode(arbeidssoekerperiode)

                    consumer.commitSync()
                } catch (error: Exception) {
                    throw Exception("Feil ved konsumering av melding fra $topic", error)
                }
            }
        }
    }
}
