package com.example.shoppinglist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import com.example.shoppinglist.ui.navigation.MainNavigation
import com.example.shoppinglist.ui.screen.MainScreen
import com.example.shoppinglist.ui.screen.SplashScreen
import com.example.shoppinglist.ui.screen.SummaryScreen
import com.example.shoppinglist.ui.theme.ShoppingListTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShoppingListTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ShoppingListNavHost()
                }
            }
        }
    }
}

@Composable
fun ShoppingListNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = MainNavigation.SplashScreen.route
) {
    NavHost(
        modifier = modifier, navController = navController, startDestination = startDestination
    ) {
        composable(MainNavigation.SplashScreen.route) {
            SplashScreen(navController = navController)
        }
        composable(MainNavigation.MainScreen.route) {
            MainScreen(
                onNavigateToSummary = { all, important ->
                    navController.navigate(
                        MainNavigation.SummaryScreen.createRoute(all, important))
                }
            )
        }

        composable(MainNavigation.SummaryScreen.route,
            // extract all and important arguments
            arguments = listOf(
                navArgument("all"){type = NavType.IntType},
                navArgument("important"){type = NavType.IntType})
            ) {
            val numallshopping = it.arguments?.getInt("all")
            val numimportant = it.arguments?.getInt("important")
            if (numallshopping != null && numimportant != null) {
                SummaryScreen(
                    numallshopping = numallshopping,
                    numimportantshopping = numimportant
                )
            }
        }
    }
}