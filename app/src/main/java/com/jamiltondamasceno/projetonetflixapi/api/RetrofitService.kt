package com.jamiltondamasceno.projetonetflixapi.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitService { // Singleton ("objeto único")

    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val BASE_URL_IMAGEM = "https://image.tmdb.org/t/p/"
    const val APIGO = "8757e6fe8d068d87174a9fd7d132912a"
    const val TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4NzU3ZTZmZThkMDY4ZDg3MTc0YTlmZDdkMTMyOTEyYSIsIm5iZiI6MTcyNDE0OTE3My43NjI5OTIsInN1YiI6IjY2ODZiNmUwOGM2ZmI0YTE1NDg1NTNmYSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.jW9vRGkYpbhOpvQ0GjMICcPyBdORGhRpRz9jgUIEpE8"

    // Config para o Interceptor:
    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .writeTimeout(10, TimeUnit.SECONDS)//Escrita/Salvando na API
        .readTimeout(20, TimeUnit.SECONDS) //Leitura/Recup.dados/lendo da API)
        //.connectTimeout(20, TimeUnit.SECONDS) //Tempo máximo de conexão- Opcional)
        .addInterceptor( AuthInterceptor() )
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl( BASE_URL )
        .addConverterFactory( GsonConverterFactory.create() )
        .client( okHttpClient )  // <- para Interceptor
        .build()

    val filmeAPI = retrofit.create( FilmeAPI::class.java )

    fun <T> recuperarAPI( classe: Class<T> ) : T {
        return retrofit.create( classe )
    }

    fun recuperarViaCEP() : ViaCepAPI {

        return Retrofit.Builder()
            .baseUrl("https://viacep.com.br/ws/")
            //.addConverterFactory( GsonConverterFactory.create() )
            .addConverterFactory( SimpleXmlConverterFactory.create() )
            .build()
            .create( ViaCepAPI::class.java )
    }

}