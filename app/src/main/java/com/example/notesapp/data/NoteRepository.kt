package com.example.notesapp.data

import androidx.lifecycle.LiveData
import com.example.notesapp.model.Note

interface NoteRepository {
    fun allNotes(): LiveData<List<Note>>
    fun getNoteById(noteId: Int): LiveData<Note>
    suspend fun insert(note: Note)
    suspend fun update(note: Note)
    suspend fun delete(note: Note)
}
