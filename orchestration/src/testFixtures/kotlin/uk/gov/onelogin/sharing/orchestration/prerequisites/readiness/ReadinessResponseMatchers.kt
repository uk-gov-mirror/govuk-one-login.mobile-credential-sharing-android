package uk.gov.onelogin.sharing.orchestration.prerequisites.readiness

import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.Matcher

object ReadinessResponseMatchers {
    fun isReady(): Matcher<in ReadinessResponse> = instanceOf(
        ReadinessResponse.Ready::class.java
    )

    fun isNotReady(matcher: Matcher<in ReadinessReason>): Matcher<in ReadinessResponse> =
        IsNotReady(matcher)
}
