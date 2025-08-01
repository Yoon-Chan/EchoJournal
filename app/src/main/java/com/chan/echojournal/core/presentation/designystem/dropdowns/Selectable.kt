package com.chan.echojournal.core.presentation.designystem.dropdowns

data class Selectable<T>(
    val item: T,
    val selected: Boolean
) {
    companion object {
        fun <T> List<T>.asUnselectedItems(): List<Selectable<T>> {
            return map { Selectable(item = it, selected = false) }
        }
    }
}
