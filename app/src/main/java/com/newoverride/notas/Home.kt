package com.newoverride.notas

import com.newoverride.notas.model.Nota

interface Home {

    interface Presenter {
        fun data(allNotes: String, displayView: List<Nota>)
    }

    interface View {
        fun showDisplay(allNotes: String, displayView: List<Nota>)
    }
}