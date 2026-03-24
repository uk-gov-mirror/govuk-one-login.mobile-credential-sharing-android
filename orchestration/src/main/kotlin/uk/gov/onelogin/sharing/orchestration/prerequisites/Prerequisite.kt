package uk.gov.onelogin.sharing.orchestration.prerequisites

enum class Prerequisite {
    BLUETOOTH,
    CAMERA,
    UNKNOWN;

    val titleCaseName: String = name.lowercase().replaceFirstChar(Char::uppercase)
}
