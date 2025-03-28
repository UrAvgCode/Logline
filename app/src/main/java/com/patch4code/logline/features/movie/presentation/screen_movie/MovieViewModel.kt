package com.patch4code.logline.features.movie.presentation.screen_movie

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.patch4code.logline.api.RetrofitHelper
import com.patch4code.logline.api.TmdbApiService
import com.patch4code.logline.features.core.domain.model.Movie
import com.patch4code.logline.features.core.domain.model.MovieUserData
import com.patch4code.logline.features.core.presentation.utils.MovieMapper
import com.patch4code.logline.features.core.presentation.utils.TmdbCredentials
import com.patch4code.logline.features.movie.domain.model.CountryProviders
import com.patch4code.logline.features.movie.domain.model.MovieCredits
import com.patch4code.logline.features.movie.domain.model.MovieDetails
import com.patch4code.logline.features.movie.domain.model.MovieVideo
import com.patch4code.logline.room_database.MovieDao
import com.patch4code.logline.room_database.MovieUserDataDao
import kotlinx.coroutines.launch

/**
 * GNU GENERAL PUBLIC LICENSE, VERSION 3.0 (https://www.gnu.org/licenses/gpl-3.0.html)
 *
 * MovieViewModel - ViewModel responsible for managing movie data locally and from TMDB api.
 *
 * @param movieUserDataDao The DAO for accessing movie user data from the db.
 * @author Patch4Code
 */
class MovieViewModel(
    private val movieUserDataDao: MovieUserDataDao,
    private val movieDao: MovieDao
): ViewModel(){

    private val tmdbApiService: TmdbApiService by lazy {
        RetrofitHelper.getInstance(TmdbCredentials.BASE_URL).create(TmdbApiService::class.java)
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _hasLoadError = MutableLiveData<Boolean>()
    val hasLoadError: LiveData<Boolean> get() = _hasLoadError


    private val _detailsData = MutableLiveData<MovieDetails>()
    val detailsData: LiveData<MovieDetails> get() = _detailsData

    private val _movieVideo = MutableLiveData<MovieVideo?>()
    val movieVideo: LiveData<MovieVideo?> get() = _movieVideo

    private val _countryProviders = MutableLiveData<CountryProviders?>()
    val countryProviders: LiveData<CountryProviders?> get() = _countryProviders

    private val _creditsData = MutableLiveData<MovieCredits>()
    val creditsData: LiveData<MovieCredits> get() = _creditsData

    private val _collectionMovies = MutableLiveData<List<Movie>>()
    val collectionMovies: LiveData<List<Movie>> get() = _collectionMovies

    private val _myRating = MutableLiveData<Int>()
    val myRating: LiveData<Int> get() = _myRating

    private val _onWatchlist = MutableLiveData<Boolean>()
    val onWatchlist: LiveData<Boolean> get() = _onWatchlist


    // Loads all movie data including details, credits, collection, videos, and providers from TMDB api.
    fun loadAllMovieData(movieId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _hasLoadError.value = false

                val movieDetailsResponse = tmdbApiService.getMovieDetails(movieId = movieId)
                val movieCreditsResponse = tmdbApiService.getMovieCredits(movieId = movieId)
                val movieCollectionResponse = tmdbApiService.getMoviesFromCollection(collectionId = movieDetailsResponse.body()?.collection?.id ?: 0)
                val movieVideosResponse = tmdbApiService.getMovieVideos(movieId)

                if (movieDetailsResponse.isSuccessful) {
                    _detailsData.value = movieDetailsResponse.body()
                }
                if (movieCreditsResponse.isSuccessful) {
                    _creditsData.value = movieCreditsResponse.body()
                }
                if (movieCollectionResponse.isSuccessful) {
                    _collectionMovies.value = movieCollectionResponse.body()?.movies
                }
                if (movieVideosResponse.isSuccessful) {
                    val firstYouTubeVideo = movieVideosResponse.body()?.videoList?.find { it.site == "YouTube" && it.type == "Trailer" }
                    _movieVideo.value = firstYouTubeVideo
                }

                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
                _hasLoadError.value = true
                Log.e("MovieViewModel", "Error loading movie data", e)
            }
        }
    }

    // Loads movie providers based on the provided country (from the settings) using TMDB api.
    fun loadMovieProviders(movieId: Int, country: String){
        //Log.e("MovieViewModel", "Country: $country")
        viewModelScope.launch {
            try {
                val movieProviderResponse = tmdbApiService.getWatchProviders(movieId)
                if(movieProviderResponse.isSuccessful){
                    val countryProvider = movieProviderResponse.body()?.availableServicesMap?.get(country)
                    _countryProviders.value = countryProvider
                    //Log.e("MovieViewModel", "Success: $countryProvider")
                }
            }catch (e: Exception){
                Log.e("MovieViewModel", "Error getting movie providers", e)
            }
        }
    }

    // Loads user rating and watchlist status for a movie from the db.
    fun loadRatingAndWatchlistStatusById(id: Int){
        var movieUserData: MovieUserData?
        viewModelScope.launch {
            movieUserData = movieUserDataDao.getMovieUserDataByMovieId(id)
            if (movieUserData != null){
                _myRating.value = movieUserData!!.rating
                _onWatchlist.value = movieUserData!!.onWatchlist
            }else{
                _myRating.value = -1
                _onWatchlist.value = false
            }
        }
    }

    fun updateMovieInDatabase(movieDetails: MovieDetails){
        viewModelScope.launch {
            val existingMovie = movieDao.getMovieById(movieDetails.id)
            if(existingMovie != null){
                val movie = existingMovie.copy(
                    posterUrl = movieDetails.posterPath,
                    popularity = movieDetails.popularity,
                    voteAverage = movieDetails.voteAverage,
                    runtime = movieDetails.runtime
                )
                movieDao.upsertMovie(movie)
            }
        }
    }

    // Updates the user rating for a movie by accessing the db.
    fun changeRating(rating: Int){
        var movie = MovieMapper.mapToMovie(_detailsData.value)
        movie = movie.copy(runtime = _detailsData.value?.runtime)

        viewModelScope.launch {
            movieUserDataDao.updateOrInsertRating(movie, rating)
        }
        _myRating.value = rating
    }

    // Updates the watchlist status for a movie by accessing the db.
    fun changeOnWatchlist(newOnWatchlistState: Boolean){
        var movie = MovieMapper.mapToMovie(_detailsData.value)
        movie = movie.copy(runtime = _detailsData.value?.runtime)

        viewModelScope.launch {
            movieUserDataDao.updateOrInsertOnWatchlist(movie, newOnWatchlistState)
        }
        _onWatchlist.value = newOnWatchlistState
    }
}

// Factory-class for creating MovieViewModel instances to manage access to the database
class MovieViewModelFactory(private val movieUserDataDao: MovieUserDataDao, private val movieDao: MovieDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MovieViewModel(movieUserDataDao, movieDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}