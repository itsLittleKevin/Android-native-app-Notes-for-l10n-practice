package com.example.notes.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: NotesViewModel,
    onBackClick: () -> Unit
) {
    var showFontSizeMenu by remember { mutableStateOf(false) }
    var showColorMenu by remember { mutableStateOf(false) }
    var showAddTagDialog by remember { mutableStateOf(false) }
    var newTagName by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                text = "Appearance",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Dark Mode")
                Switch(
                    checked = viewModel.isDarkMode.value,
                    onCheckedChange = { viewModel.toggleDarkMode() }
                )
            }
            
            HorizontalDivider()
            
            Box {
                SettingsListItem(
                    title = "Theme Color",
                    subtitle = when (viewModel.themeColor.value) {
                        Color(0xFF6750A4) -> "Default Purple"
                        Color(0xFFD32F2F) -> "Sunset Red"
                        Color(0xFF1976D2) -> "Ocean Blue"
                        Color(0xFF388E3C) -> "Forest Green"
                        else -> "Custom"
                    },
                    onClick = { showColorMenu = true }
                )
                DropdownMenu(expanded = showColorMenu, onDismissRequest = { showColorMenu = false }) {
                    DropdownMenuItem(text = { Text("Default Purple") }, onClick = { viewModel.setThemeColor(Color(0xFF6750A4)); showColorMenu = false })
                    DropdownMenuItem(text = { Text("Sunset Red") }, onClick = { viewModel.setThemeColor(Color(0xFFD32F2F)); showColorMenu = false })
                    DropdownMenuItem(text = { Text("Ocean Blue") }, onClick = { viewModel.setThemeColor(Color(0xFF1976D2)); showColorMenu = false })
                    DropdownMenuItem(text = { Text("Forest Green") }, onClick = { viewModel.setThemeColor(Color(0xFF388E3C)); showColorMenu = false })
                }
            }
            
            Box {
                SettingsListItem(
                    title = "Font Size",
                    subtitle = when (viewModel.fontSize.value) {
                        FontSize.SMALL -> "Small"
                        FontSize.MEDIUM -> "Medium"
                        FontSize.LARGE -> "Large"
                    },
                    onClick = { showFontSizeMenu = true }
                )
                DropdownMenu(expanded = showFontSizeMenu, onDismissRequest = { showFontSizeMenu = false }) {
                    DropdownMenuItem(text = { Text("Small") }, onClick = { viewModel.setFontSize(FontSize.SMALL); showFontSizeMenu = false })
                    DropdownMenuItem(text = { Text("Medium") }, onClick = { viewModel.setFontSize(FontSize.MEDIUM); showFontSizeMenu = false })
                    DropdownMenuItem(text = { Text("Large") }, onClick = { viewModel.setFontSize(FontSize.LARGE); showFontSizeMenu = false })
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Tags",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            
            viewModel.availableTags.forEach { tag ->
                key(tag) {
                    val dismissState = rememberSwipeToDismissBoxState(
                        confirmValueChange = {
                            if (it == SwipeToDismissBoxValue.EndToStart) {
                                viewModel.removeTag(tag)
                                true
                            } else false
                        }
                    )

                    SwipeToDismissBox(
                        state = dismissState,
                        enableDismissFromStartToEnd = false,
                        backgroundContent = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(vertical = 4.dp)
                                    .background(Color.Red, MaterialTheme.shapes.small),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Remove Tag",
                                    tint = Color.White,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                            }
                        }
                    ) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.surface
                        ) {
                            Text(tag, modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp))
                        }
                    }
                }
            }
            
            Button(
                onClick = { showAddTagDialog = true },
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            ) {
                Text("Add Tag")
            }
        }
    }

    if (showAddTagDialog) {
        AlertDialog(
            onDismissRequest = { showAddTagDialog = false },
            title = { Text("Add New Tag") },
            text = {
                OutlinedTextField(
                    value = newTagName,
                    onValueChange = { newTagName = it },
                    label = { Text("Tag Name") }
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.addTag(newTagName)
                    newTagName = ""
                    showAddTagDialog = false
                }) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddTagDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun SettingsListItem(title: String, subtitle: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp)
    ) {
        Text(text = title, style = MaterialTheme.typography.bodyLarge)
        Text(text = subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
