package com.mateosa.expresionexpress

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class FinalScreenActivity : AppCompatActivity() {

    private lateinit var tvVictoryText : TextView
    private lateinit var tvVictoryTeam : TextView
    private lateinit var btnVictoryBack : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_final_screen)
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
        tvVictoryText = findViewById(R.id.tvVictoryText)
        tvVictoryTeam = findViewById(R.id.tvVictoryTeam)
        btnVictoryBack = findViewById(R.id.btnVictoryBack)
    }

    private fun initListeners() {

        btnVictoryBack.setOnClickListener {
            backMenu()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                backMenu()
            }
        })

    }

    private fun initUI() {

        // true purple, false red
        val teamCheck = intent.extras?.getBoolean("VICTORY_KEY") ?: true
        if (teamCheck){
            tvVictoryText.setShadowLayer(1f, -10f, 14f, ContextCompat.getColor(applicationContext, R.color.purple))
            tvVictoryTeam.text = "Morado"
            tvVictoryTeam.setTextColor(ContextCompat.getColor(applicationContext, R.color.purple))
            btnVictoryBack.setTextColor(ContextCompat.getColor(applicationContext, R.color.purple))
        }else{
            tvVictoryText.setShadowLayer(1f, -10f, 14f, ContextCompat.getColor(applicationContext, R.color.red))
            tvVictoryTeam.text = "Rojo"
            tvVictoryTeam.setTextColor(ContextCompat.getColor(applicationContext, R.color.red))
            btnVictoryBack.setTextColor(ContextCompat.getColor(applicationContext, R.color.red))
        }

    }

    private fun backMenu(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }


}