package com.mateosa.expresionexpress

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import java.util.Dictionary

enum class ProviderType {
    BASIC,
    GOOGLE
}

class MainActivity : AppCompatActivity() {

    private lateinit var fabClock: FloatingActionButton
    private lateinit var btnPlay: Button
    private lateinit var btnOut: Button
    private lateinit var btnDictionary : Button
    private lateinit var btnInstructions : Button

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val email = intent.extras?.getString("email") ?: ""
        val provider = intent.extras?.getString("provider") ?: ""

        //guardar datos inicio sesion
        if (!email.equals("")){
            val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
            prefs.putString("email", email)
            prefs.putString("provider", provider)
            prefs.apply()
            val name = email.split("@")
            Toast.makeText(this, "Hola "+name[0], Toast.LENGTH_SHORT).show()
        }

        initComponents()
        initListeners()

    }

    private fun initComponents() {
        fabClock = findViewById(R.id.fabClock)
        btnPlay = findViewById(R.id.btnPlay)
        btnOut = findViewById(R.id.btnOut)
        btnDictionary = findViewById(R.id.btnDictionary)
        btnInstructions = findViewById(R.id.btnInstructions)
    }

    private fun initListeners() {

        fabClock.setOnClickListener {
            val intent = Intent(this, OnlyTimeActivity::class.java)
            startActivity(intent)
        }

        btnPlay.setOnClickListener {
            val intent = Intent(this, DurationMenuActivity::class.java)
            startActivity(intent)
        }

        btnOut.setOnClickListener {
            val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
            prefs.clear()
            prefs.apply()

            FirebaseAuth.getInstance().signOut()

            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)

        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                moveTaskToBack(true)
            }
        })

        btnDictionary.setOnClickListener {
            val intent = Intent(this, DictionaryActivity::class.java)
            startActivity(intent)
        }

        btnInstructions.setOnClickListener {
            val intent = Intent(this, InstructionsActivity::class.java)
            startActivity(intent)
        }

    }



}