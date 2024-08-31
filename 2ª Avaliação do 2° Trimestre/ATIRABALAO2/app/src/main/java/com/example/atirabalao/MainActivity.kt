package com.example.atirabalao

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private val handler = Handler()
    private var score = 0
    private lateinit var scoreTextView: TextView

    private val balloonRunnable = object : Runnable {
        override fun run() {
            addBalloon()
            handler.postDelayed(this, 2000) // Repetir a cada 2 segundos
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        scoreTextView = findViewById(R.id.scoreTextView)
        handler.post(balloonRunnable)
    }

    private fun addBalloon() {
        val balloon = ImageView(this)
        balloon.setImageResource(R.drawable.balloon) // Substitua por seu drawable

        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        balloon.layoutParams = layoutParams

        val container = findViewById<ViewGroup>(R.id.container) // Certifique-se de que o ID corresponde ao seu container no layout

        // Adicione o balloon ao container
        container.addView(balloon)

        // Defina a posição inicial e a animação para o balloon
        balloon.post {
            val screenWidth = container.width
            val screenHeight = container.height
            val balloonWidth = balloon.width
            val balloonHeight = balloon.height

            val randomX = (Math.random() * (screenWidth - balloonWidth)).toInt()
            val randomY = screenHeight - balloonHeight

            balloon.x = randomX.toFloat()
            balloon.y = randomY.toFloat()

            // Adicione animação para o balloon subir
            balloon.animate()
                .translationY(-screenHeight.toFloat())
                .setDuration(5000)
                .start()
        }

        // Adicione um OnClickListener ao balão
        balloon.setOnClickListener {
            container.removeView(balloon)
            updateScore() // Atualize a pontuação quando o balão for estourado
        }
    }

    private fun updateScore() {
        score += 10 // Incrementa a pontuação em 10 pontos
        scoreTextView.text = "Pontuação: $score" // Atualiza o texto do TextView com a nova pontuação
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(balloonRunnable)
    }
}
