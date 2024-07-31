package com.example.notesapp.data

import androidx.lifecycle.LiveData
import com.example.notesapp.database.NoteDao
import com.example.notesapp.model.Note

class NoteRepositoryImpl(private val noteDao: NoteDao) : NoteRepository {
    override fun allNotes(): LiveData<List<Note>> = noteDao.getAllNotes()
    override fun getNoteById(noteId: Int): LiveData<Note> = noteDao.getNoteById(noteId)

    override suspend fun insert(note: Note) = noteDao.insert(note)

    override suspend fun update(note: Note) = noteDao.update(note)

    override suspend fun delete(note: Note) = noteDao.delete(note)
}