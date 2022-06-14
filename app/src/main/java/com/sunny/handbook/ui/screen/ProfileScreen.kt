package com.sunny.handbook.ui.screen

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.sunny.handbook.MainActivity
import com.sunny.handbook.data.PreferenceManager
import com.sunny.handbook.data.RequestResult
import com.sunny.handbook.ui.components.BookItem
import com.sunny.handbook.ui.components.LoadingIndicator
import com.sunny.handbook.ui.components.findActivity
import com.sunny.handbook.ui.viewmodels.HomeViewModel


@Composable
@Destination
fun ProfileScreen(viewModel: HomeViewModel, destinationsNavigator: DestinationsNavigator) {

    val activity = findActivity() as? Activity
    val user = PreferenceManager.user
    val homeData by viewModel.homeData.observeAsState()
    val deleteBook by viewModel.deleteBook.observeAsState()
    Box(Modifier.fillMaxSize()) {

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item(span = { GridItemSpan(2) }) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)

                    ) {

                        AsyncImage(
                            model = "https://img.webmd.com/dtmcms/live/webmd/consumer_assets/site_images/article_thumbnails/other/cat_relaxing_on_patio_other/1800x1200_cat_relaxing_on_patio_other.jpg",
                            contentDescription = null,
                            modifier = Modifier
                                .size(150.dp)
                                .clip(CircleShape)
                                .background(Color.Gray.copy(alpha = 0.5f)),
                            contentScale = ContentScale.Crop
                        )

                        Column {
                            Text(
                                text = user.name,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 25.sp
                            )
                            Text(text = user.email, fontSize = 15.sp)
                            Text(text = user.phone, fontSize = 15.sp)
                        }


                    }
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = Color.Gray.copy(alpha = 0.2f)
                    ) {
                        Text(text = "More Details", modifier = Modifier.padding(16.dp))
                    }

                    Column {
                        TwoText(
                            key = "Phone number",
                            user.phone
                        )
                        TwoText(
                            key = "Room number",
                            user.room_number
                        )
                        TwoText(
                            key = "Building",
                            user.building
                        )
                        TwoText(
                            key = "Roll number",
                            user.student_roll
                        )

                    }

                }
            }

            item(span = { GridItemSpan(2) }) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = Color.Gray.copy(alpha = 0.2f)
                ) {
                    Text(text = "Your Books", modifier = Modifier.padding(16.dp))
                }
            }
            homeData?.data?.let {
                items(it.filter {
                    it.user?._id == user._id
                }) {
                    Column(Modifier.padding(horizontal = 10.dp)) {
                        BookItem(homeResponse = it, isVisible = true, onDelete = {
                            viewModel.deleteBook(it._id.toString())
                        }) {
                            destinationsNavigator.navigate(
                                com.sunny.handbook.ui.screen.destinations.BookDetailDestination(
                                    it
                                )
                            )
                        }
                    }
                }
            }

        }
        when (deleteBook) {
            is RequestResult.Error -> {

            }
            is RequestResult.Idle -> {

            }
            is RequestResult.Loading -> {

            }
            is RequestResult.Success -> {
                viewModel.getHomeData()
                viewModel.deleteBook.postValue(RequestResult.Idle())
            }
            null -> Unit
        }

        if (homeData is RequestResult.Loading || deleteBook is RequestResult.Loading)
            Column(
                Modifier
                    .zIndex(10f)
                    .align(Alignment.Center)
            ) {
                LoadingIndicator()
            }


        Column(modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(10.dp)) {
            FloatingActionButton(
                onClick = {
                    PreferenceManager.clear()
                    activity?.let {
                        Intent(it, MainActivity::class.java).apply {
                            it.startActivity(this)
                            it.finish()
                        }
                    }
                },
            ) {
                Text(text = "Logout")
            }
        }


    }

}

@Preview
@Composable
fun TwoText(
    key: String = "Name",
    value: String = "Sunny"
) {

    Row(
        Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        Text(text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    fontWeight = Bold,
                    fontSize = 16.sp
                )
            ) {
                append(key)
            }
            append(": ")
            append(value)

        })
    }
}
