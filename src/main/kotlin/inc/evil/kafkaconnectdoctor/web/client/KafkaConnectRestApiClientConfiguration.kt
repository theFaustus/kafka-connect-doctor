package inc.evil.kafkaconnectdoctor.web.client

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Configuration
class KafkaConnectRestApiClientConfiguration {

    @Value("#{systemProperties.HOST}")
    lateinit var host: String
    @Value("#{systemProperties.PORT}")
    lateinit var port: String

    companion object {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    @Bean
    fun kafkaConnectRestApiWebClient(): WebClient {
        return WebClient.builder()
            .baseUrl("$host:$port")
            .filter(logRequest())
            .build()
    }

    private fun logRequest(): ExchangeFilterFunction = ExchangeFilterFunction.ofRequestProcessor {
        log.info("Request {} : {}", it.method(), it.url()); Mono.just(it)
    }
}
