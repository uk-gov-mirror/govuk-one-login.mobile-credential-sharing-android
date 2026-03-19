package uk.gov.onelogin.sharing.holder.prerequisites.recheck

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import uk.gov.onelogin.sharing.orchestration.prerequisites.Prerequisite
import uk.gov.onelogin.sharing.orchestration.prerequisites.PrerequisiteResponse

object HolderRecheckPrerequisitesNavigationExt {
    fun NavController.navigateToHolderRecheckPrerequisites(
        missingPrerequisites: Map<Prerequisite, PrerequisiteResponse>,
        options: NavOptionsBuilder.() -> Unit = {}
    ): Unit = navigate(
        HolderRecheckPrerequisitesRoute(
            missingPrerequisites = missingPrerequisites,
        ),
        options
    )

    internal fun NavGraphBuilder.configureHolderRecheckPrerequisitesScreen(
        controller: NavController
    ) {
        composable<HolderRecheckPrerequisitesRoute> { navBackStackEntry ->
        val arguments: HolderRecheckPrerequisitesRoute = navBackStackEntry.toRoute()

            HolderRecheckPrerequisitesScreen(
                missingPrerequisites = arguments.missingPrerequisites,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}