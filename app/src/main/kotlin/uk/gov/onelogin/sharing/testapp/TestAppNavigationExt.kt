package uk.gov.onelogin.sharing.testapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlin.reflect.typeOf
import uk.gov.onelogin.sharing.orchestration.verificationrequest.VerificationRequest
import uk.gov.onelogin.sharing.orchestration.verificationrequest.VerificationRequest.Companion.VerificationRequestType
import uk.gov.onelogin.sharing.sdk.api.presenter.CredentialPresenter
import uk.gov.onelogin.sharing.sdk.api.verifier.CredentialVerifier
import uk.gov.onelogin.sharing.testapp.credential.MockCredential
import uk.gov.onelogin.sharing.testapp.credential.MockCredential.Companion.MockCredentialType
import uk.gov.onelogin.sharing.ui.impl.ShareCredential
import uk.gov.onelogin.sharing.ui.impl.VerifyCredential

object TestAppNavigationExt {
    fun NavController.navigateToTestAppHolderJourney(
        credential: MockCredential,
        options: NavOptionsBuilder.() -> Unit = {}
    ) = navigate(
        CredentialSharingDestination.Holder(credential = credential),
        options
    )

    internal fun NavGraphBuilder.configureHolderJourneyWrapper(
        navController: NavController,
        component: (MockCredential) -> CredentialPresenter
    ) {
        composable<CredentialSharingDestination.Holder>(
            typeMap = mapOf(
                typeOf<MockCredential>() to MockCredentialType
            )
        ) { navBackStackEntry ->
            val arguments: CredentialSharingDestination.Holder = navBackStackEntry.toRoute()
            val presenter = remember { component(arguments.credential) }

            ShareCredential(
                component = presenter,
                modifier = Modifier.fillMaxSize()
            )

            Box {
                IconButton(
                    modifier = Modifier.align(Alignment.TopStart),
                    onClick = {
                        presenter.orchestrator.cancel()
                        navController.popBackStack()
                    }
                ) {
                    Icon(
                        painter = painterResource(
                            android.R.drawable.ic_menu_close_clear_cancel
                        ),
                        contentDescription = "Close"
                    )
                }
            }
        }
    }

    fun NavController.navigateToTestAppVerifierJourney(
        request: VerificationRequest,
        options: NavOptionsBuilder.() -> Unit = {}
    ) = navigate(
        CredentialSharingDestination.Verifier(request = request),
        options
    )

    internal fun NavGraphBuilder.configureVerifierJourneyWrapper(
        navController: NavController,
        requestToVerifier: (VerificationRequest) -> CredentialVerifier
    ) {
        composable<CredentialSharingDestination.Verifier>(
            typeMap = mapOf(
                typeOf<VerificationRequest>() to VerificationRequestType
            )
        ) { navBackStackEntry ->
            val arguments: CredentialSharingDestination.Verifier = navBackStackEntry.toRoute()
            val verifier = remember { requestToVerifier(arguments.request) }

            VerifyCredential(
                component = verifier,
                modifier = Modifier.fillMaxSize()
            )

            Box {
                IconButton(
                    modifier = Modifier.align(Alignment.TopStart),
                    onClick = {
                        verifier.orchestrator.cancel()
                        navController.popBackStack()
                    }
                ) {
                    Icon(
                        painter = painterResource(
                            android.R.drawable.ic_menu_close_clear_cancel
                        ),
                        contentDescription = "Close"
                    )
                }
            }
        }
    }

    internal fun NavGraphBuilder.configureTestAppHomeScreen(
        onStartHolderJourney: () -> Unit = {},
        onStartVerifierJourney: () -> Unit = {},
    ) {
        composable<CredentialSharingDestination.Undetermined> {
            TestAppScreen(
                modifier = Modifier.fillMaxSize(),
                onStartHolderJourney = onStartHolderJourney,
                onStartVerifierJourney = onStartVerifierJourney,
            )
        }
    }
}