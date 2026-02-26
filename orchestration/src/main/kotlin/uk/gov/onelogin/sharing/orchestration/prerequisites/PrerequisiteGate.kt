package uk.gov.onelogin.sharing.orchestration.prerequisites

import uk.gov.onelogin.sharing.orchestration.prerequisites.authorization.AuthorizationResponse
import uk.gov.onelogin.sharing.orchestration.prerequisites.capability.CapabilityResponse
import uk.gov.onelogin.sharing.orchestration.prerequisites.readiness.ReadinessResponse

fun interface PrerequisiteGate {
    fun checkPrerequisites(request: PrerequisiteRequest): PrerequisiteResponse

    data object PrerequisiteGateMessages {
        fun completedAuthorizationCheck(journey: String, response: AuthorizationResponse): String =
            "Performed $journey authorization check: $response"

        fun completedCapabilityCheck(journey: String, response: CapabilityResponse): String =
            "Performed $journey capability check: $response"

        fun completedReadinessCheck(journey: String, response: ReadinessResponse): String =
            "Performed $journey readiness check: $response"
    }
}
