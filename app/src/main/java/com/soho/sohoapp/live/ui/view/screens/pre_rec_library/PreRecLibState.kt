package com.soho.sohoapp.live.ui.view.screens.pre_rec_library

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.soho.sohoapp.live.db.PrivateVideo

data class PreRecLibState(
    val isSuccess: Boolean = false,
    var isLoading: MutableState<Boolean> = mutableStateOf(false),
    var videoList: MutableState<MutableList<PrivateVideo>> = mutableStateOf(mutableListOf())
)
