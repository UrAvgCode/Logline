package com.moritz.movieappuitest.views

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun MyMoviesView(navController: NavController){

    //Profile Layout
    Column()
    {
        Text(text = "MyMovies View")
    }
}