package com.newoverride.notas.addnote.presenter

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.newoverride.notas.App
import com.newoverride.notas.addnote.AddNote
import com.newoverride.notas.database.RoomNote
import com.newoverride.notas.home.model.Nota
import com.newoverride.notas.home.view.HomeView

class AddNotePresenter(
    private val view: AddNote.View
) : AddNote.Presenter {
    // PASSA OS DADOS PARA OS CAMPOS TÍTULO E DESCRIÇÃO!
    override fun dataAnalist(index: Int, listdata: MutableList<Nota>) {
        if (index != -1 && listdata.isNotEmpty()) {
            view.showUpdateNote(titulo = listdata[index].titulo, desc = listdata[index].descricao)
        }
    }

    // COMPARTILHA DESCRIÇÃO PARA WHATSAPP!
    override fun compartilharWhatsApp(context: Context, desc: String) {
        if (desc.isNotEmpty()) {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                setPackage("com.whatsapp")
                putExtra(Intent.EXTRA_TEXT, desc)
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            context.startActivity(shareIntent)
        }
    }

    // SALVA A NOTA OU ATUALIZA OS DADOS DE UMA EXISTENTE, DEPOIS FECHA A ATIVIDADE VOLTANDO PARA TELA INICIAL!
    override fun salvaNotaOuAtualiza(
        context: Context,
        index: Int,
        title: String,
        desc: String,
        data: String,
        hora: String
    ) {
        if (title.isNotBlank() || desc.isNotBlank()) {
            Thread {
                val app = context.applicationContext as App
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
                (context as Activity).runOnUiThread {
                    HomeView.presenter?.data(HomeView.dataList!!)
                }
            }.start()
        }
        (context as Activity).runOnUiThread {
            context.finish()
        }
    }
}