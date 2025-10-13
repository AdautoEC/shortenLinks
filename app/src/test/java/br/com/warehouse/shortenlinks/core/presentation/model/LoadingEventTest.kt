package br.com.warehouse.shortenlinks.core.presentation.model

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class LoadingEventTest {

    @Test
    fun `map should transform success data`() {
        val successEvent = LoadingEvent.Success("data")
        val mappedEvent = successEvent.map { it.length }
        assertTrue(mappedEvent is LoadingEvent.Success)
        assertEquals(4, (mappedEvent as LoadingEvent.Success).data)
    }

    @Test
    fun `map should not transform loading or error`() {
        val loadingEvent: LoadingEvent<String> = LoadingEvent.Loading
        val errorEvent: LoadingEvent<String> = LoadingEvent.Error(Throwable("error"))

        val mappedLoading = loadingEvent.map { it.length }
        val mappedError = errorEvent.map { it.length }

        assertTrue(mappedLoading is LoadingEvent.Loading)
        assertTrue(mappedError is LoadingEvent.Error)
    }

    @Test
    fun `mapSuccess should transform success data in a flow`() = runBlocking {
        val flow = flowOf(LoadingEvent.Success("test"))
        val mappedFlow = flow.mapSuccess { it.length }
        val result = mappedFlow.first()
        assertTrue(result is LoadingEvent.Success)
        assertEquals(4, (result as LoadingEvent.Success).data)
    }

    @Test
    fun `isLoading should return true only for Loading state`() {
        assertTrue(LoadingEvent.Loading.isLoading())
        assertFalse(LoadingEvent.Success("data").isLoading())
        assertFalse(LoadingEvent.Error(Throwable()).isLoading())
    }

    @Test
    fun `isSuccess should return true only for Success state`() {
        assertFalse(LoadingEvent.Loading.isSuccess())
        assertTrue(LoadingEvent.Success("data").isSuccess())
        assertFalse(LoadingEvent.Error(Throwable()).isSuccess())
    }

    @Test
    fun `isError should return true only for Error state`() {
        assertFalse(LoadingEvent.Loading.isError())
        assertFalse(LoadingEvent.Success("data").isError())
        assertTrue(LoadingEvent.Error(Throwable()).isError())
    }

    @Test
    fun `getSuccessDataOrNull should return data for Success and null otherwise`() {
        assertEquals("data", LoadingEvent.Success("data").getSuccessDataOrNull())
        assertNull(LoadingEvent.Loading.getSuccessDataOrNull())
        assertNull(LoadingEvent.Error(Throwable()).getSuccessDataOrNull())
    }

    @Test
    fun `getSuccessData should return data for Success`() {
        assertEquals("data", LoadingEvent.Success("data").getSuccessData())
    }

    @Test(expected = ClassCastException::class)
    fun `getSuccessData should throw exception for non-Success states`() {
        (LoadingEvent.Loading as LoadingEvent.Success<Any>).data
    }

    @Test
    fun `getErrorThrowableOrNull should return throwable for Error and null otherwise`() {
        val throwable = Throwable("error")
        assertEquals(throwable, LoadingEvent.Error(throwable).getErrorThrowableOrNull())
        assertNull(LoadingEvent.Loading.getErrorThrowableOrNull())
        assertNull(LoadingEvent.Success("data").getErrorThrowableOrNull())
    }
}
