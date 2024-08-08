package com.example.notesapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.notesapp.data.NoteRepository
import com.example.notesapp.fragments.NoteViewModel
import com.example.notesapp.model.Note
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class NoteViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var noteRepository: NoteRepository

    private lateinit var noteViewModel: NoteViewModel

    @Before
    fun setUp() {
        // Initialize the mocks
        MockitoAnnotations.openMocks(this)

        // Create a mock repository before initializing the ViewModel
        val dummyNotes = MutableLiveData<List<Note>>()
        `when`(noteRepository.allNotes()).thenReturn(dummyNotes)

        // Initialize the ViewModel with the mocked repository
        noteViewModel = NoteViewModel(noteRepository)

        // Set up the Main dispatcher for tests
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        // Reset the Main dispatcher to the original state
        Dispatchers.resetMain()
    }

    @Test
    fun testAllNotes() {
        // Act: Retrieve the LiveData from the ViewModel
        val result = noteViewModel.allNotes

        // Assert: Check if the LiveData returned by the ViewModel is the same as the one from the repository
        assertNotNull(result)  // Ensure it's not null
        verify(noteRepository).allNotes()
    }

    @Test
    fun testGetNoteById() {
        // Arrange: Set up a dummy note and mock repository response
        val dummyNote = MutableLiveData<Note>()
        val noteId = 1
        `when`(noteRepository.getNoteById(noteId)).thenReturn(dummyNote)

        // Act: Call the method in the ViewModel
        val result = noteViewModel.getNoteById(noteId)

        // Assert: Verify the result and repository interaction
        assertEquals(dummyNote, result)
        verify(noteRepository).getNoteById(noteId)
    }

    @Test
    fun testInsert() = runTest {
        // Arrange: Create a dummy note
        val dummyNote = Note(
            id = 1,
            title = "Test Note",
            content = "This is a test"
        )

        // Act: Call the insert method
        noteViewModel.insert(dummyNote)

        // Advance time to ensure coroutine completion
        advanceUntilIdle()

        // Assert: Verify that the repository's insert method was called with the correct note
        verify(noteRepository).insert(dummyNote)
        println("Inserted note: $dummyNote")
    }

    @Test
    fun testUpdate() = runTest {
        // Arrange: Create a dummy note
        val dummyNote = Note(
            id = 1,
            title = "Updated Note",
            content = "This is an updated test"
        )

        println("Before update: $dummyNote")

        // Act: Call the update method
        noteViewModel.update(dummyNote)

        // Advance time to ensure coroutine completion
        advanceUntilIdle()

        // Assert: Verify that the repository's update method was called with the correct note
        verify(noteRepository).update(dummyNote)
    }

    @Test
    fun testDelete() = runTest {
        // Arrange: Create a dummy note
        val dummyNote = Note(
            id = 1,
            title = "Test Note",
            content = "This is a test"
        )

        // Act: Call the delete method
        noteViewModel.delete(dummyNote)

        advanceUntilIdle()

        // Assert: Verify that the repository's delete method was called with the correct note
        verify(noteRepository).delete(dummyNote)
    }
}

