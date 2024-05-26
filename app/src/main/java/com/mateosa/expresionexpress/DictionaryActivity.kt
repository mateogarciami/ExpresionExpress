package com.mateosa.expresionexpress

import android.os.Bundle
import android.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.InputStream

class DictionaryActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var rvDictionary : RecyclerView
    private lateinit var wordsAdapter : WordsAdapter
    private lateinit var svSearch : SearchView

    private var words = ArrayList<String>()
    private var filterWords = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dictionary)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initComponents()
        initListeners()
        initUI()

    }

    private fun initComponents() {
        rvDictionary = findViewById(R.id.rvDictionary)
        svSearch = findViewById(R.id.svSearch)
    }

    private fun initListeners() {
        svSearch.setOnQueryTextListener(this)
    }

    private fun initUI() {

        listWords()
        runAdapter()

    }

    private fun runAdapter() {
        wordsAdapter = WordsAdapter(filterWords) {position -> (position)}
        rvDictionary.layoutManager = LinearLayoutManager(this)
        rvDictionary.adapter = wordsAdapter
    }

    private fun listWords() {
        val inputStream: InputStream = resources.openRawResource(R.raw.words_list)
        val wordsArray = inputStream.bufferedReader().use { it.readText() }.split("\n")

        for (word in wordsArray){
            words.add(word)
        }
        words.sort()
        filterWords=ArrayList(words)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(w: String?): Boolean {

        filterWords.clear()

        for (word in words){
            if (word.lowercase().contains(w.toString().lowercase())){
                filterWords.add(word)
            }
        }

        runAdapter()

        return false
    }
}