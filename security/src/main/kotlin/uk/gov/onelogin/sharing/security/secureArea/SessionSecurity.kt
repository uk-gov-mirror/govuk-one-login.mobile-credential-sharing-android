package uk.gov.onelogin.sharing.security.secureArea

import java.security.interfaces.ECPublicKey

fun interface SessionSecurity {
    // Testing commit signing
    fun generateEcPublicKey(algorithm: String, parameterSpec: String): ECPublicKey?
}
