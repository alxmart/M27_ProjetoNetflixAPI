package com.jamiltondamasceno.projetonetflixapi.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitService {

    companion object {

        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val APIGO = "8757e6fe8d068d87174a9fd7d132912a"

        val retrofit = Retrofit.Builder()
            .baseUrl( BASE_URL )
            .addConverterFactory( GsonConverterFactory.create() )
            .build()

        val filmeAPI = retrofit.create( FilmeAPI::class.java )
    }
}