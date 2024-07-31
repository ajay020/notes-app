package com.example.notesapp.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notesapp.EditNoteActivity
import com.example.notesapp.R
import com.example.notesapp.adapters.NoteAdapter
import com.example.notesapp.databinding.FragmentNoteListBinding
import com.example.notesapp.model.Note

interface OnNoteClickListener {
    fun onNoteClick(note: Note)
}

class NoteListFragment : Fragment(), OnNoteClickListener {
    private var _binding: FragmentNoteListBinding? = null
    private val binding get() = _binding!!
    private val noteViewModel: NoteViewModel by viewModels()
    private lateinit var adapter: NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentNoteListBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up RecyclerView with StaggeredGridLayoutManager
        binding.recyclerview.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        // Observe notes and set adapter
        noteViewModel.allNotes.observe(viewLifecycleOwner) { notes ->
            adapter = NoteAdapter(notes, this)
            binding.recyclerview.adapter = adapter
            Log.d("DATA", notes.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Set _binding to null to prevent memory leaks
        _binding = null
    }

    override fun onNoteClick(note: Note) {
        val intent = Intent(requireContext(), EditNoteActivity::class.java).apply {
            putExtra("NOTE_ID", note.id)
        }
        startActivity(intent)
    }
}