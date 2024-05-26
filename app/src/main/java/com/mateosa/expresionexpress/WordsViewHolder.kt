package com.mateosa.expresionexpress

import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WordsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val tvItemWord: TextView = view.findViewById(R.id.tvItemWord)
    private val context = view.context

    fun render(word: String) {

        tvItemWord.text = word

        tvItemWord.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://www.google.com/search?q=$word")
            context.startActivity(intent)
        }

    }

}