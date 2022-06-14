package com.sunny.handbook.ui.screen

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sunny.handbook.Home
import com.sunny.handbook.data.PreferenceManager
import com.sunny.handbook.data.RequestResult
import com.sunny.handbook.showToast
import com.sunny.handbook.ui.components.HBButton
import com.sunny.handbook.ui.components.HBTextField
import com.sunny.handbook.ui.theme.HBBlue
import com.sunny.handbook.ui.viewmodels.AuthViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview
fun LoginScreen() {

    val viewModel: AuthViewModel = viewModel()
    var email by viewModel.email
    var password by viewModel.password
    var roll by viewModel.studentRoll
    var room by viewModel.roomNumber
    var building by viewModel.building
    var phone by viewModel.phone
    var confirmPws by viewModel.confirmPassword
    var name by viewModel.name
    var isRegister by viewModel.isRegister

    val response by viewModel.response.observeAsState()

    val context = LocalContext.current as? Activity
    Box(
        Modifier
            .fillMaxSize()
            .background(HBBlue)
            .padding(top = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Welcome",
                    fontSize = 40.sp,
                    color = Color.White,
                    fontFamily = FontFamily.Serif
                )
            }

            item(key = "Email") {
                Column(Modifier.animateItemPlacement()) {
                    HBTextField(
                        value = email,
                        hint = "Email",
                        startIcon = Icons.Filled.Person,
                        onValueChange = {
                            email = it

                        }
                    )

                }
            }
            if (isRegister) {
                item(key = "name") {
                    HBTextField(
                        value = name,
                        hint = "Name",
                        startIcon = Icons.Filled.Info,
                        onValueChange = {
                            name = it
                        },
                        modifier = Modifier.animateItemPlacement()
                    )
                }
                item(key = "roll") {
                    HBTextField(
                        value = roll,
                        hint = "Roll number",
                        startIcon = Icons.Filled.Info,
                        onValueChange = {
                            roll = it
                        },
                        modifier = Modifier.animateItemPlacement()
                    )
                }
                item(key = "room") {
                    Row(
                        Modifier
                            .fillMaxWidth()
                    ) {
                        HBTextField(
                            value = room,
                            hint = "Room no.",
                            startIcon = Icons.Filled.Info,
                            onValueChange = {
                                room = it
                            },
                            modifier = Modifier
                                .animateItemPlacement()
                                .fillMaxWidth(0.5f)
                        )
                        HBTextField(
                            value = building,
                            hint = "Building",
                            startIcon = Icons.Filled.Info,
                            onValueChange = {
                                building = it
                            },
                            modifier = Modifier
                                .animateItemPlacement()
                                .fillMaxWidth(1f)
                        )
                    }
                }

                item(key = "phone") {
                    HBTextField(
                        value = phone,
                        hint = "phone number",
                        startIcon = Icons.Filled.Info,
                        onValueChange = {
                            phone = it
                        },
                        modifier = Modifier.animateItemPlacement()
                    )
                }
            }

            item(key = "password") {
                HBTextField(
                    value = password,
                    hint = "Password",
                    startIcon = Icons.Filled.Lock,
                    onValueChange = {
                        password = it
                    },
                    keyboardStyle = KeyboardType.Password,
                    endIcon = Icons.Filled.Visibility,
                    modifier = Modifier.animateItemPlacement()
                )
            }
            if (isRegister)
                item(key = "confirmPws") {
                    HBTextField(
                        value = confirmPws,
                        hint = "Confirm password",
                        startIcon = Icons.Filled.Lock,
                        onValueChange = {
                            confirmPws = it
                        },
                        keyboardStyle = KeyboardType.Password,
                        endIcon = Icons.Filled.Visibility,
                        modifier = Modifier.animateItemPlacement()
                    )
                }
            item {
                HBButton(title = if (isRegister) "Register" else "Login") {
                    if (isRegister) {
                        viewModel.register()
                    } else {
                        viewModel.login()
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(50.dp))
                HBButton(title = if (!isRegister) "Register" else "Login") {
                    isRegister = isRegister.not()
                }
            }
        }
        when (response) {
            is RequestResult.Idle -> {

            }
            is RequestResult.Loading -> {

            }
            is RequestResult.Success -> {
                PreferenceManager.isLogin = true
                PreferenceManager.token = response?.data?.token
                response?.data?.User?.let {
                    PreferenceManager.user = it
                }
                context?.run {
                    Intent(this, Home::class.java).run {
                        startActivity(this)
                    }
                    finish()
                }
            }
            null -> Unit
        }

        if (response is RequestResult.Loading)
            CircularProgressIndicator(
                modifier = Modifier
                    .zIndex(10f)
                    .align(Alignment.Center)
            )

        if (response is RequestResult.Error) {
            context.showToast(response?.error)
        }

    }
}