package com.sunny.handbook.ui.components

import android.view.KeyEvent.ACTION_DOWN
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HBTextField(
    modifier: Modifier = Modifier,
    value: String,
    hint: String,
    startIcon: ImageVector? = null,
    endIcon: ImageVector? = null,
    keyboardStyle: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    var passwordVisible by remember { mutableStateOf(endIcon == null) }
    TextField(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .onPreviewKeyEvent {
                if (it.key == Key.Tab && it.nativeKeyEvent.action == ACTION_DOWN) {
                    focusManager.moveFocus(FocusDirection.Down)
                    true
                } else {
                    false
                }
            },

        value = value,
        onValueChange = onValueChange,
        leadingIcon = {
            startIcon?.let {
                Icon(imageVector = startIcon, contentDescription = null, tint = Color.Black)
            }
        },
        trailingIcon = {
            endIcon?.let {
                IconButton(onClick = {
                    passwordVisible = passwordVisible.not()
                }) {
                    Icon(imageVector = endIcon, contentDescription = null, tint = Color.Black)
                }

            }
        },
        placeholder = {
            Row() {
                Text(text = hint, color = Color.Black)
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardStyle,
            imeAction = ImeAction.Next
        ),
        shape = CircleShape,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            textColor = Color.Black

        ),
        maxLines = 1,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        )

    )
}

@Composable
fun HBButton(
    modifier: Modifier = Modifier,
    title: String,
    backgroundColor: Color = Color.White,
    onClick: () -> Unit
) {

    Button(
        onClick = onClick,
        shape = CircleShape,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = backgroundColor
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}