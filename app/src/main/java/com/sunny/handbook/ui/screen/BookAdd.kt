package com.sunny.handbook.ui.screen

import android.Manifest
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.sunny.handbook.data.RequestResult
import com.sunny.handbook.showToast
import com.sunny.handbook.ui.components.HBButton
import com.sunny.handbook.ui.components.LoadingIndicator
import com.sunny.handbook.ui.components.SuccessDialog
import com.sunny.handbook.ui.components.findActivity
import com.sunny.handbook.ui.screen.destinations.HomeDestination
import com.sunny.handbook.ui.theme.HBBlue
import com.sunny.handbook.ui.viewmodels.HomeViewModel
import com.sunny.handbook.viewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
@Destination
@Preview
fun BookAddScreen(
) {
   val viewModel: HomeViewModel = viewModel()
    val destinationsNavigator: DestinationsNavigator? = null

    var imageUrl by viewModel.imageUrl
    val permissionState = rememberPermissionState(Manifest.permission.READ_EXTERNAL_STORAGE)

    val progress by viewModel.uploadProgress.observeAsState()
    val imageUploadStatus by viewModel.imageUploadStatus.observeAsState()
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        Log.d("", "AddBadgeScreen: uri: $uri")
        if (uri != null) {
            val bitmap = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            }
            imageUrl = uri.toString()
            viewModel.uploadImage(bitmap)
        } else {
            Toast.makeText(context, "No image selected", Toast.LENGTH_SHORT).show()
        }
    }
    var bookAddVisible by remember {
        mutableStateOf(false)
    }
    val bookAdd by viewModel.bookAdd.observeAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {

            Row(
                Modifier
                    .fillMaxWidth()
                    .background(Color.Gray.copy(alpha = 0.2f))
                    .padding(horizontal = 4.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    destinationsNavigator?.navigate(HomeDestination)
                }) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "")
                }

                Text(text = "Add you book", fontWeight = FontWeight.SemiBold)
            }

            Column(
                Modifier
                    .padding(16.dp)
                    .clickable {
                        permissionState.launchPermissionRequest()

                        permissionState.let { perm ->
                            when (perm.permission) {
                                Manifest.permission.READ_EXTERNAL_STORAGE -> {
                                    when {
                                        perm.hasPermission -> {
                                            launcher.launch("image/*")
                                        }
                                        perm.shouldShowRationale -> {
                                            permissionState.launchPermissionRequest()
                                        }
                                        perm.isPermanentlyDenied() -> {
                                            permissionState.launchPermissionRequest()
                                            Toast
                                                .makeText(
                                                    context,
                                                    "This Feature required Permission, Enable in in the settings",
                                                    Toast.LENGTH_SHORT
                                                )
                                                .show()
                                        }
                                    }
                                }

                            }
                        }
                    }) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.2f)),
                ) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Filled.Image,
                                contentDescription = "",
                                tint = HBBlue
                            )

                            Text(text = "Upload Image")
                        }
                        AsyncImage(model = imageUrl, contentDescription = "")

                        if (imageUploadStatus is RequestResult.Loading) Column(
                            modifier = Modifier
                                .height(5.dp)
                                .fillMaxWidth((progress?.toFloat() ?: 0.0f) / 100f)
                                .align(Alignment.BottomStart)
                                .background(Color.Green),
                        ) {

                        }

                        if (imageUploadStatus is RequestResult.Loading) {
                            Column(
                                Modifier
                                    .zIndex(10f)
                                    .align(Alignment.Center)
                            ) {
                                CircularProgressIndicator(color = HBBlue)
                            }
                        }
                        if (imageUploadStatus is RequestResult.Error) {
                            context.showToast(imageUploadStatus?.error)
                            viewModel.imageUploadStatus.postValue(RequestResult.Idle())
                            viewModel.imageUrl.value = ""

                        }


                    }
                }
            }

            Column(
                Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                HBOutlinedTextField(value = viewModel.bookName.value,
                    hint = "book name",
                    onValueChange = {
                        viewModel.bookName.value = it
                    })
                HBOutlinedTextField(value = viewModel.bookAuthor.value,
                    hint = "Author name",
                    onValueChange = {
                        viewModel.bookAuthor.value = it

                    })
                HBOutlinedTextField(value = viewModel.publication.value,
                    hint = "Publication",
                    onValueChange = {
                        viewModel.publication.value = it

                    })
                HBOutlinedTextField(value = viewModel.price.value, hint = "Price", onValueChange = {
                    viewModel.price.value = it

                })
                HBButton(
                    title = "Add",
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = HBBlue,
                ) {

                    viewModel.addBook()

                }



                when (bookAdd) {
                    is RequestResult.Error -> {
                        findActivity()?.showToast(bookAdd?.error)
                        viewModel.bookAdd.postValue(RequestResult.Idle())
                    }
                    is RequestResult.Idle -> {

                    }
                    is RequestResult.Loading -> {

                    }
                    is RequestResult.Success -> {
                        bookAddVisible = true
                        viewModel.bookAdd.postValue(RequestResult.Idle())

                    }
                    null -> {

                    }
                }

            }

            if (bookAddVisible) {
                SuccessDialog {
                    bookAddVisible = false
                    destinationsNavigator?.popBackStack()
                    viewModel.getHomeData()
                    viewModel.resetBook()
                }
            }
        }
        if (bookAdd is RequestResult.Loading) {
            Column(
                Modifier
                    .zIndex(10f)
                    .align(Alignment.Center)
            ) {
                LoadingIndicator()
            }
        }
    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun HBOutlinedTextField(
    value: String = "RS agarwall",
    hint: String = "Enter Book Details",
    onValueChange: (String) -> Unit = {

    }
) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .onPreviewKeyEvent {
                if (it.key == Key.Tab && it.nativeKeyEvent.action == KeyEvent.ACTION_DOWN) {
                    focusManager.moveFocus(FocusDirection.Down)
                    true
                } else {
                    false
                }
            },
        placeholder = {
            Text(text = hint)
        },
        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
        maxLines = 1,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next
        ))
}

