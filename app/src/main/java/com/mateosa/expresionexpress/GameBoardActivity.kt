package com.mateosa.expresionexpress

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mateosa.expresionexpress.DurationMenuActivity.Companion.purplePoints
import com.mateosa.expresionexpress.DurationMenuActivity.Companion.redPoints
import com.mateosa.expresionexpress.DurationMenuActivity.Companion.totalPoints

class GameBoardActivity : AppCompatActivity() {

    private lateinit var tvTitlePoints: TextView
    private lateinit var tvRedPoints: TextView
    private lateinit var tvPurplePoints: TextView
    private lateinit var cvPurple: CardView
    private lateinit var cvRed: CardView
    private lateinit var btnStart: Button

    private var activePoints: Boolean = false
    private var activeStart: Boolean = true

    companion object {
        var clickTutorial : Boolean = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game_board)
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
        tvTitlePoints = findViewById(R.id.tvTitlePoints)
        tvRedPoints = findViewById(R.id.tvRedPoints)
        tvPurplePoints = findViewById(R.id.tvPurplePoints)
        cvPurple = findViewById(R.id.cvPurple)
        cvRed = findViewById(R.id.cvRed)
        btnStart = findViewById(R.id.btnStart)
    }

    private fun initListeners() {


        cvPurple.setOnClickListener {
            if (activePoints) {
                purplePoints += 1
                tvPurplePoints.text = purplePoints.toString()
                setActivations(true)
                victoryCheck()
            }
        }

        cvRed.setOnClickListener {
            if (activePoints) {
                redPoints += 1
                tvRedPoints.text = redPoints.toString()
                setActivations(true)
                victoryCheck()
            }
        }

        btnStart.setOnClickListener {
            if (activeStart) {
                val intent = Intent(this, WordsBoardActivity::class.java)
                startActivity(intent)
            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showOutDialog()
            }
        })
    }

    private fun showOutDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_outgame)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnExitYes: Button = dialog.findViewById(R.id.btnExitYes)
        val btnExitNo: Button = dialog.findViewById(R.id.btnExitNo)

        btnExitNo.setOnClickListener {
            dialog.hide()
        }

        btnExitYes.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        dialog.show()
    }

    private fun showClickDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_points)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val dialogScreen : LinearLayout = dialog.findViewById(R.id.dialogScreen)
        val ivHand : ImageView = dialog.findViewById(R.id.ivHand)

        val translateAnimation = TranslateAnimation(
            0f,
            0f,
            0f,
            100f
        )
        translateAnimation.duration = 1000
        translateAnimation.repeatCount = Animation.INFINITE
        translateAnimation.repeatMode = Animation.REVERSE
        ivHand.startAnimation(translateAnimation)

        dialogScreen.setOnClickListener{
            dialog.hide()
        }

        dialog.show()
    }

    private fun initUI() {
        setPoints()
        setActivations(intent.extras?.getBoolean("ACTIVATION_KEY") ?: true)
    }

    private fun victoryCheck() {

        if (redPoints>= totalPoints){
            val intent = Intent(this, FinalScreenActivity::class.java)
            intent.putExtra("VICTORY_KEY", false)
            startActivity(intent)
        }

        if (purplePoints>= totalPoints){
            val intent = Intent(this, FinalScreenActivity::class.java)
            intent.putExtra("VICTORY_KEY", true)
            startActivity(intent)
        }

    }

    private fun setActivations(activation: Boolean) {
        if (activation) {
            activePoints = false
            activeStart = true
        } else {
            if (clickTutorial){
                showClickDialog()
                clickTutorial=false
            }
            activePoints = true
            activeStart = false
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setPoints() {
        tvTitlePoints.text = "Gana el primer equipo en llegar a $totalPoints puntos"
        tvRedPoints.text = redPoints.toString()
        tvPurplePoints.text = purplePoints.toString()
    }

}