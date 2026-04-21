package uk.gov.onelogin.sharing.testapp.holder

import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlin.reflect.typeOf
import uk.gov.onelogin.sharing.sdk.api.presenter.CredentialPresenter
import uk.gov.onelogin.sharing.testapp.credential.MockCredential
import uk.gov.onelogin.sharing.testapp.credential.MockCredential.Companion.MockCredentialType

object HolderTestAppJourneyNavigationExt {
    fun NavController.navigateToTestAppHolderJourney(
        credential: MockCredential,
        options: NavOptionsBuilder.() -> Unit = {},
    ) = navigate(
        HolderTestAppJourney(credential = credential),
        options
    )

    internal fun NavGraphBuilder.configureHolderJourneyWrapper(
        navController: NavController,
        component: (MockCredential) -> CredentialPresenter,
    ) {
        composable<HolderTestAppJourney>(
            typeMap = mapOf(
                typeOf<MockCredential>() to MockCredentialType
            )
        ) { navBackStackEntry ->
            val arguments: HolderTestAppJourney = navBackStackEntry.toRoute()
            val presenter = remember { component(arguments.credential) }

            HolderTestAppJourneyScreen(presenter, navController)
        }
    }
}

