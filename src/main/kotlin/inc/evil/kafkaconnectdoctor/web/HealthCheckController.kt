package inc.evil.kafkaconnectdoctor.web

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import inc.evil.kafkaconnectdoctor.web.client.KafkaConnectRestApiProvider
import inc.evil.kafkaconnectdoctor.web.dto.Task

@RestController
@RequestMapping("/api/v1/health")
class HealthCheckController(val kafkaConnectRestApiProvider: KafkaConnectRestApiProvider) {

    @GetMapping
    fun healthcheck(): Mono<ResponseEntity<Task>> = kafkaConnectRestApiProvider.healthcheck()

}
