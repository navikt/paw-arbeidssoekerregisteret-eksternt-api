package no.nav.paw.arbeidssoekerregisteret.eksternt.api.kafka

import no.nav.paw.arbeidssoekerregisteret.eksternt.api.services.ArbeidssoekerService
import no.nav.paw.arbeidssoekerregisteret.eksternt.api.utils.logger
import no.nav.paw.arbeidssokerregisteret.api.v1.Periode
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.KafkaConsumer
import java.time.Duration

class PeriodeConsumer(
    private val topic: String,
    private val consumer: KafkaConsumer<Long, Periode>,
    private val arbeidssoekerService: ArbeidssoekerService
) {
    fun start() {
        logger.info("Lytter på topic $topic")
        consumer.subscribe(listOf(topic))

        while (true) {
            val records: ConsumerRecords<Long, Periode> =
                consumer.poll(Duration.ofMillis(1000))
                    .onEach {
                        logger.info("Mottok melding fra $topic med offset ${it.offset()} partition ${it.partition()}")
                    }
            val perioder =
                records.map { record: ConsumerRecord<Long, Periode> ->
                    record.value()
                }
            processAndCommitBatch(perioder)
        }
    }

    private fun processAndCommitBatch(batch: Iterable<Periode>) =
        try {
            arbeidssoekerService.storeBatch(batch)
            consumer.commitSync()
        } catch (error: Exception) {
            throw Exception("Feil ved konsumering av melding fra $topic", error)
        }
}
