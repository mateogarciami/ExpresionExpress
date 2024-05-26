package com.mateosa.expresionexpress

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mateosa.expresionexpress.DurationMenuActivity.Companion.usedWordsId
import java.io.InputStream
import kotlin.random.Random

class WordsBoardActivity : AppCompatActivity() {

    private lateinit var tvWord : TextView
    private lateinit var btnNextWord : CardView
    private lateinit var alarmSound: MediaPlayer
    private lateinit var sm:SensorManager
    private lateinit var sa: Sensor
    private lateinit var sel:SensorEventListener

    private var positionSensor : Double = 0.0
    private var setSensor = false
    private var setSound = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_words_board)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initComponents()
        initListeners()
        initUI()

    }

    override fun onResume(){
        if (!setSensor){
            initSensor()
        }
        super.onResume()
    }


    private fun initSensor(){
        sm.registerListener(sel,sa,SensorManager.SENSOR_DELAY_NORMAL)
        setSensor=true
    }

    private fun stopSensor(){
        sm.unregisterListener(sel)
    }

    private fun initComponents() {
        tvWord = findViewById(R.id.tvWord)
        btnNextWord = findViewById(R.id.btnNextWord)
    }

    private fun initListeners() {

        btnNextWord.setOnClickListener {

            val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), ContextCompat.getColor(applicationContext, R.color.blue), ContextCompat.getColor(applicationContext, R.color.darkBlue))
            colorAnimation.duration = 100

            colorAnimation.addUpdateListener { animator ->
                btnNextWord.setCardBackgroundColor(animator.animatedValue as Int)
            }

            colorAnimation.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    btnNextWord.setCardBackgroundColor(ContextCompat.getColor(applicationContext, R.color.blue))
                }
            })

            colorAnimation.start()

            setNewWord()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                showOutDialog()
            }
        })

    }

    private fun showOutDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_outgame)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnExitYes : Button = dialog.findViewById(R.id.btnExitYes)
        val btnExitNo : Button = dialog.findViewById(R.id.btnExitNo)

        btnExitNo.setOnClickListener {
            dialog.hide()
        }

        btnExitYes.setOnClickListener {
            if (alarmSound.isPlaying){
                alarmSound.stop()
                alarmSound.prepare()
            }
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        dialog.show()
    }

    private fun initUI() {

        val dialog = Dialog(this)
        showRotateDialog(dialog)

        sm = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sa = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!
        if(sa==null){
            Toast.makeText(this,"No hay sensor", Toast.LENGTH_LONG).show()
        }
        else{
            sel = object : SensorEventListener{
                override fun onSensorChanged(event: SensorEvent?) {
                    positionSensor = event!!.values[0].toDouble()
                    if (positionSensor>6.0){
                        dialog.hide()
                        alarmAndWord()
                        stopSensor()
                    }
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

                }

            }
        }

    }

    private fun showRotateDialog(dialog:Dialog) {
        dialog.setContentView(R.layout.dialog_rotate)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val dialogRotateScreen : LinearLayout = dialog.findViewById(R.id.dialogRotateScreen)
        val ivRotate : ImageView = dialog.findViewById(R.id.ivRotate)

        val rotateAnimation = RotateAnimation(
            0f,
            -90f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        rotateAnimation.duration = 1500
        rotateAnimation.repeatCount = Animation.INFINITE
        ivRotate.startAnimation(rotateAnimation)

        dialogRotateScreen.setOnClickListener{
            dialog.hide()
            alarmAndWord()
        }

        dialog.setOnDismissListener {
            alarmAndWord()
        }

        dialog.show()

    }

    private fun alarmAndWord() {
        if (!setSound){
            initSound()
            alarmSound.setOnCompletionListener {
                val intent = Intent(this, GameBoardActivity::class.java)
                intent.putExtra("ACTIVATION_KEY", false)
                startActivity(intent)
            }
            setNewWord()
            setSound=true
        }
    }

    private fun setNewWord() {

        val inputStream: InputStream = resources.openRawResource(R.raw.words_list)
        val wordsArray = inputStream.bufferedReader().use { it.readText() }.split("\n")

        var randomId = 0
        var noNewWordCounter = 0
        var boolWords = true
        while (boolWords){

            randomId = Random.nextInt(0, wordsArray.size)
            if (usedWordsId.find { it ==  randomId} == null)boolWords = false

            noNewWordCounter++
            if (noNewWordCounter>10)boolWords = false

        }
        tvWord.text = wordsArray [randomId]
        usedWordsId.add(randomId)

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

        alarmSound.start()

    }


}