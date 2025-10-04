package com.msa.finhub.feature.inquiry.presentation
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msa.finhub.feature.inquiry.domain.usecase.InquiryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonElement
import timber.log.Timber
class InquiryViewModel(
    private val inquiry: InquiryUseCase
) : ViewModel() {

    data class UiState(
        val loading: Boolean = false,
        val result: JsonElement? = null,
        val error: String? = null
    )

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    fun submit(spec: EndpointSpec, text: Map<String, String>, bool: Map<String, Boolean>) {
        val params = buildMap {
            text.forEach { (k, v) -> if (v.isNotBlank()) put(k, v) }
            bool.forEach { (k, v) -> put(k, v.toString()) }
        }
        _state.update { it.copy(loading = true, error = null, result = null) }
        viewModelScope.launch {
            inquiry(spec.path, params).fold(
                onSuccess = { element ->
                    _state.update { it.copy(loading = false, result = element) }
                },
                onFailure = { ex ->
                    _state.update { it.copy(loading = false, error = ex.message) }
                }
            )
        }
    }
}