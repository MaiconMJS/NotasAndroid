package com.newoverride.notas.database

import com.newoverride.notas.Home
import com.newoverride.notas.home.model.Nota

class RoomDataSource : Home.HomeDataSource {
    override fun homeData(displayData: MutableList<Nota>, callback: Home.HomeCallback) {
        if (displayData.isEmpty()) {
            callback.onLoad(true)
            callback.onFailure("Buscando Notas!")
        } else {
            callback.onComplete(false)
        }
    }
}