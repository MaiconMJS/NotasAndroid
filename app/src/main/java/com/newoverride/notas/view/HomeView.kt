package com.newoverride.notas.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.newoverride.notas.Home
import com.newoverride.notas.adapter.HomeAdapter
import com.newoverride.notas.databinding.HomeViewBinding
import com.newoverride.notas.model.Nota
import com.newoverride.notas.presenter.HomePresenter

class HomeView : AppCompatActivity(), Home.View {

    private var binding: HomeViewBinding? = null
    private var presenter: Home.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomeViewBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        // INICIANDO O PRESENTER!
        presenter = HomePresenter(this)

        // DADOS FAKE!
        val lista = mutableListOf<Nota>(
            Nota("teste", "Apenas um Teste"),
            Nota("teste", "Apenas um Teste"),
            Nota("teste", "Apenas um Teste"),
            Nota("teste", "Apenas um Teste"),
            Nota("teste", "Apenas um Teste"),
            Nota("teste", "Apenas um Teste"),
            Nota("teste", "Apenas um Teste"),
            Nota("teste", "Apenas um Teste"),
            Nota("teste", "Apenas um Teste"),
            Nota("teste", "Apenas um Teste"),
            Nota("teste", "Apenas um Teste"),
            Nota("teste", "Apenas um Teste"),
        )
        presenter!!.data(lista.size.toString(), lista)
    }

    override fun showDisplay(
        allNotes: String,
        displayView: List<Nota>
    ) {
        // EXIBINDO TOTAL DE NOTAS!
        binding!!.txtInfoAllNotes.text = allNotes
        // CONFIGURANDO ADAPTER!
        val adapter = HomeAdapter(this, displayView)
        binding!!.rvMain.adapter = adapter
        binding!!.rvMain.layoutManager = GridLayoutManager(this, 2)
    }
}