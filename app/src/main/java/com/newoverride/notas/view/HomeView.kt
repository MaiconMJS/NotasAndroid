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

    private var adapter: HomeAdapter? = null
    private var binding: HomeViewBinding? = null

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
            this?.btnAddNote?.setOnClickListener {
                val intent = Intent(it.context, AddNote::class.java)
                startActivity(intent)
            }
            // REMOVE NOTA DA LISTA!
            this?.btnImgLixeira?.setOnClickListener {
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
                presenter!!.data(dataList!!)
            }
        }
    }

    override fun showDisplay(
        displayView: MutableList<Nota>
    ) {
        binding?.txtInfoAllNotes?.text = displayView.size.toString()
        adapter = HomeAdapter(this, dataList!!, this)
        binding?.rvMain?.adapter = adapter
        binding?.rvMain?.layoutManager = GridLayoutManager(this, 2)
    }

    override fun showError(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

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

    override fun onClickEdit(position: Int) {
        val intent = Intent(this, AddNote::class.java)
        intent.putExtra("position", position)
        startActivity(intent)
    }
}