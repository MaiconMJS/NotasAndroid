package com.newoverride.notas.addnote

import android.content.Context
import com.newoverride.notas.home.model.Nota

interface AddNote {
    interface Presenter {
        fun dataAnalist(index: Int, listdata: MutableList<Nota>)
        fun compartilharWhatsApp(context: Context, desc: String)
        fun salvaNotaOuAtualiza(context: Context, index: Int, title: String, desc: String, data: String, hora: String)
    }

    interface View {
        fun showUpdateNote(titulo: String, desc: String)
    }
}