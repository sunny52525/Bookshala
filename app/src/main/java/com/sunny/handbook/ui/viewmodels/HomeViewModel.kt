package com.sunny.handbook.ui.viewmodels

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.sunny.handbook.data.*
import com.sunny.handbook.parseErrorMessage
import com.sunny.handbook.ui.network.RetrofitClient
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class HomeViewModel : ViewModel() {

    val homeData = MutableLiveData<RequestResult<List<HomeResponse>>>(RequestResult.Idle())
    val bookAdd = MutableLiveData<RequestResult<BaseResponse>>(RequestResult.Idle())
    var uploadProgress = MutableLiveData(0.0)
    var imageUploadStatus = MutableLiveData<RequestResult<Int>>(RequestResult.Idle())

    val deleteBook= MutableLiveData<RequestResult<BaseResponse>>(RequestResult.Idle())

    // Book add stuff
    var imageUrl = mutableStateOf("")
    var bookName = mutableStateOf("")
    var bookAuthor = mutableStateOf("")
    var publication = mutableStateOf("")
    var price = mutableStateOf("")


    fun resetBook() {
        imageUrl.value = ""
        bookName.value = ""
        bookAuthor.value = ""
        publication.value = ""
        price.value = ""
        uploadProgress.value = 0.0
    }

    fun validateBook(onValidated: () -> Unit) {
        when {
            bookName.isBlank() -> {
                bookAdd.postValue(RequestResult.Error("Book name cannot be empty"))
            }
            bookAuthor.isBlank() -> {
                bookAdd.postValue(RequestResult.Error("Book Author cannot be empty"))
            }
            publication.isBlank() -> {
                bookAdd.postValue(RequestResult.Error("Book Publication cannot be empty"))
            }
            price.isBlank() -> {
                bookAdd.postValue(RequestResult.Error("Price cannot be empty"))
            }
            else -> {
                onValidated()
            }
        }
    }

    init {
        getHomeData()
    }

    fun getHomeData() {
        homeData.postValue(RequestResult.Loading())
        viewModelScope.launch {
            try {
                val result = RetrofitClient.RETROFIT.home()
                homeData.postValue(RequestResult.Success(result))
            } catch (e: Exception) {
                homeData.postValue(RequestResult.Error(e.parseErrorMessage()))
            }
        }
    }

    fun addBook() = validateBook {
        bookAdd.postValue(RequestResult.Loading())
        viewModelScope.launch {
            try {
                val result = RetrofitClient.RETROFIT.addBook(
                    homeResponse = HomeResponse(
                        book = Book(
                            author = bookAuthor.value,
                            imageLink = imageUrl.value,
                            name = bookName.value,
                            price = price.value,
                            publication = publication.value

                        ),
                        user = PreferenceManager.user
                    )
                )
                bookAdd.postValue(RequestResult.Success(result))
            } catch (e: Exception) {
                bookAdd.postValue(RequestResult.Error(e.parseErrorMessage()))

            }
        }
    }


    fun deleteBook(id: String) {
        deleteBook.postValue(RequestResult.Loading())
        viewModelScope.launch {
            try {
                val result = RetrofitClient.RETROFIT.deleteBook(id)
                deleteBook.postValue(RequestResult.Success(result))
            } catch (e: Exception) {
                deleteBook.postValue(RequestResult.Error(e.parseErrorMessage()))
            }
        }
    }

    fun uploadImage(bitmap: Bitmap?) {


        imageUploadStatus.postValue(RequestResult.Loading())

        val storageRef = Firebase.storage.reference
        val imageRef =
            storageRef.child("images/${System.currentTimeMillis()}")
        val baos = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        val uploadTask = imageRef.putBytes(data)
        uploadTask.addOnProgressListener {
            val progress: Double =
                100.0 * it.bytesTransferred / it.totalByteCount
            uploadProgress.value = progress
            Log.d("TAG", "uploadImage: $progress")
        }.addOnCompleteListener {
            if (it.isSuccessful) {
                imageRef.downloadUrl.addOnSuccessListener {
                    imageUrl.value = it.toString()
                    imageUploadStatus.postValue(RequestResult.Success(100))

                }

            }
        }.addOnFailureListener {
            imageUrl.value = ""
            imageUploadStatus.postValue(RequestResult.Error(it.message))
        }


    }
}