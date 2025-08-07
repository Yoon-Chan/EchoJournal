package com.chan.echojournal.core.presentation.util

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.res.stringResource

@Stable
sealed interface UiText {
    data class Dynamic(val value: String) : UiText

    @Stable
    data class StringResource(
        @StringRes val id: Int,
        val args: List<Any> = listOf()
    ) : UiText

    @Stable
    data class Combined(
        val format: String,
        val uiTexts: List<UiText>
    ) : UiText

    @Composable
    fun asString(): String {
        return when (this) {
            is Dynamic -> value
            is StringResource -> stringResource(id, *args.toTypedArray())
            is Combined -> {
                val strings = uiTexts.map { uiText ->
                    when (uiText) {
                        is Combined -> throw IllegalArgumentException("Can't nest combined UiTexts")
                        is Dynamic -> uiText.value
                        is StringResource -> stringResource(uiText.id, *uiText.args.toTypedArray())
                    }
                }

                //%s, %s +1
                String.format(format, *strings.toTypedArray())
            }
        }
    }

    fun asString(context: Context): String {
        return when (this) {
            is Dynamic -> value
            is StringResource -> context.getString(id, *args.toTypedArray())
            is Combined -> {
                val strings = uiTexts.map { uiText ->
                    when (uiText) {
                        is Combined -> throw IllegalArgumentException("Can't nest combined UiTexts")
                        is Dynamic -> uiText.value
                        is StringResource -> context.getString(uiText.id, *uiText.args.toTypedArray())
                    }
                }

                //%s, %s +1
                String.format(format, *strings.toTypedArray())
            }
        }
    }
}