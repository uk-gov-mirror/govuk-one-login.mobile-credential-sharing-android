package uk.gov.onelogin.sharing.core.res.values

import androidx.annotation.StringRes
import uk.gov.onelogin.sharing.core.R

/**
 * Test data used with [StringsXmlTest] for resource IDs that explicitly state a value.
 *
 * Ensures that tests would fail when strings update without updating this test data.
 */
enum class StringsXmlData(val expected: String, @param:StringRes val resourceId: Int) {
    AN_ERROR_HAS_OCCURRED(
        expected = "An error has occurred",
        resourceId = R.string.an_error_has_occurred
    ),
    DISABLED(
        expected = "Disabled",
        resourceId = R.string.disabled
    ),
    DENIED(
        expected = "Denied",
        resourceId = R.string.denied
    ),
    ENABLED(
        expected = "Enabled",
        resourceId = R.string.enabled
    ),
    GRANTED(
        expected = "Granted",
        resourceId = R.string.granted
    ),
    DISCONNECTED_UNEXPECTEDLY(
        expected = "Bluetooth disconnected unexpectedly",
        resourceId = R.string.bluetooth_disconnected_unexpectedly
    ),
    PERMISSIONS_REVOKED(
        expected = "Bluetooth permissions were revoked during the session",
        resourceId = R.string.bluetooth_permissions_revoked
    ),
    TRY_AGAIN(
        expected = "Try again",
        resourceId = R.string.try_again
    )
}
