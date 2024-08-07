package com.example.notesapp.ui

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.notesapp.R
import com.example.notesapp.databinding.ActivityEditNoteBinding
import com.example.notesapp.fragments.NoteViewModel
import com.example.notesapp.model.Note
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditNoteActivity : AppCompatActivity() {
    private val noteViewModel: NoteViewModel by viewModels()
    private lateinit var binding: ActivityEditNoteBinding
    private var noteId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // stop overlapping toolbar and status bar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set up toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Edit Note"

        // Handle back button click
        binding.toolbar.setNavigationOnClickListener {
            this.onBackPressedDispatcher
                .onBackPressed()
        }

        noteId = intent.getIntExtra("NOTE_ID", -1)
        if (noteId != -1) {
            noteViewModel.getNoteById(noteId).observe(this) { note ->
                if (note != null) {
                    binding.editNoteTitle.setText(note.title)
                    binding.editNoteContent.setText(note.content)
                } else {
                    //"Note not found or deleted!"mit
                    finish() // Close the activity and return to the previous screen
                }
            }
        }
    }

    private fun updateNote() {
        val title = binding.editNoteTitle.text.toString().trim()
        val content = binding.editNoteContent.text.toString().trim()
        if (title.isNotEmpty()) {
            val updatedNote = Note(
                id = noteId,
                title = title,
                content = content
            )
            noteViewModel.update(updatedNote)
            Toast.makeText(this, "Note updated!", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Enter title", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_edit_note, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete -> {
                // Handle delete action
                if (noteId != -1) {
                    showDeleteConfirmationDialog(this) {
                        noteViewModel.delete(Note(id = noteId, title = "", content = ""))
                        Toast.makeText(this, "Note deleted!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
                true
            }

            R.id.action_save -> {
                updateNote()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showDeleteConfirmationDialog(context: Context, onPositiveClick: () -> Unit) {
        AlertDialog.Builder(context)
            .setTitle("Confirm Delete")
            .setMessage("Are you sure you want to delete this note?")
            .setPositiveButton("Delete") { dialog, _ ->
                onPositiveClick()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}