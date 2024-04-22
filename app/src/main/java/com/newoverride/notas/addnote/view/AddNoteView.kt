package com.newoverride.notas.addnote.view

import android.os.Build
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.newoverride.notas.ObterHoraData
import com.newoverride.notas.addnote.AddNote
import com.newoverride.notas.addnote.presenter.AddNotePresenter
import com.newoverride.notas.databinding.AddNoteBinding
import com.newoverride.notas.home.view.HomeView

class AddNoteView : AppCompatActivity(), AddNote.View {

    private var binding: AddNoteBinding? = null
    private var presenter: AddNote.Presenter? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AddNoteBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        // INICIANDO O PRESENTER!
        presenter = AddNotePresenter(this)

        // OBTENDO ÍNDICE DA NOTA PRESSIONADA!
        val index = intent.getIntExtra("position", -1)

        // PASSANDO PARA O PRESENTER O ÍNDICE E A LISTA!
        presenter!!.dataAnalist(index, HomeView.dataList!!)

        // ESCUTANDO BOTÃO VOLTAR E SALVANDO UMA NOTA NOVA SE NÃO FOR VAZIA && EDITANDO NOTAS EXISTENTES!
        with(binding!!) {
            btnAdicionarNota.setOnClickListener {
                presenter!!.salvaNotaOuAtualiza(
                    context = this@AddNoteView,
                    index = index,
                    title = binding!!.txtTitulo.text.toString(),
                    desc = binding!!.txtDesc.text.toString(),
                    data = ObterHoraData().data(),
                    hora = ObterHoraData().hora()
                )
            }
            // COMPARTILHA DESCRIÇÃO PARA WHATSAPP!
            btnWhats.setOnClickListener {
                presenter!!.compartilharWhatsApp(
                    context = this@AddNoteView,
                    desc = binding!!.txtDesc.text.toString()
                )
            }
        }
    }

    // PASSA OS DADOS PARA OS CAMPOS TÍTULO E DESCRIÇÃO!
    override fun showUpdateNote(titulo: String, desc: String) {
        binding!!.txtTitulo.setText(titulo)
        binding!!.txtDesc.setText(desc)
    }

    // ATIVA FUNÇÃO SALVAR NOTA NO BOTÃO BACK DO ANDROID NAVIGATION BAR!
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun handleOnBackPressed() {
            val index = intent.getIntExtra("position", -1)
            presenter!!.salvaNotaOuAtualiza(
                this@AddNoteView,
                index,
                binding!!.txtTitulo.text.toString(),
                binding!!.txtDesc.text.toString(),
                ObterHoraData().data(),
                ObterHoraData().hora()
            )
        }
    }

    // DESTROI VARIÁVEIS DE CAMPO!
    override fun onDestroy() {
        binding = null
        presenter = null
        super.onDestroy()
    }
}