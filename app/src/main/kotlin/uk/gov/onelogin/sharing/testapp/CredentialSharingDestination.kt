package uk.gov.onelogin.sharing.testapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import uk.gov.onelogin.sharing.orchestration.verificationrequest.VerificationRequest
import uk.gov.onelogin.sharing.testapp.credential.select.MockCredential

@Parcelize
@Serializable
sealed interface CredentialSharingDestination : Parcelable {
    @Parcelize
    @Serializable
    data object Undetermined : CredentialSharingDestination

    @Parcelize
    @Serializable
    data class Holder(val credential: MockCredential) : CredentialSharingDestination

    @Parcelize
    @Serializable
    data class Verifier(val request: VerificationRequest) : CredentialSharingDestination

    @Parcelize
    @Serializable
    data object SelectAttributes : CredentialSharingDestination
}
