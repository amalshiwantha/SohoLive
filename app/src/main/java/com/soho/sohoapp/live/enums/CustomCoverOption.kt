package com.soho.sohoapp.live.enums

enum class CustomCoverOption(
    val title: String,
    var isEnabled: Boolean
) {
    PROFILE(
        title = "Show Profile",
        isEnabled = true
    ),
    AGENCY(
        title = "Show Agency Info",
        isEnabled = false
    ),
    INFO(
        title = "Show Listing Details",
        isEnabled = true
    ),
    DATE_TIME(
        title = "Show Date & Time",
        isEnabled = false
    ),
}
