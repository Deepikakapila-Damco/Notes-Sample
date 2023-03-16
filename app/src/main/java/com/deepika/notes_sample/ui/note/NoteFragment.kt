package com.deepika.notes_sample.ui.note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.deepika.notes_sample.R
import com.deepika.notes_sample.databinding.FragmentNoteBinding
import com.deepika.notes_sample.db.DbRepository
import com.deepika.notes_sample.db.NoteEntity
import com.deepika.notes_sample.models.NoteRequest
import com.deepika.notes_sample.models.NoteResponse
import com.deepika.notes_sample.utils.NetworkResult

import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NoteFragment : Fragment() {
    @Inject
    lateinit var  repository: DbRepository

    @Inject
    lateinit var noteDB: NoteEntity

    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!
    private val noteViewModel by viewModels<NoteViewModel>()
    private var note: NoteResponse? = null
    private var noteId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInitialData()
        bindHandlers()
        bindObservers()
    }

    private fun bindObservers() {
        noteViewModel.statusLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Success -> {
                    findNavController().popBackStack()
                }
                is NetworkResult.Error -> {

                }
                is NetworkResult.Loading -> {

                }
            }
        })
    }

    private fun bindHandlers() {
       /*  todo
       binding.btnDelete.setOnClickListener {
            note?.let { noteViewModel.deleteNote(it!!._id) }
        }*/

        binding.btnDelete.setOnClickListener {
            note?.let {
               val noteId = Integer.parseInt( it.noteId)
                val desc = it.noteDesc
                val title = it.noteTitle

                noteDB = NoteEntity(noteId, title, desc)
                repository.deleteNote(noteDB)
                findNavController().popBackStack()
            }
        }

        binding.apply {
            btnSubmit.setOnClickListener {

                val title = txtTitle.text.toString()
                val description = txtDescription.text.toString()
                val noteRequest = NoteRequest(title, description)
//todo                if (note == null) {
//                    noteViewModel.createNote(noteRequest)
//                } else {
//                    noteViewModel.updateNote(note!!._id, noteRequest)
//                }

                if (note == null) {
                    if (title.isNotEmpty() || description.isNotEmpty()){
                        noteDB= NoteEntity(0,title,description)
                        repository.saveNote(noteDB)
                        findNavController().popBackStack()
                    }
                } else {
                    if (title.isNotEmpty() || description.isNotEmpty()){
                        noteDB= NoteEntity(noteId,title,description)
                        repository.updateNote(noteDB)
                        findNavController().popBackStack()
                    }
                }



            }
        }
    }

    private fun setInitialData() {
        val jsonNote = arguments?.getString("note")
        if (jsonNote != null) {
            note = Gson().fromJson<NoteResponse>(jsonNote, NoteResponse::class.java)
            note?.let {
//                {"noteDesc":"hello good night guys.....\n","noteId":3,"noteTitle":"good night "}
                binding.txtTitle.setText(it.noteTitle)
                binding.txtDescription.setText(it.noteDesc)
                 noteId = Integer.parseInt( it.noteId)
            }
        }
        else{
            binding.addEditText.text = resources.getString(R.string.add_note)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}