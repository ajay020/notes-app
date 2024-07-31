package com.example.notesapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.notesapp.databinding.ActivityAddNoteBinding
import com.example.notesapp.databinding.ActivityEditNoteBinding
import com.example.notesapp.fragments.NoteViewModel
import com.example.notesapp.model.Note

class EditNoteActivity : AppCompatActivity() {
    val noteViewModel: NoteViewModel by viewModels()
    lateinit var binding: ActivityEditNoteBinding

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
        supportActionBar?.title = "Edit note"

        // Handle back button click
        binding.toolbar.setNavigationOnClickListener {
            this.onBackPressedDispatcher
                .onBackPressed()
        }

        val noteId = intent.getIntExtra("NOTE_ID", -1)
        if (noteId != -1) {
            noteViewModel.getNoteById(noteId).observe(this) { note ->
                binding.editNoteTitle.setText(note.title)
                binding.editNoteContent.setText(note.content)
            }
        }

        binding.buttonSave.setOnClickListener {
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
    }
}