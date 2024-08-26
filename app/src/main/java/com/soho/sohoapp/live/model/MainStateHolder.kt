package com.soho.sohoapp.live.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object MainStateHolder {
    var mState by mutableStateOf(MainState())
        private set

    fun updateState(newState: MainState) {
        mState = newState
    }
}

/*
* how to access
* =============
*
* top level vars (MainState.kt)
* --------------
* var liveOrientation: MutableState<String> = mutableStateOf(Orientation.PORT.name),
*
* Assign
* ----------
* val mState = GlobalStateHolder.mState
* val selectedOrientation by remember { mutableStateOf(mState.liveOrientation) }
*
* add new value
* ----------------
* selectedOrientation.value = Orientation.PORT.name
*
* get updated value
* -----------------
* isSelected = selectedOrientation.value == Orientation.PORT.name
*
* */
