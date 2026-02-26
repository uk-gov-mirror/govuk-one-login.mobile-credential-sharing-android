package uk.gov.onelogin.sharing.orchestration.prerequisites.readiness

import uk.gov.onelogin.sharing.orchestration.prerequisites.Capability

/**
 * Sealed class to represent different types of requests sent to the
 * [uk.gov.onelogin.sharing.orchestration.prerequisites.PrerequisiteGateLayer.AuthorizationLayer] interface.
 */
data class ReadinessRequest(val capabilities: List<Capability>) :
    Iterable<Capability> by capabilities {
    constructor(
        vararg capability: Capability
    ) : this(
        capability.toList()
    )
}
