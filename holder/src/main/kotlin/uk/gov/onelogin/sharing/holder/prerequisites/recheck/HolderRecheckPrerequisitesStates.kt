package uk.gov.onelogin.sharing.holder.prerequisites.recheck

import android.Manifest
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import uk.gov.onelogin.sharing.orchestration.holder.session.HolderSessionState
import uk.gov.onelogin.sharing.orchestration.prerequisites.Prerequisite
import uk.gov.onelogin.sharing.orchestration.prerequisites.PrerequisiteResponse
import uk.gov.onelogin.sharing.orchestration.prerequisites.authorization.UnauthorizedReason

internal class HolderRecheckPrerequisitesStates :
    PreviewParameterProvider<HolderSessionState.Preflight> {

    private val data = listOf(
        "Missing bluetooth permission" to
            mapOf(
                Prerequisite.BLUETOOTH to PrerequisiteResponse.Unauthorized(
                    UnauthorizedReason.MissingPermissions(Manifest.permission.BLUETOOTH)
                )
            ),
        "Missing camera permission" to mapOf(
            Prerequisite.CAMERA to PrerequisiteResponse.Unauthorized(
                UnauthorizedReason.MissingPermissions(Manifest.permission.CAMERA)
            )
        )
    ).map { (name, responseMap) ->
        name to HolderSessionState.Preflight(responseMap)
    }

    override val values: Sequence<HolderSessionState.Preflight> = data
        .map { it.second }
        .asSequence()

    override fun getDisplayName(index: Int): String? = data
        .map { it.first }
        .getOrNull(index)
}
