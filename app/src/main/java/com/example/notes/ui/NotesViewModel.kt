package com.example.notes.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.notes.model.Note

enum class SortOrder { TITLE_ASC, TITLE_DESC, DATE_ASC, DATE_DESC }
enum class ViewMode { LIST, GRID }
enum class FontSize { SMALL, MEDIUM, LARGE }

class NotesViewModel : ViewModel() {
    private val _notes = mutableStateListOf<Note>(
        Note(title = "Welcome", content = "This is a sample note for localization practice.", tag = "Important", timestamp = 1704067200000L), // Jan 1, 2024
        Note(title = "Shopping List", content = "Apples, Milk, Bread", tag = "Personal", timestamp = 1706745600000L), // Feb 1, 2024
        Note(title = "Project Idea", content = "Create a localized app for students.", tag = "Work", timestamp = 1709251200000L) // Mar 1, 2024
    )
    
    private val _sortOrder = mutableStateOf(SortOrder.DATE_DESC)
    val sortOrder: State<SortOrder> = _sortOrder

    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    val notes: List<Note>
        get() {
            val filtered = if (_searchQuery.value.isEmpty()) {
                _notes
            } else {
                _notes.filter { 
                    it.title.contains(_searchQuery.value, ignoreCase = true) || 
                    it.content.contains(_searchQuery.value, ignoreCase = true) 
                }
            }

            return when (_sortOrder.value) {
                SortOrder.TITLE_ASC -> filtered.sortedBy { it.title }
                SortOrder.TITLE_DESC -> filtered.sortedByDescending { it.title }
                SortOrder.DATE_ASC -> filtered.sortedBy { it.timestamp }
                SortOrder.DATE_DESC -> filtered.sortedByDescending { it.timestamp }
            }
        }

    private val _viewMode = mutableStateOf(ViewMode.LIST)
    val viewMode: State<ViewMode> = _viewMode

    private val _fontSize = mutableStateOf(FontSize.MEDIUM)
    val fontSize: State<FontSize> = _fontSize

    private val _themeColor = mutableStateOf(Color(0xFF6750A4)) // Default Purple
    val themeColor: State<Color> = _themeColor

    private val _availableTags = mutableStateListOf("Work", "Personal", "Ideas", "Important")
    val availableTags: List<String> = _availableTags

    private val _isDarkMode = mutableStateOf(false)
    val isDarkMode: State<Boolean> = _isDarkMode

    fun toggleDarkMode() {
        _isDarkMode.value = !_isDarkMode.value
    }

    fun setSortOrder(order: SortOrder) {
        _sortOrder.value = order
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setViewMode(mode: ViewMode) {
        _viewMode.value = mode
    }

    fun setFontSize(size: FontSize) {
        _fontSize.value = size
    }

    fun setThemeColor(color: Color) {
        _themeColor.value = color
    }

    fun addNote(title: String, content: String, tag: String?) {
        _notes.add(Note(title = title, content = content, tag = tag))
    }

    fun updateNote(id: String, title: String, content: String, tag: String?) {
        val index = _notes.indexOfFirst { it.id == id }
        if (index != -1) {
            _notes[index] = _notes[index].copy(title = title, content = content, tag = tag)
        }
    }

    fun deleteNote(note: Note) {
        _notes.remove(note)
    }

    fun getNoteById(id: String): Note? {
        return _notes.find { it.id == id }
    }

    fun addTag(tag: String) {
        if (tag.isNotBlank() && !_availableTags.contains(tag)) {
            _availableTags.add(tag)
        }
    }

    fun removeTag(tag: String) {
        _availableTags.remove(tag)
    }
}
