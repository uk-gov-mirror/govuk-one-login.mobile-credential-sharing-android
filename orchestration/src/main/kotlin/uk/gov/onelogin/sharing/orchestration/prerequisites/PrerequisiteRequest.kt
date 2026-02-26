package uk.gov.onelogin.sharing.orchestration.prerequisites

import uk.gov.onelogin.orchestration.Orchestrator
import uk.gov.onelogin.sharing.orchestration.prerequisites.authorization.AuthorizationRequest
import uk.gov.onelogin.sharing.orchestration.prerequisites.capability.CapabilityRequest
import uk.gov.onelogin.sharing.orchestration.prerequisites.readiness.ReadinessRequest

class PrerequisiteRequest private constructor(
    val capabilities: List<Capability>,
    val journey: String,
    val permissions: List<String>
) {

    fun generateAuthorizationRequest(): AuthorizationRequest =
        AuthorizationRequest.AuthorizePermission(permissions)

    fun generateCapabilityRequest(): CapabilityRequest =
        CapabilityRequest(capabilities = capabilities)

    fun generateReadinessRequest(): ReadinessRequest = ReadinessRequest(capabilities = capabilities)

    companion object {
        @JvmStatic
        fun holder(capabilities: List<Capability>, permissions: List<String>): PrerequisiteRequest =
            PrerequisiteRequest(
                capabilities = capabilities,
                journey = Orchestrator.Holder.JOURNEY_NAME,
                permissions = permissions
            )

        @JvmStatic
        fun verifier(
            capabilities: List<Capability>,
            permissions: List<String>
        ): PrerequisiteRequest = PrerequisiteRequest(
            capabilities = capabilities,
            journey = Orchestrator.Verifier.JOURNEY_NAME,
            permissions = permissions
        )
    }
}
