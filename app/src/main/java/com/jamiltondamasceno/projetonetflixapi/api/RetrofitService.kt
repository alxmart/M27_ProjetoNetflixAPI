package com.jamiltondamasceno.projetonetflixapi.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitService { // Singleton ("objeto único")

    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val BASE_URL_IMAGEM = "https://image.tmdb.org/t/p/"
    const val APIGO = "8757e6fe8d068d87174a9fd7d132912a"

    // Config para o Interceptor:
    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .writeTimeout(10, TimeUnit.SECONDS)//Escrita/Salvando na API
        .readTimeout(20, TimeUnit.SECONDS) //Leitura/Recup.dados/lendo da API)
        //.connectTimeout(20, TimeUnit.SECONDS) //Tempo máximo de conexão- Opcional)
        .addInterceptor(  )
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl( BASE_URL )
        .addConverterFactory( GsonConverterFactory.create() )
        .client( okHttpClient )  // <- para Interceptor
        .build()

    val filmeAPI = retrofit.create( FilmeAPI::class.java )
}