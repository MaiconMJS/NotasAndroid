package com.newoverride.notas.database

import com.newoverride.notas.Home
import com.newoverride.notas.model.Nota

class RoomDataSource : Home.HomeDataSource {
    override fun homeData(displayData: MutableList<Nota>, callback: Home.HomeCallback) {
        callback.onLoad(true)
        if (displayData.isEmpty()) {
            callback.onFailure("Erro ao Carregar Notas!")
        } else {
            callback.onComplete(false)
        }
    }
}