package inc.evil.kafkaconnectdoctor.web.dto

data class Task(
    val id: Int,
    val state: State,
    val worker_id: String,
    val trace: String? = null
)
