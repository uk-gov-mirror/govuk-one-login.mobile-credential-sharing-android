package uk.gov.onelogin.sharing.testapp.credential

import android.content.Context
import java.util.Base64
import java.util.UUID
import uk.gov.onelogin.sharing.testapp.R

object MockCredentials {

    private fun loadPrivateKey(context: Context): ByteArray =
        context.assets.open("test_private_key.pem")
            .bufferedReader()
            .readText()
            .toByteArray()

    fun mockCredential(context: Context): MockCredential {
        val base64 = context.resources
            .openRawResource(R.raw.mock_credential)
            .bufferedReader()
            .readText()
            .trim()

        return MockCredential(
            id = UUID.randomUUID().toString(),
            displayName = "Jane Doe",
            rawCredential = Base64.getUrlDecoder().decode(base64),
            privateKey = loadPrivateKey(context)
        )
    }

    fun getMockCredentials(context: Context): List<MockCredential> = listOf(
        mockCredential(context)
    )
}