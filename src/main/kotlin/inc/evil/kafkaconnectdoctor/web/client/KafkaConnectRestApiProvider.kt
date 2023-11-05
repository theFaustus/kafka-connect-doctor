package inc.evil.kafkaconnectdoctor.web.client

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import inc.evil.kafkaconnectdoctor.web.dto.HealthResponse
import inc.evil.kafkaconnectdoctor.web.dto.State
import inc.evil.kafkaconnectdoctor.web.dto.Task
import java.io.PrintWriter
import java.io.StringWriter

@Component
class KafkaConnectRestApiProvider(val kafkaConnectRestApiWebClient: WebClient) {

    @Value("#{systemProperties.CONNECTOR_NAME}")
    lateinit var connectorName: String

    companion object {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    fun healthcheck(): Mono<ResponseEntity<Task>> {
        return kafkaConnectRestApiWebClient.get()
            .uri("/connectors/$connectorName/status")
            .retrieve()
            .bodyToMono(HealthResponse::class.java)
            .map(::toErrorAwareResponse)
            .onErrorResume(::handleError)
    }

    private fun handleError(it: Throwable): Mono<ResponseEntity<Task>> {
        log.error("Oops! Encountered error querying status for $connectorName connector.", it);
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)
        it.printStackTrace(printWriter)
        return Mono.just(ResponseEntity.internalServerError().body(Task(-1, State.FAILED, "n/a", stringWriter.toString())))
    }

    private fun toErrorAwareResponse(it: HealthResponse): ResponseEntity<Task> {
        val failedTask = it.tasks.firstOrNull { it.state == State.FAILED }
        return if (failedTask != null) {
            log.error("One of $connectorName's tasks failed. \n{}", failedTask.trace)
            ResponseEntity.internalServerError().body(failedTask)
        } else {
            ResponseEntity.ok(it.tasks.firstOrNull { it.state == State.RUNNING })
        }
    }

}
