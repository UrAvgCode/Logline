package com.patch4code.logline.features.list.presentation.screen_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.patch4code.logline.features.core.domain.model.SortOption
import com.patch4code.logline.features.list.domain.model.ListTableSortOptions
import com.patch4code.logline.features.list.domain.model.MovieList
import com.patch4code.logline.features.list.domain.model.MovieWithListItem
import com.patch4code.logline.room_database.MovieInListDao
import com.patch4code.logline.room_database.MovieListDao
import kotlinx.coroutines.launch

/**
 * GNU GENERAL PUBLIC LICENSE, VERSION 3.0 (https://www.gnu.org/licenses/gpl-3.0.html)
 *
 * ListsTableView - ViewModel responsible for managing data related to the lists table screen.
 * Functionality for deleting and adding lists communicating with db.
 *
 * @param movieListDao The DAO for accessing movie list data from the db.
 * @author Patch4Code
 */
class ListsTableViewModel(private val movieListDao: MovieListDao, private val movieInListDao: MovieInListDao) : ViewModel(){

    private val _userMovieLists = MutableLiveData<List<MovieList>>()
    val userMovieLists: LiveData<List<MovieList>> get() = _userMovieLists

    private val _moviesInLists = MutableLiveData<List<MovieWithListItem>>()
    val moviesInLists: LiveData<List<MovieWithListItem>> get() = _moviesInLists

    // Gets the list of user movie lists from the database.
    fun getMovieLists(sortOption: SortOption){
        if (sortOption !in ListTableSortOptions.options) {
            throw IllegalArgumentException("Unsupported sort option for ListTable: $sortOption")
        }

        viewModelScope.launch {
            val sortedMovieLists = when (sortOption) {
                SortOption.ByTimeUpdated -> movieListDao.getMovieListsOrderedByTimeUpdated()
                SortOption.ByListNameAsc -> movieListDao.getMovieListsOrderedByNameAsc()
                SortOption.ByListNameDesc -> movieListDao.getMovieListsOrderedByNameDesc()
                SortOption.ByAddedAsc -> movieListDao.getMovieListsOrderedByTimeCreatedAsc()
                SortOption.ByAddedDesc -> movieListDao.getMovieListsOrderedByTimeCreatedDesc()
                else -> emptyList()
            }
            _userMovieLists.value = sortedMovieLists
        }
    }
    fun getMoviesInLists(){
        viewModelScope.launch {
            _moviesInLists.value = movieInListDao.getAllMovieWithListItems()
        }
    }

    // Adds a new movie list to the database and updates the user movie lists
    fun addMovieList(movieList: MovieList, sortOption: SortOption) {
        viewModelScope.launch {
            movieListDao.upsertMovieList(movieList)
            getMovieLists(sortOption)
        }
    }
    // Removes a movie list from the database and updates the user movie lists
    fun removeMovieList(listId: String, sortOption: SortOption) {
        viewModelScope.launch {
            movieListDao.deleteMovieListById(listId)
            movieInListDao.deleteAllMoviesFromList(listId)
            getMovieLists(sortOption)
        }
    }
}

// Factory-class for creating ListsTableViewModel instances to manage access to the database
class ListsTableViewModelFactory(private val movieListDao: MovieListDao, private val movieInListDao: MovieInListDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListsTableViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ListsTableViewModel(movieListDao, movieInListDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}