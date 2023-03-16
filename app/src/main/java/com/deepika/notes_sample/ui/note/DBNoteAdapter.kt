package com.deepika.notes_sample.ui.note

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.deepika.notes_sample.utils.Constants.BUNDLE_NOTE_ID
import com.deepika.notes_sample.databinding.NoteItemBinding
import com.deepika.notes_sample.db.NoteEntity
import com.deepika.notes_sample.models.NoteResponse
import com.deepika.notes_sample.ui.note.NoteAdapter

import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DBNoteAdapter @Inject constructor() : RecyclerView.Adapter<DBNoteAdapter.ViewHolder>(){
//class DBNoteAdapter(private val onNoteClicked: (NoteResponse) -> Unit) : RecyclerView.Adapter<DBNoteAdapter.ViewHolder>(){
    private lateinit var binding:NoteItemBinding
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DBNoteAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = NoteItemBinding.inflate(inflater, parent, false)
        context = parent.context
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: DBNoteAdapter.ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
        holder.setIsRecyclable(false)
    }

    override fun getItemCount(): Int =differ.currentList.size

    inner class ViewHolder : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: NoteEntity) {
            //InitView
            binding.apply {
                //Set text
                title.text = item.noteTitle
                desc.text= item.noteDesc

                root.setOnClickListener {
//                    onNoteClicked(item)
//                    val intent=Intent(context,UpdateNoteActivity::class.java)
//                    intent.putExtra(BUNDLE_NOTE_ID, item.noteId)
//                    context.startActivity(intent)
                }

            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<NoteEntity>() {
        override fun areItemsTheSame(oldItem: NoteEntity, newItem: NoteEntity): Boolean {
            return oldItem.noteId == newItem.noteId
        }

        override fun areContentsTheSame(oldItem: NoteEntity, newItem: NoteEntity): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
}
