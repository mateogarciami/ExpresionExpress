package com.mateosa.expresionexpress

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class WordsAdapter(private var words : List<String>, private val onWordSelected: (Int) -> Unit) :
    RecyclerView.Adapter<WordsViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_word, parent, false)
        return WordsViewHolder(view)
    }

    override fun getItemCount() = words.size

    override fun onBindViewHolder(holder: WordsViewHolder, position: Int) {
        holder.render(words[position])
        holder.itemView.setOnClickListener{
            onWordSelected(position)
        }
    }


}