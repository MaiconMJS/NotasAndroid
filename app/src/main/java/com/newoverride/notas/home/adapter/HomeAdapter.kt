package com.newoverride.notas.home.adapter

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.newoverride.notas.Home
import com.newoverride.notas.databinding.NotasBinding
import com.newoverride.notas.home.model.Nota
import com.newoverride.notas.home.view.HomeView

class HomeAdapter(
    private val context: Context,
    private val notas: MutableList<Nota>,
    private val editCallBack: Home.EditOnClick
) :
    RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {
    @SuppressLint("NotifyDataSetChanged")
    inner class HomeViewHolder(private val binding: NotasBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(nota: Nota) {
            binding.txtCardTitulo.text = nota.titulo
            binding.txtCardDescricao.text = nota.descricao
            binding.checkbox.isChecked = nota.removeNote
            binding.dataInfo.text = nota.data
            binding.horaInfo.text = nota.hora
            binding.checkbox.visibility = if (nota.ativoCheckBox) View.VISIBLE else View.GONE

            // FAZ UMA ANIMAÇÃO NO CHECKBOX SE FOR VISÍVEL!
            val checkBoxView = binding.checkbox.visibility == View.VISIBLE
            val checkBox = binding.checkbox
            if (checkBoxView) {
                checkBox.translationX = -200f // COMEÇA FORA DA TELA PARA A ESQUERDA!
                val slideInAnimator =
                    ObjectAnimator.ofFloat(checkBox, "translationX", -200f, 0f)
                slideInAnimator.duration = 300
                slideInAnimator.start()
            } else {
                val slideOutAnimator =
                    ObjectAnimator.ofFloat(checkBox, "translationX", 0f, 200f)
                slideOutAnimator.duration = 300
                slideOutAnimator.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        binding.checkbox.visibility = View.GONE
                    }
                })
                slideOutAnimator.start()
            }
        }

        // ESCUTANDO CARD E CHECKBOX!
        init {
            with(binding) {
                cardView.setOnClickListener {
                    editCallBack.onClickEdit(adapterPosition)
                }
                cardView.setOnLongClickListener {
                    HomeView.dataList!![adapterPosition].ativoCheckBox =
                        !HomeView.dataList!![adapterPosition].ativoCheckBox
                    HomeView.dataList!![adapterPosition].removeNote = false
                    notifyItemChanged(adapterPosition)
                    true
                }
                checkbox.setOnClickListener {
                    HomeView.dataList!![adapterPosition].removeNote =
                        !HomeView.dataList!![adapterPosition].removeNote
                }
            }
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