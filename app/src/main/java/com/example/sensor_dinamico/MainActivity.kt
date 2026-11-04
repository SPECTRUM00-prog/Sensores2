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

    private lateinit var contenedorPrincipal: LinearLayout
    private lateinit var tvTitulo: TextView
    private lateinit var tvValores: TextView
    private lateinit var tvEstado: TextView

    private lateinit var sManager: SensorManager
    private var miSensor: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        contenedorPrincipal = findViewById(R.id.contenedorPrincipal)
        tvTitulo = findViewById(R.id.tvTitulo)
        tvValores = findViewById(R.id.tvValores)
        tvEstado = findViewById(R.id.tvEstado)

        sManager = getSystemService(SENSOR_SERVICE) as SensorManager
        miSensor = sManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        if (miSensor == null) {
            tvEstado.text = "Sin sensor de luz"
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_LIGHT) {
            val lux = event.values[0]
            tvValores.text = "Luz actual: $lux lx"

            if (lux < 50) {
                contenedorPrincipal.setBackgroundColor(Color.BLACK)
                tvTitulo.setTextColor(Color.WHITE)
                tvValores.setTextColor(Color.WHITE)
                tvEstado.setTextColor(Color.WHITE)
                tvEstado.text = "Modo Oscuro (Poca Luz)"

            } else if (lux > 500) {
                contenedorPrincipal.setBackgroundColor(Color.WHITE)
                tvTitulo.setTextColor(Color.BLACK)
                tvValores.setTextColor(Color.BLACK)
                tvEstado.setTextColor(Color.BLACK)
                tvEstado.text = "Modo Claro (Mucha Luz)"
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onResume() {
        super.onResume()
        miSensor?.let {
            sManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }
}
