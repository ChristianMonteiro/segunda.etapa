package com.example.atirabalao

import android.content.Context
import android.animation.ObjectAnimator
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import kotlin.random.Random

class Balloon(context: Context, private val spawnArea: ViewGroup) : AppCompatImageView(context) {

    init {
        // Define a imagem do balão (substitua pelo seu recurso de balão)
        setImageResource(R.drawable.balloon)
        // Define o tamanho menor para o balão
        val size = 100 // Tamanho menor do balão em pixels
        layoutParams = ViewGroup.LayoutParams(size, size)
        // Ajusta a view após definir os parâmetros
        requestLayout()
        setOnClickListener {
            // Remove o balão ao clicar
            (parent as ViewGroup).removeView(this)
        }
    }

    fun floatUp() {
        post {
            val parent = parent as ViewGroup
            val parentHeight = parent.height
            val balloonHeight = height
            val animator = ObjectAnimator.ofFloat(this, "translationY", parentHeight.toFloat(), -balloonHeight.toFloat())
            animator.duration = 5000 // Duração da animação em milissegundos
            animator.start()
        }
    }

    fun setRandomPosition() {
        post {
            val random = Random.Default
            val parent = spawnArea
            val parentWidth = parent.width
            val balloonWidth = width
            // Define a posição X aleatória dentro da largura da área de spawn
            x = random.nextFloat() * (parentWidth - balloonWidth)
            // Define a posição Y fora da tela, ajustado para começar a partir da área de spawn
            y = parent.height.toFloat()
            // Atualiza o layout
            requestLayout()
        }
    }
}
