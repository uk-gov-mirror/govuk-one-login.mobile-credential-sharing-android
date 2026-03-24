package uk.gov.onelogin.sharing.holder.prerequisites.recheck.preview

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import uk.gov.onelogin.sharing.orchestration.holder.session.HolderSessionState

@OptIn(ExperimentalPermissionsApi::class)
data class HolderRecheckPrerequisitesStatesEntry(
    val name: String,
    val permissionState: MultiplePermissionsState,
    val sessionState: HolderSessionState.Preflight
) {
    constructor(
        data: Triple<String, MultiplePermissionsState, HolderSessionState.Preflight>
    ) : this(
        name = data.first,
        permissionState = data.second,
        sessionState = data.third
    )
}
