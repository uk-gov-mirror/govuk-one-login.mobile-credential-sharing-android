package uk.gov.onelogin.sharing.holder.prerequisites.recheck

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.binding
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uk.gov.onelogin.sharing.core.HolderUiScope
import uk.gov.onelogin.sharing.orchestration.Orchestrator
import uk.gov.onelogin.sharing.orchestration.holder.session.HolderSessionState

@ContributesIntoMap(HolderUiScope::class, binding = binding<ViewModel>())
@ViewModelKey(HolderRecheckPrerequisitesViewModel::class)
class HolderRecheckPrerequisitesViewModel(
    private val orchestrator: Orchestrator.Holder,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) : ViewModel() {

    private val _holderUpdatedState = MutableStateFlow<HolderSessionState?>(null)
    val holderUpdatedState: StateFlow<HolderSessionState?> = _holderUpdatedState

    fun checkPrerequisites(): Job = viewModelScope.launch(dispatcher) {
        orchestrator.checkPrerequisites().also {
            orchestrator.holderSessionState.collect { sessionState ->
                _holderUpdatedState.update { sessionState }
            }
        }
    }

    fun clearState(): Job = viewModelScope.launch(dispatcher) {
        _holderUpdatedState.update { null }
    }
}