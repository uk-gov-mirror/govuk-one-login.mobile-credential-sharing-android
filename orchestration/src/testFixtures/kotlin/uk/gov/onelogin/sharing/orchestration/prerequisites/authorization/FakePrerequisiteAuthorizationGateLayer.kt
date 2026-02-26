package uk.gov.onelogin.sharing.orchestration.prerequisites.authorization

import uk.gov.onelogin.sharing.orchestration.prerequisites.PrerequisiteGateLayer

data class FakePrerequisiteAuthorizationGateLayer(private val result: AuthorizationResponse) :
    PrerequisiteGateLayer.AuthorizationLayer {
    override fun checkAuthorization(request: AuthorizationRequest): AuthorizationResponse = result
}
