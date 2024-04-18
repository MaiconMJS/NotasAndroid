package com.newoverride.notas.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.newoverride.notas.AddNote
import com.newoverride.notas.Home
import com.newoverride.notas.adapter.HomeAdapter
import com.newoverride.notas.database.DependencyInjector
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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomeViewBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        // INICIANDO O PRESENTER!
        presenter = HomePresenter(this, DependencyInjector.homeRepository())
        presenter?.data(dataList!!)

        // ESCUTANDO BOTÕES
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
        }
    }

    // REMOVE A NOTA SELECIONADA DA LIXEIRA!
    private fun removeNote() {
        val itemsToRemove = mutableListOf<Int>()
        dataList!!.forEachIndexed { index, nota ->
            if (nota.removeNote) {
                itemsToRemove.add(index) // ADICIONA O ÍNDICE DOS ITENS A SEREM REMOVIDOS!
            }
        }
        // REMOVENDO ITENS DE TRÁS PARA FRENTE PARA NÃO AFETAR OS ÍNDICES DOS ITENS A SEREM REMOVIDOS EM SEGUIDA!
        for (index in itemsToRemove.reversed()) {
            dataList!!.removeAt(index)
            adapter?.notifyItemRemoved(index) // NOTIFICA QUE UM ITEM FOI REMOVIDO NA POSIÇÃO ESPECÍFICA
        }
        updateTotal()
    }

    // ATUALIZA O TOTAL DE NOTAS AO DELETAR!
    private fun updateTotal() {
        binding?.txtInfoAllNotes?.text = dataList?.size.toString()
    }

    // EXIBE NO DISPLAY AS NOTAS
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