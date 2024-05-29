package com.soho.sohoapp.live.network.api.soho

import com.soho.sohoapp.live.model.LoginRequest
import com.soho.sohoapp.live.network.response.AuthResponse


interface SohoApiServices {
    companion object {
        const val LOGIN = "user/login?key=?"
        const val SIGNUP = "user/signup?key=?"
        const val FORGET_PW = "user/forgetPw?key=?"
    }

    suspend fun login(loginRequest: LoginRequest): AuthResponse
    suspend fun signup(signupModel: String): AuthResponse
    suspend fun forgetPassword(email: String): AuthResponse

}