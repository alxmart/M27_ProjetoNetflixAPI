package com.jamiltondamasceno.projetonetflixapi

// Tipos Genéricos = Generic types
fun <T> minhaFuncao( vararg itens: T) {

     itens.forEach { item ->
        print(item)
    }

}

class Carro <T>(anoCarro: T) {

}


fun main() {

    val carro = Carro("1970")

    minhaFuncao("Jamilton", "Ana", 10, carro, true, 10.20 )


}