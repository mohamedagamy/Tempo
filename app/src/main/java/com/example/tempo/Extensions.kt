package com.example.tempo

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import timber.log.Timber

fun Context.showToast(msg: String, length: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, msg, length).show()
}

fun Context.openLink(url: String) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(url)
    intent.addCategory(Intent.CATEGORY_BROWSABLE)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    try {
        this.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Timber.e("browser can't open url: $url")
    }
}