package com.patch4code.logline.features.diary.presentation.components.editelement

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.patch4code.logline.R
import com.patch4code.logline.ui.theme.ChineseBlack

/**
 * GNU GENERAL PUBLIC LICENSE, VERSION 3.0 (https://www.gnu.org/licenses/gpl-3.0.html)
 *
 * DiaryEditSaveChangesSection - Composable function representing the section of
 * the DiaryEditElementView for saving changes or discarding them.
 *
 * @author Patch4Code
 */
@Composable
fun DiaryEditSaveChangesSection(isEdit: Boolean = true, onSaveChanges: () -> Unit, onDiscardChanges: () -> Unit){

    val context = LocalContext.current

    val toastText = stringResource(id = R.string.diary_edit_save_toast_text)

    HorizontalDivider(modifier = Modifier.padding(top = 16.dp))

    Row(modifier = Modifier.fillMaxWidth().background(ChineseBlack), horizontalArrangement = Arrangement.SpaceBetween) {
        // discard DiaryEdit changes button
        IconButton(onClick = { onDiscardChanges() }) {
            Icon(imageVector = Icons.Default.Close, contentDescription = stringResource(id = R.string.diary_edit_exit_description_text))
        }

        // save DiaryEdit button with toast message
        IconButton(
            onClick = {
            if (isEdit){ Toast.makeText(context, toastText, Toast.LENGTH_LONG).show() }
            onSaveChanges()
            }
        ){
            Icon(imageVector = Icons.Default.Check, contentDescription = stringResource(id = R.string.diary_edit_save_description_text))
        }
    }
}
