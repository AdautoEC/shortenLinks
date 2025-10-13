package br.com.warehouse.shortenlinks.features.shorten.data.repository

import br.com.warehouse.shortenlinks.features.shorten.data.service.ShortenService
import br.com.warehouse.shortenlinks.features.shorten.domain.repository.ShortenRepository
import com.google.gson.Gson
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.EOFException

class ShortenRepositoryImplTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var repository: ShortenRepository
    private lateinit var service: ShortenService

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
        service = retrofit.create(ShortenService::class.java)
        repository = ShortenRepositoryImpl(service)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `given a success response, when shortenUrl is called, then returns shorten response`() = runTest {
        // Arrange
        val responseJson = """{"alias":"alias123","_links":{"self":"https://very-long-url.com/some-path","short":"http://short.url/alias123"}} """
        mockWebServer.enqueue(MockResponse().setBody(responseJson).setResponseCode(201))
        val longUrl = "https://very-long-url.com/some-path"

        // Act
        val result = repository.shortenUrl(longUrl)

        // Assert
        assertEquals("alias123", result.alias)
        assertEquals("http://short.url/alias123", result.links.shortUrl)

        val request = mockWebServer.takeRequest()
        assertEquals("/api/alias", request.path)
        assertEquals("POST", request.method)
        assertEquals("{\"url\":\"https://very-long-url.com/some-path\"}", request.body.readUtf8())
    }

    @Test
    fun `given a server error response, when shortenUrl is called, then throws HttpException`() = runTest {
        // Arrange
        mockWebServer.enqueue(MockResponse().setResponseCode(500))
        val longUrl = "https://very-long-url.com/some-path"

        // Act
        try {
            repository.shortenUrl(longUrl)
            fail("Should have thrown HttpException")
        } catch (e: HttpException) {
            // Assert
            assertEquals(500, e.code())
        }
    }

    @Test
    fun `given a client error response, when shortenUrl is called, then throws HttpException`() = runTest {
        // Arrange
        mockWebServer.enqueue(MockResponse().setResponseCode(400))
        val longUrl = "invalid-url"

        // Act
        try {
            repository.shortenUrl(longUrl)
            fail("Should have thrown HttpException")
        } catch (e: HttpException) {
            // Assert
            assertEquals(400, e.code())
        }
    }

    @Test
    fun `given a malformed json response, when shortenUrl is called, then throws Exception`() = runTest {
        // Arrange
        val responseJson = "{\"alias\":\"alias123\",\"_links\":{\"self\""
        mockWebServer.enqueue(MockResponse().setBody(responseJson).setResponseCode(201))
        val longUrl = "https://very-long-url.com/some-path"

        // Act & Assert
        try {
            repository.shortenUrl(longUrl)
            fail("Should have thrown EOFException")
        } catch (_: EOFException) {
            // Success
        }
    }

    @Test
    fun `given an empty body response, when shortenUrl is called, then throws EOFException`() = runTest {
        // Arrange
        mockWebServer.enqueue(MockResponse().setResponseCode(201))
        val longUrl = "https://very-long-url.com/some-path"

        // Act & Assert
        try {
            repository.shortenUrl(longUrl)
            fail("Should have thrown EOFException")
        } catch (_: EOFException) {
            // Success
        }
    }

    @Test
    fun `given a valid alias, when getOriginalUrl is called, then returns original url`() = runTest {
        // Arrange
        val responseJson = """{"url": "https://the-original-url.com"}"""
        mockWebServer.enqueue(MockResponse().setBody(responseJson).setResponseCode(200))
        val alias = "my-alias"

        // Act
        val result = repository.getOriginalUrl(alias)

        // Assert
        assertEquals("https://the-original-url.com", result.url)

        val request = mockWebServer.takeRequest()
        assertEquals("/api/alias/my-alias", request.path)
        assertEquals("GET", request.method)
    }

    @Test
    fun `given a not found response, when getOriginalUrl is called, then throws HttpException`() = runTest {
        // Arrange
        mockWebServer.enqueue(MockResponse().setResponseCode(404))
        val alias = "non-existent-alias"

        // Act
        try {
            repository.getOriginalUrl(alias)
            fail("Should have thrown HttpException")
        } catch (e: HttpException) {
            // Assert
            assertEquals(404, e.code())
        }
    }

    @Test
    fun `given a server error response, when getOriginalUrl is called, then throws HttpException`() = runTest {
        // Arrange
        mockWebServer.enqueue(MockResponse().setResponseCode(500))
        val alias = "any-alias"

        // Act
        try {
            repository.getOriginalUrl(alias)
            fail("Should have thrown HttpException")
        } catch (e: HttpException) {
            // Assert
            assertEquals(500, e.code())
        }
    }
}