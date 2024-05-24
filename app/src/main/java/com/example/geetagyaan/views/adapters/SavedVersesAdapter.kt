package com.example.geetagyaan.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.geetagyaan.databinding.VerseItemViewBinding
import com.example.geetagyaan.roomdb.VersesSave



class SavedVersesAdapter(val onVerseItemClicked:(VersesSave) -> Unit) :
    RecyclerView.Adapter<SavedVersesAdapter.SavedVersesViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<VersesSave>() {
        override fun areItemsTheSame(oldItem: VersesSave, newItem: VersesSave): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: VersesSave, newItem: VersesSave): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedVersesViewHolder {
        return SavedVersesViewHolder(
            VerseItemViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                true
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: SavedVersesViewHolder, position: Int) {
        val verse = differ.currentList[position]
        holder.binding.verseNum.text = "Verse ${verse.chapter_number}.${verse.verse_number}"
        holder.binding.verseTxt.text = verse.translations[0].description


        holder.itemView.setOnClickListener {
            onVerseItemClicked(verse)
        }
    }

    class SavedVersesViewHolder(val binding: VerseItemViewBinding) : ViewHolder(binding.root) {

    }
}