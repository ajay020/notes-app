package com.example.notesapp.data

import androidx.lifecycle.MutableLiveData
import com.example.notesapp.database.NoteDao
import com.example.notesapp.model.Note
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class NoteRepositoryImplTest {

    @Mock
    private lateinit var noteDao: NoteDao

    private lateinit var noteRepository: NoteRepositoryImpl

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        noteRepository = NoteRepositoryImpl(noteDao)
    }

    @Test
    fun testAllNotes() {
        // Arrange
        val dummyNotes = MutableLiveData<List<Note>>()
        `when`(noteDao.getAllNotes()).thenReturn(dummyNotes)

        // Act
        val result = noteRepository.allNotes()

        // Assert
        assertEquals(dummyNotes, result)
        verify(noteDao).getAllNotes()
    }

    @Test
    fun testGetNoteById() {
        // Arrange
        val dummyNote = MutableLiveData<Note>()
        `when`(noteDao.getNoteById(1)).thenReturn(dummyNote)

        // Act
        val result = noteRepository.getNoteById(1)

        // Assert
        assertEquals(dummyNote, result)
        verify(noteDao).getNoteById(1)
    }

    @Test
    fun testInsertNote() = runBlocking {
        // Arrange
        val note = Note(id = 0, title = "Test Note", content = "This is a test")

        // Act
        noteRepository.insert(note)

        // Assert
        verify(noteDao).insert(note)
    }

    @Test
    fun testUpdateNote() = runBlocking {
        // Arrange
        val note = Note(id = 1, title = "Test Note", content = "This is a test")

        // Act
        noteRepository.update(note)

        // Assert
        verify(noteDao).update(note)
    }

    @Test
    fun testDeleteNote() = runBlocking {
        // Arrange
        val note = Note(id = 1, title = "Test Note", content = "This is a test")

        // Act
        noteRepository.delete(note)

        // Assert
        verify(noteDao).delete(note)
    }
}
