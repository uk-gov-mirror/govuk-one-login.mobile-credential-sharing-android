package uk.gov.onelogin.sharing.orchestration.prerequisites.capability

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

/**
 * State table for the different reasons that a device fails capability checks.
 */
@Keep
@Serializable
@Parcelize
sealed class IncapableReason : Parcelable {
    /**
     * State for when the device doesn't have the necessary hardware for a given
     * [uk.gov.onelogin.sharing.orchestration.prerequisites.Prerequisite].
     */
    @Serializable
    @Parcelize
    data object MissingHardware : IncapableReason()

    /**
     * State for when the Android-powered device's cameras cannot be queried.
     */
    @Serializable
    @Parcelize
    data object CannotCheckCamera : IncapableReason()
}
