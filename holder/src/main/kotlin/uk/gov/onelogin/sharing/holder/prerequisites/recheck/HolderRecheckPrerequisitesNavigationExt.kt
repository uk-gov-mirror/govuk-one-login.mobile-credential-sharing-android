package uk.gov.onelogin.sharing.holder.prerequisites.recheck

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import uk.gov.onelogin.sharing.holder.presentation.HolderPresentQrNavigationExt.navigateToHolderPresentQrScreen

object HolderRecheckPrerequisitesNavigationExt {
    fun NavController.navigateToHolderRecheckPrerequisites(
        options: NavOptionsBuilder.() -> Unit = {}
    ): Unit = navigate(
        HolderRecheckPrerequisitesRoute,
        options
    )

    @OptIn(ExperimentalPermissionsApi::class)
    internal fun NavGraphBuilder.configureHolderRecheckPrerequisitesScreen(
        controller: NavController
    ) {
        composable<HolderRecheckPrerequisitesRoute> {

            HolderRecheckPrerequisitesScreen(
                modifier = Modifier.fillMaxSize(),
                onPresentEngagement = {
                    controller.navigateToHolderPresentQrScreen()
                }
            )
        }
    }
}
