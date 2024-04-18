package com.newoverride.notas.view

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.newoverride.notas.databinding.AddNoteBinding
import com.newoverride.notas.model.Nota

class AddNote : AppCompatActivity() {

    private var binding: AddNoteBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AddNoteBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        val index = intent.getIntExtra("position", -1)
        updateNote(index)

        // ESCUTANDO BOTÃO VOLTAR E SALVANDO UM NOTA NOVA SE NÃO FOR VAZIO && EDITANDO NOTAS EXISTENTES!
        binding!!.btnAdicionarNota.setOnClickListener {
            saveNote(index)
        }
    }

    // ATIVA FUNÇÃO SALVAR NOTA NO BOTÃO BACK DO ANDROID NAVIGATION BAR!
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val index = intent.getIntExtra("position", -1)
            saveNote(index)
        }
    }

    // PASSA OS DADOS PARA OS CAMPOS TÍTULO E DESCRIÇÃO!
    private fun updateNote(index: Int) {
        if (index != -1 && HomeView.dataList != null) {
            val nota = HomeView.dataList?.get(index)
            binding?.txtTitulo?.setText(nota?.titulo)
            binding?.txtDesc?.setText(nota?.descricao)
        }
        HomeView.txtSelectAllVerify = !HomeView.txtSelectAllVerify
    }

    // SALVA A NOTA OU ATUALIZA OS DADOS DE UMA EXISTENTE!
    private fun saveNote(index: Int) {
        val title = binding?.txtTitulo?.text.toString()
        val desc = binding?.txtDesc?.text.toString()
        if (title.isNotBlank() || desc.isNotBlank()) {
            if (index != -1) {
                HomeView.dataList?.set(index, Nota(title, desc))
            } else {
                HomeView.dataList?.add(Nota(title, desc))
            }
            HomeView.presenter?.data(HomeView.dataList!!)
        }
        HomeView.txtSelectAllVerify = !HomeView.txtSelectAllVerify
        finish()
    }
    // DESTROI VARIÁVEIS DE CAMPO!
    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}