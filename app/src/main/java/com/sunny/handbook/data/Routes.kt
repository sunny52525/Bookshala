package com.sunny.handbook.data

sealed class Routes(val route:String){
    object Home:Routes("home")
    object BookDetail:Routes("book_detail")
    object BookAdd:Routes("book_add")
    object BuyBook:Routes("buy_book")
}
