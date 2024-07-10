package com.soho.sohoapp.live.enums

enum class StepInfo(
    val counter: String,
    var title: String,
    val info: String
) {
    STEP_1(
        counter = "Step 1 of 5",
        title = "Link livestream to your property",
        info = "Prospect interested in your listing will be notified of your scheduled livestream and will be livecasted on your property listing page."
    ),
    STEP_2(
        counter = "Step 2 of 5",
        title = "Select profile to go live",
        info = "Which profile do you want to showcase when you go live?"
    ),
    STEP_3(
        counter = "Step 3 of 5",
        title = "Select multicast destinations",
        info = "Your livestream will be shown simultaneously on your selected destinations. "
    ),
    STEP_4(
        counter = "Step 4 of 5",
        title = "Give your audience more information on your livestream",
        info = "By providing these details will allow us to better inform prospective buyers and renters what your livestream is about."
    ),
    STEP_5(
        counter = "Step 5 of 5",
        title = "When do you want to go live?",
        info = "You can choose to go live immediately or create schedules for later date(s)."
    )
}
