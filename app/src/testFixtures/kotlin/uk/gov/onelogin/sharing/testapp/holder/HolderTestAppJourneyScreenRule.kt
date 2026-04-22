package uk.gov.onelogin.sharing.testapp.holder

import androidx.compose.ui.semantics.Role
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.performClick
import uk.gov.android.ui.componentsv2.rules.ComposeContentTestRuleExtensions.onNodeWithRole
import uk.gov.onelogin.sharing.holder.prerequisites.HolderPrerequisitesScreenRule

class HolderTestAppJourneyScreenRule(
    composeTestRule: ComposeContentTestRule
) : ComposeContentTestRule by composeTestRule {
   private var hasClosedJourney: Boolean = false

    private val prerequisitesScreenRule = HolderPrerequisitesScreenRule(this)

    fun assertHasClosedJourney() = waitUntil { hasClosedJourney }

    fun assertPrerequisitesNotStartedTextIsDisplayed() =
        prerequisitesScreenRule.assertNotStartedTextIsDisplayed()

    fun performCloseJourneyClick() = onNodeWithRole(Role.Button).performClick()

    fun updateHasClosedJourney(hasClosedJourney: Boolean = true) {
        this.hasClosedJourney = hasClosedJourney
    }
}