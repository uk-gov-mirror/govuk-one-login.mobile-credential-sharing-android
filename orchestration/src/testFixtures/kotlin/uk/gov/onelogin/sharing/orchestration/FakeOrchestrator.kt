package uk.gov.onelogin.sharing.orchestration

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import uk.gov.onelogin.orchestration.Orchestrator
import uk.gov.onelogin.sharing.orchestration.holder.session.HolderSessionState
import uk.gov.onelogin.sharing.orchestration.verifier.session.VerifierSessionState

class FakeOrchestrator(
    initialHolderState: MutableStateFlow<HolderSessionState> = MutableStateFlow(
        HolderSessionState.NotStarted
    ),
    initialVerifierState: MutableStateFlow<VerifierSessionState> = MutableStateFlow(
        VerifierSessionState.NotStarted
    )
) : Orchestrator.Holder,
    Orchestrator.Verifier {

    override val holderSessionState: SharedFlow<HolderSessionState> = initialHolderState
    override val verifierSessionState: SharedFlow<VerifierSessionState> = initialVerifierState

    var startCount = 0
    var cancelCount = 0

    override fun start() {
        startCount++
    }

    override fun cancel() {
        cancelCount++
    }

    override fun reset() {
        startCount = 0
        cancelCount = 0
    }
}
