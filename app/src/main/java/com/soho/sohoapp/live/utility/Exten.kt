package com.soho.sohoapp.live.utility

import android.Manifest
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.SohoLiveApp.Companion.context
import com.soho.sohoapp.live.enums.FieldType
import com.soho.sohoapp.live.network.response.DataGoLiveSubmit
import com.soho.sohoapp.live.ui.view.activity.HaishinActivity
import com.soho.sohoapp.live.ui.view.activity.HaishinActivity.Companion.KEY_STREAM
import com.soho.sohoapp.live.ui.view.screens.signin.SignInState
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.Locale

@OptIn(DelicateCoroutinesApi::class)
fun downloadFile(url: String, title: String, onDownloadStatus: (String) -> Unit) {
    println("url "+url)
    var statusMsg: String
    val progressTitle = "SohoLive Video"
    val fileName = title.toFileName() + ".mp4"
    val request = DownloadManager.Request(Uri.parse(url)).apply {
        setTitle(progressTitle)
        setDescription("Downloading $title")
        setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS, "SohoLive/$fileName"
        )
        setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
    }

    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    val downloadId = downloadManager.enqueue(request)

    GlobalScope.launch {
        while (true) {
            val status = getDownloadStatus(downloadId)
            statusMsg = when (status) {
                DownloadManager.STATUS_RUNNING -> "Downloading"
                DownloadManager.STATUS_SUCCESSFUL -> "Download Completed"
                DownloadManager.STATUS_FAILED -> "Download Failed"
                else -> "Connecting..."
            }

            onDownloadStatus(statusMsg)

            if (status == DownloadManager.STATUS_SUCCESSFUL || status == DownloadManager.STATUS_FAILED) {
                break
            }

            delay(1000)
        }
    }
}

@SuppressLint("Range")
private fun getDownloadStatus(downloadId: Long): Int {
    val query = DownloadManager.Query().setFilterById(downloadId)
    val cursor =
        (context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager).query(query)
    if (cursor.moveToFirst()) {
        val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
        cursor.close()
        return status
    }
    cursor.close()
    return DownloadManager.STATUS_FAILED
}

fun String.toFileName(): String {
    return this.replace(" ", "_")
        .replace("/", "_")
        .lowercase()
}

fun shareIntent(shareLink: String) {
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, shareLink)
        type = "text/plain"
    }
    val chooser = Intent.createChooser(shareIntent, "Share via").apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startActivity(chooser)
}

fun showToast(msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}


fun openLiveCaster(apiRes: DataGoLiveSubmit) {
    val intent = Intent(context, HaishinActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    intent.putExtra(KEY_STREAM, apiRes.streamKey)
    context.startActivity(intent)
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun isPermissionsGranted(): Boolean {

    val permissions = listOf(
        Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO
    )

    val multiplePermissionsState = rememberMultiplePermissionsState(permissions = permissions)

    LaunchedEffect(Unit) {
        multiplePermissionsState.launchMultiplePermissionRequest()
    }

    return multiplePermissionsState.permissions.all { it.status.isGranted }
}

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

