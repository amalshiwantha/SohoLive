package com.soho.sohoapp.live.enums

import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.SohoLiveApp.Companion.context

enum class AlertConfig(
    var title: String,
    var message: String,
    val confirmBtnText: String,
    val dismissBtnText: String,
    var isConfirm: Boolean = false
) {
    SIGN_IN_ERROR(
        title = "SignIn Problem",
        message = "Something went wrong. Please re-try later",
        confirmBtnText = "",
        dismissBtnText = "OK",
    ),
    SIGN_IN_ALERT(
        title = "Sign In Alert",
        message = "Do you want to sign in?",
        confirmBtnText = "Yes",
        dismissBtnText = "No",
    ),
    SIGN_OUT_ALERT(
        title = "Sign Out Alert",
        message = "Do you want to sign out?",
        confirmBtnText = "Sign Out",
        dismissBtnText = "Cancel"
    ),
    DELETE_ALERT(
        title = "Delete Alert",
        message = "Do you want to delete this item?",
        confirmBtnText = "Delete",
        dismissBtnText = "Cancel"
    ),
    GO_LIVE_ERROR(
        title = "Property Listing Problem",
        message = "Something went wrong. Please re-try later",
        confirmBtnText = "",
        dismissBtnText = "OK",
    ),
    GO_LIVE_SUBMIT_ERROR(
        title = "Submission Problem",
        message = "Something went wrong. Please re-try later",
        confirmBtnText = "",
        dismissBtnText = "OK",
    ),
    COMMON_OK(
        title = context.getString(R.string.problem),
        message = "Something went wrong. Please re-try later",
        confirmBtnText = "",
        dismissBtnText = "OK",
    ),
    VID_LIB_ERROR(
        title = "Loading Problem",
        message = "Something went wrong. Please re-try later",
        confirmBtnText = "",
        dismissBtnText = "OK",
    )
}
