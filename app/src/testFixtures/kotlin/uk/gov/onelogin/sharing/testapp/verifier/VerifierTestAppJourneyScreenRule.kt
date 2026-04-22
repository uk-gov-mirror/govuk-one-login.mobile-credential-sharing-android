package uk.gov.onelogin.sharing.testapp.verifier

import androidx.compose.ui.semantics.Role
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.performClick
import uk.gov.android.ui.componentsv2.rules.ComposeContentTestRuleExtensions.onNodeWithRole

class VerifierTestAppJourneyScreenRule(
    composeTestRule: ComposeContentTestRule
) : ComposeContentTestRule by composeTestRule {
   private var hasClosedJourney: Boolean = false

    fun assertHasClosedJourney() = waitUntil { hasClosedJourney }

    fun performCloseJourneyClick() = onNodeWithRole(Role.Button).performClick()

    fun updateHasClosedJourney(hasClosedJourney: Boolean = true) {
        this.hasClosedJourney = hasClosedJourney
    }
}