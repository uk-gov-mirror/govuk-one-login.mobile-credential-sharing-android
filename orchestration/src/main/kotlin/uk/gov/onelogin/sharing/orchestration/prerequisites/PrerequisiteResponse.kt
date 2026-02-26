package uk.gov.onelogin.sharing.orchestration.prerequisites

import uk.gov.onelogin.sharing.orchestration.prerequisites.authorization.AuthorizationResponse
import uk.gov.onelogin.sharing.orchestration.prerequisites.capability.CapabilityResponse
import uk.gov.onelogin.sharing.orchestration.prerequisites.readiness.ReadinessResponse

data class PrerequisiteResponse(
    val authorizationResponse: AuthorizationResponse,
    val capabilityResponse: CapabilityResponse,
    val readinessResponse: ReadinessResponse
) {
    fun passesAuthorization(): Boolean = authorizationResponse == AuthorizationResponse.Authorized

    fun passesCapabilities(): Boolean = capabilityResponse == CapabilityResponse.Capable

    fun passesReadiness(): Boolean = readinessResponse == ReadinessResponse.Ready

    fun passesPrerequisites(): Boolean =
        passesAuthorization() && passesCapabilities() && passesReadiness()
}
