package uk.gov.onelogin.sharing.orchestration.prerequisites.matchers

import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import uk.gov.onelogin.sharing.orchestration.prerequisites.PrerequisiteResponse
import uk.gov.onelogin.sharing.orchestration.prerequisites.readiness.ReadinessResponse

internal class HasReadinessResponse(private val matcher: Matcher<in ReadinessResponse>) :
    TypeSafeMatcher<PrerequisiteResponse>() {
    override fun describeTo(description: Description?) {
        matcher.describeTo(description)
    }

    override fun describeMismatchSafely(
        item: PrerequisiteResponse?,
        mismatchDescription: Description?
    ) {
        matcher.describeMismatch(item?.readinessResponse, mismatchDescription)
    }

    override fun matchesSafely(item: PrerequisiteResponse?): Boolean =
        matcher.matches(item?.readinessResponse)
}
