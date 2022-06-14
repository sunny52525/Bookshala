package com.sunny.handbook.ui.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.dt.composedatepicker.ComposeCalendar
import com.dt.composedatepicker.SelectDateListener
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.sunny.handbook.data.HomeResponse
import com.sunny.handbook.data.Routes
import com.sunny.handbook.ui.components.HBButton
import com.sunny.handbook.ui.components.SuccessDialog
import com.sunny.handbook.ui.theme.HBBlue
import java.util.*


@com.ramcosta.composedestinations.annotation.Destination
@Composable
fun ScheduleMeet(homeResponse: HomeResponse, destinationsNavigator: DestinationsNavigator) {

    var open by remember {
        mutableStateOf(false)
    }
    var dateSelected by remember {
        mutableStateOf("")
    }
    var showSuccessDialog by remember {
        mutableStateOf(false)
    }


    Box(
        Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        if (showSuccessDialog) {
            SuccessDialog(
                title = "Your meeting is scheduled",
                subtitle = "Please bring the required amount to make up the deal"
            ) {
                showSuccessDialog=false

                destinationsNavigator.popBackStack(route = Routes.Home.route, inclusive = false)
            }
        }
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, 2010)
        calendar.set(Calendar.MONTH, 6)
        val calendarMax = Calendar.getInstance()
        calendarMax.set(Calendar.YEAR, 2032)
        calendarMax.set(Calendar.MONTH, 9)

        if (open) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(color = Color.Transparent.copy(alpha = 0.5f))
                    .zIndex(10f),
                contentAlignment = Alignment.Center
            ) {

                ComposeCalendar(minDate = calendar.time,
                    maxDate = calendarMax.time,
                    locale = Locale("en"),
                    title = "Select Date",
                    listener = object : SelectDateListener {
                        override fun onDateSelected(date: Date) {
                            Log.i("DATE", date.toString())
                            dateSelected = date.toString()
                            open = false
                        }

                        override fun onCanceled() {
                            open = false
                        }
                    })
            }
        }

        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Text(text = "Schedule meet", fontSize = 25.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(20.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = { open = true }, colors = ButtonDefaults.buttonColors(
                        backgroundColor = HBBlue
                    )
                ) {
                    Text(text = "Select date")

                }

                Spacer(modifier = Modifier.width(10.dp))
                Text(text = dateSelected)
            }


            Column(
                Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(16.dp),
            ) {
                AsyncImage(
                    model = homeResponse.book?.imageLink,
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(500.dp)
                        .padding(bottom = 16.dp)
                )
                Text(
                    text = homeResponse.book?.name.toString(),
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = MaterialTheme.typography.body1.fontSize,
                )
                Text(
                    text = homeResponse.book?.author.toString(),
                    color = Color.Black,
                )


                UserCard(homeResponse.user)
            }


        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 8.dp, vertical = 8.dp)
        ) {
            HBButton(
                title = "Schedule",
                modifier = Modifier
                    .fillMaxWidth(),
                backgroundColor = HBBlue
            ) {

                showSuccessDialog=true


            }
        }
    }
}