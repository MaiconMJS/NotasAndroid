package com.newoverride.notas

import android.app.Application
import com.newoverride.notas.database.AppDatabase

class App: Application() {
    lateinit var db: AppDatabase
    override fun onCreate() {
        super.onCreate()
        db = AppDatabase.getDatabase(this)
    }
}