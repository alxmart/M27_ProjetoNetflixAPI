package com.jamiltondamasceno.projetonetflixapi.api

import com.jamiltondamasceno.projetonetflixapi.model.Endereco
import okhttp3.Response
import retrofit2.http.GET

interface ViaCepAPI {

    @GET("01001000/json")
    suspend fun recuperarEndereco(): retrofit2.Response<Endereco>

}