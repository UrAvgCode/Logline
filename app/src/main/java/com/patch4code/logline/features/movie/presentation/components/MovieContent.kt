package com.patch4code.logline.features.movie.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.patch4code.logline.features.core.domain.model.Movie
import com.patch4code.logline.features.movie.domain.model.CountryProviders
import com.patch4code.logline.features.movie.domain.model.MovieCredits
import com.patch4code.logline.features.movie.domain.model.MovieDetails
import com.patch4code.logline.features.movie.domain.model.MovieVideo
import com.patch4code.logline.features.movie.presentation.components.cast_and_crew.MovieCastAndCrew
import com.patch4code.logline.features.movie.presentation.components.header.MovieHeader
import com.patch4code.logline.features.movie.presentation.components.watch_providers.MovieWatchProviders
import com.patch4code.logline.features.movie.presentation.screen_movie.MovieViewModel
import com.patch4code.logline.room_database.LoglineDatabase

/**
 * GNU GENERAL PUBLIC LICENSE, VERSION 3.0 (https://www.gnu.org/licenses/gpl-3.0.html)
 *
 * MovieContent - Composable function that displays the content of a movie including header-section
 * (with poster, title, year, length and MovieHeaderToolbar), description, rating, genres, featuresBar
 * (with trailer-, reviews- and share-button), watch-providers, cast and crew, more details, more movies
 * like the current and the source reference.
 *
 * @author Patch4Code
 */
@Composable
fun MovieContent(
    movieDetails: MovieDetails?,
    movieCredits: MovieCredits?,
    collectionMovies: List<Movie>?,
    movieVideo: MovieVideo?,
    movieProviders: CountryProviders?,
    watchCountry: String?,
    openPosterPopup: MutableState<Boolean>,
    navController: NavController,
    movieViewModel: MovieViewModel,
    db: LoglineDatabase
) {

    LazyColumn (modifier = Modifier.padding(16.dp)){
        item {
            MovieHeader(
                movieDetails = movieDetails,
                movieViewModel = movieViewModel,
                db = db,
                onPosterClick = { openPosterPopup.value = true }
            )
            MovieDescription(movieDetails?.tagline, movieDetails?.overview)
            MovieRatings(movieDetails?.voteAverage)
            MovieGenres(movieDetails?.genres)

            MovieFeaturesBar(movieVideo, movieDetails, navController)
            MovieWatchProviders(movieProviders, watchCountry, movieDetails?.id)

            MovieCastAndCrew(movieCredits, navController)
            MovieMoreDetails(movieDetails)
            MovieMoreLikeThis(navController, collectionMovies, movieDetails?.title)
            MovieOtherSites(movieDetails)
            MovieSourceReference()
        }
    }
    MoviePosterPopup(openPosterPopup.value, movieDetails, onPosterPopupClose = {openPosterPopup.value = false})
}