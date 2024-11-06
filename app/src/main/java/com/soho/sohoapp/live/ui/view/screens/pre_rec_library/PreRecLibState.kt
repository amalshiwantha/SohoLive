package com.soho.sohoapp.live.ui.view.screens.pre_rec_library

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.soho.sohoapp.live.db.PrivateVideo

data class PreRecLibState(
    val isDone: Boolean = false,
    val isSuccess: Boolean = false,
    val isLoadedItem: Boolean = false,
    val uploadUrl: String? = null,
    val fileUrl: String? = null,
    var privateVideo: MutableState<PrivateVideo?> = mutableStateOf(null),
    var isLoading: MutableState<Boolean> = mutableStateOf(false),
    var videoList: MutableState<MutableList<PrivateVideo>> = mutableStateOf(mutableListOf()),
    var isUploading: MutableState<Boolean> = mutableStateOf(false)
)
