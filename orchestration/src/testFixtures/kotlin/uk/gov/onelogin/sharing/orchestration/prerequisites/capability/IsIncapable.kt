package uk.gov.onelogin.sharing.orchestration.prerequisites.capability

import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

internal class IsIncapable(private val matcher: Matcher<in IncapableReason>) :
    TypeSafeMatcher<CapabilityResponse>() {
    override fun describeTo(description: Description?) {
        matcher.describeTo(description)
    }

    override fun describeMismatchSafely(
        item: CapabilityResponse?,
        mismatchDescription: Description?
    ) {
        matcher.describeMismatch(
            (item as? CapabilityResponse.Incapable)?.reason,
            mismatchDescription
        )
    }

    override fun matchesSafely(item: CapabilityResponse?): Boolean =
        matcher.matches((item as? CapabilityResponse.Incapable)?.reason)
}
