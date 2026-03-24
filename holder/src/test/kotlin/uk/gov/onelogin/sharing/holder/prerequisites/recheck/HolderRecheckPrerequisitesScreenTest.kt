package uk.gov.onelogin.sharing.holder.prerequisites.recheck

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChild
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.espresso.intent.matcher.IntentMatchers.hasFlags
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.google.testing.junit.testparameterinjector.TestParameters
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestParameterInjector
import uk.gov.android.ui.theme.m3.GdsTheme
import uk.gov.onelogin.sharing.core.MainDispatcherRule
import uk.gov.onelogin.sharing.core.presentation.permissions.FakeMultiplePermissionsState
import uk.gov.onelogin.sharing.holder.R
import uk.gov.onelogin.sharing.orchestration.FakeOrchestrator
import uk.gov.onelogin.sharing.orchestration.Orchestrator
import uk.gov.onelogin.sharing.orchestration.holder.session.HolderSessionState
import uk.gov.onelogin.sharing.orchestration.prerequisites.Prerequisite
import uk.gov.onelogin.sharing.orchestration.prerequisites.PrerequisiteResponse

@OptIn(ExperimentalPermissionsApi::class)
@RunWith(RobolectricTestParameterInjector::class)
class HolderRecheckPrerequisitesScreenTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    @get:Rule
    val composeTestRule = HolderRecheckPrerequisitesScreenRule(createComposeRule())

    private val resources: Resources = ApplicationProvider.getApplicationContext<Context>()
        .resources

    private var holderSessionState = HolderSessionState.PresentingEngagement("")

    private val orchestrator by lazy {
        FakeOrchestrator(
            initialHolderState = MutableStateFlow(holderSessionState)
        )
    }

    private lateinit var viewModel: HolderRecheckPrerequisitesViewModel

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    @TestParameters(valuesProvider = RecheckPrerequisitesInitiallyDeniedPermissionParameters::class)
    fun `Permissions are initially denied`(
        missingPrerequisites: Map<Prerequisite, PrerequisiteResponse>,
        permissionStates: List<PermissionState>,
        getExpectedTitle: (Resources) -> String,
    ) = runTest(dispatcherRule.testDispatcher) {
        performJourney(
            missingPrerequisites = missingPrerequisites,
            permissionStates = permissionStates,
            getExpectedTitle = getExpectedTitle,
        ) { resources ->
            resources.getString(R.string.recheck_prerequisites_try_again)
        }

        assertTrue { composeTestRule.hasPresentedEngagement }
    }

    @Test
    @TestParameters(
        valuesProvider = RecheckPrerequisitesPermanentlyDeniedPermissionParameters::class
    )
    fun `Permanent permission denial opens the app settings`(
        missingPrerequisites: Map<Prerequisite, PrerequisiteResponse>,
        permissionStates: List<PermissionState>,
        getExpectedTitle: (Resources) -> String,
    ) = runTest(dispatcherRule.testDispatcher) {
        performJourney(
            missingPrerequisites = missingPrerequisites,
            permissionStates = permissionStates,
            getExpectedTitle = getExpectedTitle,
        ) { resources ->
            resources.getString(R.string.recheck_prerequisites_open_app_permissions)
        }

        composeTestRule.openAppSettingsIsIntended()
    }

    private fun performJourney(
        missingPrerequisites: Map<Prerequisite, PrerequisiteResponse>,
        permissionStates: List<PermissionState>,
        getExpectedTitle: (Resources) -> String,
        getPrimaryButtonText: (Resources) -> String,
    ) = composeTestRule.run {
        setContent {
            Render(
                missingPrerequisites = missingPrerequisites,
                multiplePermissionsState = FakeMultiplePermissionsState(
                    permissionStates
                ) {
                    viewModel.checkPrerequisites()
                }
            )
        }
        assertTitleHasText(getExpectedTitle(resources))
        assertPrimaryButtonHasText(getPrimaryButtonText(resources))

        performPrimaryButtonClick()

        waitForIdle()
    }

    private fun createViewModel(
        orchestrator: Orchestrator.Holder = this.orchestrator,
        dispatcher: CoroutineDispatcher = dispatcherRule.testDispatcher,
    ) = HolderRecheckPrerequisitesViewModel(
        dispatcher = dispatcher,
        orchestrator = orchestrator
    )

    @Composable
    fun Render(
        missingPrerequisites: Map<Prerequisite, PrerequisiteResponse>,
        multiplePermissionsState: MultiplePermissionsState,
    ) {
        viewModel = createViewModel()

        GdsTheme {
            HolderRecheckPrerequisitesScreen(
                missingPrerequisites = missingPrerequisites,
                multiplePermissionsState = multiplePermissionsState,
                viewModel = viewModel,
                onHandlePreflight = {
                    composeTestRule.hasHandledPreflight = true
                },
                onPresentEngagement = {
                    composeTestRule.hasPresentedEngagement = true
                }
            )
        }
    }
}

class HolderRecheckPrerequisitesScreenRule(
    composeTestRule: ComposeContentTestRule,
) : ComposeContentTestRule by composeTestRule {

    var hasHandledPreflight: Boolean = false
    var hasPresentedEngagement: Boolean = false

    /**
     * Ensure that the test class' [Before] function calls [Intents.init] and and [After] function
     * calls [Intents.release] before invoking this function.
     *
     * @param packageData The [String] within the [Intent.getData] property. Defaults to
     * `package:uk.gov.onelogin.sharing.holder.test`. Change this value when using outside of the
     * `holder` gradle module.
     */
    fun openAppSettingsIsIntended(
        packageData: String = "package:uk.gov.onelogin.sharing.holder.test",
    ) = intended(
        allOf(
            hasAction("android.settings.APPLICATION_DETAILS_SETTINGS"),
            hasData(packageData),
            hasFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    )

    fun assertTitleHasText(expected: String): SemanticsNodeInteraction = onTitleNode()
        .assertTextEquals(expected)

    fun assertPrimaryButtonHasText(expected: String) = onPrimaryButtonNode()
        .onChild()
        .assertTextEquals(expected)

    fun onTitleNode(): SemanticsNodeInteraction = onNodeWithTag("title", useUnmergedTree = true)
        .assertExists("Cannot find title!")

    fun onPrimaryButtonNode() = onNodeWithTag("primaryButton", useUnmergedTree = true)
        .assertExists("Cannot find primary button")

    fun performPrimaryButtonClick() = onPrimaryButtonNode()
        .performClick()
}