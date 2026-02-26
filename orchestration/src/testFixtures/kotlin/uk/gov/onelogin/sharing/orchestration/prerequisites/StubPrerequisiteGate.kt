package uk.gov.onelogin.sharing.orchestration.prerequisites

import java.util.Collections

data class StubPrerequisiteGate(private val responses: List<PrerequisiteResponse>) :
    PrerequisiteGate {
    var callCount: Int = 0

    constructor(
        vararg responses: PrerequisiteResponse
    ) : this(
        responses.toList()
    )

    constructor(
        response: PrerequisiteResponse,
        size: Int
    ) : this(
        Collections.nCopies(size, response)
    )

    override fun checkPrerequisites(request: PrerequisiteRequest): PrerequisiteResponse =
        responses[callCount].also {
            ++callCount
        }
}
