package com.patch4code.loglinemovieapp.features.core.presentation.components.movie_search_dialog

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.patch4code.loglinemovieapp.features.core.domain.model.Movie
import com.patch4code.loglinemovieapp.features.list.presentation.components.list.items.MovieListAddMovieCard

/**
 * GNU GENERAL PUBLIC LICENSE, VERSION 3.0 (https://www.gnu.org/licenses/gpl-3.0.html)
 *
 * MovieSearchDialogLazyColumn - Composable function for displaying movie search results in a lazy column
 *
 * @author Patch4Code
 */
@Composable
fun MovieSearchDialogLazyColumn(
    searchResult: List<Movie>?,
    selectedMovie: MutableState<Movie?>
){

    // Lazy Column of movies that are clickable to select one
    LazyColumn (modifier = Modifier.padding(top = 8.dp)) {
        searchResult?.forEach{ movie->
            item{
                Card(colors = CardDefaults.cardColors(containerColor = if(selectedMovie.value == movie) Color.Gray else Color.Transparent)) {
                    MovieListAddMovieCard(
                        movie = movie,
                        selectMovie = {clickedMovie->
                            selectedMovie.value = clickedMovie
                        }
                    )
                }
            }
        }
    }
}