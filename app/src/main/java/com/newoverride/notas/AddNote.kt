package com.newoverride.notas

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.newoverride.notas.databinding.AddNoteBinding
import com.newoverride.notas.model.Nota
import com.newoverride.notas.view.HomeView

class AddNote : AppCompatActivity() {

    private var binding: AddNoteBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AddNoteBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        val index = intent.getIntExtra("position", -1)
        if (index != -1 && HomeView.dataList != null) {
            val nota = HomeView.dataList?.get(index)
            binding?.txtTitulo?.setText(nota?.titulo)
            binding?.txtDesc?.setText(nota?.descricao)
        }
        // ESCUTANDO BOTÃO VOLTAR E SALVANDO UM NOTA NOVA SE NÃO FOR VAZIO && EDITANDO NOTAS EXISTENTES!
        with(binding) {
            this?.btnAdicionarNota?.setOnClickListener {
                val title = txtTitulo.text.toString()
                val desc = txtDesc.text.toString()
                if (!title.isNullOrBlank() && !desc.isNullOrBlank()) {
                    if (index != -1) {
                        HomeView.dataList?.set(index, Nota(title, desc))
                    } else {
                        HomeView.dataList?.add(Nota(title, desc))
                    }
                    HomeView.presenter?.data(HomeView.dataList!!)
                }
                finish()
            }
        }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}