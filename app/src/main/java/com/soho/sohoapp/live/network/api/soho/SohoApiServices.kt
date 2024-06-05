package com.soho.sohoapp.live.network.api.soho

import com.soho.sohoapp.live.model.SignInRequest
import com.soho.sohoapp.live.network.response.AuthResponse


interface SohoApiServices {
    companion object {
        const val LOGIN = "sessions/login"
        const val SIGNUP = "user/signup?key=?"
        const val FORGET_PW = "user/forgetPw?key=?"
    }

    suspend fun login(signInRequest: SignInRequest): AuthResponse
    suspend fun signup(signupModel: String): AuthResponse
    suspend fun forgetPassword(email: String): AuthResponse

}