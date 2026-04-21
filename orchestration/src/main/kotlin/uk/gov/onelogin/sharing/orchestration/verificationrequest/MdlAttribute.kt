package uk.gov.onelogin.sharing.orchestration.verificationrequest

import android.os.Parcel
import android.os.Parcelable
import kotlinx.serialization.Serializable

@Serializable
sealed interface MdlAttribute : Parcelable {
    val value: String
    fun validate(data: Any): Boolean

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(value)
    }

    @Serializable
    data object FamilyName : MdlAttribute {
        override val value = "family_name"
        override fun validate(data: Any) = data is String && data.length <= MAX_LENGTH
    }

    @Serializable
    data object GivenName : MdlAttribute {
        override val value = "given_name"
        override fun validate(data: Any) = data is String && data.length <= MAX_LENGTH
    }

    @Serializable
    data object BirthDate : MdlAttribute {
        override val value = "birth_date"
        override fun validate(data: Any) = data is String && FULL_DATE_PATTERN.matches(data)
    }

    @Serializable
    data object IssueDate : MdlAttribute {
        override val value = "issue_date"
        override fun validate(data: Any) = data is String && FULL_DATE_PATTERN.matches(data)
    }

    @Serializable
    data object ExpiryDate : MdlAttribute {
        override val value = "expiry_date"
        override fun validate(data: Any) = data is String && FULL_DATE_PATTERN.matches(data)
    }

    @Serializable
    data object IssuingCountry : MdlAttribute {
        override val value = "issuing_country"
        override fun validate(data: Any) = data is String && data.length == COUNTRY_CODE_LENGTH
    }

    @Serializable
    data object IssuingAuthority : MdlAttribute {
        override val value = "issuing_authority"
        override fun validate(data: Any) = data is String && data.length <= MAX_LENGTH
    }

    @Serializable
    data object DocumentNumber : MdlAttribute {
        override val value = "document_number"
        override fun validate(data: Any) = data is String && data.length <= MAX_LENGTH
    }

    @Serializable
    data object Portrait : MdlAttribute {
        override val value = "portrait"
        override fun validate(data: Any) = true
    }

    @Serializable
    data object BirthPlace : MdlAttribute {
        override val value = "birth_place"
        override fun validate(data: Any) = data is String && data.length <= MAX_LENGTH
    }

    @Serializable
    data object DrivingPrivileges : MdlAttribute {
        override val value = "driving_privileges"
        override fun validate(data: Any) = data is List<*>
    }

    @Serializable
    data object UnDistinguishingSign : MdlAttribute {
        override val value = "un_distinguishing_sign"
        override fun validate(data: Any) = data is String && data.isNotEmpty()
    }

    @Serializable
    data object ResidentAddress : MdlAttribute {
        override val value = "resident_address"
        override fun validate(data: Any) = data is String && data.length <= MAX_LENGTH
    }

    @Serializable
    data object ResidentPostalCode : MdlAttribute {
        override val value = "resident_postal_code"
        override fun validate(data: Any) = data is String && data.length <= MAX_LENGTH
    }

    @Serializable
    data object ResidentCity : MdlAttribute {
        override val value = "resident_city"
        override fun validate(data: Any) = data is String && data.length <= MAX_LENGTH
    }

    @Serializable
    data class AgeOver(val age: Int) : MdlAttribute {
        init {
            require(age in MIN_AGE..MAX_AGE) {
                "age must be between $MIN_AGE and $MAX_AGE, was $age"
            }
        }

        override val value = "age_over_%02d".format(age)
        override fun validate(data: Any) = data is Boolean
    }

    @Serializable
    data class Custom(override val value: String) : MdlAttribute {
        override fun validate(data: Any) = true
    }

    companion object CREATOR : Parcelable.Creator<MdlAttribute> {
        private val FULL_DATE_PATTERN = "\\d{4}-\\d{2}-\\d{2}".toRegex()
        private const val COUNTRY_CODE_LENGTH = 2
        private const val MIN_AGE = 0
        private const val MAX_AGE = 99
        const val MAX_LENGTH = 150

            override fun createFromParcel(parcel: Parcel): MdlAttribute {
                val value = requireNotNull(parcel.readString()) {
                    "Cannot read 'MdlAttribute' from parcel!"
                }

                return when (value) {
                    FamilyName.value -> FamilyName
                    GivenName.value -> GivenName
                    BirthDate.value -> BirthDate
                    IssueDate.value -> IssueDate
                    ExpiryDate.value -> ExpiryDate
                    IssuingCountry.value -> IssuingCountry
                    IssuingAuthority.value -> IssuingAuthority
                    DocumentNumber.value -> DocumentNumber
                    Portrait.value -> Portrait
                    BirthPlace.value -> BirthPlace
                    DrivingPrivileges.value -> DrivingPrivileges
                    UnDistinguishingSign.value -> UnDistinguishingSign
                    ResidentAddress.value -> ResidentAddress
                    ResidentPostalCode.value -> ResidentPostalCode
                    ResidentCity.value -> ResidentCity
                    else -> {
                        if (value.startsWith("age_over_")) {
                            AgeOver(value.takeLast(2).toInt())
                        } else {
                            Custom(value)
                        }
                    }
                }
            }

            override fun newArray(size: Int): Array<MdlAttribute?> {
                return arrayOfNulls(size)
            }

    }
}
