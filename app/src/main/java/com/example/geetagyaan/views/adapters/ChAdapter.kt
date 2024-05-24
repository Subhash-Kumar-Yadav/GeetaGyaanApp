package com.example.geetagyaan.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.geetagyaan.databinding.ItemViewChapterBinding

import com.example.geetagyaan.models.ChaptersItem
import com.example.geetagyaan.viewmodels.MainViewModel

class ChAdapter(
    val onChapterIVClicked: (ChaptersItem) -> Unit,
    val clickedToSaveFavouriteChapterItem: (ChaptersItem) -> Unit,
    private val check: Boolean,
    val onFavoriteFilledClicked: (ChaptersItem) -> Unit,
    val viewModel: MainViewModel
) : RecyclerView.Adapter<ChAdapter.ChViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<ChaptersItem>() {
        override fun areItemsTheSame(oldItem: ChaptersItem, newItem: ChaptersItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ChaptersItem, newItem: ChaptersItem): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChViewHolder {
        return ChViewHolder(
            ItemViewChapterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ChViewHolder, position: Int) {
        val chapter = differ.currentList[position]

        val keys = viewModel.getAllSavedChaptersKeySP()

        if (!check) {
            holder.binding.apply {
                saveChapterBtn.visibility = View.GONE
                savedChapterFilledButton.visibility = View.GONE
            }
        } else {
            if (keys.contains(chapter.chapter_summary.toString())) {
                holder.binding.apply {
                    saveChapterBtn.visibility = View.GONE
                    savedChapterFilledButton.visibility = View.VISIBLE
                }
            } else {
                holder.binding.apply {
                    saveChapterBtn.visibility = View.VISIBLE
                    savedChapterFilledButton.visibility = View.GONE
                }
            }
        }

        holder.binding.apply {
            tvChapterNum.text = "Chapter ${chapter.chapter_number}"
            chapterTitles.text = chapter.name_translated
            chapterDescription.text = chapter.chapter_summary
            totalViews.text = chapter.verses_count.toString()
        }
        holder.itemView.setOnClickListener {
            onChapterIVClicked(chapter)
        }
        holder.binding.apply {
            saveChapterBtn.setOnClickListener {
                saveChapterBtn.visibility = View.GONE
                savedChapterFilledButton.visibility = View.VISIBLE
                clickedToSaveFavouriteChapterItem(chapter)
            }
            savedChapterFilledButton.setOnClickListener {
                saveChapterBtn.visibility = View.VISIBLE
                savedChapterFilledButton.visibility = View.GONE
                onFavoriteFilledClicked(chapter)
            }
        }
    }

    class ChViewHolder(val binding: ItemViewChapterBinding) : ViewHolder(binding.root) {
    }
}