package com.newoverride.notas

import android.content.Context
import android.widget.TextView
import com.newoverride.notas.home.model.Nota

interface Home {

    interface Presenter {
        fun data(displayView: MutableList<Nota>)
        fun verificaSeHaNotasSelecionadas()
        fun buscaNoBancoPassaParaDataList(context: Context)
        fun removeNote(context: Context, binding: TextView)
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

    interface EditOnClick {
        fun onClickEdit(position: Int)
    }
}