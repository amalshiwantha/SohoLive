package com.soho.sohoapp.live.utility

import android.annotation.SuppressLint
import android.content.Context
import com.soho.sohoapp.live.R
import zendesk.core.AnonymousIdentity
import zendesk.core.Zendesk
import zendesk.support.Support

class ZendeskClient private constructor(context: Context) {

    private val context: Context = context.applicationContext

    companion object {

        //Pass only application context
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: ZendeskClient? = null

        fun getInstance(context: Context): ZendeskClient =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ZendeskClient(context).also {
                    INSTANCE = it
                }
            }
    }

    fun getZendeskInstance(): Zendesk {
        if (!Zendesk.INSTANCE.isInitialized) {
            initZendeskInstance()
        }
        return Zendesk.INSTANCE
    }

    fun getSupportInstance(): Support {
        if (!Support.INSTANCE.isInitialized) {
            if (!Zendesk.INSTANCE.isInitialized) {
                initZendeskInstance()
            }
            Support.INSTANCE.init(Zendesk.INSTANCE)
        }
        return Support.INSTANCE
    }

    private fun initZendeskInstance() {
        Zendesk.INSTANCE.init(
            context,
            context.getString(R.string.key_zendesk_app_url),
            context.getString(R.string.key_zendesk_application_id),
            context.getString(R.string.key_zendesk_mobile_sdk_client)
        )
        if (Zendesk.INSTANCE.identity == null) {
            Zendesk.INSTANCE.setIdentity(AnonymousIdentity.Builder().build())
        }
        Support.INSTANCE.init(Zendesk.INSTANCE)
    }
}