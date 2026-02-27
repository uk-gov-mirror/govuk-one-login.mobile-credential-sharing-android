package uk.gov.onelogin.sharing.orchestration.prerequisites.readiness

import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat

class NoOpReadinessPrerequisiteGateTest {
    private val layer by lazy {
        NoOpReadinessPrerequisiteGate()
    }

    @Test
    fun `Only returns ready responses`() = runTest {
        assertThat(
            layer.checkReadiness(ReadinessRequest()),
            equalTo(ReadinessResponse.Ready)
        )
    }
}
