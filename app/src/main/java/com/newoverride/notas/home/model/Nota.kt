package com.newoverride.notas.home.model

data class Nota(
    val id: Int? = null,
    val titulo: String,
    val descricao: String,
    var ativoCheckBox: Boolean = false,
    var removeNote: Boolean = false,
    val hora: String = "",
    val data: String = ""
)
