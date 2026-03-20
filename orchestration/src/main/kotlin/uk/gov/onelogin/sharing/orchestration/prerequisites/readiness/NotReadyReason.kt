package uk.gov.onelogin.sharing.orchestration.prerequisites.readiness

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

/**
 * State table for the different reasons that a device isn't ready to complete the User journey.
 */
@Keep
@Parcelize
@Serializable
sealed class NotReadyReason : Parcelable {
    /**
     * State for when the device doesn't have the necessary hardware for a given
     * [uk.gov.onelogin.sharing.orchestration.prerequisites.Prerequisite].
     */
    @Serializable
    @Parcelize
    data object BluetoothTurnedOff : NotReadyReason()

    /**
     * State for when the Android-powered device's camera is already in use by another app.
     *
     * This may occur during picture-in-picture modes or with foldable devices.
     */
    @Serializable
    @Parcelize
    data object CameraAlreadyInUse : NotReadyReason()

    /**
     * State for when the Android-powered device's cameras cannot be queried.
     */
    @Serializable
    @Parcelize
    data object CannotCheckCamera : NotReadyReason()
}
