package uk.gov.onelogin.sharing.testapp.credential.select

import androidx.compose.ui.semantics.Role
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import uk.gov.android.ui.componentsv2.rules.ComposeContentTestRuleExtensions.onAllNodesWithRole
import uk.gov.onelogin.sharing.testapp.credential.MockCredential

class SelectCredentialsScreenRule(
    composeTestRule: ComposeContentTestRule
) : ComposeContentTestRule by composeTestRule {
    private var selectedCredential: MockCredential? = null

    fun assertSelectableCredentialCount(expected: Int) = onAllNodesWithRole(Role.Button)
        .assertCountEquals(expected)

    fun assertSelectedCredentialEquals(expected: MockCredential) = waitUntil {
        expected == selectedCredential
    }

    fun performCredentialClick(credential: MockCredential) = onNodeWithText(
        credential.displayName,
        useUnmergedTree = true
    ).performClick()

    fun updateMockCredential(credential: MockCredential) {
        this.selectedCredential = credential
    }
}