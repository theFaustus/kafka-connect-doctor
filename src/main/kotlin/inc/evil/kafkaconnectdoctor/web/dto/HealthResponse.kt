package inc.evil.kafkaconnectdoctor.web.dto

data class HealthResponse(
    val name: String,
    val connector: Connector,
    val tasks: List<Task> = listOf()
)
