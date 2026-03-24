package uk.gov.onelogin.sharing.holder.res.values

import androidx.annotation.StringRes
import uk.gov.onelogin.sharing.holder.R
import uk.gov.onelogin.sharing.core.R as coreR

/**
 * Test data used with [StringsXmlTest] for resource IDs that defer to another string resource.
 *
 * Ensures that tests would fail when deferred strings update without updating this test data.
 */
enum class DeferredStringsXmlData(
    @param:StringRes val defersTo: Int,
    @param:StringRes val resourceId: Int
) {
    HOLDER_PREREQUISITES_NOT_STARTED(
        defersTo = R.string.checking_journey_requirements,
        resourceId = R.string.holder_prerequisites_not_started
    ),
    HOLDER_PREREQUISITES_PREFLIGHT(
        defersTo = R.string.have_not_met_requirements,
        resourceId = R.string.holder_prerequisites_preflight
    ),
    HOLDER_PREREQUISITES_PRESENTING_ENGAGEMENT(
        defersTo = R.string.creating_qr_code,
        resourceId = R.string.holder_prerequisites_presenting_engagement
    ),
    HOLDER_PREREQUISITES_READY_TO_PRESENT(
        defersTo = R.string.generating_qr_code_data,
        resourceId = R.string.holder_prerequisites_ready_to_present
    ),
    HOLDER_WELCOME_OPEN_APP_PERMISSIONS(
        defersTo = coreR.string.open_app_permissions,
        resourceId = R.string.holder_welcome_open_app_permissions
    ),
    RECHECK_PREREQUISITES_MISSING_PREREQUISITE_PERMISSIONS(
        defersTo = coreR.string.missing_prerequisite_permissions,
        resourceId = R.string.recheck_prerequisites_missing_prerequisite_permissions
    ),
    RECHECK_PREREQUISITES_MULTIPLE_PREREQUISITES_NOT_MET(
        defersTo = coreR.string.prerequisites_not_met,
        resourceId = R.string.recheck_prerequisites_multiple_prerequisites_not_met
    ),
    RECHECK_PREREQUISITES_OPEN_APP_PERMISSIONS(
        defersTo = coreR.string.open_app_permissions,
        resourceId = R.string.recheck_prerequisites_open_app_permissions
    ),
    RECHECK_PREREQUISITES_PHONE_IS_NOT_READY(
        defersTo = coreR.string.phone_is_not_ready,
        resourceId = R.string.recheck_prerequisites_phone_is_not_ready
    ),
    RECHECK_PREREQUISITES_TRY_AGAIN(
        defersTo = coreR.string.try_again,
        resourceId = R.string.recheck_prerequisites_try_again
    ),
    RECHECK_PREREQUISITES_UNSUPPORTED_JOURNEY(
        defersTo = coreR.string.unsupported_journey,
        resourceId = R.string.recheck_prerequisites_unsupported_journey
    ),
}
