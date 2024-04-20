package com.example.shoppinglist.ui.navigation

sealed class MainNavigation(val route: String) {
    object SplashScreen : MainNavigation("splashscreen")
    object MainScreen : MainNavigation("mainscreen")
    object SummaryScreen : MainNavigation(
        "summaryscreen?all={all}&important={important}") {
        fun createRoute(all: Int, important: Int) : String {
            return "summaryscreen?all=$all&important=$important"
        }
    }
}