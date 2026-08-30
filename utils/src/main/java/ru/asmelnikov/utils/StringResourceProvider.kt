package ru.asmelnikov.utils

import android.content.Context
import androidx.annotation.StringRes

interface StringResourceProvider {

    fun getString(@StringRes resourceId: Int): String

    fun getString(@StringRes resourceId: Int, vararg arguments: Any): String

    class StringResourceProviderImpl(private val context: Context) : StringResourceProvider {
        override fun getString(resourceId: Int): String = context.getString(resourceId)

        override fun getString(resourceId: Int, vararg arguments: Any): String = context.getString(
            resourceId,
            *arguments,
        )
    }
}
