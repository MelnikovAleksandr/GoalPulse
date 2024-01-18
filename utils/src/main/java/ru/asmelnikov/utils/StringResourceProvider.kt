package ru.asmelnikov.utils

import android.content.Context
import androidx.annotation.StringRes

interface StringResourceProvider {

    fun getString(@StringRes resourceId: Int, vararg arguments: Any): String

    class StringResourceProviderImpl(private val context: Context) : StringResourceProvider {
        override fun getString(resourceId: Int, vararg arguments: Any): String {
            return context.getString(resourceId, *arguments)
        }
    }
}