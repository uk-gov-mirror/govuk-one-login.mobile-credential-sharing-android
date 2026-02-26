package uk.gov.onelogin.sharing.orchestration.prerequisites.matchers

import org.hamcrest.Matcher
import uk.gov.onelogin.sharing.orchestration.prerequisites.PrerequisiteResponse
import uk.gov.onelogin.sharing.orchestration.prerequisites.authorization.AuthorizationResponse
import uk.gov.onelogin.sharing.orchestration.prerequisites.authorization.matchers.AuthorizationResponseMatchers
import uk.gov.onelogin.sharing.orchestration.prerequisites.capability.CapabilityResponse
import uk.gov.onelogin.sharing.orchestration.prerequisites.capability.CapabilityResponseMatchers
import uk.gov.onelogin.sharing.orchestration.prerequisites.readiness.ReadinessResponse
import uk.gov.onelogin.sharing.orchestration.prerequisites.readiness.ReadinessResponseMatchers

data object PrerequisiteResponseMatchers {

    fun hasAuthorizationResponse(
        matcher: Matcher<in AuthorizationResponse> = AuthorizationResponseMatchers.isAuthorized()
    ): Matcher<in PrerequisiteResponse> = HasAuthorizationResponse(matcher)

    fun hasCapabilityResponse(
        matcher: Matcher<in CapabilityResponse> = CapabilityResponseMatchers.isCapable()
    ): Matcher<in PrerequisiteResponse> = HasCapabilityResponse(matcher)

    fun hasReadinessResponse(
        matcher: Matcher<in ReadinessResponse> = ReadinessResponseMatchers.isReady()
    ): Matcher<in PrerequisiteResponse> = HasReadinessResponse(matcher)
}
