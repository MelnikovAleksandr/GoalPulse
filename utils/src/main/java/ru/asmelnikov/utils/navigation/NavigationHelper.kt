package ru.asmelnikov.utils.navigation

import ru.asmelnikov.utils.composables.MainAppState

fun MainAppState.popUp() {
    backStack.removeLastOrNull()
}

fun MainAppState.navigate(route: Routes) {
    backStack.add(route)
}
