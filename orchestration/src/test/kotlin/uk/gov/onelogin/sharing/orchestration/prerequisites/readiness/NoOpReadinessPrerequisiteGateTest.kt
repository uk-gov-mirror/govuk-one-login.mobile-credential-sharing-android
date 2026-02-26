package uk.gov.onelogin.sharing.orchestration.prerequisites.readiness

import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert

class NoOpReadinessPrerequisiteGateTest {
    private val layer by lazy {
        NoOpReadinessPrerequisiteGate
    }

    @Test
    fun `Only returns ready responses`() = runTest {
        MatcherAssert.assertThat(
            layer.checkReadiness(ReadinessRequest(emptyList())),
            equalTo(ReadinessResponse.Ready)
        )
    }
}
