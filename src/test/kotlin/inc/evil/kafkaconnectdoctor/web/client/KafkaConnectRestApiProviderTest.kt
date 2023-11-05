package inc.evil.kafkaconnectdoctor.web.client

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.mockserver.client.MockServerClient
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.reactive.function.client.WebClient
import reactor.test.StepVerifier
import inc.evil.kafkaconnectdoctor.common.AbstractTestcontainersTest
import inc.evil.kafkaconnectdoctor.common.stubPath
import inc.evil.kafkaconnectdoctor.web.dto.State
import inc.evil.kafkaconnectdoctor.web.dto.Task

@ExtendWith(MockitoExtension::class)
class KafkaConnectRestApiProviderTest : AbstractTestcontainersTest() {

    private lateinit var webClient: WebClient
    private lateinit var kafkaConnectRestApiProvider: KafkaConnectRestApiProvider

    @BeforeEach
    fun setUp() {
        webClient = WebClient.create("http://localhost:${mockServer.serverPort}")
        kafkaConnectRestApiProvider = KafkaConnectRestApiProvider(webClient)
        kafkaConnectRestApiProvider.connectorName = "foo.connector"
    }

    @Test
    fun healthcheck_whenConnectorTaskRunning_returns200() {
        //language=json
        val responseBody = """
            {
              "name": "foo.connector",
              "connector": {
                "state": "RUNNING",
                "worker_id": "127.0.0.1:8083"
              },
              "tasks": [
                {
                  "id": 0,
                  "state": "RUNNING",
                  "worker_id": "127.0.0.1:8083"
                }
              ],
              "type": "source"
            }
        """.trimIndent()

        mockServerClient.stubPath(requestPath = "/connectors/foo.connector/status", responseBody = responseBody)

        StepVerifier.create(kafkaConnectRestApiProvider.healthcheck())
            .expectNext(ResponseEntity.ok(Task(0, State.RUNNING, "127.0.0.1:8083")))
    }

    @Test
    fun healthcheck_whenConnectorTaskFailed_returns500() {
        //language=json
        val responseBody = """
            {
              "name": "foo.connector",
              "connector": {
                "state": "RUNNING",
                "worker_id": "127.0.0.1:8083"
              },
              "tasks": [
                {
                  "id": 0,
                  "state": "FAILED",
                  "worker_id": "127.0.0.1:8083",
                  "trace": "org.apache.kafka.connect.errors.ConnectException: Failed to Query table after retries"
                }
              ],
              "type": "source"
            }
        """.trimIndent()

        mockServerClient.stubPath(requestPath = "/connectors/foo.connector/status", responseBody = responseBody)

        StepVerifier.create(kafkaConnectRestApiProvider.healthcheck())
            .expectNext(ResponseEntity.internalServerError().body(Task(0, State.FAILED, "127.0.0.1:8083", trace = "org.apache.kafka.connect.errors.ConnectException: Failed to Query table after retries")))
    }

}
