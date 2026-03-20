package uk.gov.onelogin.sharing.holder.prerequisites.recheck

import android.content.Context
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.binding
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import dev.zacsweers.metrox.viewmodel.metroViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uk.gov.android.ui.componentsv2.button.ButtonTypeV2
import uk.gov.android.ui.componentsv2.button.GdsButton
import uk.gov.android.ui.componentsv2.heading.GdsHeading
import uk.gov.android.ui.componentsv2.heading.GdsHeadingAlignment
import uk.gov.android.ui.componentsv2.images.GdsIcon
import uk.gov.android.ui.patterns.errorscreen.v2.ErrorScreen
import uk.gov.android.ui.patterns.errorscreen.v2.ErrorScreenIcon
import uk.gov.android.ui.theme.m3.GdsTheme
import uk.gov.onelogin.sharing.core.HolderUiScope
import uk.gov.onelogin.sharing.core.presentation.buttons.openSettingsIntent
import uk.gov.onelogin.sharing.holder.R
import uk.gov.onelogin.sharing.holder.presentation.isPermanentlyDenied
import uk.gov.onelogin.sharing.orchestration.Orchestrator
import uk.gov.onelogin.sharing.orchestration.Orchestrator.Verifier.Companion.requiredPermissions
import uk.gov.onelogin.sharing.orchestration.holder.session.HolderSessionState
import uk.gov.onelogin.sharing.orchestration.prerequisites.Prerequisite
import uk.gov.onelogin.sharing.orchestration.prerequisites.PrerequisiteResponse
import uk.gov.onelogin.sharing.orchestration.prerequisites.authorization.UnauthorizedReason

@ContributesIntoMap(HolderUiScope::class, binding = binding<ViewModel>())
@ViewModelKey(HolderRecheckPrerequisitesViewModel::class)
class HolderRecheckPrerequisitesViewModel(
    private val orchestrator: Orchestrator.Holder,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) : ViewModel() {

    private val _holderUpdatedState = MutableStateFlow<HolderSessionState?>(null)
    val holderUpdatedState: StateFlow<HolderSessionState?> = _holderUpdatedState

    fun checkPrerequisites(): Job = viewModelScope.launch(dispatcher) {
        orchestrator.checkPrerequisites().also {
            orchestrator.holderSessionState.collect { sessionState ->
                _holderUpdatedState.update { sessionState }
            }
        }
    }

    fun clearState(): Job = viewModelScope.launch(dispatcher) {
        _holderUpdatedState.update { null }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HolderRecheckPrerequisitesScreen(
    missingPrerequisites: Map<Prerequisite, PrerequisiteResponse>,
    modifier: Modifier = Modifier,
    viewModel: HolderRecheckPrerequisitesViewModel = metroViewModel(),
    onHandlePreflight: (Map<Prerequisite, PrerequisiteResponse>) -> Unit = {},
    onPresentEngagement: () -> Unit = {},
    onPermanentlyDenyPermission: () -> Unit = {}
) {
    val currentOnHandlePreflight by rememberUpdatedState(onHandlePreflight)
    val currentOnPresentEngagement by rememberUpdatedState(onPresentEngagement)
    val context = LocalContext.current
    val state by viewModel.holderUpdatedState.collectAsStateWithLifecycle()

    val multiplePermissionsState = rememberMultiplePermissionsState(
        missingPrerequisites.getMissingPermissions()
    ) {
        viewModel.checkPrerequisites()
    }

    HolderRecheckPrerequisitesContent(
        missingPrerequisites = missingPrerequisites,
        modifier = modifier,
        onTryAgainClick = {
            calculateButtonActionFrom(
                context = context,
                missingPrerequisites,
                multiplePermissionsState,
                onPermanentlyDenyPermission,
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
fun HolderRecheckPrerequisitesContent(
    missingPrerequisites: Map<Prerequisite, PrerequisiteResponse>,
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
                    .padding(horizontal = horizontalPadding),
                textAlign = GdsHeadingAlignment.CenterAligned
            )
        },
        primaryButton = {
            GdsButton(
                text = stringResource(R.string.try_again),
                buttonType = ButtonTypeV2.Primary(),
                onClick = onTryAgainClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    )
}

@OptIn(ExperimentalPermissionsApi::class)
private fun calculateButtonActionFrom(
    context: Context,
    missingPrerequisites: Map<Prerequisite, PrerequisiteResponse>,
    permissionContract: MultiplePermissionsState,
    onPermanentlyDenyPermission: () -> Unit = {}
) {
    val missingPermissions = missingPrerequisites.getMissingPermissions()

    if (permissionContract.isPermanentlyDenied()) {
        context.startActivity(openSettingsIntent(context))
    } else if (missingPermissions.isNotEmpty()) {
        permissionContract.launchMultiplePermissionRequest()
    }
}

private fun calculateTitleFrom(
    missingPrerequisites: Map<Prerequisite, PrerequisiteResponse>,
): String {

    return if (missingPrerequisites.size == 1) {
        val prerequisite = missingPrerequisites.keys.first()
        when (missingPrerequisites.values.first()) {
            is PrerequisiteResponse.Unauthorized ->
                "Missing " +
                        prerequisite.name.lowercase().replaceFirstChar(Char::uppercase) +
                        " permissions"

            is PrerequisiteResponse.Incapable -> "Unsupported journey"
            is PrerequisiteResponse.NotReady -> "Phone isn't ready"
            else -> ""
        }
    } else {
        "Prerequisites not met"
    }
}

@Composable
@Preview(showBackground = true)
fun HolderRecheckPrerequisitesPreview(
    @PreviewParameter(HolderRecheckPrerequisitesStates::class)
    state: HolderSessionState.Preflight,
) {
    GdsTheme {
        HolderRecheckPrerequisitesContent(
            missingPrerequisites = state.missingPrerequisites,
            modifier = Modifier.fillMaxSize()
        )
    }
}
