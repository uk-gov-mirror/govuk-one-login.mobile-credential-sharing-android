package uk.gov.onelogin.sharing.orchestration.prerequisites.capability

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import uk.gov.onelogin.sharing.orchestration.prerequisites.PrerequisiteGateLayer

@ContributesBinding(AppScope::class)
data object NoOpCapabilityPrerequisiteGate : PrerequisiteGateLayer.CapabilityLayer {
    override fun checkCapability(request: CapabilityRequest): CapabilityResponse =
        CapabilityResponse.Capable
}
