package com.soho.sohoapp.live.utility

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DownloadManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Environment
import android.util.Base64
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.soho.sohoapp.live.R
import com.soho.sohoapp.live.SohoLiveApp.Companion.context
import com.soho.sohoapp.live.enums.FieldType
import com.soho.sohoapp.live.enums.Orientation
import com.soho.sohoapp.live.model.AlertData
import com.soho.sohoapp.live.model.ForceExit
import com.soho.sohoapp.live.ui.view.screens.signin.SignInState
import com.soho.sohoapp.live.utility.Const.Companion.ERR_403
import com.soho.sohoapp.live.utility.Const.Companion.ERR_404
import com.soho.sohoapp.live.utility.Const.Companion.ERR_500
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

//Get Force Logout Error message
fun getForceExitMessage(errCode: Int?): ForceExit {
    return when (errCode) {
        ERR_403 -> ForceExit(
            "Not eligible",
            "Your current plan is not eligible for multicast. Please upgrade to a multicast plan.",
            ERR_403
        )

        ERR_404 -> ForceExit(
            "Subscription expired",
            "Contact us at support.soho.com.au or visit soho.com.au/agents/livecast for more information",
            ERR_404
        )

        else -> ForceExit("", "", ERR_500)
    }
}

//Rotate Screen for Video Recording
fun rotateScreen(rotateScreen: String, componentActivity: ComponentActivity) {
    val newOrientation = if (rotateScreen == Orientation.LAND.name) {
        Configuration.ORIENTATION_LANDSCAPE
    } else {
        Configuration.ORIENTATION_PORTRAIT
    }

    componentActivity.requestedOrientation =
        if (newOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
}

fun getThumbUrl(playbackId: String): String {
    return "https://image.mux.com/${playbackId}/thumbnail.png?fit_mode=preserve&time=5"
}

fun copyToClipboard(smName: String, link: String, isShowToast: Boolean = false) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("label", link)
    clipboard.setPrimaryClip(clip)

    if (isShowToast) {
        showToastTrans("Link to ${smName.lowercase()} copied")
    }
}

fun getInitialBg(initials: String): Int {
    val patternAe = Regex("[a-e]")
    val patternFj = Regex("[f-j]")
    val patternKo = Regex("[k-o]")
    val patternPt = Regex("[p-t]")

    return if (patternAe.containsMatchIn(initials)) {
        R.drawable.initial_bg_blue
    } else if (patternFj.containsMatchIn(initials)) {
        R.drawable.initial_bg_yellow
    } else if (patternKo.containsMatchIn(initials)) {
        R.drawable.initial_bg_green
    } else if (patternPt.containsMatchIn(initials)) {
        R.drawable.initial_bg_purple
    } else {
        R.drawable.initial_bg_grey
    }
}

fun getAppVersion(): Pair<String, Int> {
    val packageManager = context.packageManager
    val packageName = context.packageName
    var pkg: Pair<String, Int>

    try {
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        pkg = Pair(packageInfo.versionName, packageInfo.versionCode)
    } catch (e: PackageManager.NameNotFoundException) {
        pkg = Pair("0.0.0", 0)
    }

    return pkg
}

/*
* Display alert box with ok and cancel buttons
* */
fun showAlertMessage(activity: Activity, alertState: AlertData, onClick: () -> Unit) {
    val builder = AlertDialog.Builder(activity)
    builder
        .setTitle(alertState.title)
        .setMessage(alertState.message)
        .setCancelable(false)
        .setPositiveButton("OK") { dialog, id ->
            onClick()
        }
    val dialog = builder.create()
    dialog.show()
}

/*
* show progress login view when api call or long time tasks
* */
fun showProgressDialog(
    activity: Activity,
    message: String? = null,
    currentDialog: AlertDialog? = null,
    isVisible: Boolean = true
): AlertDialog? {
    var dialog: AlertDialog? = currentDialog

    if (isVisible) {
        if (dialog == null || !dialog.isShowing) {
            val builder = AlertDialog.Builder(activity)
            val inflater = activity.layoutInflater
            val dialogView = inflater.inflate(R.layout.dialog_progress, null)
            builder.setView(dialogView)
            builder.setCancelable(false)
            dialog = builder.create()
            dialog.show()
        }

        dialog.findViewById<TextView>(R.id.txt_msg)?.text = message
    } else {
        dialog?.dismiss()
        dialog = null
    }

    return dialog
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

fun showToastTrans(message: String) {
    val inflater = LayoutInflater.from(context)
    val layout: View = inflater.inflate(R.layout.custom_toast, null)

    val text: TextView = layout.findViewById(R.id.tv_message)
    text.text = message

    val toast = Toast(context)
    toast.duration = Toast.LENGTH_SHORT
    toast.view = layout

    toast.setGravity(Gravity.CENTER, 0, 0)
    toast.show()
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

    /*
    * At least 8 characters
	•	At least one uppercase letter (A-Z)
	•	At least one number (0-9)
	•	At least one special character (@#$%&_)
	* */
    val pwPattern = Regex("^(?=.*[A-Z])(?=.*[0-9])(?=.*[@#\$%&_]).{8,}\$")
    val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]+(\\.[a-z]+)?"
    var newPassword: String? = null

    mapList.forEach {

        val fieldType = it.key
        val inputValue = it.value

        //check field conditions
        val errorMessage = when (fieldType) {

            FieldType.LOGIN_EMAIL -> {
                if (inputValue.isNullOrEmpty()) {
                    context.getString(R.string.email_empty)
                } else {
                    if (!inputValue.matches(emailPattern.toRegex()))
                        context.getString(R.string.email_notvalid) else null
                }
            }

            FieldType.LOGIN_PW -> {
                if (inputValue.isNullOrEmpty()) {
                    context.getString(R.string.password_empty)
                } else {
                    null
                }
            }

            FieldType.RESET_NPW -> if (inputValue.isNullOrEmpty()) {
                "New Password cannot be empty"
            } else if (!pwPattern.matches(inputValue)) {
                "Password must be at least 8 characters long, contain a number, an uppercase letter, and a special character (@#$%&_)."
            } else {
                newPassword = inputValue
                null
            }

            FieldType.RESET_CPW -> if (inputValue.isNullOrEmpty()) {
                "Confirm Password cannot be empty"
            } else if (inputValue != newPassword) {
                "Password doesn't match"
            } else {
                null
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


fun playVideo(shareableLink: String?) {
    shareableLink?.let {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    } ?: run {
        showToast("No Video Link")
    }
}

@OptIn(DelicateCoroutinesApi::class)
fun downloadFile(url: String, title: String, onDownloadStatus: (String) -> Unit) {
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
                DownloadManager.STATUS_RUNNING -> "Downloading..."
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