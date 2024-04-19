package com.newoverride.notas.model

data class Nota(
    val id: Int? = null,
    val titulo: String,
    val descricao: String,
    var ativoCheckBox: Boolean = false,
    var removeNote: Boolean = false,
    var animacaoExecutada: Boolean = false
)
