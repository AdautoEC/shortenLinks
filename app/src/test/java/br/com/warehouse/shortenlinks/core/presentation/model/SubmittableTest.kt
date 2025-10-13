package br.com.warehouse.shortenlinks.core.presentation.model

import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SubmittableTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @Test
    fun `given autoSubmit is false, when collected, then emits initial value`() = runTest(testDispatcher) {
        val submittable = SubmittableImpl<Unit, String>(
            params = flowOf(Unit),
            scope = backgroundScope,
            started = SharingStarted.Eagerly,
            autoSubmit = false,
            initialValue = SubmitLoadingState.Idle,
            createSubmitEvents = { flowOf(LoadingEvent.Success("data")) }
        )

        submittable.flow.test {
            assertEquals(SubmitLoadingState.Idle, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given autoSubmit is true, when collected, then emits success`() = runTest(testDispatcher) {
        val submittable = SubmittableImpl<Unit, String>(
            params = flowOf(Unit),
            scope = backgroundScope,
            started = SharingStarted.Eagerly,
            autoSubmit = true,
            initialValue = SubmitLoadingState.Idle,
            createSubmitEvents = { flowOf(LoadingEvent.Success("auto-submitted")) }
        )

        submittable.flow.test {
            assertEquals(SubmitLoadingState.Success("auto-submitted"), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given autoSubmit is false, when submit is called, then emits success`() = runTest(testDispatcher) {
        val submittable = SubmittableImpl<Unit, String>(
            params = flowOf(Unit),
            scope = backgroundScope,
            started = SharingStarted.Eagerly,
            autoSubmit = false,
            initialValue = SubmitLoadingState.Idle,
            createSubmitEvents = { flowOf(LoadingEvent.Success("submitted")) }
        )

        submittable.flow.test {
            assertEquals(SubmitLoadingState.Idle, awaitItem())

            submittable.submit()
            advanceUntilIdle()

            assertEquals(SubmitLoadingState.Success("submitted"), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given flow emits error, when submit is called, then emits error`() = runTest(testDispatcher) {
        val error = Throwable("Network Error")
        val submittable = SubmittableImpl<Unit, String>(
            params = flowOf(Unit),
            scope = backgroundScope,
            started = SharingStarted.Eagerly,
            autoSubmit = false,
            initialValue = SubmitLoadingState.Idle,
            createSubmitEvents = { flow { emit(LoadingEvent.Error(error)) } }
        )

        submittable.flow.test {
            assertEquals(SubmitLoadingState.Idle, awaitItem())

            submittable.submit()
            advanceUntilIdle()

            val resultingState = awaitItem()
            assert(resultingState is SubmitLoadingState.Error)
            assertEquals(error, (resultingState as SubmitLoadingState.Error).exception)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given success state, when reset is called, then emits idle`() = runTest(testDispatcher) {
        val submittable = SubmittableImpl<Unit, String>(
            params = flowOf(Unit),
            scope = backgroundScope,
            started = SharingStarted.Eagerly,
            autoSubmit = false,
            initialValue = SubmitLoadingState.Idle,
            createSubmitEvents = { flowOf(LoadingEvent.Success("data")) }
        )

        submittable.flow.test {
            assertEquals(SubmitLoadingState.Idle, awaitItem())

            submittable.submit()
            advanceUntilIdle()
            assertEquals(SubmitLoadingState.Success("data"), awaitItem())

            submittable.reset()
            advanceUntilIdle()
            assertEquals(SubmitLoadingState.Idle, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given new param, when submitted, then resets and emits new success`() = runTest(testDispatcher) {
        val paramsFlow = MutableStateFlow("param1")
        val submittable = SubmittableImpl<String, String>(
            params = paramsFlow,
            scope = backgroundScope,
            started = SharingStarted.Eagerly,
            autoSubmit = false,
            initialValue = SubmitLoadingState.Idle,
            createSubmitEvents = { param -> flowOf(LoadingEvent.Success("data for $param")) }
        )

        submittable.flow.test {
            assertEquals(SubmitLoadingState.Idle, awaitItem())

            submittable.submit()
            advanceUntilIdle()
            assertEquals(SubmitLoadingState.Success("data for param1"), awaitItem())

            paramsFlow.value = "param2"
            advanceUntilIdle()
            assertEquals(SubmitLoadingState.Idle, awaitItem())

            submittable.submit()
            advanceUntilIdle()
            assertEquals(SubmitLoadingState.Success("data for param2"), awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }
}