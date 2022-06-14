package com.sunny.handbook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.utils.composable
import com.sunny.handbook.data.Routes
import com.sunny.handbook.ui.screen.*
import com.sunny.handbook.ui.screen.destinations.*
import com.sunny.handbook.ui.theme.HandbookTheme
import com.sunny.handbook.ui.viewmodels.HomeViewModel

class Home : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: HomeViewModel by viewModels()
        setContent {
            HandbookTheme(darkTheme = false) {


                val navController = rememberNavController()

                val homeData by viewModel.homeData.observeAsState()
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    NavHost(navController = navController, startDestination = Routes.Home.route) {
                        composable(destination = HomeDestination) {
                            viewModel.resetBook()
                            Home(homeData, destinationsNavigator(navController = navController))
                        }
                        composable(BookDetailDestination) {

                            BookDetail(
                                data = this.navArgs.data,
                                destinationsNavigator(navController = navController)
                            )
                        }
                        composable(ScheduleMeetDestination) {
                            ScheduleMeet(
                                homeResponse = this.navArgs.homeResponse,
                                destinationsNavigator(navController = navController)
                            )
                        }
                        composable(ProfileScreenDestination) {
                            ProfileScreen(viewModel,
                                destinationsNavigator(navController = navController))
                        }
                        composable(BookAddScreenDestination) {
                            BookAddScreen(
                                viewModel,
                                destinationsNavigator(navController = navController)
                            )
                        }


                    }
                }
            }
        }
    }
}

@Composable
fun Greeting2(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    HandbookTheme {
        Greeting2("Android")
    }
}