package com.example.notes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.notes.model.Note
import com.example.notes.ui.AboutScreen
import com.example.notes.ui.NoteEditorScreen
import com.example.notes.ui.NoteListScreen
import com.example.notes.ui.NotesViewModel
import com.example.notes.ui.SettingsScreen
import com.example.notes.ui.theme.NotesTheme

class MainActivity : ComponentActivity() {
    private val viewModel: NotesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotesTheme(darkTheme = viewModel.isDarkMode.value) {
                NotesApp(viewModel)
            }
        }
    }
}

@Composable
fun NotesApp(viewModel: NotesViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "list") {
        composable("list") {
            NoteListScreen(
                viewModel = viewModel,
                onNoteClick = { note: Note -> navController.navigate("editor?id=${note.id}") },
                onAddNoteClick = { navController.navigate("editor") },
                onSettingsClick = { navController.navigate("settings") },
                onAboutClick = { navController.navigate("about") }
            )
        }
        composable("settings") {
            SettingsScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
        composable("about") {
            AboutScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(
            route = "editor?id={id}",
            arguments = listOf(navArgument("id") { 
                type = NavType.StringType
                nullable = true
                defaultValue = null 
            })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            val note = id?.let { viewModel.getNoteById(it) }
            
            NoteEditorScreen(
                note = note,
                availableTags = viewModel.availableTags,
                onSave = { title, content, tag ->
                    if (id == null) {
                        viewModel.addNote(title, content, tag)
                    } else {
                        viewModel.updateNote(id, title, content, tag)
                    }
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}
