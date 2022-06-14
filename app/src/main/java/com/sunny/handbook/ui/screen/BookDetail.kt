package com.sunny.handbook.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.sunny.handbook.data.HomeResponse
import com.sunny.handbook.data.User
import com.sunny.handbook.ui.components.HBButton
import com.sunny.handbook.ui.screen.destinations.ScheduleMeetDestination
import com.sunny.handbook.ui.theme.HBBlue

@Composable
@com.ramcosta.composedestinations.annotation.Destination
fun BookDetail(data: HomeResponse, destinationsNavigator: DestinationsNavigator) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(10.dp)
            .verticalScroll(rememberScrollState())

    ) {
        Column(
            Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp),
        ) {
            AsyncImage(
                model = data.book?.imageLink,
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
                    .padding(bottom = 16.dp)
            )
            Text(
                text = data.book?.name.toString(),
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                fontSize = MaterialTheme.typography.body1.fontSize,
            )
            Text(
                text = data.book?.author.toString(),
                color = Color.Black,
            )

            Text(
                text = data.book?.publication.toString(),
                color = Color.Black,
            )
            Text(
                text = "Price: Rs${data.book?.price}",
                color = Color.Black,
            )

            Spacer(modifier = Modifier.height(10.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color.LightGray
            ) {
                Text(text = "Seller info", modifier = Modifier.padding(8.dp))
            }
            UserCard(data.user)
        }
        HBButton(
            title = "Buy", modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            backgroundColor = HBBlue
        ) {

            destinationsNavigator.navigate(ScheduleMeetDestination(homeResponse = data))

        }
    }

}


@Composable
fun UserCard(user: User?) {

    Column(
        Modifier
            .background(Color.White)
            .padding(vertical = 16.dp)

    ) {

        Text(
            text = "Name: ${user?.name}",
            color = Color.Black,
        )
        Text(
            text = "Email: ${user?.email}",
            color = Color.Black,
        )
        Text(
            text = "Phone: ${user?.phone}",
            color = Color.Black,
        )
        Text(
            text = "Room number ${user?.room_number}",
            color = Color.Black,
        )


    }
}