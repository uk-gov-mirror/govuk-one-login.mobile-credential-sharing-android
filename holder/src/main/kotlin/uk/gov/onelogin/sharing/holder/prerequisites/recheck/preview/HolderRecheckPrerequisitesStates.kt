@file:OptIn(ExperimentalPermissionsApi::class)

package uk.gov.onelogin.sharing.holder.prerequisites.recheck.preview

import android.Manifest
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import uk.gov.onelogin.sharing.core.presentation.permissions.FakeMultiplePermissionsState
import uk.gov.onelogin.sharing.core.presentation.permissions.FakePermissionState
import uk.gov.onelogin.sharing.orchestration.holder.session.HolderSessionState
import uk.gov.onelogin.sharing.orchestration.prerequisites.Prerequisite
import uk.gov.onelogin.sharing.orchestration.prerequisites.PrerequisiteResponse
import uk.gov.onelogin.sharing.orchestration.prerequisites.authorization.UnauthorizedReason

internal class HolderRecheckPrerequisitesStates :
    PreviewParameterProvider<HolderRecheckPrerequisitesStatesEntry> {

    companion object {
        internal val unauthorizedBluetoothPermission = Prerequisite.BLUETOOTH to
                PrerequisiteResponse.Unauthorized(
                    UnauthorizedReason.MissingPermissions(Manifest.permission.BLUETOOTH)
                )
        internal val unauthorizedCameraPermission = Prerequisite.CAMERA to
                PrerequisiteResponse.Unauthorized(
                    UnauthorizedReason.MissingPermissions(Manifest.permission.CAMERA)
                )
    }

    private fun deniedBluetoothPermissionState(
        isPermanentlyDenied: Boolean = false,
    ) = FakePermissionState(
        Manifest.permission.BLUETOOTH,
        PermissionStatus.Denied(!isPermanentlyDenied)
    )

    private fun deniedCameraPermissionState(
        isPermanentlyDenied: Boolean = false,
    ) = FakePermissionState(
        Manifest.permission.CAMERA,
        PermissionStatus.Denied(!isPermanentlyDenied)
    )

    private val data =
        listOf<Triple<String, Map<Prerequisite, PrerequisiteResponse>, List<PermissionState>>>(
            Triple(
                "Bluetooth: Denied permission",
                mapOf(unauthorizedBluetoothPermission),
                listOf(
                    deniedBluetoothPermissionState()
                )
            ),
            Triple(
                "Bluetooth: Permanently denied permission",
                mapOf(unauthorizedBluetoothPermission),
                listOf(
                    deniedBluetoothPermissionState(isPermanentlyDenied = true)

                )
            ),
            Triple(
                "Camera: Denied permission",
                mapOf(unauthorizedCameraPermission),
                listOf(
                    deniedCameraPermissionState()
                )
            ),
            Triple(
                "Camera: Permanently denied permission",
                mapOf(unauthorizedCameraPermission),
                listOf(
                    deniedCameraPermissionState(isPermanentlyDenied = true)
                )
            ),
            Triple(
                "Multiple: Denied permission",
                mapOf(
                    unauthorizedBluetoothPermission,
                    unauthorizedCameraPermission,
                ),
                listOf(
                    deniedBluetoothPermissionState(),
                    deniedCameraPermissionState()
                )
            ),
            Triple(
                "Multiple: Permanently Denied single permission",
                mapOf(
                    unauthorizedBluetoothPermission,
                    unauthorizedCameraPermission,
                ),
                listOf(
                    deniedBluetoothPermissionState(),
                    deniedCameraPermissionState(isPermanentlyDenied = true)
                )
            ),
        ).map { (name, responseMap, permissionStates) ->
            HolderRecheckPrerequisitesStatesEntry(
                name = name,
                permissionState = FakeMultiplePermissionsState(permissionStates),
                sessionState = HolderSessionState.Preflight(responseMap)
            )
        }

    override val values: Sequence<HolderRecheckPrerequisitesStatesEntry> = data
        .asSequence()

    override fun getDisplayName(index: Int): String? = data
        .map { it.name }
        .getOrNull(index)
}
