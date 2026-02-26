package uk.gov.onelogin.sharing.orchestration.prerequisites

import uk.gov.onelogin.sharing.orchestration.prerequisites.authorization.AuthorizationRequest
import uk.gov.onelogin.sharing.orchestration.prerequisites.authorization.AuthorizationResponse
import uk.gov.onelogin.sharing.orchestration.prerequisites.capability.CapabilityRequest
import uk.gov.onelogin.sharing.orchestration.prerequisites.capability.CapabilityResponse
import uk.gov.onelogin.sharing.orchestration.prerequisites.readiness.ReadinessRequest
import uk.gov.onelogin.sharing.orchestration.prerequisites.readiness.ReadinessResponse

/**
 * Sealed interface that contains abstractions designed to verify a single aspect of the User's
 * device.
 *
 * Together, they form the basis of the default implementation for [PrerequisiteGate].
 *
 * @sample PrerequisiteGateImpl
 */
sealed interface PrerequisiteGateLayer {
    /**
     * Abstraction for authorizing the User. This covers things such as verifying that the device
     * has the correct permissions.
     */
    fun interface AuthorizationLayer : PrerequisiteGateLayer {
        /**
         * Validate the [request]ed capabilities are authorized.
         */
        fun checkAuthorization(request: AuthorizationRequest): AuthorizationResponse
    }

    /**
     * Abstraction for confirming the capabilities of the Android-powered device. This covers things
     * such as checking that applicable hardware is available on the device.
     */
    fun interface CapabilityLayer : PrerequisiteGateLayer {
        fun checkCapability(request: CapabilityRequest): CapabilityResponse
    }

    /**
     * Abstraction for confirming the readiness of the capabilities within the Android-powered
     * device. This covers things such as checking that Bluetooth is currently enabled.
     */
    fun interface ReadinessLayer : PrerequisiteGateLayer {
        fun checkReadiness(request: ReadinessRequest): ReadinessResponse
    }
}
