package com.example.notesapp.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.notesapp.R
import com.example.notesapp.databinding.ActivityAddNoteBinding
import com.example.notesapp.fragments.NoteViewModel
import com.example.notesapp.model.Note
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding
    private val noteViewModel: NoteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Add Note"

        // Change the back button to a close icon
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)

        // Handle close button click
        binding.toolbar.setNavigationOnClickListener {
            finish() // Close the activity
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_note, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when(item.itemId){
            R.id.action_save -> {
                saveNote()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }


    private fun saveNote() {
        val title = binding.editNoteTitle.text.toString().trim()
        val content = binding.editNoteContent.text.toString().trim()

        if (title.isNotEmpty() ) {
            val note = Note(
                title = title,
                content = if (content.isEmpty() || content.isBlank()) "" else content
            )
            noteViewModel.insert(note)
            Toast.makeText(this, "Note saved!", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Enter title", Toast.LENGTH_SHORT).show()
        }
    }
}