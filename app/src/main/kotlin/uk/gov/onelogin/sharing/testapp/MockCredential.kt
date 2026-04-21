package uk.gov.onelogin.sharing.testapp

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

    companion object {
        internal val MockCredentialType = object : NavType<MockCredential>(
            isNullableAllowed = false
        ) {
            override fun get(bundle: Bundle, key: String): MockCredential? =
                bundle.getString(key)?.let { parseValue(it) }

            override fun put(bundle: Bundle, key: String, value: MockCredential) {
                bundle.putString(key, serializeAsValue(value))
            }

            override fun parseValue(value: String): MockCredential = Json.decodeFromString(value)

            override fun serializeAsValue(value: MockCredential): String = Json.encodeToString(value)
        }
    }
}
