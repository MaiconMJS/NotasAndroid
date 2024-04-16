package com.newoverride.notas.presenter

import com.newoverride.notas.Home
import com.newoverride.notas.database.HomeRepository
import com.newoverride.notas.model.Nota

class HomePresenter(
    private val view: Home.View,
    private val repository: HomeRepository
) : Home.Presenter {
    override fun data(displayView: MutableList<Nota>) {
        repository.homeData(displayView, object : Home.HomeCallback {
            override fun onLoad(active: Boolean) {
                view.showLoading(active)
            }

            override fun onComplete(active: Boolean) {
                view.showLoading(active)
                view.showDisplay(displayView)
            }

            override fun onFailure(msg: String) {
                view.showError(msg)
            }
        })
    }
}