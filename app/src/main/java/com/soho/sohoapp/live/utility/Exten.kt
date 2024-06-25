package com.soho.sohoapp.live.utility

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import androidx.compose.runtime.MutableState
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.SohoLiveApp.Companion.context
import com.soho.sohoapp.live.enums.FieldType
import com.soho.sohoapp.live.ui.view.screens.signin.SignInState
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.Locale

fun formValidation(
    state: MutableState<SignInState>, mapList: MutableMap<FieldType, String?>
): SignInState {

    mapList.forEach {

        val fieldType = it.key
        val inputValue = it.value

        //check field conditions
        val errorMessage = when (fieldType) {

            FieldType.LOGIN_EMAIL -> {
                val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

                if (inputValue.isNullOrEmpty()) {
                    context.getString(R.string.email_empty)
                } else {
                    /*if (!inputValue.matches(emailPattern.toRegex()))
                        context.getString(R.string.email_notvalid) else null*/
                    null
                }
            }

            FieldType.LOGIN_PW -> {
                if (inputValue.isNullOrEmpty()) {
                    context.getString(R.string.password_empty)
                } else {
                    null
                }
            }
        }

        //update local state
        state.value = state.value.copy(errorStates = state.value.errorStates.toMutableMap().apply {
            if (errorMessage != null) {
                put(fieldType, errorMessage)
            } else {
                remove(fieldType)
            }
        })
    }

    return state.value
}

fun String.toUppercaseFirst(): String {
    return this.replaceFirstChar {
        if (it.isLowerCase())
            it.titlecase(Locale.getDefault()) else it.toString()
    }
}

fun Double.visibleValue(): String? {
    val value = this.toInt()
    return if (value > 0) {
        value.toString()
    } else {
        null
    }
}

fun printHashKey() {
    try {
        val info: PackageInfo = context.packageManager
            .getPackageInfo(context.packageName, PackageManager.GET_SIGNATURES)
        for (signature in info.signatures) {
            val md: MessageDigest = MessageDigest.getInstance("SHA")
            md.update(signature.toByteArray())
            val hashKey: String = String(Base64.encode(md.digest(), 0))
            Log.d("hashkey", "Hash Key: $hashKey")
        }
    } catch (e: NoSuchAlgorithmException) {
        Log.e("Error", "${e.localizedMessage}")
    } catch (e: Exception) {
        Log.e("Exception", "${e.localizedMessage}")
    }
}

