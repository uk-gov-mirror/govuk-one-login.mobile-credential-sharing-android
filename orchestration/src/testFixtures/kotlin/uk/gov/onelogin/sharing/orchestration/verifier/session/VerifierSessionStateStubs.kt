package uk.gov.onelogin.sharing.orchestration.verifier.session

import uk.gov.onelogin.sharing.orchestration.session.DeviceResponse
import uk.gov.onelogin.sharing.orchestration.session.SessionErrorStubs
import uk.gov.onelogin.sharing.orchestration.verifier.session.VerifierSessionState.Complete.Cancelled
import uk.gov.onelogin.sharing.orchestration.verifier.session.VerifierSessionState.Complete.Failed
import uk.gov.onelogin.sharing.orchestration.verifier.session.VerifierSessionState.Complete.Success

object VerifierSessionStateStubs {
    val userCancellation: VerifierSessionState = Cancelled
    val userJourneyFailure: VerifierSessionState = Failed(SessionErrorStubs.dummySessionError)
    val successStub: VerifierSessionState = Success(DeviceResponse)
}
