package com.newoverride.notas.model

data class Nota(
    val titulo: String,
    val descricao: String,
    var ativoCheckBox: Boolean = false,
    var removeNote: Boolean = false
)
