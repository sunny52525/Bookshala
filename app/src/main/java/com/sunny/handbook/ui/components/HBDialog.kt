package com.sunny.handbook.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HBDialog(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {

   Dialog(onDismissRequest = onClick) {
       Card(shape = RoundedCornerShape(10.dp)) {
           Column(modifier = Modifier.padding(16.dp)) {
               Spacer(modifier = Modifier.height(16.dp))
               Text(text = title)
               Spacer(modifier = Modifier.height(10.dp))
               Text(text = subtitle)
               Spacer(modifier = Modifier.height(10.dp))

               Card(
                   onClick = onClick, border = BorderStroke(
                       width = 1.dp,
                       color = Color.Green
                   )
               ) {
                   Text(text = "Okay")

               }
           }
       }
   }


}


@Preview
@Composable
fun HBDialog() {
    HBDialog(title = "SUCCESS", subtitle = "Login was successfull") {

    }
}