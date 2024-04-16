package com.newoverride.notas.presenter

import com.newoverride.notas.Home
import com.newoverride.notas.model.Nota

class HomePresenter(
    private val view: Home.View
) : Home.Presenter {
    override fun data(allNotes: String, displayView: List<Nota>) {
        view.showDisplay(allNotes, displayView)
    }
}