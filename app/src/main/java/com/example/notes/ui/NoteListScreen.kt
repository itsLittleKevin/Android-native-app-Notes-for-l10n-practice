package com.example.notes.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notes.model.Note
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteListScreen(
    viewModel: NotesViewModel,
    onNoteClick: (Note) -> Unit,
    onAddNoteClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onAboutClick: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    var showSortMenu by remember { mutableStateOf(false) }
    var showViewMenu by remember { mutableStateOf(false) }
    var noteToDelete by remember { mutableStateOf<Note?>(null) }
    var isSearchActive by remember { mutableStateOf(false) }
    
    val notesCount = viewModel.notes.size
    val context = LocalContext.current
    val viewMode = viewModel.viewMode.value
    val searchQuery by viewModel.searchQuery

    val fontSize = when (viewModel.fontSize.value) {
        FontSize.SMALL -> 12.sp
        FontSize.MEDIUM -> 16.sp
        FontSize.LARGE -> 20.sp
    }

    Scaffold(
        topBar = {
            if (isSearchActive) {
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { viewModel.setSearchQuery(it) },
                    onSearch = { /* Done by query change */ },
                    active = true,
                    onActiveChange = { if (!it) isSearchActive = false },
                    placeholder = { Text("Search Notes…") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        IconButton(onClick = { 
                            if (searchQuery.isEmpty()) isSearchActive = false else viewModel.setSearchQuery("") 
                        }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear")
                        }
                    },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = if (isSearchActive) 0.dp else 16.dp)
                ) {
                    // Search suggestions could go here
                }
            } else {
                TopAppBar(
                    title = { 
                        Column {
                            Text("Notes")
                            Text(
                                text = if (notesCount == 1) "1 note" else "$notesCount notes",
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { isSearchActive = true }) {
                            Icon(Icons.Default.Search, contentDescription = "Search Notes")
                        }
                        IconButton(onClick = { showMenu = !showMenu }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "More")
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Sort by") },
                                onClick = { 
                                    showMenu = false
                                    showSortMenu = true 
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("View mode") },
                                onClick = { 
                                    showMenu = false
                                    showViewMenu = true 
                                }
                            )
                            HorizontalDivider()
                            DropdownMenuItem(
                                text = { Text("Export Notes") },
                                onClick = { 
                                    showMenu = false
                                    Toast.makeText(context, "Exporting to /Documents/Notes_Export.txt", Toast.LENGTH_SHORT).show()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Import Notes") },
                                onClick = { 
                                    showMenu = false
                                    Toast.makeText(context, "Looking for backup files...", Toast.LENGTH_SHORT).show()
                                }
                            )
                            HorizontalDivider()
                            DropdownMenuItem(
                                text = { Text("Settings") },
                                onClick = {
                                    showMenu = false
                                    onSettingsClick()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Help & Feedback") },
                                onClick = { showMenu = false }
                            )
                            DropdownMenuItem(
                                text = { Text("About") },
                                onClick = {
                                    showMenu = false
                                    onAboutClick()
                                }
                            )
                        }
                        
                        DropdownMenu(expanded = showSortMenu, onDismissRequest = { showSortMenu = false }) {
                            DropdownMenuItem(text = { Text("Title (A-Z)") }, onClick = { 
                                viewModel.setSortOrder(SortOrder.TITLE_ASC)
                                showSortMenu = false 
                            })
                            DropdownMenuItem(text = { Text("Title (Z-A)") }, onClick = { 
                                viewModel.setSortOrder(SortOrder.TITLE_DESC)
                                showSortMenu = false 
                            })
                            DropdownMenuItem(text = { Text("Date (Newest)") }, onClick = { 
                                viewModel.setSortOrder(SortOrder.DATE_DESC)
                                showSortMenu = false 
                            })
                            DropdownMenuItem(text = { Text("Date (Oldest)") }, onClick = { 
                                viewModel.setSortOrder(SortOrder.DATE_ASC)
                                showSortMenu = false 
                            })
                        }
                        
                        DropdownMenu(expanded = showViewMenu, onDismissRequest = { showViewMenu = false }) {
                            DropdownMenuItem(text = { Text("List View") }, onClick = { 
                                viewModel.setViewMode(ViewMode.LIST)
                                showViewMenu = false 
                            })
                            DropdownMenuItem(text = { Text("Grid View") }, onClick = { 
                                viewModel.setViewMode(ViewMode.GRID)
                                showViewMenu = false 
                            })
                        }
                    }
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddNoteClick) {
                Icon(Icons.Default.Add, contentDescription = "New Note")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            if (viewModel.notes.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No notes found")
                }
            } else {
                if (viewMode == ViewMode.LIST) {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(viewModel.notes, key = { it.id }) { note ->
                            SwipeToDeleteNote(
                                note = note,
                                onDeleteRequested = { noteToDelete = it },
                                content = { NoteItem(note = note, fontSize = fontSize, onClick = { onNoteClick(note) }) }
                            )
                        }
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize().padding(4.dp)
                    ) {
                        items(viewModel.notes, key = { it.id }) { note ->
                            NoteItem(note = note, fontSize = fontSize, onClick = { onNoteClick(note) })
                        }
                    }
                }
            }
        }
    }

    if (noteToDelete != null) {
        AlertDialog(
            onDismissRequest = { noteToDelete = null },
            title = { Text("Delete Note") },
            text = { Text("Are you sure you want to delete this note?") },
            confirmButton = {
                TextButton(onClick = {
                    noteToDelete?.let { viewModel.deleteNote(it) }
                    noteToDelete = null
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { noteToDelete = null }) {
                    Text("No")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDeleteNote(
    note: Note,
    onDeleteRequested: (Note) -> Unit,
    content: @Composable () -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.EndToStart) {
                onDeleteRequested(note)
                false
            } else false
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            val color = if (dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart) Color.Red else Color.Transparent
            Box(
                modifier = Modifier.fillMaxSize().padding(8.dp).background(color, MaterialTheme.shapes.medium),
                contentAlignment = Alignment.CenterEnd
            ) {
                Row(modifier = Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("Delete", color = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.Default.Delete, contentDescription = null, tint = Color.White)
                }
            }
        }
    ) {
        content()
    }
}

@Composable
fun NoteItem(note: Note, fontSize: androidx.compose.ui.unit.TextUnit, onClick: () -> Unit) {
    val dateFormat = SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.US)
    val dateString = dateFormat.format(Date(note.timestamp))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = note.title, style = MaterialTheme.typography.titleLarge.copy(fontSize = (fontSize.value + 4).sp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = note.content, maxLines = 2, style = MaterialTheme.typography.bodyMedium.copy(fontSize = fontSize))
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (note.tag != null) {
                    SuggestionChip(
                        onClick = {},
                        label = { Text(note.tag) }
                    )
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }
                
                Text(
                    text = dateString,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
