package ru.asmelnikov.team_info.components

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

internal fun getWebIntent(uri: String, context: Context) {
    try {
        Intent(Intent.ACTION_VIEW).also {
            it.data = uri.toUri()
            if (it.resolveActivity(context.packageManager) != null) {
                context.startActivity(it)
            }
        }
    } catch (_: Exception) {
    }
}
