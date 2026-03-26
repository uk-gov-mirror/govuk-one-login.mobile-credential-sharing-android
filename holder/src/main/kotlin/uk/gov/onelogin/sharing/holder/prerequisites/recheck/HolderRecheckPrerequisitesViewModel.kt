package uk.gov.onelogin.sharing.holder.prerequisites.recheck

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.binding
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.plus
import uk.gov.onelogin.sharing.core.HolderUiScope
import uk.gov.onelogin.sharing.orchestration.Orchestrator
import uk.gov.onelogin.sharing.orchestration.holder.session.HolderSessionState

@ContributesIntoMap(HolderUiScope::class, binding = binding<ViewModel>())
@ViewModelKey(HolderRecheckPrerequisitesViewModel::class)
class HolderRecheckPrerequisitesViewModel(
    private val orchestrator: Orchestrator.Holder,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) : ViewModel() {

    val holderUpdatedState: StateFlow<HolderSessionState?> = orchestrator
        .holderSessionState
        .stateIn(
            viewModelScope.plus(dispatcher),
            SharingStarted.Eagerly,
            null
        )
}
