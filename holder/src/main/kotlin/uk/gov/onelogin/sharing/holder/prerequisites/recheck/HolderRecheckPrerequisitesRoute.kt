package uk.gov.onelogin.sharing.holder.prerequisites.recheck

import android.os.Parcelable
import androidx.annotation.Keep
import kotlin.reflect.typeOf
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import uk.gov.onelogin.sharing.holder.prerequisites.recheck.navigation.PrerequisiteHashMapNavEntry
import uk.gov.onelogin.sharing.orchestration.prerequisites.Prerequisite
import uk.gov.onelogin.sharing.orchestration.prerequisites.PrerequisiteResponse

@Keep
@Parcelize
@Serializable
internal data class HolderRecheckPrerequisitesRoute(
    val missingPrerequisites: HashMap<Prerequisite, PrerequisiteResponse>
) : Parcelable {
    internal companion object {
        val typeMap = mapOf(
            typeOf<HashMap<Prerequisite, PrerequisiteResponse>>() to PrerequisiteHashMapNavEntry
        )
    }
}
