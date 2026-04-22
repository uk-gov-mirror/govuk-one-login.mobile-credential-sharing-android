package uk.gov.onelogin.sharing.testapp.credential.select

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import java.util.UUID
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.runner.RunWith
import uk.gov.onelogin.sharing.testapp.credential.MockCredentialData.mockCredential

@RunWith(AndroidJUnit4::class)
class SelectCredentialsScreenTest {

    @get:Rule
    val composeTestRule = SelectCredentialsScreenRule(createComposeRule())

    private val otherCredential = mockCredential.copy(
        id = UUID.randomUUID().toString(),
        displayName = "Unit test"
    )

    private val credentials = listOf(
        mockCredential,
        otherCredential
    )

    @Test
    fun `Tapped credentials are passed to the lambda`() = runTest {
        composeTestRule.run {
            setContent {
                SelectCredentialsScreen(
                    credentials = credentials,
                    onSelectCredential = composeTestRule::updateMockCredential
                )
            }

            assertSelectableCredentialCount(credentials.size)

            performCredentialClick(mockCredential)
            assertSelectedCredentialEquals(mockCredential)
        }
    }
}
