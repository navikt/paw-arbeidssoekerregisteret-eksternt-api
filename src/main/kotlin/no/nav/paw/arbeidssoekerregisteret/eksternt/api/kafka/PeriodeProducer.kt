package no.nav.paw.arbeidssoekerregisteret.eksternt.api.kafka

import no.nav.paw.arbeidssoekerregisteret.eksternt.api.config.APPLICATION_CONFIG_FILE
import no.nav.paw.arbeidssoekerregisteret.eksternt.api.config.ApplicationConfiguration
import no.nav.paw.arbeidssoekerregisteret.eksternt.api.kafka.serdes.PeriodeSerializer
import no.nav.paw.arbeidssokerregisteret.api.v1.Periode
import no.nav.paw.config.hoplite.loadNaisOrLocalConfiguration
import no.nav.paw.config.kafka.KAFKA_CONFIG_WITH_SCHEME_REG
import no.nav.paw.config.kafka.KafkaConfig
import no.nav.paw.config.kafka.KafkaFactory
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer

fun main() {
    val kafkaConfig = loadNaisOrLocalConfiguration<KafkaConfig>(KAFKA_CONFIG_WITH_SCHEME_REG)
    val applicationConfig = loadNaisOrLocalConfiguration<ApplicationConfiguration>(APPLICATION_CONFIG_FILE)

    produserPeriodeMeldinger(kafkaConfig, applicationConfig)
}

fun produserPeriodeMeldinger(
    kafkaConfig: KafkaConfig,
    applicationConfig: ApplicationConfiguration
) {
    val localProducer = LocalProducer(kafkaConfig, applicationConfig)
    try {
        PeriodeProducerUtils().lagTestPerioder().forEach { periode ->
            localProducer.producePeriodeMessage(applicationConfig.periodeTopic, periode.id.toString(), periode)
        }
    } catch (e: Exception) {
        println("LocalProducer periode error: ${e.message}")
        localProducer.closePeriodeProducer()
    }
}

class LocalProducer(kafkaConfig: KafkaConfig, applicationConfig: ApplicationConfiguration) {
    private val periodeProducer: Producer<String, Periode> =
        KafkaFactory(kafkaConfig)
            .createProducer<String, Periode>(
                clientId = applicationConfig.gruppeId,
                keySerializer = StringSerializer::class,
                valueSerializer = PeriodeSerializer::class
            )

    fun producePeriodeMessage(
        topic: String,
        key: String,
        value: Periode
    ) {
        val record = ProducerRecord(topic, key, value)
        periodeProducer.send(record) { _, exception ->
            if (exception != null) {
                println("Failed to send periode message: $exception")
            } else {
                println("Message sent successfully to topic: $topic")
            }
        }.get()
    }

    fun closePeriodeProducer() {
        periodeProducer.close()
    }
}
