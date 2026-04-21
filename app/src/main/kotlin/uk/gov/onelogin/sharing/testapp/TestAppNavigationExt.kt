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
import uk.gov.onelogin.sharing.sdk.api.verifier.CredentialVerifier
import uk.gov.onelogin.sharing.ui.impl.VerifyCredential

object TestAppNavigationExt {
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