package com.newoverride.notas.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.newoverride.notas.adapter.HomeAdapter
import com.newoverride.notas.databinding.HomeViewBinding
import com.newoverride.notas.model.Nota

class HomeView : AppCompatActivity() {

    private var binding: HomeViewBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomeViewBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        val lista = mutableListOf<Nota>(
            Nota("Teste De Card", "Apenas um Teste", false),
            Nota("Teste wdawdwadwa", "Apenas um testeawdwadawdwaadwadwa", false),
            Nota("Teste wdawdwadwa", "Apenas um testeawdwadawdwaadwadwa", true),
            Nota("Teste wdawdwadwa", "Apenas um testeawdwadawdwaadwadwawadawdawdwadawdawdawdwadwadwadwadwadwadwadwadwadwadwadwadwad", true),
            Nota("Teste wdawdwadwa", "Apenas um testeawdwadawdwaadwadwa", true),
            Nota("Teste wdawdwadwa", "Apenas um testeawdwadawdwaadwadwa", true),
            Nota("Teste wdawdwadwa", "Apenas um testeawdwadawdwaadwadwa", true),
            Nota("Teste wdawdwadwa", "Apenas um testeawdwadawdwaadwadwa", true),
            Nota("Teste wdawdwadwa", "Apenas um testeawdwadawdwaadwadwa", true),
            Nota("Teste wdawdwadwa", "Apenas um testeawdwadawdwaadwadwa", true),
            Nota("Teste wdawdwadwa", "Apenas um testeawdwadawdwaadwadwa", true),
            Nota("Teste wdawdwadwa", "Apenas um testeawdwadawdwaadwadwa", true),
            Nota("Teste wdawdwadwa", "Apenas um testeawdwadawdwaadwadwa", true),
            Nota("Teste wdawdwadwa", "Apenas um testeawdwadawdwaadwadwa", true),
            Nota("Teste wdawdwadwa", "Apenas um testeawdwadawdwaadwadwa", true),
            Nota("Teste wdawdwadwa", "Apenas um testeawdwadawdwaadwadwa", true),
        )
        val adapter = HomeAdapter(this, lista)
        binding!!.rvMain.layoutManager = GridLayoutManager(this, 2)
        binding!!.rvMain.adapter = adapter
    }
}