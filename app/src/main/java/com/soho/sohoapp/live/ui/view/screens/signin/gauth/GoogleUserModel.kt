package com.soho.sohoapp.live.ui.view.screens.signin.gauth

data class GoogleUserModel(
    val name: String? = null,
    val email: String? = null,
    val image: String? = null,
    val token: String? = null,
    val isLoggedIn: Boolean = false
)
