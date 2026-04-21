package uk.gov.onelogin.sharing.testapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
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
import uk.gov.onelogin.sharing.testapp.TestAppNavigationExt.configureTestAppHomeScreen
import uk.gov.onelogin.sharing.testapp.TestAppNavigationExt.configureVerifierJourneyWrapper
import uk.gov.onelogin.sharing.testapp.TestAppNavigationExt.navigateToTestAppVerifierJourney
import uk.gov.onelogin.sharing.testapp.credential.MockCredentials
import uk.gov.onelogin.sharing.testapp.credential.SampleCredentialProvider
import uk.gov.onelogin.sharing.testapp.credential.attribute.select.SelectCredentialAttributesNavigationExt.configureVerifierAttributesSelection
import uk.gov.onelogin.sharing.testapp.credential.attribute.select.SelectCredentialAttributesNavigationExt.navigateToVerifierAttributesSelection
import uk.gov.onelogin.sharing.testapp.credential.select.SelectCredentialNavigationExt.configureSelectMockCredentialDialog
import uk.gov.onelogin.sharing.testapp.credential.select.SelectCredentialNavigationExt.navigateToHolderCredentialSelection
import uk.gov.onelogin.sharing.testapp.holder.HolderTestAppJourneyNavigationExt.configureHolderJourneyWrapper
import uk.gov.onelogin.sharing.testapp.holder.HolderTestAppJourneyNavigationExt.navigateToTestAppHolderJourney

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
                Column(modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxSize()
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = CredentialSharingDestination.Undetermined,
                        modifier = Modifier.safeContentPadding()
                    ) {
                        configureTestAppHomeScreen(
                            onStartHolderJourney = {
                                navController.navigateToHolderCredentialSelection()
                            },
                            onStartVerifierJourney = {
                                navController.navigateToVerifierAttributesSelection()
                            }
                        )
                        configureSelectMockCredentialDialog(
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
                                .presenter(
                                    SampleCredentialProvider(
                                        credential
                                    )
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
}
