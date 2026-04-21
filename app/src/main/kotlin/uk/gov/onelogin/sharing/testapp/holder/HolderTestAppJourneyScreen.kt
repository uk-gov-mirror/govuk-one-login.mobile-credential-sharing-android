package uk.gov.onelogin.sharing.testapp.holder

import android.R
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import uk.gov.onelogin.sharing.sdk.api.presenter.CredentialPresenter
import uk.gov.onelogin.sharing.ui.impl.ShareCredential

@Composable
internal fun HolderTestAppJourneyScreen(
    presenter: CredentialPresenter,
    navController: NavController,
) {
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
                    R.drawable.ic_menu_close_clear_cancel
                ),
                contentDescription = "Close"
            )
        }
    }
}
