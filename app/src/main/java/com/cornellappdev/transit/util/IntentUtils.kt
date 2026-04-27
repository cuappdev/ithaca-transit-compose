package com.cornellappdev.transit.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.net.toUri

object IntentUtils {
    fun Context.openDeepLink(packageName: String) {
        val launchIntent = packageManager.getLaunchIntentForPackage(packageName)

        if (launchIntent != null) {
            startActivity(launchIntent)
        } else {
            val playStoreIntent = Intent(Intent.ACTION_VIEW, "market://details?id=$packageName".toUri())
                .setPackage("com.android.vending")
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            try {
                startActivity(playStoreIntent)
            } catch (e: ActivityNotFoundException) {
                val webStoreIntent = Intent(Intent.ACTION_VIEW, "https://play.google.com/store/apps/details?id=$packageName".toUri())
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                try {
                    startActivity(webStoreIntent)
                } catch (e2: ActivityNotFoundException) {
                    Log.e("IntentUtils","no handler for play store web URL" ,e2)
                }
            }
        }
    }
}