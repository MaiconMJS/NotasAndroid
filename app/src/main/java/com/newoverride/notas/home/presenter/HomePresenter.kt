package com.newoverride.notas.home.presenter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.newoverride.notas.App
import com.newoverride.notas.Home
import com.newoverride.notas.R
import com.newoverride.notas.database.HomeRepository
import com.newoverride.notas.database.RoomNote
import com.newoverride.notas.home.model.Nota
import com.newoverride.notas.home.view.HomeView

class HomePresenter(
    private val view: Home.View,
    private val repository: HomeRepository
) : Home.Presenter {
    override fun data(displayView: MutableList<Nota>) {
        repository.homeData(displayView, object : Home.HomeCallback {
            override fun onLoad(active: Boolean) {
                view.showLoading(active)
                Handler(Looper.getMainLooper()).postDelayed({
                    view.showLoading(false)
                }, 2000)
            }

            override fun onComplete(active: Boolean) {
                view.showLoading(active)
                view.showDisplay(displayView)
            }

            override fun onFailure(msg: String) {
                view.showError(msg)
                view.showDisplay(displayView)
            }
        })
    }

    // VERIFICA SE HÁ NOTAS SELECIONADAS SE HOUVER DESMARCA TUDO SE NÃO MARCA TODAS!
    @SuppressLint("NotifyDataSetChanged")
    override fun verificaSeHaNotasSelecionadas() {
        val allSelect = HomeView.dataList!!.all { it.removeNote }
        HomeView.dataList!!.forEach { nota ->
            nota.ativoCheckBox = !allSelect
            nota.removeNote = !allSelect
        }
        HomeView.txtSelectAllVerify = !allSelect
        HomeView.adapter!!.notifyDataSetChanged()
    }

    // BUSCA NO BANCO DE DADOS E EXIBE NA TELA!
    override fun buscaNoBancoPassaParaDataList(context: Context) {
        Thread {
            val app = context.applicationContext as App
            val dao = app.db.noteDao()
            val listOfBanco = dao.getAll().reversed()
            listOfBanco.forEach { value ->
                HomeView.dataList!!.add(
                    Nota(
                        id = value.id,
                        titulo = value.title.toString(),
                        descricao = value.desc.toString(),
                        data = value.data.toString(),
                        hora = value.hora.toString()
                    )
                )
            }
            (context as Activity).runOnUiThread {
                HomeView.presenter!!.data(HomeView.dataList!!)
            }
        }.start()
    }

    // REMOVE A NOTA SELECIONADA AO PRESSIONAR A LIXEIRA!
    override fun removeNote(context: Context, binding: TextView) {
        // VERIFICA SE ALGUM CHECKBOX FOI MARCADO!
        val isAnyNoteSelected = HomeView.dataList!!.any { nota -> nota.removeNote }
        if (isAnyNoteSelected) {
            val dialogBuilder = AlertDialog.Builder(context, R.style.RoundedDialog)
            dialogBuilder.setMessage(context.getString(R.string.tem_certeza))
                .setCancelable(false)
                .setPositiveButton(context.getString(R.string.sim)) { _, _ ->
                    val itemsToRemove = mutableListOf<Int>()
                    val listRoomNote = mutableListOf<RoomNote>()
                    HomeView.dataList!!.forEachIndexed { index, nota ->
                        if (nota.removeNote) {
                            itemsToRemove.add(index) // ADICIONA O ÍNDICE DOS ITENS A SEREM REMOVIDOS!
                            listRoomNote.add(
                                RoomNote(
                                    id = nota.id!!.toInt(),
                                    title = nota.titulo,
                                    desc = nota.descricao,
                                    data = nota.data,
                                    hora = nota.hora
                                )
                            )
                        }
                    }
                    Thread {
                        val app = context.applicationContext as App
                        val dao = app.db.noteDao()
                        dao.delete(listRoomNote)
                    }.start()

                    // REMOVENDO ITENS DE TRÁS PARA FRENTE PARA NÃO AFETAR OS ÍNDICES DOS ITENS A SEREM REMOVIDOS EM SEGUIDA!
                    for (index in itemsToRemove.reversed()) {
                        HomeView.dataList!!.removeAt(index)
                        HomeView.adapter?.notifyItemRemoved(index) // NOTIFICA QUE UM ITEM FOI REMOVIDO NA POSIÇÃO ESPECÍFICA!
                    }
                    binding.text = HomeView.dataList!!.size.toString()
                    (context as Activity).runOnUiThread {
                        Toast.makeText(
                            context,
                            context.getString(R.string.exclusao_concluida),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                .setNegativeButton(context.getString(R.string.nao)) { dialog, _ ->
                    dialog.cancel()
                }
            val alert = dialogBuilder.create()
            alert.setTitle(context.getString(R.string.confirma))
            alert.show()
            // PERSONALIZANDO A COR DOS BOTÕES PARA VERDE!
            alert.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(context.resources.getColor(R.color.green))
            alert.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(context.resources.getColor(R.color.green))
        }
    }
}