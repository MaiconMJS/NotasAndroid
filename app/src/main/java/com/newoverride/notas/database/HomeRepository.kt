package com.newoverride.notas.database

import com.newoverride.notas.Home
import com.newoverride.notas.home.model.Nota

class HomeRepository(private val dataSource: Home.HomeDataSource) {
    fun homeData(displayData: MutableList<Nota>, callback: Home.HomeCallback) {
        dataSource.homeData(displayData, callback)
    }
}