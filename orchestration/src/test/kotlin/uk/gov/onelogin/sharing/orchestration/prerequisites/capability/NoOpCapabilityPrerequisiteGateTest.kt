package uk.gov.onelogin.sharing.orchestration.prerequisites.capability

import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat

class NoOpCapabilityPrerequisiteGateTest {

    private val layer by lazy {
        NoOpCapabilityPrerequisiteGate()
    }

    @Test
    fun `Only returns capable responses`() = runTest {
        assertThat(
            layer.checkCapability(CapabilityRequest()),
            equalTo(CapabilityResponse.Capable)
        )
    }
}
