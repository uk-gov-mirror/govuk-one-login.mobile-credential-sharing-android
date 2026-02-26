package uk.gov.onelogin.sharing.orchestration.prerequisites.readiness

/**
 * Sealed class to represent different types of responses returned by the
 * [uk.gov.onelogin.sharing.orchestration.prerequisites.PrerequisiteGateLayer.AuthorizationLayer] interface.
 */
sealed class ReadinessResponse {
    /**
     * State for when the device passes the requested authorization.
     */
    data object Ready : ReadinessResponse()

    /**
     * State for when the device fails the requested authorization.
     */
    data class NotReady(val reason: ReadinessReason) : ReadinessResponse()
}
