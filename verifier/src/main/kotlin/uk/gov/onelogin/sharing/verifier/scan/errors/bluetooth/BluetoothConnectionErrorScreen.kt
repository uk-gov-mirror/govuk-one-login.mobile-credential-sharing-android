package uk.gov.onelogin.sharing.verifier.scan.errors.bluetooth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import uk.gov.android.ui.componentsv2.button.ButtonTypeV2
import uk.gov.android.ui.componentsv2.button.GdsButton
import uk.gov.android.ui.componentsv2.heading.GdsHeading
import uk.gov.android.ui.componentsv2.heading.GdsHeadingAlignment
import uk.gov.android.ui.componentsv2.images.GdsIcon
import uk.gov.android.ui.patterns.errorscreen.v2.ErrorScreen
import uk.gov.android.ui.patterns.errorscreen.v2.ErrorScreenIcon
import uk.gov.android.ui.theme.m3.GdsTheme
import uk.gov.android.ui.theme.spacingDouble
import uk.gov.onelogin.sharing.verifier.R

@Composable
fun BluetoothConnectionErrorScreen(
    modifier: Modifier = Modifier,
    onTryAgainClick: () -> Unit = {}
) {
    ErrorScreen(
        modifier = modifier.fillMaxWidth(),
        icon = { horizontalPadding ->
            GdsIcon(
                image = ImageVector.vectorResource(ErrorScreenIcon.ErrorIcon.icon),
                contentDescription = stringResource(ErrorScreenIcon.ErrorIcon.description),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = horizontalPadding),
                color = colorScheme.onBackground
            )
        },
        title = { horizontalPadding ->
            GdsHeading(
                text = stringResource(R.string.bluetooth_connection_error_title),
                modifier = Modifier
                    .padding(horizontal = horizontalPadding),
                textAlign = GdsHeadingAlignment.CenterAligned
            )
        },
        body = { horizontalPadding ->
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(spacingDouble),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = horizontalPadding)
                ) {
                    Text(
                        stringResource(R.string.bluetooth_connection_error_body),
                        textAlign = TextAlign.Center
                    )
                }
            }
        },
        primaryButton = {
            GdsButton(
                text = stringResource(R.string.bluetooth_connection_error_try_again),
                buttonType = ButtonTypeV2.Primary(),
                onClick = onTryAgainClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    )
}

@Composable
@Preview
internal fun BluetoothConnectionErrorScreenPreview() {
    GdsTheme {
        BluetoothConnectionErrorScreen()
    }
}
