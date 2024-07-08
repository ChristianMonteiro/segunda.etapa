package com.example.jogodavelha

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.jogodavelha.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    //Vetor bidimensional como aparência do tabuleiro
    val tabuleiro = arrayOf(
        arrayOf("A", "B", "C"),
        arrayOf("D", "E", "F"),
        arrayOf("G", "H", "I")
    )

    //Valor do jogador (x)
    var jogadorAtual = "X"

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    //Função para os botões view é o botão clicado
    fun buttonClick(view: View) {
        //O botão clicado é associado com um valor
        val buttonSelecionado = view as Button
        //O texto do botão vê o jogador atual
        buttonSelecionado.text = jogadorAtual

        //De acordo com o botão clicado, a posição da matriz receberá o Jogador
        when(buttonSelecionado.id){
            binding.buttonZero.id -> tabuleiro[0][0] = jogadorAtual
            binding.buttonUm.id -> tabuleiro[0][1] = jogadorAtual
            binding.buttonDois.id -> tabuleiro[0][2] = jogadorAtual
            binding.buttonTres.id -> tabuleiro[1][0] = jogadorAtual
            binding.buttonQuatro.id -> tabuleiro[1][1] = jogadorAtual
            binding.buttonCinco.id -> tabuleiro[1][2] = jogadorAtual
            binding.buttonSeis.id -> tabuleiro[2][0] = jogadorAtual
            binding.buttonSete.id -> tabuleiro[2][1] = jogadorAtual
            binding.buttonOito.id -> tabuleiro[2][2] = jogadorAtual
        }
        //Vê o vencedor pela função verificaTabuleiro
        val vencedor = verificaVencedor(tabuleiro)

        if (!vencedor.isNullOrBlank()) {
            Toast.makeText(this, "Vencedor: $vencedor", Toast.LENGTH_LONG).show()
            reiniciarJogo()
            return
        }

        if (jogadorAtual == "X") {
            buttonSelecionado.setBackgroundResource(R.drawable.robo)
            jogadorAtual = "O"
            if (modoDificil) {
                botJogarDificil()
            } else {
                botJogar()
            }
        } else {
            buttonSelecionado.setBackgroundResource(R.drawable.player)
            jogadorAtual = "X"
        }
        buttonSelecionado.isEnabled = false
    }

    private fun reiniciarJogo() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun verificaVencedor(tabuleiro: Array<Array<String>>): String? {
        //Verifica células
        for (i in 0 until 3) {
            // Verifica se há três itens iguais na linha
            if (tabuleiro[i][0] == tabuleiro[i][1] && tabuleiro[i][1] == tabuleiro[i][2]) {
                return tabuleiro[i][0]
            }
            //Verifica se tem 3 itens iguais na coluna
            if (tabuleiro[0][i] == tabuleiro[1][i] && tabuleiro[1][i] == tabuleiro[2][i]) {
                return tabuleiro[0][i]
            }
        }
        //Verifica diagonalmente
        if (tabuleiro[0][0] == tabuleiro[1][1] && tabuleiro[1][1] == tabuleiro[2][2]) {
            return tabuleiro[0][0]
        }
        if (tabuleiro[0][2] == tabuleiro[1][1] && tabuleiro[1][1] == tabuleiro[2][0]) {
            return tabuleiro[0][2]
        }
        // Verifica a quantidade de jogadores
        var empate = 0
        for (linha in tabuleiro) {
            for (valor in linha) {
                if (valor == "X" || valor == "O") {
                    empate++
                }
            }
        }
        //Se tem 9 jogadas e não há três letras iguais, houve um empate
        if (empate == 9) {
            return "Empatou"
        }
        //Sem vencedor
        return null
    }

    private var modoDificil = false

    fun setModoDificil(view: View) {
        modoDificil = !modoDificil
        Toast.makeText(this, "Modo difícil: $modoDificil", Toast.LENGTH_SHORT).show()
    }

    private fun botJogar() {
        //Bot jogada aleatória (modo fácil)
        val jogadasPossiveis = mutableListOf<Button>()
        if (binding.buttonZero.isEnabled) jogadasPossiveis.add(binding.buttonZero)
        if (binding.buttonUm.isEnabled) jogadasPossiveis.add(binding.buttonUm)
        if (binding.buttonDois.isEnabled) jogadasPossiveis.add(binding.buttonDois)
        if (binding.buttonTres.isEnabled) jogadasPossiveis.add(binding.buttonTres)
        if (binding.buttonQuatro.isEnabled) jogadasPossiveis.add(binding.buttonQuatro)
        if (binding.buttonCinco.isEnabled) jogadasPossiveis.add(binding.buttonCinco)
        if (binding.buttonSeis.isEnabled) jogadasPossiveis.add(binding.buttonSeis)
        if (binding.buttonSete.isEnabled) jogadasPossiveis.add(binding.buttonSete)
        if (binding.buttonOito.isEnabled) jogadasPossiveis.add(binding.buttonOito)

        val jogadaSelecionada = jogadasPossiveis.random()
        buttonClick(jogadaSelecionada)
    }

    private fun botJogarDificil() {
        //Colocar jogada estratégica no modo difícil pelo meio de lógica do bot jogar (minimax ou heurística simples)

        //Vê se pode vencer na próxima jogada, bloquear a jogada do jogador se ele puder vencer na próxima jogada, aleatório se nenhuma opção for viável

        val jogadaVencedora = encontrarJogadaVencedora("O")
        if (jogadaVencedora != null) {
            buttonClick(jogadaVencedora)
            return
        }

        val jogadaBloqueio = encontrarJogadaVencedora("X")
        if (jogadaBloqueio != null) {
            buttonClick(jogadaBloqueio)
            return
        }

        botJogar()
    }

    private fun encontrarJogadaVencedora(jogador: String): Button? {
        for (i in 0 until 3) {
            for (j in 0 until 3) {
                if (tabuleiro[i][j] != "X" && tabuleiro[i][j] != "O") {
                    val temp = tabuleiro[i][j]
                    tabuleiro[i][j] = jogador
                    val vencedor = verificaVencedor(tabuleiro)
                    tabuleiro[i][j] = temp
                    if (vencedor == jogador) {
                        return getButtonByPosition(i, j)
                    }
                }
            }
        }
        return null
    }

    private fun getButtonByPosition(i: Int, j: Int): Button {
        return when {
            i == 0 && j == 0 -> binding.buttonZero
            i == 0 && j == 1 -> binding.buttonUm
            i == 0 && j == 2 -> binding.buttonDois
            i == 1 && j == 0 -> binding.buttonTres
            i == 1 && j == 1 -> binding.buttonQuatro
            i == 1 && j == 2 -> binding.buttonCinco
            i == 2 && j == 0 -> binding.buttonSeis
            i == 2 && j == 1 -> binding.buttonSete
            i == 2 && j == 2 -> binding.buttonOito
            else -> binding.buttonZero
        }
    }
}
}