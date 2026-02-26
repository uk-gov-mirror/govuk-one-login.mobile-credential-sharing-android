package uk.gov.onelogin.sharing.orchestration.prerequisites.readiness

import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

internal class IsNotReady(private val matcher: Matcher<in ReadinessReason>) :
    TypeSafeMatcher<ReadinessResponse>() {
    override fun describeTo(description: Description?) {
        matcher.describeTo(description)
    }

    override fun describeMismatchSafely(
        item: ReadinessResponse?,
        mismatchDescription: Description?
    ) {
        matcher.describeMismatch(
            (item as? ReadinessResponse.NotReady)?.reason,
            mismatchDescription
        )
    }

    override fun matchesSafely(item: ReadinessResponse?): Boolean =
        matcher.matches((item as? ReadinessResponse.NotReady)?.reason)
}
