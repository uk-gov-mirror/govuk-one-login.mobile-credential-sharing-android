package uk.gov.onelogin.sharing.orchestration.prerequisites.capability

/**
 * Sealed class to represent different types of responses returned by the
 * [uk.gov.onelogin.sharing.orchestration.prerequisites.PrerequisiteGateLayer.AuthorizationLayer] interface.
 */
sealed class CapabilityResponse {
    /**
     * State for when the device passes the requested authorization.
     */
    data object Capable : CapabilityResponse()

    /**
     * State for when the device fails the requested authorization.
     */
    data class Incapable(val reason: IncapableReason) : CapabilityResponse()
}
