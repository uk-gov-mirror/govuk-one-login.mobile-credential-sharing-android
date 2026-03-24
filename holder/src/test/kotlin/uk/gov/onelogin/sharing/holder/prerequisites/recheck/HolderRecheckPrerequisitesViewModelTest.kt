package uk.gov.onelogin.sharing.holder.prerequisites.recheck

import kotlin.test.Test
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import uk.gov.onelogin.sharing.core.MainDispatcherRule
import uk.gov.onelogin.sharing.orchestration.FakeOrchestrator
import uk.gov.onelogin.sharing.orchestration.holder.session.HolderSessionState

class HolderRecheckPrerequisitesViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val sessionState = MutableStateFlow<HolderSessionState>(
        HolderSessionState.Preflight(mapOf())
    )

    private val orchestrator by lazy {
        FakeOrchestrator(
            initialHolderState = sessionState,
        )
    }

    private val model by lazy {
        HolderRecheckPrerequisitesViewModel(
            dispatcher = dispatcherRule.testDispatcher,
            orchestrator = orchestrator,
        )
    }

    @Test
    fun `Rechecking prerequisites defers to orchestrator's latest session state`() = runTest(
        dispatcherRule.testDispatcher
    ) {
        model.checkPrerequisites()

        assertThat(
            model.holderUpdatedState.value,
            equalTo(orchestrator.holderSessionState.value)
        )
    }

    @Test
    fun `Resetting the ViewModel doesn't update the orchestrator`() = runTest(
        dispatcherRule.testDispatcher
    ) {
        `Rechecking prerequisites defers to orchestrator's latest session state`()

        model.clearState()

        assertThat(
            model.holderUpdatedState.value,
            not(equalTo(orchestrator.holderSessionState.value))
        )
    }
}
