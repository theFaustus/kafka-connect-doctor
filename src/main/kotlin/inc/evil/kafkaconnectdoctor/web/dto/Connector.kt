package inc.evil.kafkaconnectdoctor.web.dto

data class Connector(
    val state: State,
    val worker_id: String
)
