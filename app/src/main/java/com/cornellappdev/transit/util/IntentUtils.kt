package com.cornellappdev.transit.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

object IntentUtils {
    fun Context.openDeepLink(packageName: String) {
        val launchIntent = packageManager.getLaunchIntentForPackage(packageName)

        if (launchIntent != null) {
            startActivity(launchIntent)
        } else {
            try {
                val playStoreIntent = Intent(Intent.ACTION_VIEW, "market://details?id=$packageName".toUri())
                playStoreIntent.setPackage("com.android.vending")
                startActivity(playStoreIntent)
            } catch (e2: ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW, "https://play.google.com/store/apps/details?id=$packageName}".toUri()))

            }
        }
    }
}