package com.sunny.handbook.ui.components

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.sunny.handbook.data.HomeResponse

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BookItem(
    homeResponse: HomeResponse,
    isVisible: Boolean = false,
    onDelete: () -> Unit = {},
    onClick: () -> Unit,
) {

    homeResponse.book?.let {
        Card(
            onClick = onClick,
            shape = RoundedCornerShape(6.dp),
            border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.5f))
        ) {
            Box {
                if (isVisible)
                    Column(
                        modifier = Modifier
                            .align(TopEnd)
                            .zIndex(12f)
                    ) {
                        DeleteButton(onDelete)
                    }
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AsyncImage(
                        model = homeResponse.book.imageLink, contentDescription = null,
                        modifier = Modifier.height(100.dp),
                        contentScale = ContentScale.Crop
                    )

                    Text(
                        text = homeResponse.book.name,
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.align(CenterHorizontally),
                        fontSize = MaterialTheme.typography.body1.fontSize,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Rs.${homeResponse.book.price}",
                        color = Color.Black,
                        modifier = Modifier.align(CenterHorizontally)
                    )
                    Text(
                        text = "Seller : ${homeResponse.user?.name}",
                        color = Color.Black,
                        modifier = Modifier.align(CenterHorizontally)
                    )


                }
            }
        }

    }
}
//
//@Preview
//@Composable
//fun BookItem() {
//    BookItem(
//        book = Gson().fromJson(
//            """
//             "book": {
//            "name": "The Power of your subconscious Mind",
//            "author": "Joseph Murphy",
//            "publication": "Amazing Reads",
//            "imageLink": "https://images-na.ssl-images-amazon.com/images/I/51QTTApN-XL._SX324_BO1,204,203,200_.jpg",
//            "price": "198"
//        }
//        """.trimIndent(), Book::class.java
//        ),
//    )
//}


@Preview
@Composable
fun SuccessDialog(
    title: String = "Your photo was uploaded",
    subtitle: String = "Now people in your college can see the book and contact you if they are interested",
    onClick: () -> Unit = {

    }
) {
    Dialog(onDismissRequest = { }) {
        Card(shape = RoundedCornerShape(12.dp)) {
            Column(
                Modifier
                    .background(Color.White)
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally


            ) {
                Spacer(modifier = Modifier.height(20.dp))

                Card(
                    shape = CircleShape,
                    backgroundColor = Color.Green,
                    modifier = Modifier.size(60.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = title, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = subtitle, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(10.dp))
                Divider()
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "OK",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.clickable(onClick = onClick)
                )
            }
        }
    }
}

@Composable
fun findActivity() = LocalContext.current as? Context

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun DeleteButton(
    onClick: () -> Unit = {}
) {
    Card(
        onClick = onClick,
        backgroundColor = Color.LightGray,
        shape = CircleShape,
        modifier = Modifier.zIndex(12f)
    ) {
        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = "",
            tint = Color.Red,
            modifier = Modifier.padding(8.dp)

        )

    }
}