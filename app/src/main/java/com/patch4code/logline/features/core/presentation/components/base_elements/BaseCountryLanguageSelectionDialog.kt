package com.patch4code.logline.features.core.presentation.components.base_elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.patch4code.logline.R
import com.patch4code.logline.features.core.presentation.components.modifier.setPaddingBasedOnApiLvl

/**
 * GNU GENERAL PUBLIC LICENSE, VERSION 3.0 (https://www.gnu.org/licenses/gpl-3.0.html)
 *
 * BaseCountryLanguageSelectionDialog - Composable function for displaying a dialog to select
 * countries or languages. Allows toggling selection, displays a customizable title, and includes
 * a close button.
 *
 * @author Patch4Code
 */

@Composable
fun BaseCountryLanguageSelectionDialog(
    showDialog: MutableState<Boolean>,
    items: Map<String, String>,
    title: String,
    isSelected: (String) -> Boolean,
    onItemToggle: (String) -> Unit,
    onClose: () -> Unit
) {
    if (!showDialog.value) return

    Dialog(
        onDismissRequest = {
            onClose()
        },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f)) {
                    val sortedItems = items.entries.sortedBy { it.value }
                    sortedItems.forEach { item ->
                        item {
                            CountryLanguageItem(
                                language = item,
                                isSelected = isSelected(item.key),
                                onSelect = { onItemToggle(item.key) }
                            )
                        }
                    }
                }
                Button(
                    onClick = onClose,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = setPaddingBasedOnApiLvl(64.dp))
                ) {
                    Text(stringResource(id = R.string.close_button_text))
                }
            }
        }
    }
}

@Composable
fun CountryLanguageItem(language: Map.Entry<String, String>, isSelected: Boolean, onSelect: () -> Unit
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable(onClick = onSelect)
        .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = language.value, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = stringResource(id = R.string.selected_description),
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.alpha(if(isSelected) 1.0F else 0.0F)
        )
    }
}