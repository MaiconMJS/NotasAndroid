package com.newoverride.notas.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.newoverride.notas.App
import com.newoverride.notas.ObterHoraData
import com.newoverride.notas.database.NoteDao
import com.newoverride.notas.database.RoomNote
import com.newoverride.notas.databinding.AddNoteBinding
import com.newoverride.notas.model.Nota

class AddNote : AppCompatActivity() {

    private var binding: AddNoteBinding? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AddNoteBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        val index = intent.getIntExtra("position", -1)
        updateNote(index)

        // ESCUTANDO BOTÃO VOLTAR E SALVANDO UM NOTA NOVA SE NÃO FOR VAZIO && EDITANDO NOTAS EXISTENTES!
        with(binding!!) {
            btnAdicionarNota.setOnClickListener {
                salvaNotaOuAtualiza(index)
            }
            // COMPARTILHA DESCRIÇÃO POR WHATSAPP!
            btnWhats.setOnClickListener {
                val descricao = binding!!.txtDesc.text.toString()
                if (descricao.isNotEmpty()) {
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        type = "text/plain"
                        setPackage("com.whatsapp")
                        putExtra(Intent.EXTRA_TEXT, descricao)
                    }
                    val shareIntent = Intent.createChooser(sendIntent, null)
                    startActivity(shareIntent)
                }
            }
        }
    }

    // ATIVA FUNÇÃO SALVAR NOTA NO BOTÃO BACK DO ANDROID NAVIGATION BAR!
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun handleOnBackPressed() {
            val index = intent.getIntExtra("position", -1)
            salvaNotaOuAtualiza(index)
        }
    }

    // PASSA OS DADOS PARA OS CAMPOS TÍTULO E DESCRIÇÃO!
    private fun updateNote(index: Int) {
        if (index != -1 && HomeView.dataList != null) {
            val nota = HomeView.dataList?.get(index)
            binding?.txtTitulo?.setText(nota?.titulo)
            binding?.txtDesc?.setText(nota?.descricao)
        }
    }

    // SALVA A NOTA OU ATUALIZA OS DADOS DE UMA EXISTENTE!
    @RequiresApi(Build.VERSION_CODES.O)
    private fun salvaNotaOuAtualiza(index: Int) {
        val title = binding?.txtTitulo?.text.toString()
        val desc = binding?.txtDesc?.text.toString()
        val hora = ObterHoraData().hora()
        val data = ObterHoraData().data()
        if (title.isNotBlank() || desc.isNotBlank()) {
            Thread {
                val app = application as App
                val dao = app.db.noteDao()
                if (index != -1) { // ATUALIZA NOTA EXSTENTE!
                    val id = HomeView.dataList!![index].id
                    val dataUpdate = HomeView.dataList!![index].data
                    val horaUpdate = HomeView.dataList!![index].hora
                    dao.update(
                        RoomNote(
                            id = id!!.toInt(),
                            title = title,
                            desc = desc,
                            data = dataUpdate,
                            hora = horaUpdate
                        )
                    )
                } else { // INSERE NOVA NOTA!
                    val newNote = RoomNote(title = title, desc = desc, data = data, hora = hora)
                    dao.insert(newNote)
                }
                atualizarListaDeNotas(dao)
            }.start()
        }
        finish()
    }

    // ATUALIZA NOTAS SALVAS OU ATUALIZADAS!
    private fun atualizarListaDeNotas(dao: NoteDao) {
        val listAll = dao.getAll().reversed()
        HomeView.dataList!!.clear()
        HomeView.dataList!!.addAll(listAll.map { value ->
            Nota(
                id = value.id,
                titulo = value.title.toString(),
                descricao = value.desc.toString(),
                data = value.data.toString(),
                hora = value.hora.toString()
            )
        })
        runOnUiThread {
            HomeView.presenter?.data(HomeView.dataList!!)
        }
    }

    // DESTROI VARIÁVEIS DE CAMPO!
    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}