@ExperimentalPermissionsApi
fun PermissionState.isPermanentlyDenied(): Boolean {
    return !shouldShowRationale && !hasPermission
}

@Preview
@Composable
fun BookAdd() {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {

            Row(
                Modifier
                    .fillMaxWidth()
                    .background(Color.Gray.copy(alpha = 0.2f))
                    .padding(horizontal = 4.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                }) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "")
                }

                Text(text = "Add your book", fontWeight = FontWeight.SemiBold)
                
            }

            Column(
                Modifier
                    .padding(16.dp)
                    .clickable {

                    }) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.2f)),
                ) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Filled.Image,
                                contentDescription = "",
                                tint = HBBlue
                            )

                            Text(text = "Upload Image")
                        }
                        AsyncImage(model = null, contentDescription = "")

//                        if (imageUploadStatus is RequestResult.Loading) Column(
//                            modifier = Modifier
//                                .height(5.dp)
//                                .fillMaxWidth((progress?.toFloat() ?: 0.0f) / 100f)
//                                .align(Alignment.BottomStart)
//                                .background(Color.Green),
//                        ) {
//
//                        }

//                        if (imageUploadStatus is RequestResult.Loading) {
//                            Column(
//                                Modifier
//                                    .zIndex(10f)
//                                    .align(Alignment.Center)
//                            ) {
//                                CircularProgressIndicator(color = HBBlue)
//                            }
//                        }
//                        if (imageUploadStatus is RequestResult.Error) {
//                            context.showToast(imageUploadStatus?.error)
//                            viewModel.imageUploadStatus.postValue(RequestResult.Idle())
//                            viewModel.imageUrl.value = ""
//
//                        }


                    }
                }
            }

            Column(
                Modifier
                    .padding(horizontal = 16.dp)

                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                HBOutlinedTextField(value = "",
                    hint = "book name",
                    onValueChange = {
//                        viewModel.bookName.value = it
                    })
                HBOutlinedTextField(value = "",
                    hint = "Author name",
                    onValueChange = {
//                        viewModel.bookAuthor.value = it

                    })
                HBOutlinedTextField(value = "",
                    hint = "Publication",
                    onValueChange = {
//                        viewModel.publication.value = it

                    })
                HBOutlinedTextField(value = "", hint = "Price", onValueChange = {
//                    viewModel.price.value = it

                })
                HBButton(
                    title = "Add",
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = HBBlue,
                ) {

//                    viewModel.addBook()

                }



//                when (bookAdd) {
//                    is RequestResult.Error -> {
//                        findActivity()?.showToast(bookAdd?.error)
//                        viewModel.bookAdd.postValue(RequestResult.Idle())
//                    }
//                    is RequestResult.Idle -> {
//
//                    }
//                    is RequestResult.Loading -> {
//
//                    }
//                    is RequestResult.Success -> {
//                        bookAddVisible = true
//                        viewModel.bookAdd.postValue(RequestResult.Idle())
//
//                    }
//                    null -> {
//
//                    }
//                }
//
//            }
//
//            if (bookAddVisible) {
//                SuccessDialog {
//                    bookAddVisible = false
//                    destinationsNavigator?.popBackStack()
//                    viewModel.getHomeData()
//                    viewModel.resetBook()
//                }
//            }
//        }
//        if (bookAdd is RequestResult.Loading) {
//            Column(
//                Modifier
//                    .zIndex(10f)
//                    .align(Alignment.Center)
//            ) {
//                LoadingIndicator()
//            }
        }
    }

}}
