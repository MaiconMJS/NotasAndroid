package com.newoverride.notas.home.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.newoverride.notas.Home
import com.newoverride.notas.home.adapter.HomeAdapter
import com.newoverride.notas.addnote.view.AddNoteView
import com.newoverride.notas.database.DependencyInjector
import com.newoverride.notas.databinding.HomeViewBinding
import com.newoverride.notas.home.model.Nota
import com.newoverride.notas.home.presenter.HomePresenter

class HomeView : AppCompatActivity(), Home.View, Home.EditOnClick {

    private var binding: HomeViewBinding? = null

    // VARIÁVEIS STÁTICAS!
    companion object {
        var dataList: MutableList<Nota>? = mutableListOf()
        var presenter: Home.Presenter? = null
        var txtSelectAllVerify = false

        @SuppressLint("StaticFieldLeak")
        var adapter: HomeAdapter? = null
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
                val intent = Intent(it.context, AddNoteView::class.java)
                startActivity(intent)
            }
            // REMOVE NOTA DA LISTA!
            // REMOVE A NOTA SELECIONADA AO PRESSIONAR A LIXEIRA!
            this?.btnImgLixeira?.setOnClickListener {
                presenter!!.removeNote(this@HomeView, binding!!.txtInfoAllNotes)
            }
            // VERIFICA SE HÁ NOTAS SELECIONADAS SE HOUVER DESMARCA TUDO SE NÃO MARCA TODAS!
            this?.txtSelectAll!!.setOnClickListener {
                presenter!!.verificaSeHaNotasSelecionadas()
            }
        }
        // BUSCA NO BANCO DE DADOS E EXIBE NA TELA!
        presenter!!.buscaNoBancoPassaParaDataList(this)
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

    // EXIBE NA TELA UM PROGRESS DE CARREGAMENTO SE A LISTA FOR VAZIA!
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

    // RESPONSÁVEL POR LEVAR A POSIÇÃO DE UMA NOTA PARA TELA ADDNOTE!
    override fun onClickEdit(position: Int) {
        val intent = Intent(this, AddNoteView::class.java)
        intent.putExtra("position", position)
        startActivity(intent)
    }
}