package com.mateosa.expresionexpress

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DurationMenuActivity : AppCompatActivity() {

    private lateinit var btnShort: Button
    private lateinit var btnMedium: Button
    private lateinit var btnLong: Button
    private lateinit var btnPersonalized: Button
    private lateinit var intent: Intent

    companion object {
        var purplePoints : Int = 0
        var redPoints : Int = 0
        var totalPoints : Int = 0
        var usedWordsId = ArrayList<Int>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_duration_menu)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initComponents()
        initListeners()

    }

    private fun initComponents() {
        btnShort = findViewById(R.id.btnShort)
        btnMedium = findViewById(R.id.btnMedium)
        btnLong = findViewById(R.id.btnLong)
        btnPersonalized = findViewById(R.id.btnPersonalized)
        intent = Intent(this, GameBoardActivity::class.java)
    }

    private fun initListeners() {
        btnShort.setOnClickListener {
            restartPoints()
            totalPoints = 5
            startGame()
        }
        btnMedium.setOnClickListener {
            restartPoints()
            totalPoints = 7
            startGame()
        }
        btnLong.setOnClickListener {
            restartPoints()
            totalPoints = 9
            startGame()
        }

        btnPersonalized.setOnClickListener {
            showPersonalizedDialog()
        }
    }

    private fun showPersonalizedDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_personalized)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val fabPlus: FloatingActionButton = dialog.findViewById(R.id.fabPlus)
        val fabLess: FloatingActionButton = dialog.findViewById(R.id.fabLess)
        val tvPersonalized: TextView = dialog.findViewById(R.id.tvPersonalized)
        val btnPersonalizedAccept : Button = dialog.findViewById(R.id.btnPersonalizedAccept)

        var personalizedPoints = 1

        tvPersonalized.text = personalizedPoints.toString()

        btnPersonalizedAccept.setOnClickListener {
            restartPoints()
            totalPoints = personalizedPoints
            dialog.hide()
            startGame()
        }

        fabPlus.setOnClickListener {
            if (personalizedPoints<40){
                personalizedPoints++
                tvPersonalized.text = personalizedPoints.toString()
            }
        }

        fabLess.setOnClickListener {
            if (personalizedPoints>1){
                personalizedPoints--
                tvPersonalized.text = personalizedPoints.toString()
            }
        }

        dialog.show()
    }

    private fun startGame() {
        intent.putExtra("ACTIVATION_KEY", true)
        startActivity(intent)
    }

    private fun restartPoints() {
        purplePoints = 0
        redPoints = 0
    }
}