package uk.gov.onelogin.sharing.holder.prerequisites.recheck

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import dev.zacsweers.metrox.viewmodel.metroViewModel
import uk.gov.android.ui.componentsv2.button.ButtonTypeV2
import uk.gov.android.ui.componentsv2.button.GdsButton
import uk.gov.android.ui.componentsv2.heading.GdsHeading
import uk.gov.android.ui.componentsv2.heading.GdsHeadingAlignment
import uk.gov.android.ui.componentsv2.images.GdsIcon
import uk.gov.android.ui.patterns.errorscreen.v2.ErrorScreen
import uk.gov.android.ui.patterns.errorscreen.v2.ErrorScreenIcon
import uk.gov.android.ui.theme.m3.GdsTheme
import uk.gov.onelogin.sharing.core.presentation.buttons.openSettingsIntent
import uk.gov.onelogin.sharing.holder.R
import uk.gov.onelogin.sharing.holder.prerequisites.recheck.preview.HolderRecheckPrerequisitesStates
import uk.gov.onelogin.sharing.holder.prerequisites.recheck.preview.HolderRecheckPrerequisitesStatesEntry
import uk.gov.onelogin.sharing.holder.presentation.isPermanentlyDenied
import uk.gov.onelogin.sharing.orchestration.holder.session.HolderSessionState
import uk.gov.onelogin.sharing.orchestration.prerequisites.Prerequisite
import uk.gov.onelogin.sharing.orchestration.prerequisites.PrerequisiteResponse

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun HolderRecheckPrerequisitesScreen(
    missingPrerequisites: Map<Prerequisite, PrerequisiteResponse>,
    modifier: Modifier = Modifier,
    viewModel: HolderRecheckPrerequisitesViewModel = metroViewModel(),
    multiplePermissionsState: MultiplePermissionsState = rememberMultiplePermissionsState(
        missingPrerequisites.getMissingPermissions()
    ) {
        viewModel.checkPrerequisites()
    },
    onHandlePreflight: (Map<Prerequisite, PrerequisiteResponse>) -> Unit = {},
    onPresentEngagement: () -> Unit = {},
) {
    val currentOnHandlePreflight by rememberUpdatedState(onHandlePreflight)
    val currentOnPresentEngagement by rememberUpdatedState(onPresentEngagement)
    val context = LocalContext.current
    val state by viewModel.holderUpdatedState.collectAsStateWithLifecycle()

    HolderRecheckPrerequisitesContent(
        buttonText = stringResource(
            calculateButtonTextFrom(multiplePermissionsState)
        ),
        missingPrerequisites = missingPrerequisites,
        modifier = modifier,
        onTryAgainClick = {
            calculateButtonActionFrom(
                context = context,
                missingPrerequisites,
                multiplePermissionsState,
            )
        },
    )

    DisposableEffect(state) {
        when (state) {
            is HolderSessionState.Preflight -> {
                currentOnHandlePreflight(
                    (state as HolderSessionState.Preflight).missingPrerequisites
                )
            }

            is HolderSessionState.PresentingEngagement -> {
                currentOnPresentEngagement()
            }

            else -> {
                // other session states don't affect this screen's behaviour
            }
        }

        onDispose {
            viewModel.clearState()
        }
    }
}

fun Map<Prerequisite, PrerequisiteResponse>.getMissingPermissions(): List<String> = values
    .filter { it is PrerequisiteResponse.Unauthorized }
    .map { it as PrerequisiteResponse.Unauthorized }
    .flatMap { it.getMissingPermissions() }

@Composable
internal fun HolderRecheckPrerequisitesContent(
    missingPrerequisites: Map<Prerequisite, PrerequisiteResponse>,
    buttonText: String,
    modifier: Modifier = Modifier,
    onTryAgainClick: () -> Unit = {},
) {
    val title = calculateTitleFrom(missingPrerequisites)
    ErrorScreen(
        modifier = modifier,
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
                text = title,
                modifier = Modifier
                    .testTag("title")
                    .padding(horizontal = horizontalPadding),
                textAlign = GdsHeadingAlignment.CenterAligned
            )
        },
        primaryButton = {
            GdsButton(
                text = buttonText,
                buttonType = ButtonTypeV2.Primary(),
                onClick = onTryAgainClick,
                modifier = Modifier
                    .testTag("primaryButton")
                    .fillMaxWidth()
            )
        }
    )
}

@OptIn(ExperimentalPermissionsApi::class)
private fun calculateButtonActionFrom(
    context: Context,
    missingPrerequisites: Map<Prerequisite, PrerequisiteResponse>,
    permissionContract: MultiplePermissionsState,
) {
    val missingPermissions = missingPrerequisites.getMissingPermissions()

    if (permissionContract.isPermanentlyDenied()) {
        context.startActivity(openSettingsIntent(context))
    } else if (missingPermissions.isNotEmpty()) {
        permissionContract.launchMultiplePermissionRequest()
    }
}

@OptIn(ExperimentalPermissionsApi::class)
private fun calculateButtonTextFrom(
    permissionState: MultiplePermissionsState
): Int = if (permissionState.isPermanentlyDenied()) {
    R.string.recheck_prerequisites_open_app_permissions
} else {
    R.string.recheck_prerequisites_try_again
}

@Composable
private fun calculateTitleFrom(
    missingPrerequisites: Map<Prerequisite, PrerequisiteResponse>,
): String {

    return if (missingPrerequisites.size == 1) {
        val prerequisite = missingPrerequisites.keys.first()
        when (missingPrerequisites.values.first()) {
            is PrerequisiteResponse.Unauthorized ->
                stringResource(
                    R.string.recheck_prerequisites_missing_prerequisite_permissions,
                    prerequisite.titleCaseName
                )
            is PrerequisiteResponse.Incapable ->
                stringResource(R.string.recheck_prerequisites_unsupported_journey)
            is PrerequisiteResponse.NotReady ->
                stringResource(R.string.recheck_prerequisites_phone_is_not_ready)
            else -> ""
        }
    } else {
        stringResource(R.string.recheck_prerequisites_multiple_prerequisites_not_met)
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
@Preview(showBackground = true)
internal fun HolderRecheckPrerequisitesPreview(
    @PreviewParameter(HolderRecheckPrerequisitesStates::class)
    entry: HolderRecheckPrerequisitesStatesEntry,
) {
    GdsTheme {
        HolderRecheckPrerequisitesContent(
            buttonText = stringResource(
                calculateButtonTextFrom(entry.permissionState)
            ),
            missingPrerequisites = entry.sessionState.missingPrerequisites,
            modifier = Modifier.fillMaxSize()
        )
    }
}
