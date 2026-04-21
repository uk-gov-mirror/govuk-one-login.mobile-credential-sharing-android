package uk.gov.onelogin.sharing.testapp.holder

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import uk.gov.onelogin.sharing.testapp.credential.MockCredential

@Parcelize
@Serializable
internal data class HolderTestAppJourney(val credential: MockCredential) : Parcelable

