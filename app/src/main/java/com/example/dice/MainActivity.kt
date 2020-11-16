package com.example.dice

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import com.google.android.flexbox.FlexboxLayout
import kotlin.math.sqrt
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var textDiceCount: TextView
    private lateinit var seekBar: SeekBar
    private lateinit var buttonShake: Button
    private lateinit var diceContainer: FlexboxLayout

    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor
    private val shakeThreshold = 3F + SensorManager.GRAVITY_EARTH
    private var diceCount = 5
        set(value) {
            field = value
            textDiceCount.text = getString(R.string.diceCount, value)
            populateDiceContainer(value)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textDiceCount = findViewById(R.id.textDiceCount)
        seekBar = findViewById(R.id.seekBar)
        buttonShake = findViewById(R.id.buttonShake)
        diceContainer = findViewById(R.id.diceContainer)

        sensorManager = getSystemService(SensorManager::class.java)!!
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        seekBar.setOnSeekBarChangeListener { _, progress, _ ->
            diceCount = progress
        }
        buttonShake.setOnClickListener { generateRandomAndShow() }
        //set init values
        seekBar.progress = 5
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(
            accelerometerListener,
            accelerometer,
            SensorManager.SENSOR_DELAY_UI
        )
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(accelerometerListener)
    }

    private fun populateDiceContainer(count: Int) {
        diceContainer.removeAllViews()
        (0 until count).forEach { _ ->
            diceContainer.addView(DiceView(this))
        }
    }
    private fun generateRandomAndShow() {
        for (view in diceContainer.children) {
            if (view is DiceView) {
                val random = Random.Default.nextInt(6) + 1
                view.dotsCount = random
            }
        }
    }

    private val accelerometerListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            val acceleration =
                event!!.values.asSequence().take(3).map { it * it }.sum().let { sqrt(it) }
            if (acceleration > shakeThreshold) {
                generateRandomAndShow()
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
    }
}