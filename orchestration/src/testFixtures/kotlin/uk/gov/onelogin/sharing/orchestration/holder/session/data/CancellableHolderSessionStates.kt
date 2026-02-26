package uk.gov.onelogin.sharing.orchestration.holder.session.data

import com.google.testing.junit.testparameterinjector.TestParameterValuesProvider
import uk.gov.onelogin.sharing.orchestration.holder.session.HolderSessionState

/**
 * Parameterised test input for valid [HolderSessionState] objects that can transition to
 * [HolderSessionState.Complete.Cancelled] within the
 * [uk.gov.onelogin.orchestration.HolderOrchestrator], as per the [uk.gov.onelogin.sharing.orchestration.holder.session.validHolderTransitions] [Map].
 */
class CancellableHolderSessionStates : TestParameterValuesProvider() {
    override fun provideValues(context: Context?): List<*>? = listOf(
        HolderSessionState.Preflight,
        HolderSessionState.ReadyToPresent,
        HolderSessionState.PresentingEngagement,
        HolderSessionState.Connecting,
        HolderSessionState.RequestReceived,
        HolderSessionState.ProcessingResponse
    )
}
