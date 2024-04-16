package com.newoverride.notas.database

object DependencyInjector {
    fun homeRepository() : HomeRepository {
        return HomeRepository(RoomDataSource())
    }
}