package uk.gov.onelogin.sharing.orchestration.prerequisites

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import uk.gov.logging.testdouble.SystemLogger
import uk.gov.onelogin.sharing.orchestration.prerequisites.authorization.AuthorizationResponse
import uk.gov.onelogin.sharing.orchestration.prerequisites.authorization.FakePrerequisiteAuthorizationGateLayer
import uk.gov.onelogin.sharing.orchestration.prerequisites.authorization.UnauthorizedReason
import uk.gov.onelogin.sharing.orchestration.prerequisites.authorization.matchers.AuthorizationResponseMatchers.hasUnauthorizedReason
import uk.gov.onelogin.sharing.orchestration.prerequisites.authorization.matchers.UnauthorizedReasonMatchers.isMissingPermissionInstance
import uk.gov.onelogin.sharing.orchestration.prerequisites.capability.CapabilityResponse
import uk.gov.onelogin.sharing.orchestration.prerequisites.capability.CapabilityResponseMatchers.isIncapable
import uk.gov.onelogin.sharing.orchestration.prerequisites.capability.IncapableReason
import uk.gov.onelogin.sharing.orchestration.prerequisites.matchers.PrerequisiteResponseMatchers.hasAuthorizationResponse
import uk.gov.onelogin.sharing.orchestration.prerequisites.matchers.PrerequisiteResponseMatchers.hasCapabilityResponse
import uk.gov.onelogin.sharing.orchestration.prerequisites.matchers.PrerequisiteResponseMatchers.hasReadinessResponse
import uk.gov.onelogin.sharing.orchestration.prerequisites.readiness.ReadinessReason
import uk.gov.onelogin.sharing.orchestration.prerequisites.readiness.ReadinessResponse
import uk.gov.onelogin.sharing.orchestration.prerequisites.readiness.ReadinessResponseMatchers.isNotReady

class PrerequisiteGateImplTest {

    private val logger = SystemLogger()

    private var authorizationResponse: AuthorizationResponse = AuthorizationResponse.Authorized
    private var capabilityResponse: CapabilityResponse = CapabilityResponse.Capable
    private var readinessResponse: ReadinessResponse = ReadinessResponse.Ready

    private val authorization by lazy {
        FakePrerequisiteAuthorizationGateLayer(authorizationResponse)
    }
    private val capability by lazy {
        PrerequisiteGateLayer.CapabilityLayer {
            capabilityResponse
        }
    }
    private val readiness by lazy {
        PrerequisiteGateLayer.ReadinessLayer {
            readinessResponse
        }
    }

    private val gate by lazy {
        PrerequisiteGateImpl(
            authorizationLayer = authorization,
            capabilityLayer = capability,
            readinessLayer = readiness,
            logger = logger
        )
    }

    private val request = PrerequisiteRequest.holder(
        capabilities = listOf(),
        permissions = listOf()
    )

    private val logMessagePrefix = "Performed ${request.journey}"
    private val authorizationMessage = "$logMessagePrefix authorization check:"
    private val capabilityMessage = "$logMessagePrefix capability check:"
    private val readinessMessage = "$logMessagePrefix readiness check:"

    private val response by lazy {
        gate.checkPrerequisites(request)
    }

    @After
    fun verifyLogMessages() {
        listOf(
            "$authorizationMessage $authorizationResponse",
            "$capabilityMessage $capabilityResponse",
            "$readinessMessage $readinessResponse"
        ).forEach { expectedMessage ->
            assertTrue {
                logger.any {
                    it.message == expectedMessage
                }
            }
        }
    }

    @Test
    fun `Fully validates prerequisites`() = runTest {
        assertThat(
            response,
            allOf(
                hasAuthorizationResponse(),
                hasCapabilityResponse(),
                hasReadinessResponse()
            )
        )

        listOf(
            "Should pass authorization" to response::passesAuthorization,
            "Should pass capabilities" to response::passesCapabilities,
            "Should pass prerequisites" to response::passesPrerequisites,
            "Should pass readiness" to response::passesReadiness
        ).forEach { (description, actual) ->
            assertTrue(description, actual)
        }
    }

    @Test
    fun `Gate fails authorization check`() = runTest {
        authorizationResponse = AuthorizationResponse.Unauthorized(
            UnauthorizedReason.MissingPermissions(emptyList())
        )

        assertThat(
            response,
            hasAuthorizationResponse(
                hasUnauthorizedReason(
                    isMissingPermissionInstance()
                )
            )
        )

        listOf(
            "Should fail authorization" to response::passesAuthorization,
            "Should fail prerequisites" to response::passesPrerequisites
        ).forEach { (description, actual) ->
            assertFalse(description, actual)
        }
    }

    @Test
    fun `Gate fails capability check`() = runTest {
        capabilityResponse = CapabilityResponse.Incapable(
            IncapableReason.MissingHardware
        )

        assertThat(
            response,
            hasCapabilityResponse(
                isIncapable(
                    equalTo(IncapableReason.MissingHardware)
                )
            )
        )

        listOf(
            "Should fail capabilities" to response::passesCapabilities,
            "Should fail prerequisites" to response::passesPrerequisites
        ).forEach { (description, actual) ->
            assertFalse(description, actual)
        }
    }

    @Test
    fun `Gate fails readiness check`() = runTest {
        readinessResponse = ReadinessResponse.NotReady(
            ReadinessReason.HardwareTurnedOff
        )

        assertThat(
            response,
            hasReadinessResponse(
                isNotReady(
                    equalTo(ReadinessReason.HardwareTurnedOff)
                )
            )
        )

        listOf(
            "Should fail readiness" to response::passesReadiness,
            "Should fail prerequisites" to response::passesPrerequisites
        ).forEach { (description, actual) ->
            assertFalse(description, actual)
        }
    }
}
