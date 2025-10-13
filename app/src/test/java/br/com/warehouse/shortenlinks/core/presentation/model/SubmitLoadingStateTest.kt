package br.com.warehouse.shortenlinks.core.presentation.model

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SubmitLoadingStateTest {

    @Test
    fun `aggregateEventsToSubmitLoadingState should map LoadingEvent to SubmitLoadingState`() = runBlocking {
        // Test Loading
        var event: LoadingEvent<String>? = LoadingEvent.Loading
        var flow = flowOf(event).aggregateEventsToSubmitLoadingState()
        assertTrue(flow.first() is SubmitLoadingState.Loading)

        // Test Success
        event = LoadingEvent.Success("data")
        flow = flowOf(event).aggregateEventsToSubmitLoadingState()
        val successState = flow.first()
        assertTrue(successState is SubmitLoadingState.Success)
        assertEquals("data", (successState as SubmitLoadingState.Success).data)

        // Test Error
        val error = Throwable("error")
        event = LoadingEvent.Error(error)
        flow = flowOf(event).aggregateEventsToSubmitLoadingState()
        val errorState = flow.first()
        assertTrue(errorState is SubmitLoadingState.Error)
        assertEquals(error, (errorState as SubmitLoadingState.Error).exception)
        
        // Test Null
        event = null
        flow = flowOf(event).aggregateEventsToSubmitLoadingState()
        assertTrue(flow.first() is SubmitLoadingState.Idle)
    }

    @Test
    fun `map should transform success data`() {
        val successState = SubmitLoadingState.Success("data")
        val mappedState = successState.map { it.length }
        assertTrue(mappedState is SubmitLoadingState.Success)
        assertEquals(4, (mappedState as SubmitLoadingState.Success).data)
    }

    @Test
    fun `map should not transform other states`() {
        val idleState: SubmitLoadingState<String> = SubmitLoadingState.Idle
        val loadingState: SubmitLoadingState<String> = SubmitLoadingState.Loading
        val errorState: SubmitLoadingState<String> = SubmitLoadingState.Error(Throwable("error"))

        assertTrue(idleState.map { it.length } is SubmitLoadingState.Idle)
        assertTrue(loadingState.map { it.length } is SubmitLoadingState.Loading)
        assertTrue(errorState.map { it.length } is SubmitLoadingState.Error)
    }

    @Test
    fun `mapSuccess should transform success data in a flow`() = runBlocking {
        val flow = flowOf(SubmitLoadingState.Success("test"))
        val mappedFlow = flow.mapSuccess { it.length }
        val result = mappedFlow.first()
        assertTrue(result is SubmitLoadingState.Success)
        assertEquals(4, (result as SubmitLoadingState.Success).data)
    }

    @Test
    fun `state checkers should return correct boolean`() {
        assertTrue(SubmitLoadingState.Loading.isLoading())
        assertFalse(SubmitLoadingState.Idle.isLoading())

        assertTrue(SubmitLoadingState.Success("data").isSuccess())
        assertFalse(SubmitLoadingState.Loading.isSuccess())

        assertTrue(SubmitLoadingState.Error(Throwable()).isError())
        assertFalse(SubmitLoadingState.Loading.isError())
    }

    @Test
    fun `getSuccessDataOrNull should return data for Success and null otherwise`() {
        assertEquals("data", SubmitLoadingState.Success("data").getSuccessDataOrNull())
        assertNull(SubmitLoadingState.Loading.getSuccessDataOrNull())
        assertNull(SubmitLoadingState.Idle.getSuccessDataOrNull())
        assertNull(SubmitLoadingState.Error(Throwable()).getSuccessDataOrNull())
    }

    @Test
    fun `getSuccessData should return data for Success`() {
        assertEquals("data", SubmitLoadingState.Success("data").getSuccessData())
    }

    @Test(expected = ClassCastException::class)
    fun `getSuccessData should throw exception for non-Success states`() {
        (SubmitLoadingState.Loading as SubmitLoadingState.Success<Any>).data
    }

    @Test
    fun `getErrorThrowableOrNull should return throwable for Error and null otherwise`() {
        val throwable = Throwable("error")
        assertEquals(throwable, SubmitLoadingState.Error(throwable).getErrorThrowableOrNull())
        assertNull(SubmitLoadingState.Loading.getErrorThrowableOrNull())
        assertNull(SubmitLoadingState.Success("data").getErrorThrowableOrNull())
        assertNull(SubmitLoadingState.Idle.getErrorThrowableOrNull())
    }
}
