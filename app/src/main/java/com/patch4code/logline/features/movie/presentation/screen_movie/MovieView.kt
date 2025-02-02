package com.patch4code.logline.features.movie.presentation.screen_movie

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.patch4code.logline.features.core.presentation.components.load.LoadErrorDisplay
import com.patch4code.logline.features.core.presentation.components.load.LoadingIndicator
import com.patch4code.logline.features.core.presentation.utils.JSONHelper.toJson
import com.patch4code.logline.features.core.presentation.utils.MovieMapper
import com.patch4code.logline.features.movie.presentation.components.MovieContent
import com.patch4code.logline.features.movie.presentation.utils.MovieTopBarNavigationHelper
import com.patch4code.logline.features.navigation.domain.model.Screen
import com.patch4code.logline.features.navigation.presentation.components.topbar_providers.ProvideCustomTopBarBackNavigationIcon
import com.patch4code.logline.features.navigation.presentation.components.topbar_providers.ProvideTopBarTitle
import com.patch4code.logline.preferences_datastore.StoreSettings
import com.patch4code.logline.room_database.LoglineDatabase
import java.net.URLEncoder

/**
 * GNU GENERAL PUBLIC LICENSE, VERSION 3.0 (https://www.gnu.org/licenses/gpl-3.0.html)
 *
 * MovieView - Composable function representing the movie screen view.
 * Displays details and related information about a movie.
 *
 * @author Patch4Code
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MovieView(
    navController: NavController,
    db: LoglineDatabase,
    id: String?,
    movieViewModel: MovieViewModel = viewModel(
        factory = MovieViewModelFactory(db.movieUserDataDao, db.movieDao)
    )
){

    val context = LocalContext.current
    val movieId = id?.toIntOrNull() ?: 0

    val openPosterPopup = remember { mutableStateOf(false)  }

    LaunchedEffect(Unit) {
        movieViewModel.loadRatingAndWatchlistStatusById(movieId)
        movieViewModel.loadAllMovieData(movieId)
    }

    // TopBar config
    ProvideTopBarTitle(title = Screen.MovieScreen.title.asString())
    ProvideCustomTopBarBackNavigationIcon{
        MovieTopBarNavigationHelper.handleBackNavigation(navController, openPosterPopup)
    }

    val movieDetails = movieViewModel.detailsData.observeAsState().value
    val movieCredits = movieViewModel.creditsData.observeAsState().value
    val movieVideo = movieViewModel.movieVideo.observeAsState().value
    val movieProviders = movieViewModel.countryProviders.observeAsState().value
    val collectionMovies = movieViewModel.collectionMovies.observeAsState().value

    val dataSettingsStore = remember { StoreSettings(context) }
    val watchCountry = dataSettingsStore.getWatchProvidersCountry.collectAsState(initial = "").value

    DisposableEffect(movieDetails) {
        if(!watchCountry.isNullOrEmpty()){
            movieViewModel.loadMovieProviders(movieId, watchCountry)
        }
        if(movieDetails != null){
            movieViewModel.updateMovieInDatabase(movieDetails)
        }
        onDispose {}
    }

    val isLoading by movieViewModel.isLoading.observeAsState(initial = false)
    val hasLoadError by movieViewModel.hasLoadError.observeAsState(initial = false)

    if(isLoading){
        LoadingIndicator()
    }else if(hasLoadError){
        LoadErrorDisplay(onReload = { movieViewModel.loadAllMovieData(movieId) })
    }else{
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    var movieToLog = MovieMapper.mapToMovie(movieDetails)
                    movieToLog = movieToLog.copy(runtime = movieDetails?.runtime)
                    val jsonMovie = movieToLog.toJson()
                    val encodedJsonMovie = URLEncoder.encode(jsonMovie, "UTF-8")
                    navController.navigate(Screen.MovieLogScreen.withArgs(encodedJsonMovie))
                }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                }
            }
        ){
            MovieContent(
                movieDetails = movieDetails,
                movieCredits = movieCredits,
                collectionMovies = collectionMovies,
                movieVideo = movieVideo,
                movieProviders = movieProviders,
                watchCountry = watchCountry,
                openPosterPopup = openPosterPopup,
                navController = navController,
                movieViewModel = movieViewModel,
                db = db
            )
        }
    }
}