package com.newoverride.notas

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ObterHoraData {
    // OBTER A HORA ATUAL DO SISTEMA ANDROID!
    @RequiresApi(Build.VERSION_CODES.O)
    val current: LocalDateTime = LocalDateTime.now()

    @RequiresApi(Build.VERSION_CODES.O)
    fun data(): String {
        // FORMATA A DATA!
        val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return current.format(dateFormatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun hora(): String {
        // FORMATA A HORA!
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        return current.format(timeFormatter)
    }
}