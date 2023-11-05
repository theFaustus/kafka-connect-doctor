package inc.evil.kafkaconnectdoctor.common

import org.mockserver.client.MockServerClient
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import org.mockserver.model.Parameter
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.testcontainers.containers.MockServerContainer

fun MockServerClient.stubPath(
    requestPath: String,
    requestMethod: HttpMethod = HttpMethod.GET,
    responseBody: String,
    responseStatus: HttpStatus = HttpStatus.OK,
    queryParams: List<Parameter> = emptyList(),
    pathParams: List<Parameter> = emptyList()
) {
    `when`(
        HttpRequest.request()
            .withPath(requestPath)
            .withMethod(requestMethod.name())
            .also { queryParams.forEach { q -> it.withQueryStringParameter(q) } }
            .also { pathParams.forEach { p -> it.withPathParameter(p) } })
        .respond(HttpResponse.response().withStatusCode(responseStatus.value()).withBody(responseBody))
}
