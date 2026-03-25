package uk.gov.onelogin.sharing.verifier.res.values

import androidx.annotation.StringRes
import uk.gov.onelogin.sharing.core.R as coreR
import uk.gov.onelogin.sharing.verifier.R

/**
 * Test data used with [StringsXmlTest] for resource IDs that defer to another string resource.
 *
 * Ensures that tests would fail when deferred strings update without updating this test data.
 */
enum class DeferredStringsXmlData(
    @param:StringRes val defersTo: Int,
    @param:StringRes val resourceId: Int
) {
    SCANNED_INVALID_QR_BODY(
        R.string.did_not_scan_digital_credential_qr,
        R.string.scanned_invalid_qr_body
    ),
    SCANNED_INVALID_QR_TITLE(
        R.string.cannot_use_the_scanned_qr,
        R.string.scanned_invalid_qr_title
    ),
    SCANNED_INVALID_QR_TRY_AGAIN(
        coreR.string.try_again,
        R.string.scanned_invalid_qr_try_again
    )
}
