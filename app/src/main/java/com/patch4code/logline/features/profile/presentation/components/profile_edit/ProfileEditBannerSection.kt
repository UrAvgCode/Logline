package com.patch4code.logline.features.profile.presentation.components.profile_edit

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.patch4code.logline.R
import com.patch4code.logline.features.navigation.domain.model.Screen
import com.patch4code.logline.features.profile.domain.model.UserProfile
import com.patch4code.logline.features.profile.presentation.screen_profile.ProfileViewModel
import com.patch4code.logline.features.profile.presentation.utils.ProfileEditExtensions

/**
 * GNU GENERAL PUBLIC LICENSE, VERSION 3.0 (https://www.gnu.org/licenses/gpl-3.0.html)
 *
 * ProfileEditBannerSection - Composable function for changing the banner image of the user profile.
 *
 * @author Patch4Code
 */
@Composable
fun ProfileEditBannerSection(userProfile: UserProfile?, profileViewModel: ProfileViewModel, navController: NavController){

    val context = LocalContext.current

    val bannerImagePath = userProfile?.bannerImagePath

    // Activity result launcher for picking a visual media (image) from the device
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { localUri ->
            // Save the image locally in the app's internal storage
            val internalMemoryUri = localUri?.let { ProfileEditExtensions.saveImageToStorage(context, it, "banner_image.jpg") }
            if (internalMemoryUri != null){
                // Set the banner image path in the db via ViewModel
                profileViewModel.setBannerImagePath(internalMemoryUri.toString())
                //refresh
                navController.navigate(
                    Screen.ProfileEditScreen.route,
                    builder = {
                        popUpTo(Screen.ProfileEditScreen.route) {
                            inclusive = true
                        }
                    }
                )
            }
        }
    )

    Column {
        Text(text = stringResource(id = R.string.banner_image_title), style = MaterialTheme.typography.labelLarge, modifier = Modifier.padding(8.dp))
        // card showing current banner image, launches singlePhotoPickerLauncher onClick
        Card (
            onClick = {
                singlePhotoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            },
            modifier = Modifier
                .height(120.dp)
                .fillMaxWidth()
                .border(width = 2.dp, color = Color.DarkGray)
        ){
            AsyncImage(
                model = bannerImagePath,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                contentDescription = stringResource(id = R.string.banner_image_description),
                error = painterResource(id = R.drawable.default_banner_image)
            )
        }
        // reset button that empties the banner image path in the db via ViewModel
        TextButton(
            onClick = {
                ProfileEditExtensions.deleteFile(bannerImagePath)
                profileViewModel.setBannerImagePath("")
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = stringResource(id = R.string.reset_to_default_text))
        }
    }
}