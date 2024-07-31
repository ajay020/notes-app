package com.example.notesapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.notesapp.databinding.ActivityAddNoteBinding
import com.example.notesapp.fragments.NoteViewModel
import com.example.notesapp.model.Note

class AddNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding
    private val noteViewModel: NoteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSave.setOnClickListener {
           saveNote()
        }
    }

    private fun saveNote() {
        val title = binding.editNoteTitle.text.toString().trim()
        val content = binding.editNoteContent.text.toString().trim()

        if (title.isNotEmpty()) {
            val note = Note(
                title = title,
                content = content
            )
            noteViewModel.insert(note)
            Toast.makeText(this, "Note saved!", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Enter title", Toast.LENGTH_SHORT).show()
        }
    }
}