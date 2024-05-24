package com.example.geetagyaan.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.geetagyaan.databinding.VerseItemViewBinding


class VerseAdapter(val onVerseItemClicked: (String, Int) -> Unit, val onClick: Boolean) :
    RecyclerView.Adapter<VerseAdapter.VerseViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerseViewHolder {
        return VerseViewHolder(
            VerseItemViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: VerseViewHolder, position: Int) {
        val verse = differ.currentList[position]
        holder.binding.verseNum.text = "Verse ${position + 1}"
        holder.binding.verseTxt.text = verse

        if (onClick) {
            holder.binding.ivNext.visibility = View.VISIBLE
        } else {
            holder.binding.ivNext.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            onVerseItemClicked(verse, position + 1)
        }
    }

    class VerseViewHolder(val binding: VerseItemViewBinding) : ViewHolder(binding.root) {

    }
}