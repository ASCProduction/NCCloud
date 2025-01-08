package tk.shkabaj.android.shkabaj.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.ParametersBuilder
import io.ktor.http.URLBuilder
import io.ktor.http.Url
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.http.parameters
import io.ktor.serialization.kotlinx.KotlinxSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent

class NetworkClient private constructor(
    val httpClient: HttpClient,
    private val baseUrl: String?,
    private val basePort: Int
) : KoinComponent {

    companion object {
        private val jsonSerializer = Json { ignoreUnknownKeys = true }

        fun getInstance(withBaseUrl: Boolean): NetworkClient {
            val httpClient = HttpClient {
                expectSuccess = true
                install(ContentNegotiation) {
                    register(ContentType.Text.Html, KotlinxSerializationConverter(Json))
                    json(jsonSerializer)
                }
            }
            return NetworkClient(
                httpClient = httpClient,
                baseUrl = if (withBaseUrl) NetworkConfig.BASE_URL else null,
                basePort = NetworkConfig.BASE_PORT
            )
        }
    }

    fun URLBuilder.setCommonURLBuilderPart(url: String, parameters: Parameters? = null) {
        val uri = Url(urlString = baseUrl ?: url)
        parameters?.entries()?.forEach { (key, value) -> this.parameters.appendAll(key, value) }
        protocol = uri.protocol
        host = uri.host
        port = basePort
        encodedPath = baseUrl?.let { url } ?: uri.encodedPath
    }

    suspend inline fun <reified T> postRequest(url: String, parameters: Any? = null): T {
        return httpClient.post {
            contentType(ContentType.Application.Json)
            url {
                setCommonURLBuilderPart(url)
                setBody(parameters)
            }
        }.body()
    }

    suspend inline fun <reified T> submitForm(url: String, params: Map<String, String>): T {
        return httpClient.submitForm(
            formParameters = parameters {
                params.forEach { append(it.key, it.value) }
            }
        ) {
            url { setCommonURLBuilderPart(url) }
        }.body()
    }

    suspend inline fun <reified T> getRequest(
        url: String,
        parametersBuilder: ParametersBuilder? = null
    ): T {
        return httpClient.get {
            url { setCommonURLBuilderPart(url, parametersBuilder?.build()) }
        }.body()
    }

    suspend inline fun <reified T> getCryptoRequest(
        url: String,
        parametersBuilder: ParametersBuilder? = null
    ): T {
        return httpClient.get {
            url {
                setCommonURLBuilderPart(url, parametersBuilder?.build())
            }
            header("accept", "application/json")
            header("x-cg-pro-api-key", "CG-zZXRmBW5AoXEeppUpCxh9d4y")
        }.body()
    }

}