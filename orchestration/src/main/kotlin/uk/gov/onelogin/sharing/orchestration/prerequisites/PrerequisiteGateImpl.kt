package uk.gov.onelogin.sharing.orchestration.prerequisites

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import uk.gov.logging.api.Logger
import uk.gov.onelogin.sharing.core.logger.logTag
import uk.gov.onelogin.sharing.orchestration.prerequisites.PrerequisiteGate.PrerequisiteGateMessages.completedAuthorizationCheck
import uk.gov.onelogin.sharing.orchestration.prerequisites.PrerequisiteGate.PrerequisiteGateMessages.completedCapabilityCheck
import uk.gov.onelogin.sharing.orchestration.prerequisites.PrerequisiteGate.PrerequisiteGateMessages.completedReadinessCheck

@ContributesBinding(AppScope::class)
class PrerequisiteGateImpl(
    private val authorizationLayer: PrerequisiteGateLayer.AuthorizationLayer,
    private val capabilityLayer: PrerequisiteGateLayer.CapabilityLayer,
    private val logger: Logger,
    private val readinessLayer: PrerequisiteGateLayer.ReadinessLayer
) : PrerequisiteGate {

    override fun checkPrerequisites(request: PrerequisiteRequest): PrerequisiteResponse {
        val authorizationResponse = authorizationLayer.checkAuthorization(
            request.generateAuthorizationRequest()
        ).also {
            logger.debug(
                logTag,
                completedAuthorizationCheck(
                    request.journey,
                    it
                )
            )
        }

        val capabilityResponse = capabilityLayer.checkCapability(
            request.generateCapabilityRequest()
        ).also {
            logger.debug(
                logTag,
                completedCapabilityCheck(
                    request.journey,
                    it
                )
            )
        }

        val readinessResponse = readinessLayer.checkReadiness(
            request.generateReadinessRequest()
        ).also {
            logger.debug(
                logTag,
                completedReadinessCheck(
                    request.journey,
                    it
                )
            )
        }

        return PrerequisiteResponse(
            authorizationResponse = authorizationResponse,
            capabilityResponse = capabilityResponse,
            readinessResponse = readinessResponse
        )
    }
}
