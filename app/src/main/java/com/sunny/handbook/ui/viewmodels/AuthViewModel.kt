package com.sunny.handbook.ui.viewmodels

import android.text.TextUtils
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sunny.handbook.data.AuthResponse
import com.sunny.handbook.data.LoginRequest
import com.sunny.handbook.data.RegisterRequest
import com.sunny.handbook.data.RequestResult
import com.sunny.handbook.parseErrorMessage
import com.sunny.handbook.ui.network.RetrofitClient.RETROFIT
import kotlinx.coroutines.launch

/**
 * {
"name":"Sunny",
"student_roll":"2019404022",
"phone":"7004468293",
"email":"kumarsunny3232@gmail.com",
"room_number":"708",
"building":"HR1",
"password":"help"
}
 */

fun MutableState<String>.isBlank(): Boolean {
    return value.isBlank()
}

fun MutableState<String>.trim(): String {
    return value.trim()
}

class AuthViewModel : ViewModel() {

    var name = mutableStateOf("")
    var studentRoll = mutableStateOf("")
    var phone = mutableStateOf("")
    var email = mutableStateOf("")
    var roomNumber = mutableStateOf("")
    var building = mutableStateOf("")
    var password = mutableStateOf("")
    var confirmPassword = mutableStateOf("")
    var isRegister = mutableStateOf(false)

    var response = MutableLiveData<RequestResult<AuthResponse>>(RequestResult.Idle())
    fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this)
            .matches()
    }

    private fun loginValidate(onValidated: () -> Unit) {
        if (email.value.isEmailValid()) {
            onValidated()
        } else {
            response.value = RequestResult.Error("Not a valid email")

        }
    }

    fun validateRegister(onValidated: () -> Unit) {
        when {
            email.value.isBlank() -> {
                response.value = RequestResult.Error("Email cannot be blank")
            }
            name.value.isEmpty() -> {
                response.value = RequestResult.Error("Name cannot be empty")
            }
            studentRoll.isBlank() -> {
                response.value = RequestResult.Error("Roll cannot be empty")
            }
            phone.isBlank() -> {
                response.value = RequestResult.Error("Phone cannot be empty")
            }
            roomNumber.isBlank() -> {
                response.value = RequestResult.Error("Room number cannot be empty")
            }
            building.isBlank() -> {
                response.value = RequestResult.Error("Building cannot be empty")
            }
            password.isBlank() -> {
                response.value = RequestResult.Error("Password cannot be empty")
            }
            email.value.isEmailValid().not() -> {
                response.value = RequestResult.Error("Invalid Email")
            }
            password.value != confirmPassword.value -> {
                response.value = RequestResult.Error("Password don't match")
            }
            else -> {
                onValidated()
            }
        }
    }

    fun login() = loginValidate {
        viewModelScope.launch {
            response.value = RequestResult.Loading()
            try {
                RETROFIT.login(
                    login = LoginRequest(
                        email = email.trim(),
                        password = password.trim()
                    )
                ).let {
                    response.value = RequestResult.Success(it)
                }
            } catch (e: Exception) {
                response.value = RequestResult.Error(e.parseErrorMessage())
            }
        }
    }


    fun register() = validateRegister {
        viewModelScope.launch {
            response.value = RequestResult.Loading()
            try {
                RETROFIT.register(
                    registerRequest = RegisterRequest(
                        name = name.trim(),
                        student_roll = studentRoll.trim(),
                        phone = phone.trim(),
                        email = email.trim(),
                        room_number = roomNumber.trim(),
                        building = building.trim(),
                        password = password.trim()
                    )
                ).let {
                    response.value = RequestResult.Success(it)
                }
            } catch (e: Exception) {
                response.value = RequestResult.Error(e.parseErrorMessage())
            }
        }
    }


}