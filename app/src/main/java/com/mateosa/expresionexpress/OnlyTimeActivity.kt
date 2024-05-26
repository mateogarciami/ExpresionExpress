package com.mateosa.expresionexpress

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.random.Random

class OnlyTimeActivity : AppCompatActivity() {

    private lateinit var fabStartTimeClock: FloatingActionButton
    private lateinit var fabBack: FloatingActionButton
    private lateinit var alarmSound: MediaPlayer
    private lateinit var pauseSound: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_only_time)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initComponents()
        initListeners()

    }

    private fun initListeners() {

        fabBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        fabStartTimeClock.setOnClickListener {
            activateSound()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if (alarmSound.isPlaying){
                    desactivateSound()
                }
                finish()
            }
        })

    }

    private fun initComponents() {
        fabStartTimeClock = findViewById(R.id.fabStartTime_Clock)
        fabBack = findViewById(R.id.fabBack)
        initSound()
        pauseSound = MediaPlayer.create(this, R.raw.pause_sound)
    }

    private fun initSound() {
        val randomSound = Random.nextInt(0, 6)
        alarmSound = MediaPlayer.create(this, when(randomSound){
            0 -> R.raw.alarm_sound_55
            1 -> R.raw.alarm_sound_60
            2 -> R.raw.alarm_sound_65
            3 -> R.raw.alarm_sound_70
            4 -> R.raw.alarm_sound_75
            else -> R.raw.alarm_sound_80
        }
        )
    }

    private fun activateSound() {

        if (alarmSound.isPlaying){
            desactivateSound()
        }else{
            initSound()
            alarmSound.start()
        }

    }

    private fun desactivateSound() {
        alarmSound.stop()
        alarmSound.prepare()
        pauseSound.start()
    }

}