package com.moritz.movieappuitest.views.list.overview

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.moritz.movieappuitest.Screen
import com.moritz.movieappuitest.dataclasses.MovieList
import com.moritz.movieappuitest.viewmodels.ListsTableTableViewModel
import com.moritz.movieappuitest.viewmodels.NavigationViewModel
import com.moritz.movieappuitest.views.list.overview.item.ListsTableItem
import com.moritz.movieappuitest.views.swipe.swipeToDeleteContainer

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ListsTableView(navController: NavController, navViewModel: NavigationViewModel, listsTableTableViewModel: ListsTableTableViewModel = viewModel()){

    LaunchedEffect(Unit) {
        navViewModel.updateScreen(Screen.ListsTableScreen)
    }

    val openAddListDialog = remember { mutableStateOf(false)  }
    val openDeleteListDialog = remember { mutableStateOf(false)  }
    val listToDelete = remember { mutableStateOf<MovieList?>(null) }

    val myUserMovieLists = listsTableTableViewModel.userMovieLists.observeAsState().value

    Scaffold (
        floatingActionButton = {
            FloatingActionButton(onClick = { openAddListDialog.value = true }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add new list")
            }
        },
        content = {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                itemsIndexed(
                    items = myUserMovieLists ?: emptyList(),
                    key = { _, item -> item.hashCode() }
                ) { _, list ->
                    swipeToDeleteContainer(
                        item = list,
                        onDelete = {
                            listToDelete.value = list
                            openDeleteListDialog.value = true
                        }
                    ) {
                        ListsTableItem(navController, list)
                    }
                }
            }
            AddListDialog(
                openAddListDialog = openAddListDialog.value,
                onSave = { listName, isPublic ->
                    openAddListDialog.value = false
                    listsTableTableViewModel.addUserMovieList(MovieList(listName, isPublic, mutableListOf()))
                },
                onCancel = {openAddListDialog.value = false}
            )
            DeleteListDialog(
                openDeleteListDialog = openDeleteListDialog.value,
                onDelete = {
                    listToDelete.value?.let { listToDelete ->
                        listsTableTableViewModel.removeUserMovieList(listToDelete)
                    }
                    listToDelete.value = null
                    openDeleteListDialog.value = false
                },
                onCancel = {
                    listsTableTableViewModel.updateUserMovieLists()
                    openDeleteListDialog.value = false

                    //workaround with scene reload
                    navController.navigate(Screen.ListsTableScreen.route){
                        popUpTo(Screen.ListsTableScreen.route){
                            inclusive = true
                        }
                    }
                }
            )
        }
    )
}