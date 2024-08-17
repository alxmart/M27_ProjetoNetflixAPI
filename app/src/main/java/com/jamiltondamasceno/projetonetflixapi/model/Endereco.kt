package com.jamiltondamasceno.projetonetflixapi.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "xmlcep", strict = false)

data class Endereco(

    @field:Element(name = "bairro") // Atributo (campo)
    @param:Element(name = "bairro") // Parâmetro do construtor
    val bairro: String,

    @field:Element(name = "complemento") // Atributo (campo)
    @param:Element(name = "complemento") // Parâmetro do construtor
    val complemento: String,

    @field:Element(name = "localidade") // Atributo (campo)
    @param:Element(name = "localidade") // Parâmetro do construtor
    val localidade: String,

    @field:Element(name = "logradouro") // Atributo (campo)
    @param:Element(name = "logradouro") // Parâmetro do construtor
    val logradouro: String,
)

/*
data class Endereco(
    val bairro: String,
    val cep: String,
    val complemento: String,
    val ddd: String,
    val gia: String,
    val ibge: String,
    val localidade: String,
    val logradouro: String,
    val siafi: String,
    val uf: String,
    val unidade: String
)*/
