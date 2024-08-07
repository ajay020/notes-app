package com.example.notesapp.fragments

import androidx.lifecycle.*
import com.example.notesapp.data.NoteRepository
import com.example.notesapp.model.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {
    val allNotes: LiveData<List<Note>> = repository.allNotes()

    fun getNoteById(noteId: Int): LiveData<Note> {
        return repository.getNoteById(noteId)
    }

    fun insert(note: Note) = viewModelScope.launch {
        repository.insert(note)
    }

    fun update(note: Note) = viewModelScope.launch {
        repository.update(note)
    }

    fun delete(note: Note) = viewModelScope.launch {
        repository.delete(note)
    }
}
