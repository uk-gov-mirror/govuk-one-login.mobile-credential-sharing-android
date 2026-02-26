package uk.gov.onelogin.sharing.orchestration.prerequisites.readiness

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import uk.gov.onelogin.sharing.orchestration.prerequisites.PrerequisiteGateLayer

@ContributesBinding(AppScope::class)
data object NoOpReadinessPrerequisiteGate : PrerequisiteGateLayer.ReadinessLayer {
    override fun checkReadiness(request: ReadinessRequest): ReadinessResponse =
        ReadinessResponse.Ready
}
