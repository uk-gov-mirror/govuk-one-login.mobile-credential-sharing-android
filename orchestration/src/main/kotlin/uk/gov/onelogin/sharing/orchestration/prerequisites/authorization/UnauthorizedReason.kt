package uk.gov.onelogin.sharing.orchestration.prerequisites.authorization

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

/**
 * State table for the different reasons that a device fails to authorize.
 */
@Keep
@Parcelize
@Serializable
sealed class UnauthorizedReason : Parcelable {
    /**
     * State for when the User hasn't granted the requested permissions.
     *
     * @param missingPermissions The list of permissions that the User needs to grant.
     */
    @Serializable
    @Parcelize
    data class MissingPermissions(val missingPermissions: Set<String>) :
        UnauthorizedReason(),
        Iterable<String> by missingPermissions {
        constructor(
            vararg permissions: String
        ) : this (permissions.toSet())
    }
}
