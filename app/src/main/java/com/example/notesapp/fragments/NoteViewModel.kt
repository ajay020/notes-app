package com.example.notesapp.fragments

import android.app.Application
import androidx.lifecycle.*
import com.example.notesapp.data.NoteRepositoryImpl
import com.example.notesapp.database.NoteDatabase
import com.example.notesapp.model.Note
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: NoteRepositoryImpl
    val allNotes: LiveData<List<Note>>

    init {
        val noteDao = NoteDatabase.getDatabase(application).noteDao()
        repository = NoteRepositoryImpl(noteDao)
        allNotes = repository.allNotes()
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
