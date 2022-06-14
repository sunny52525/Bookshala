package com.sunny.handbook

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.json.JSONObject
import retrofit2.HttpException

inline fun Modifier.noRippleClickable(crossinline onClick: () -> Unit): Modifier = composed {
    clickable(indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}


const val BASE_URL = "https://bookshala.herokuapp.com/"

fun Context?.showToast(msg: String?) {
    Toast.makeText(this, msg.toString(), Toast.LENGTH_SHORT).show()
}

fun Boolean.then(callable: () -> Unit) {
    if (this)
        callable.invoke()
}

fun Exception.parseErrorMessage(): String {
    var errorMessage = message
    return when (this) {
        is HttpException -> {
            errorMessage = response()?.errorBody()?.string()
            try {
                JSONObject(errorMessage ?: "").getString("message")
            } catch (e: Exception) {
                errorMessage.toString()
            }
        }
        else -> {
            errorMessage.toString()
        }
    }
}

fun Any?.isNotNull(): Boolean {
    return this != null
}

fun Modifier.debug(
    width: Dp = 1.dp,
    color: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Red,
): Modifier {
    return then(Modifier.border(width, color))
}