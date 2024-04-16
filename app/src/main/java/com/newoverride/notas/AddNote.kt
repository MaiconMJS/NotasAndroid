package com.newoverride.notas

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.newoverride.notas.databinding.AddNoteBinding
import com.newoverride.notas.view.HomeView

class AddNote : AppCompatActivity() {

    private var binding: AddNoteBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AddNoteBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        // ESCUTANDO BOTÃO VOLTAR!
        with(binding) {
            this?.btnAdicionarNota?.setOnClickListener {
                val title = txtTitulo.text.toString()
                val desc = txtDesc.text.toString()
                val intent = Intent(it.context, HomeView::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}