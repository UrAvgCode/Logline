package com.patch4code.logline.features.profile.presentation.components.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.patch4code.logline.features.profile.domain.model.ProfileNavigationElement

/**
 * GNU GENERAL PUBLIC LICENSE, VERSION 3.0 (https://www.gnu.org/licenses/gpl-3.0.html)
 *
 * ProfileNavigation - Composable function for displaying the profile navigation elements.
 * Uses predefined list of profile navigation elements and displays a custom navigation element for each item.
 * Provides navigation to predefined destination on click (MyMovies, Diary, Reviews and Lists).
 *
 * @author Patch4Code
 */
@Composable
fun ProfileNavigation(navController: NavController){

    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfileNavigationElement().getProfileNavigationElements(context).forEach { item ->
            Box(modifier = Modifier
                .padding(12.dp)
                .clickable { navController.navigate(item.route) }) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(imageVector = item.navIcon, contentDescription = item.title)
                    Text(text = item.title)
                }
            }
        }
    }
}