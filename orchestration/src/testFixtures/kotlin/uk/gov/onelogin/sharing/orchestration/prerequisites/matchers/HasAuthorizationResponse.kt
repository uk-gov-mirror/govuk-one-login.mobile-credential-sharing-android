package uk.gov.onelogin.sharing.orchestration.prerequisites.matchers

import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import uk.gov.onelogin.sharing.orchestration.prerequisites.PrerequisiteResponse
import uk.gov.onelogin.sharing.orchestration.prerequisites.authorization.AuthorizationResponse

internal class HasAuthorizationResponse(private val matcher: Matcher<in AuthorizationResponse>) :
    TypeSafeMatcher<PrerequisiteResponse>() {
    override fun describeTo(description: Description?) {
        matcher.describeTo(description)
    }

    override fun describeMismatchSafely(
        item: PrerequisiteResponse?,
        mismatchDescription: Description?
    ) {
        matcher.describeMismatch(item?.authorizationResponse, mismatchDescription)
    }

    override fun matchesSafely(item: PrerequisiteResponse?): Boolean =
        matcher.matches(item?.authorizationResponse)
}
