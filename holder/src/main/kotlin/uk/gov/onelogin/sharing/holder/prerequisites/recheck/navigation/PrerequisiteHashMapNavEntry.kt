package uk.gov.onelogin.sharing.holder.prerequisites.recheck.navigation

import android.os.Build
import androidx.navigation.NavType
import androidx.savedstate.SavedState
import kotlinx.serialization.json.Json
import uk.gov.onelogin.sharing.orchestration.prerequisites.Prerequisite
import uk.gov.onelogin.sharing.orchestration.prerequisites.PrerequisiteResponse

internal object PrerequisiteHashMapNavEntry : NavType<HashMap<Prerequisite, PrerequisiteResponse>>(
    isNullableAllowed = false
) {
    override fun put(
        bundle: SavedState,
        key: String,
        value: HashMap<Prerequisite, PrerequisiteResponse>
    ) {
        bundle.putSerializable(key, value)
    }

    override fun get(
        bundle: SavedState,
        key: String
    ): HashMap<Prerequisite, PrerequisiteResponse>? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val result = mutableMapOf<Prerequisite, PrerequisiteResponse>()
            val map = bundle.getParcelable(key, HashMap::class.java)

            map?.entries?.forEach { (key, value) ->
                result.put(
                    key as Prerequisite,
                    value as PrerequisiteResponse
                )
            }

            HashMap(result)
        } else {
            @Suppress("DEPRECATION")
            bundle.getParcelable(key)
        }

    override fun parseValue(value: String): HashMap<Prerequisite, PrerequisiteResponse> =
        Json.Default.decodeFromString<HashMap<Prerequisite, PrerequisiteResponse>>(value)

    override fun serializeAsValue(value: HashMap<Prerequisite, PrerequisiteResponse>): String =
        Json.Default.encodeToString(value)
}
