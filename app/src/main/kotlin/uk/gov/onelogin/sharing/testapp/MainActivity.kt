package uk.gov.onelogin.sharing.testapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import uk.gov.android.ui.theme.m3.GdsTheme
import uk.gov.logging.api.BuildConfig
import uk.gov.onelogin.sharing.orchestration.verificationrequest.DocumentType
import uk.gov.onelogin.sharing.orchestration.verificationrequest.VerificationRequest
import uk.gov.onelogin.sharing.orchestration.verificationrequest.VerifierConfig
import uk.gov.onelogin.sharing.sdk.api.presenter.PresentCredentialSdk
import uk.gov.onelogin.sharing.sdk.api.verifier.VerifyCredentialSdk
import uk.gov.onelogin.sharing.testapp.TestAppNavigationExt.configureHolderJourneyWrapper
import uk.gov.onelogin.sharing.testapp.TestAppNavigationExt.configureSelectMockCredential
import uk.gov.onelogin.sharing.testapp.TestAppNavigationExt.configureTestAppHomeScreen
import uk.gov.onelogin.sharing.testapp.TestAppNavigationExt.configureVerifierAttributesSelection
import uk.gov.onelogin.sharing.testapp.TestAppNavigationExt.configureVerifierJourneyWrapper
import uk.gov.onelogin.sharing.testapp.TestAppNavigationExt.navigateToHolderCredentialSelection
import uk.gov.onelogin.sharing.testapp.TestAppNavigationExt.navigateToTestAppHolderJourney
import uk.gov.onelogin.sharing.testapp.TestAppNavigationExt.navigateToTestAppVerifierJourney
import uk.gov.onelogin.sharing.testapp.TestAppNavigationExt.navigateToVerifierAttributesSelection

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var presentCredentialSdk: PresentCredentialSdk

    @Inject
    lateinit var verifyCredentialSdk: VerifyCredentialSdk

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val mockCredentials = MockCredentials.getMockCredentials(this)

        if (BuildConfig.DEBUG) {
            Log.d(
                "Mock Credential",
                mockCredentials.toString()
            )
        }

        setContent {
            val navController = rememberNavController()

            GdsTheme {
                NavHost(
                    navController = navController,
                    startDestination = CredentialSharingDestination.Undetermined,
                ) {
                    configureTestAppHomeScreen(
                        onStartHolderJourney = {
                            navController.navigateToHolderCredentialSelection()
                        },
                        onStartVerifierJourney = {
                            navController.navigateToVerifierAttributesSelection()
                        }
                    )
                    configureSelectMockCredential(
                        mockCredentials = mockCredentials
                    ) { selectedCredential ->
                        navController.navigateToTestAppHolderJourney(selectedCredential) {
                            popUpTo<CredentialSharingDestination.Undetermined> {
                                inclusive = false
                            }
                        }
                    }
                    configureHolderJourneyWrapper(navController) { credential ->
                        presentCredentialSdk
                            .presenter(SampleCredentialProvider(
                                credential)
                            )
                    }
                    configureVerifierAttributesSelection { attributeGroup ->
                        navController.navigateToTestAppVerifierJourney(
                            VerificationRequest.typed(
                                DocumentType.Mdl,
                                attributeGroup = attributeGroup
                            )
                        ) {
                            popUpTo<CredentialSharingDestination.Undetermined> {
                                inclusive = false
                            }
                        }
                    }
                    configureVerifierJourneyWrapper(navController) { verificationRequest ->
                        verifyCredentialSdk.verifier(
                            VerifierConfig(
                                verificationRequest = verificationRequest,
                                trustedCertificates = emptyList()
                            )
                        )
                    }
                }
            }
        }
    }
}
