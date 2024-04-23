package com.patch4code.loglinemovieapp.features.reviews.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.patch4code.loglinemovieapp.R
import com.patch4code.loglinemovieapp.features.core.presentation.utils.MovieHelper

@Composable
fun ReviewDetailsPoster(movieTitle: String, posterPath: String, onPosterPressed:()->Unit){

    val moviePosterUrl: String = MovieHelper.processPosterUrl(posterPath)

    Card (modifier = Modifier
        .height(200.dp)
        .width(133.dp)
        .clickable { onPosterPressed() },
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
    ) {
        AsyncImage(
            model = moviePosterUrl,
            contentDescription = "$movieTitle${stringResource(id = R.string.poster_description_appendage)}",
            error = painterResource(id = R.drawable.movie_poster_placeholder)
        )
    }
}