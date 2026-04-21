package uk.gov.onelogin.sharing.testapp.credential

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/**
 * Data model for a mock credential used in the Test App.
 *
 * @property id A unique string identifier.
 * @property displayName A string to identify the credential in the Test App UI.
 * @property rawCredential The full, decrypted raw CBOR data byte string for the mDL.
 * @property privateKey The raw bytes of the private key that corresponds to the public key
 * embedded in the mock credential's MSO.
 */
@Parcelize
@Serializable
data class MockCredential(
    val id: String,
    val displayName: String,
    val rawCredential: ByteArray,
    val privateKey: ByteArray
) : Parcelable {

    override fun toString(): String = "MockCredential(id=$id, displayName=$displayName)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MockCredential

        if (id != other.id) return false
        if (displayName != other.displayName) return false
        if (!rawCredential.contentEquals(other.rawCredential)) return false
        if (!privateKey.contentEquals(other.privateKey)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + displayName.hashCode()
        result = 31 * result + rawCredential.contentHashCode()
        result = 31 * result + privateKey.contentHashCode()
        return result
    }

    companion object {
        val MockCredentialType: NavType<MockCredential> = object : NavType<MockCredential>(
            isNullableAllowed = false
        ) {
            override fun get(bundle: Bundle, key: String): MockCredential? =
                bundle.getString(key)?.let { parseValue(it) }

            override fun put(bundle: Bundle, key: String, value: MockCredential) {
                bundle.putString(key, serializeAsValue(value))
            }

            override fun parseValue(value: String): MockCredential = Json.Default.decodeFromString(value)

            override fun serializeAsValue(
                value: MockCredential
            ): String = Json.Default.encodeToString(value)
        }
    }
}