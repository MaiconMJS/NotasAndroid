package com.newoverride.notas.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.newoverride.notas.databinding.NotasBinding
import com.newoverride.notas.model.Nota

class HomeAdapter(private val context: Context, private val notas: List<Nota>) :
    RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {
    inner class HomeViewHolder(private val binding: NotasBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(nota: Nota) {
            binding.txtCardTitulo.text = nota.titulo
            binding.txtCardDescricao.text = nota.descricao
            binding.checkbox.visibility = if (nota.ativoCheckBox) View.VISIBLE else View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding = NotasBinding.inflate(LayoutInflater.from(context), parent, false)
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(notas[position])
    }

    override fun getItemCount() = notas.size
}