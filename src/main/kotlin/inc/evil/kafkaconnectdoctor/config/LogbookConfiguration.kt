package inc.evil.kafkaconnectdoctor.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.zalando.logbook.Logbook
import org.zalando.logbook.core.Conditions.exclude
import org.zalando.logbook.core.Conditions.requestTo
import org.zalando.logbook.core.HeaderFilters.authorization
import org.zalando.logbook.core.QueryFilters.replaceQuery

@Configuration
class LogbookConfiguration {
    @Bean
    fun logbook(): Logbook {
        return Logbook.builder()
            .condition(
                exclude(
                    requestTo("/health"),
                    requestTo("/admin/**")
                )
            )
            .headerFilter(authorization())
            .queryFilter(replaceQuery("password", "<secret>"))
            .build()
    }
}
