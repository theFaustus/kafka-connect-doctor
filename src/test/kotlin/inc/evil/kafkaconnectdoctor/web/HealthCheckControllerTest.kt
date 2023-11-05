package inc.evil.kafkaconnectdoctor.web

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.ResponseEntity
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono
import inc.evil.kafkaconnectdoctor.web.client.KafkaConnectRestApiProvider
import inc.evil.kafkaconnectdoctor.web.dto.State
import inc.evil.kafkaconnectdoctor.web.dto.Task

@WebFluxTest(controllers = [HealthCheckController::class])
class HealthCheckControllerTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @MockBean
    private lateinit var kafkaConnectRestApiProvider: KafkaConnectRestApiProvider

    @Test
    fun healthcheck_returnsExpectedResponse() {
        whenever(kafkaConnectRestApiProvider.healthcheck()).thenReturn(Mono.just(ResponseEntity.ok(Task(1, State.RUNNING, "1"))))

        webTestClient.get()
            .uri("/api/v1/health")
            .exchange()
            .expectStatus().isOk
            .expectBody().json("{\"id\":1,\"state\":\"RUNNING\",\"worker_id\":\"1\",\"trace\":null}")
    }
}
