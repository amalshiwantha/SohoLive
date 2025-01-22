package com.soho.sohoapp.live.enums

import com.soho.sohoapp.live.ui.view.activity.main.MainActivity.Companion.maxSteps


enum class StepInfo(
    val counter: String,
    var title: String,
    val info: String
) {
    STEP_1(
        counter = "Step 1 of $maxSteps",
        title = "Link video to your property",
        info = "Prospects interested in your property listing will be notified when video is available, and it will be featured directly on your property listing page. "
    ),
    STEP_2(
        counter = "Step 2 of $maxSteps",
        title = "Select profile to promote",
        info = "The selected profile’s photo, name, and star rating (if available) will be displayed on the video."
    ),
    STEP_3(
        counter = "Step 3 of $maxSteps",
        title = "Give your audience more information on your video",
        info = "Please provide details that help prospective buyers and renters understand the purpose of your video."
    ),
    STEP_4(
        counter = "Step 4 of $maxSteps",
        title = "Select your video format",
        info = "You can either stream live, allowing viewers to watch in real-time on your property listing, or record your video and share it at a later time."
    )
}
