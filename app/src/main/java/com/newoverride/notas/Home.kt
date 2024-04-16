package com.newoverride.notas

import com.newoverride.notas.model.Nota

interface Home {

    interface Presenter {
        fun data(displayView: MutableList<Nota>)
    }

    interface View {
        fun showDisplay(displayView: MutableList<Nota>)
        fun showError(msg: String)
        fun showLoading(active: Boolean = false)
    }

    interface HomeDataSource {
        fun homeData(displayData: MutableList<Nota>, callback: HomeCallback)
    }

    interface HomeCallback {
        fun onLoad(active: Boolean = false)
        fun onComplete(active: Boolean = false)
        fun onFailure(msg: String)
    }
}