package com.soho.sohoapp.live.viewmodel

import androidx.lifecycle.ViewModel
import com.soho.sohoapp.live.model.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun setMessage(message: String) {
        _uiState.update { currentState ->
            currentState.copy(
                isLoading = false,
                loadingMessage = message
            )
        }
    }
}