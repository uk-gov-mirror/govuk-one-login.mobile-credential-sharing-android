package uk.gov.onelogin.sharing.orchestration.prerequisites.capability

import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.Matcher

object CapabilityResponseMatchers {
    fun isCapable(): Matcher<in CapabilityResponse> = instanceOf(
        CapabilityResponse.Capable::class.java
    )

    fun isIncapable(matcher: Matcher<in IncapableReason>): Matcher<in CapabilityResponse> =
        IsIncapable(matcher)
}
