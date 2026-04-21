package uk.gov.onelogin.sharing.testapp.credential

import androidx.test.core.app.ApplicationProvider

object MockCredentialData {
    val mockCredential = MockCredentials.mockCredential(
        ApplicationProvider.getApplicationContext()
    )
}
