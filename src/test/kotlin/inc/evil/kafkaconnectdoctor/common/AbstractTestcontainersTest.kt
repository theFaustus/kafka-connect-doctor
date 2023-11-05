package inc.evil.kafkaconnectdoctor.common

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Tag
import org.mockserver.client.MockServerClient
import org.testcontainers.containers.MockServerContainer
import org.testcontainers.utility.DockerImageName


@Tag("integration-test")
abstract class AbstractTestcontainersTest {

    companion object {

        var mockServer = MockServerContainer(
            DockerImageName
                .parse("mockserver/mockserver")
                .withTag("mockserver-" + MockServerClient::class.java.getPackage().implementationVersion)
        )

        lateinit var mockServerClient: MockServerClient

        @JvmStatic
        @BeforeAll
        internal fun setUp(): Unit {
            mockServer.start()
            mockServerClient = MockServerClient(mockServer.host, mockServer.serverPort)
        }
    }

}
