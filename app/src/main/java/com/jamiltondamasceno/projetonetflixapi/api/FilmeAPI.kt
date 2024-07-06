package com.jamiltondamasceno.projetonetflixapi.api

import com.jamiltondamasceno.projetonetflixapi.model.FilmeRecente
import retrofit2.Response
import retrofit2.http.GET

interface FilmeAPI {

    @GET("movie/latest?api_key=${RetrofitService.APIGO}")
    suspend fun recuperarFilmeRecente(): Response<FilmeRecente>




}