package com.newoverride.notas.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.newoverride.notas.Home
import com.newoverride.notas.adapter.HomeAdapter
import com.newoverride.notas.database.DependencyInjector
import com.newoverride.notas.databinding.HomeViewBinding
import com.newoverride.notas.model.Nota
import com.newoverride.notas.presenter.HomePresenter

class HomeView : AppCompatActivity(), Home.View {

    private var presenter: Home.Presenter? = null
    private var dataList: MutableList<Nota>? = mutableListOf()

    companion object {
        @SuppressLint("StaticFieldLeak")
        var adapter: HomeAdapter? = null
        @SuppressLint("StaticFieldLeak")
        var binding: HomeViewBinding? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomeViewBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        // INICIANDO O PRESENTER!
        presenter = HomePresenter(this, DependencyInjector.homeRepository())
        presenter!!.data(dataList!!)
    }

    override fun showDisplay(
        displayView: MutableList<Nota>
    ) {
        // EXIBINDO TOTAL DE NOTAS!
        binding!!.txtInfoAllNotes.text = displayView.size.toString()
        // CONFIGURANDO ADAPTER!
        adapter = HomeAdapter(this, displayView)
        binding!!.rvMain.adapter = adapter
        binding!!.rvMain.layoutManager = GridLayoutManager(this, 2)
    }

    override fun showError(msg: String) {
        binding!!.txtInfoErr.text = msg
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
}