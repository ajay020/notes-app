package com.example.notesapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notesapp.ui.EditNoteActivity
import com.example.notesapp.adapters.NoteAdapter
import com.example.notesapp.databinding.FragmentNoteListBinding
import com.example.notesapp.model.Note
import dagger.hilt.android.AndroidEntryPoint

interface OnNoteClickListener {
    fun onNoteClick(note: Note)
}

@AndroidEntryPoint
class NoteListFragment : Fragment(), OnNoteClickListener {
    private var _binding: FragmentNoteListBinding? = null
    private val binding get() = _binding!!
    private val noteViewModel: NoteViewModel by viewModels()
    private lateinit var adapter: NoteAdapter
    private var currentColumns = 2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoteListBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeNotes()
    }

    fun updateLayout(columns: Int) {
        currentColumns = columns
        val layoutManager = binding.recyclerview.layoutManager as StaggeredGridLayoutManager
        layoutManager.spanCount = columns
    }

    private fun setupRecyclerView() {
        binding.recyclerview.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        // Initialize the adapter once
        adapter = NoteAdapter(mutableListOf(), this)
        binding.recyclerview.adapter = adapter
    }

    private fun observeNotes() {
        // Observe notes and set adapter
        noteViewModel.allNotes.observe(viewLifecycleOwner) { notes ->
            if (notes.isNullOrEmpty()) {
                // Show empty message if no notes
                binding.recyclerview.visibility = View.GONE
                binding.emptyPlaceHolder.visibility = View.VISIBLE
            } else {
                // Show notes in RecyclerView
                binding.recyclerview.visibility = View.VISIBLE
                binding.emptyPlaceHolder.visibility = View.GONE

                // Update the adapter with new data
                adapter.updateNotes(notes)
            }
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