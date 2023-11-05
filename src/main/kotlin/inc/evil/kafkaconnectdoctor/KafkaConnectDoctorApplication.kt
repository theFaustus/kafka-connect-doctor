package inc.evil.kafkaconnectdoctor

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KafkaConnectDoctorApplication

fun main(args: Array<String>) {
	runApplication<KafkaConnectDoctorApplication>(*args)
}
