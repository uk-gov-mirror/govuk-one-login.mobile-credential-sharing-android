package uk.gov.onelogin.sharing.verifier.scan.errors.bluetooth

import androidx.annotation.Keep
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import uk.gov.onelogin.sharing.verifier.scan.VerifierScanRoute

@Keep
@Serializable
object BluetoothConnectionErrorRoute {
    fun NavGraphBuilder.configureBluetoothConnectionErrorRoute(onTryAgainClick: () -> Unit = {}) {
        composable<BluetoothConnectionErrorRoute> {
            BluetoothConnectionErrorScreen(onTryAgainClick = onTryAgainClick)
        }
    }

    fun NavController.navigateToBluetoothConnectionErrorRoute() = navigate(
        BluetoothConnectionErrorRoute
    ) {
        popUpTo<VerifierScanRoute> {
            inclusive = false
        }
    }
}
