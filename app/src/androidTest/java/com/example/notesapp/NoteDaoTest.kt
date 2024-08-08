package com.example.notesapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.notesapp.Utils.getOrAwaitValue
import com.example.notesapp.database.NoteDao
import com.example.notesapp.database.NoteDatabase
import com.example.notesapp.model.Note
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class NoteDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()  // This rule handles LiveData testing on the main thread

    // Instance of the in-memory database and DAO
    private lateinit var database: NoteDatabase
    private lateinit var noteDao: NoteDao

    @Before
    fun setUp() {
        // Create an in-memory database for testing
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NoteDatabase::class.java
        ).allowMainThreadQueries().build()

        noteDao = database.noteDao()
    }

    @After
    fun tearDown() {
        // Close the database after testing
        database.close()
    }

    @Test
    fun insertNote_retrievesSameNote() = runBlocking {
        // Create a sample note and insert it
        val actualNote = Note(id = 1, title = "Test Note", content = "This is a test", timestamp = System.currentTimeMillis())
        noteDao.insert(actualNote)

        // Retrieve the inserted note by ID
        val loaded = noteDao.getAllNotes().getOrAwaitValue().first()

        // Assert that the loaded data is the same as what was inserted
        assertThat(loaded, `is`(actualNote))
    }

    @Test
    fun updateNote_retrievesUpdatedNote() = runBlocking {
        // Insert a note
        val note = Note(id = 1, title = "Test Note", content = "This is a test", timestamp = System.currentTimeMillis())
        noteDao.insert(note)

        // Update the note
        val updatedNote = note.copy(title = "Updated Note")
        noteDao.update(updatedNote)

        // Retrieve the note and verify the update
        val retrievedNote = noteDao.getNoteById(note.id).getOrAwaitValue() // getOrAwaitValue() is a helper function
        assertEquals("Updated Note", retrievedNote.title)
    }

    @Test
    fun deleteNote_retrievesEmptyList() = runBlocking {
        // Create a sample note and insert it
        val note = Note(id = 1, title = "Test Note", content = "This is a test", timestamp = System.currentTimeMillis())
        noteDao.insert(note)

        // Delete the note
        noteDao.delete(note)

        // Retrieve all notes and check if the list is empty
        val allNotes = noteDao.getAllNotes().getOrAwaitValue()
        assertTrue(allNotes.isEmpty())
    }
}
