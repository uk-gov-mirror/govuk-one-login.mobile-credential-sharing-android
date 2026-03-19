package uk.gov.onelogin.sharing.holder.prerequisites.recheck

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.binding
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import dev.zacsweers.metrox.viewmodel.metroViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
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
import uk.gov.onelogin.sharing.holder.R
import uk.gov.onelogin.sharing.orchestration.Orchestrator
import uk.gov.onelogin.sharing.orchestration.holder.session.HolderSessionState
import uk.gov.onelogin.sharing.orchestration.prerequisites.Prerequisite
import uk.gov.onelogin.sharing.orchestration.prerequisites.PrerequisiteResponse

@ContributesIntoMap(HolderUiScope::class, binding = binding<ViewModel>())
@ViewModelKey(HolderRecheckPrerequisitesViewModel::class)
class HolderRecheckPrerequisitesViewModel(
    orchestrator: Orchestrator.Holder,
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
) : ViewModel() {
    private val _preflightState = MutableStateFlow<HolderSessionState.Preflight?>(null)
    val preflightState: StateFlow<HolderSessionState.Preflight?> = _preflightState
}

@Composable
fun HolderRecheckPrerequisitesScreen(
    missingPrerequisites: Map<Prerequisite, PrerequisiteResponse>,
    modifier: Modifier = Modifier,
    viewModel: HolderRecheckPrerequisitesViewModel = metroViewModel(),
    onTryAgainClick: () -> Unit = {},
) {
    HolderRecheckPrerequisitesContent(
        missingPrerequisites = missingPrerequisites,
        modifier = modifier,
        onTryAgainClick = onTryAgainClick,
    )
}

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
