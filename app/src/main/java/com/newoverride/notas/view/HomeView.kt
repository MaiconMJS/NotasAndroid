package com.newoverride.notas.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.newoverride.notas.App
import com.newoverride.notas.Home
import com.newoverride.notas.R
import com.newoverride.notas.adapter.HomeAdapter
import com.newoverride.notas.database.DependencyInjector
import com.newoverride.notas.database.RoomNote
import com.newoverride.notas.databinding.HomeViewBinding
import com.newoverride.notas.model.Nota
import com.newoverride.notas.presenter.HomePresenter

class HomeView : AppCompatActivity(), Home.View, Home.editOnClick {

    private var binding: HomeViewBinding? = null
    private var adapter: HomeAdapter? = null

    // VARIÁVEIS STÁTICAS!
    companion object {
        var dataList: MutableList<Nota>? = mutableListOf()
        var presenter: Home.Presenter? = null
        var txtSelectAllVerify = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomeViewBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        // INICIANDO O PRESENTER!
        presenter = HomePresenter(this, DependencyInjector.homeRepository())

        // ESCUTANDO BOTÕES!
        with(binding) {
            // NAVEGA PARA PRÓXIMA TELA!
            this?.btnAddNote?.setOnClickListener {
                val intent = Intent(it.context, AddNote::class.java)
                startActivity(intent)
            }
            // REMOVE NOTA DA LISTA!
            this?.btnImgLixeira?.setOnClickListener {
                removeNote()
            }
            // VERIFICA SE HÁ NOTAS SELECIONADAS SE HOUVER DESMARCA TUDO SE NÃO MARCA TODAS!
            this?.txtSelectAll!!.setOnClickListener {
                verificaSeHaNotasSelecionadas()
            }
        }
        // CHAMADA DE FUNÇÃO!
        buscaNoBancoPassaParaDataList()
    }

    // BUSCA NO BANCO DE DADOS E EXIBE NA TELA!
    private fun buscaNoBancoPassaParaDataList() {
        Thread {
            val app = application as App
            val dao = app.db.noteDao()
            val listOfBanco = dao.getAll()
            listOfBanco.forEach { value ->
                dataList!!.add(
                    Nota(
                        id = value.id,
                        titulo = value.title.toString(),
                        descricao = value.desc.toString()
                    )
                )
            }
            runOnUiThread {
                presenter!!.data(dataList!!)
            }
        }.start()
    }

    // VERIFICA SE HÁ NOTAS SELECIONADAS SE HOUVER DESMARCA TUDO SE NÃO MARCA TODAS!
    @SuppressLint("NotifyDataSetChanged")
    private fun verificaSeHaNotasSelecionadas() {
        val allSelect = dataList!!.all { it.removeNote }
        dataList!!.forEach { nota ->
            nota.ativoCheckBox = !allSelect
            nota.removeNote = !allSelect
        }
        txtSelectAllVerify = !allSelect
        adapter!!.notifyDataSetChanged()
    }

    // REMOVE A NOTA SELECIONADA AO PRESSIONAR A LIXEIRA!
    private fun removeNote() {
        // VERIFICA SE ALGUM CHECKBOX FOI MARCADO!
        val isAnyNoteSelected = dataList!!.any { nota -> nota.removeNote }
        if (isAnyNoteSelected) {
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setMessage(getString(R.string.tem_certeza))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.sim)) { _, _ ->
                    val itemsToRemove = mutableListOf<Int>()
                    val listRoomNote = mutableListOf<RoomNote>()
                    dataList!!.forEachIndexed { index, nota ->
                        if (nota.removeNote) {
                            itemsToRemove.add(index) // ADICIONA O ÍNDICE DOS ITENS A SEREM REMOVIDOS!
                            listRoomNote.add(
                                RoomNote(
                                    id = nota.id!!.toInt(),
                                    title = nota.titulo,
                                    desc = nota.descricao
                                )
                            )
                        }
                    }
                    Thread {
                        val app = application as App
                        val dao = app.db.noteDao()
                        dao.delete(listRoomNote)
                    }.start()

                    // REMOVENDO ITENS DE TRÁS PARA FRENTE PARA NÃO AFETAR OS ÍNDICES DOS ITENS A SEREM REMOVIDOS EM SEGUIDA!
                    for (index in itemsToRemove.reversed()) {
                        dataList!!.removeAt(index)
                        adapter?.notifyItemRemoved(index) // NOTIFICA QUE UM ITEM FOI REMOVIDO NA POSIÇÃO ESPECÍFICA!
                    }
                    updateTotal()
                    Toast.makeText(
                        this@HomeView,
                        getString(R.string.exclusao_concluida),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .setNegativeButton(getString(R.string.nao)) { dialog, _ ->
                    dialog.cancel()
                }
            val alert = dialogBuilder.create()
            alert.setTitle(getString(R.string.confirma))
            alert.show()
            // PERSONALIZANDO A COR DOS BOTÕES PARA AMARELO!
            alert.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(getResources().getColor(R.color.yellow))
            alert.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(getResources().getColor(R.color.yellow))
        }
    }

    // ATUALIZA O TOTAL DE NOTAS AO DELETAR!
    private fun updateTotal() {
        binding?.txtInfoAllNotes?.text = dataList?.size.toString()
    }

    // EXIBE NO DISPLAY AS NOTAS!
    override fun showDisplay(
        displayView: MutableList<Nota>
    ) {
        binding?.txtInfoAllNotes?.text = displayView.size.toString()
        adapter = HomeAdapter(this, dataList!!, this)
        binding?.rvMain?.adapter = adapter
        binding?.rvMain?.layoutManager = GridLayoutManager(this, 2)
    }

    // EXIBE NO DISPLAY INFORMAÇÃO DE BUSCA DE NOTAS!
    override fun showError(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    // EXIBE NA TELA UM PROGRESS DE CARREGAMENTO!
    override fun showLoading(active: Boolean) {
        binding!!.progressBar.visibility = if (active) View.VISIBLE else View.GONE
    }

    // DESTROI VARIÁVEIS DE CAMPO!
    override fun onDestroy() {
        binding = null
        presenter = null
        adapter = null
        dataList = null
        super.onDestroy()
    }

    // RESPONSÁVEL POR LEVAR A POSIÇÃO A TELA ADDNOTE!
    override fun onClickEdit(position: Int) {
        val intent = Intent(this, AddNote::class.java)
        intent.putExtra("position", position)
        startActivity(intent)
    }
}