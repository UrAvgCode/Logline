package com.patch4code.logline.features.list.presentation.components.lists_table.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.patch4code.logline.R

/**
 * GNU GENERAL PUBLIC LICENSE, VERSION 3.0 (https://www.gnu.org/licenses/gpl-3.0.html)
 *
 * AddListDialog - Composable function displaying a dialog for adding a new list.
 *
 * @author Patch4Code
 */
@Composable
fun AddListDialog(openAddListDialog: Boolean, onSave:(listName: String)->Unit, onCancel:()->Unit){

    if(openAddListDialog){

        val textInput = remember { mutableStateOf("") }
        val isRanked = remember { mutableStateOf(false) } //not used yet

        AlertDialog(
            onDismissRequest = { onCancel() },
            title = { Text(text = stringResource(id = R.string.add_list_dialog_title)) },
            text = {
                Column {
                    OutlinedTextField(
                        value = textInput.value,
                        onValueChange = { textInput.value = it },
                        label = { Text(text = stringResource(id = R.string.list_dialog_text_field_label)) },
                        singleLine = true
                    )

                    /*
                    Row (modifier = Modifier.padding(top = 8.dp)){
                        Switch(
                            checked = isRanked.value,
                            onCheckedChange = {
                                isRanked.value = it
                            }
                        )
                        Text(text = "Public", modifier = Modifier.padding(start = 8.dp).align(
                            Alignment.CenterVertically
                        ), )
                    }
                    */
                }
            },
            confirmButton = {
                Button(onClick = { onSave(textInput.value) }) {
                    Text(text = stringResource(id = R.string.save_button_text))
                }
            },
            dismissButton = {
                Button(onClick = { onCancel() }) {
                    Text(text = stringResource(id = R.string.cancel_button_text))
                }
            }
        )
    }
}