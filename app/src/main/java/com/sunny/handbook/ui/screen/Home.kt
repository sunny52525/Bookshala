package com.sunny.handbook.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.sunny.handbook.data.HomeResponse
import com.sunny.handbook.data.RequestResult
import com.sunny.handbook.showToast
import com.sunny.handbook.ui.components.BookItem
import com.sunny.handbook.ui.components.LoadingIndicator
import com.sunny.handbook.ui.screen.destinations.BookAddScreenDestination
import com.sunny.handbook.ui.screen.destinations.BookDetailDestination
import com.sunny.handbook.ui.screen.destinations.ProfileScreenDestination
import com.sunny.handbook.ui.theme.HBBlue

@Destination(start = true)
@Composable
fun Home(
    homeData: RequestResult<List<HomeResponse>>?,
    destinationsNavigator: DestinationsNavigator
) {

    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 8.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Text(text = "Bookshala", fontWeight = FontWeight.SemiBold, fontSize = 25.sp)
                }

                IconButton(onClick = {
                    destinationsNavigator.navigate(ProfileScreenDestination())
                }, modifier = Modifier.align(Alignment.CenterStart)) {
                    Icon(imageVector = Icons.Filled.Person, contentDescription = "")
                }

                Button(
                    onClick = {
                        destinationsNavigator.navigate(BookAddScreenDestination)
                    }, colors = ButtonDefaults.buttonColors(
                        backgroundColor = HBBlue
                    ),
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Text(text = "Sell", color = Color.Black)
                }
                Divider(modifier = Modifier.align(Alignment.BottomCenter))


            }
            LazyVerticalGrid(columns = GridCells.Fixed(2), content = {

                homeData?.data?.let {
                    items(it) {
                        Column(Modifier.padding(horizontal = 10.dp)) {
                            BookItem(homeResponse = it) {
                                destinationsNavigator.navigate(BookDetailDestination(it))
                            }
                        }
                    }
                }

            }, verticalArrangement = Arrangement.spacedBy(10.dp))

        }
        if (homeData is RequestResult.Loading)
            Column(
                Modifier
                    .zIndex(10f)
                    .align(Alignment.Center)) {
                LoadingIndicator()
            }

        if (homeData is RequestResult.Error) {
            context.showToast("Some error occurred")
        }
    }
}