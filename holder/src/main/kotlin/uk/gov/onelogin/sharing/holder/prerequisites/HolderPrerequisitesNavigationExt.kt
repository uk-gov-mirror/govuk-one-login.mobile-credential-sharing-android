package uk.gov.onelogin.sharing.holder.prerequisites

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import uk.gov.onelogin.sharing.holder.prerequisites.recheck.HolderRecheckPrerequisitesNavigationExt.navigateToHolderRecheckPrerequisites
import uk.gov.onelogin.sharing.holder.presentation.HolderPresentQrNavigationExt.navigateToHolderPresentQrScreen

object HolderPrerequisitesNavigationExt {
    fun NavController.navigateToHolderPrerequisitesScreen(
        options: NavOptionsBuilder.() -> Unit = {}
    ) = navigate(HolderPrerequisitesRoute, options)

    internal fun NavGraphBuilder.configureHolderPrerequisitesScreen(controller: NavController) {
        composable<HolderPrerequisitesRoute> {
            HolderPrerequisitesScreen(
                onHandlePreflight = { missingPrerequisites ->
                    controller.navigateToHolderRecheckPrerequisites(
                        missingPrerequisites = missingPrerequisites
                    )
                },
                onPresentEngagement = {
                    controller.navigateToHolderPresentQrScreen()
                }
            )
        }
    }
}
