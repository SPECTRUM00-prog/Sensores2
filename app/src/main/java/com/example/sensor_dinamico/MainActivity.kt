package com.example.sensor_dinamico

import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var LayoutColor: LinearLayout
    private lateinit var tvTitulo: TextView
    private lateinit var tvValores: TextView
    private lateinit var tvEstado: TextView

    // Objeto que permite ocupar cualquier sensor
    private lateinit var sensorManager: SensorManager

    // Atributo que permite ocupar un sensor en particular
    private var sensorluz: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        LayoutColor = findViewById(R.id.LayoutColor)
        tvTitulo = findViewById(R.id.tvTitulo)
        tvValores = findViewById(R.id.tvValores)
        tvEstado = findViewById(R.id.tvEstado)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        sensorluz = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        if (sensorluz == null) {
            tvEstado.text = "No hay sensor de luz :("
        }
    }

    override fun onSensorChanged(p0: SensorEvent?) {

        if (p0?.sensor?.type == Sensor.TYPE_LIGHT) {

            val luz = p0.values[0]

            tvValores.text = "Luz: $luz lx"

            if (luz < 50) {
                LayoutColor.setBackgroundColor(Color.BLACK)
                tvTitulo.setTextColor(Color.WHITE)
                tvValores.setTextColor(Color.WHITE)
                tvEstado.setTextColor(Color.WHITE)
                tvEstado.text = "Modo Oscuro"

            } else if (luz > 500) {
                LayoutColor.setBackgroundColor(Color.WHITE)
                tvTitulo.setTextColor(Color.BLACK)
                tvValores.setTextColor(Color.BLACK)
                tvEstado.setTextColor(Color.BLACK)
                tvEstado.text = "Modo Claro"
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    override fun onResume() {
        super.onResume()

        sensorluz?.let {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }
}