package uk.gov.onelogin.sharing.orchestration.prerequisites.readiness

/**
 * State table for the different reasons for a device being incapable of completing a User journey.
 */
sealed class ReadinessReason {
    /**
     * State for when the Android-powered device doesn't have the necessary hardware.
     */
    data object HardwareTurnedOff : ReadinessReason()
}
