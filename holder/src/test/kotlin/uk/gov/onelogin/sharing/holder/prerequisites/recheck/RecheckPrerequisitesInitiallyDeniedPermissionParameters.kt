package uk.gov.onelogin.sharing.holder.prerequisites.recheck

import android.Manifest
import android.content.res.Resources
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.testing.junit.testparameterinjector.TestParameters
import com.google.testing.junit.testparameterinjector.TestParametersValuesProvider
import uk.gov.onelogin.sharing.core.presentation.permissions.FakePermissionState
import uk.gov.onelogin.sharing.holder.R
import uk.gov.onelogin.sharing.holder.prerequisites.recheck.preview.HolderRecheckPrerequisitesStates
import uk.gov.onelogin.sharing.orchestration.prerequisites.Prerequisite
import uk.gov.onelogin.sharing.orchestration.prerequisites.PrerequisiteResponse

@OptIn(ExperimentalPermissionsApi::class)
class RecheckPrerequisitesInitiallyDeniedPermissionParameters : TestParametersValuesProvider() {
    private data class RecheckPrerequisitesInitiallyDeniedPermissionState(
        val name: String,
        val missingPrerequisites: Map<Prerequisite, PrerequisiteResponse>,
        val permissionStates: List<PermissionState>,
        val expectedTitle: (Resources) -> String
    )

    override fun provideValues(context: Context?): List<TestParameters.TestParametersValues?>? =
        listOf(
            bluetoothInitialDenial,
            cameraInitialDenial,
            multipleInitialDenials
        ).map { data ->
            TestParameters.TestParametersValues.builder()
                .name(data.name)
                .addParameter("missingPrerequisites", data.missingPrerequisites)
                .addParameter("permissionStates", data.permissionStates)
                .addParameter("getExpectedTitle", data.expectedTitle)
                .build()
        }

    companion object {
        private val bluetoothInitialDenial = RecheckPrerequisitesInitiallyDeniedPermissionState(
            name = "Bluetooth initially denied",
            missingPrerequisites = mapOf(
                HolderRecheckPrerequisitesStates.Companion.unauthorizedBluetoothPermission
            ),
            permissionStates = listOf(
                FakePermissionState(
                    permission = Manifest.permission.BLUETOOTH,
                    status = PermissionStatus.Denied(true)
                )
            )
        ) { resources ->
            resources.getString(
                R.string.recheck_prerequisites_missing_prerequisite_permissions,
                Prerequisite.BLUETOOTH.titleCaseName
            )
        }

        private val cameraInitialDenial = RecheckPrerequisitesInitiallyDeniedPermissionState(
            name = "Camera initially denied",
            missingPrerequisites = mapOf(
                HolderRecheckPrerequisitesStates.Companion.unauthorizedCameraPermission
            ),
            permissionStates = listOf(
                FakePermissionState(
                    permission = Manifest.permission.CAMERA,
                    status = PermissionStatus.Denied(true)
                )
            )
        ) { resources ->
            resources.getString(
                R.string.recheck_prerequisites_missing_prerequisite_permissions,
                Prerequisite.CAMERA.titleCaseName
            )
        }

        private val multipleInitialDenials = RecheckPrerequisitesInitiallyDeniedPermissionState(
            name = "Multiple permissions denied",
            missingPrerequisites = mapOf(
                HolderRecheckPrerequisitesStates.Companion.unauthorizedBluetoothPermission,
                HolderRecheckPrerequisitesStates.Companion.unauthorizedCameraPermission
            ),
            permissionStates = listOf(
                FakePermissionState(
                    permission = Manifest.permission.BLUETOOTH,
                    status = PermissionStatus.Denied(true)
                ),
                FakePermissionState(
                    permission = Manifest.permission.CAMERA,
                    status = PermissionStatus.Denied(true)
                )
            )
        ) { resources ->
            resources.getString(R.string.recheck_prerequisites_multiple_prerequisites_not_met)
        }
    }
}
