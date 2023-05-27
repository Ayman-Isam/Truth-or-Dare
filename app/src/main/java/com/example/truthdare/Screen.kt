package com.example.truthdare

sealed class Screen (val route: String){
    object Home: Screen(route = "home_screen")
    object Add: Screen(route = "add_screen")
    object View: Screen(route = "view_screen")
}