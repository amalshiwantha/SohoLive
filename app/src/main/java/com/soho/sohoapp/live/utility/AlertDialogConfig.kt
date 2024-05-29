package com.soho.sohoapp.live.utility

enum class AlertDialogConfig(
    val title: String,
    val message: String,
    val confirmBtnText: String,
    val dismissBtnText: String,
    var isConfirm: Boolean = false
) {
    SIGN_IN_ERROR(
        title = "Sign In Problem",
        message = "Please check your credentials and try again.",
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
    )
}
