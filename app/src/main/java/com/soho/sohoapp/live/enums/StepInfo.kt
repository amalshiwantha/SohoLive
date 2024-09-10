package com.soho.sohoapp.live.enums

import com.soho.sohoapp.live.ui.view.activity.main.MainActivity.Companion.maxSteps


enum class StepInfo(
    val counter: String,
    var title: String,
    val info: String
) {
    STEP_1(
        counter = "Step 1 of $maxSteps",
        title = "Link livestream to your property",
        info = "Prospect interested in your listing will be notified of your scheduled livestream and will be livecasted on your property listing page."
    ),
    STEP_2(
        counter = "Step 2 of $maxSteps",
        title = "Select profile to go live",
        info = "Which profile do you want to showcase when you go live?"
    ),
    STEP_3(
        counter = "Step 3 of $maxSteps",
        title = "Select multicast destinations",
        info = "Your livestream will be shown simultaneously on your selected destinations. "
    ),
    STEP_4(
        counter = "Step 4 of $maxSteps",
        title = "Give your audience more information on your livestream",
        info = "Providing these details will allow us to better inform prospective buyers and renters what your livestream is about."
    )
}
