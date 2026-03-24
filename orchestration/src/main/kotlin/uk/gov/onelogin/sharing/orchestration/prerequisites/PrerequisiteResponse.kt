package uk.gov.onelogin.sharing.orchestration.prerequisites

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import uk.gov.onelogin.sharing.orchestration.prerequisites.authorization.UnauthorizedReason
import uk.gov.onelogin.sharing.orchestration.prerequisites.capability.IncapableReason
import uk.gov.onelogin.sharing.orchestration.prerequisites.readiness.NotReadyReason

@Serializable
@Parcelize
sealed class PrerequisiteResponse : Parcelable {
    @Serializable
    @Parcelize
    data object MeetsPrerequisites : PrerequisiteResponse()

    @Serializable
    @Parcelize
    data class Incapable(val reason: IncapableReason) : PrerequisiteResponse()

    @Serializable
    @Parcelize
    data class NotReady(val reason: NotReadyReason) : PrerequisiteResponse()

    @Serializable
    @Parcelize
    data class Unauthorized(val reason: UnauthorizedReason) : PrerequisiteResponse() {
        fun getMissingPermissions(): Set<String> = (
            reason as? UnauthorizedReason.MissingPermissions
            )?.missingPermissions ?: emptySet()
    }
}
