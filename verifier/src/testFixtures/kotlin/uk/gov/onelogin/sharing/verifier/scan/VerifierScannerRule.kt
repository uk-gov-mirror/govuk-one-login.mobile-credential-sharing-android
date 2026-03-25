package uk.gov.onelogin.sharing.verifier.scan

import android.content.Context
import android.content.res.Resources
import android.net.Uri
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import uk.gov.android.ui.componentsv2.matchers.SemanticsMatchers.hasRole
import uk.gov.onelogin.sharing.core.presentation.ButtonTestTags.PERMISSION_PERMANENT_DENIAL_BUTTON
import uk.gov.onelogin.sharing.core.presentation.ButtonTestTags.PERMISSION_RATIONALE_BUTTON
import uk.gov.onelogin.sharing.core.presentation.ButtonTestTags.PERMISSION_REQUIRED_BUTTON
import uk.gov.onelogin.sharing.verifier.R
import uk.gov.onelogin.sharing.verifier.scan.BarcodeAnalysisUrlContractAssertions.hasState

/**
 * JUnit 4 Rule for encapsulating assertion / performance behaviour for the [VerifierScanner] UI
 * composable.
 */
class VerifierScannerRule(
    composeTestRule: ComposeContentTestRule,
    private val permissionDeniedText: String
) : ComposeContentTestRule by composeTestRule {

    /**
     * Convenience constructor that extracts [String] values via the provided [resources] parameter.
     */
    constructor(
        composeTestRule: ComposeContentTestRule,
        resources: Resources = ApplicationProvider.getApplicationContext<Context>().resources
    ) : this(
        composeTestRule = composeTestRule,
        permissionDeniedText = resources.getString(R.string.enable_camera_permission_to_continue)
    )

    fun assertCameraViewfinderIsDisplayed() = onCameraViewfinder().assertIsDisplayed()

    /**
     * Requires the use of [Intents.init] and [Intents.release] as part of test classes / functions
     * that utilise this function.
     */
    fun assertIntentLaunched(expected: Uri) {
        intended(hasState(expected))
    }

    fun assertPermissionPermanentlyDeniedButtonIsDisplayed() =
        onPermissionPermanentlyDeniedButton().assertIsDisplayed()

    fun assertPermissionDeniedButtonIsDisplayed() = onPermissionDeniedButton().assertIsDisplayed()

    fun assertPermissionRationaleButtonIsDisplayed() =
        onPermissionRationaleButton().assertIsDisplayed()

    fun assertPermissionDeniedTextIsDisplayed() = onPermissionDeniedText().assertIsDisplayed()

    fun onCameraViewfinder() = onNodeWithTag("cameraViewfinder").assertExists()

    fun onPermissionPermanentlyDeniedButton() = onNodeWithTag(
        PERMISSION_PERMANENT_DENIAL_BUTTON
    ).assertExists()
        .assert(hasRole(Role.Button))
        .assertHasClickAction()

    fun onPermissionDeniedButton() = onNodeWithTag(
        PERMISSION_REQUIRED_BUTTON
    ).assertExists()
        .assert(hasRole(Role.Button))
        .assertHasClickAction()

    fun onPermissionRationaleButton() = onNodeWithTag(
        PERMISSION_RATIONALE_BUTTON
    ).assertExists()
        .assert(hasRole(Role.Button))
        .assertHasClickAction()

    fun onPermissionDeniedText() = onNodeWithText(permissionDeniedText).assertIsDisplayed()

    fun performPermissionDeniedClick() = onPermissionPermanentlyDeniedButton().performClick()

    /**
     * Due to issues with the metro dependency injection framework's compiler, don't use this
     * in android instrumentation tests.
     */
    fun render(onInvalidBarcode: (String) -> Unit = {}, onValidBarcode: () -> Unit = {}) {
        setContent {
            VerifierScanner(
                onInvalidBarcode = onInvalidBarcode,
                onValidBarcode = onValidBarcode
            ) {}
        }
    }
}
