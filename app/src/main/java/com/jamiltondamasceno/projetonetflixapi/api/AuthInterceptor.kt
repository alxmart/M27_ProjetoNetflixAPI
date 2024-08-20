package com.jamiltondamasceno.projetonetflixapi.api

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        // 1) Acessar a requisição
        val construtorRequisicao = chain.request().newBuilder()

        // 2) Alterar URL ou Rota da requisição
        //https://api.themoviedb.org/3/  +  movie/latest

        /*val urlAtual = chain.request().url()
        val novaUrl = urlAtual.newBuilder()
        novaUrl.addQueryParameter(
        "api_key",
        RetrofitService.APIGO)
            .build()

        // 3) Configurar nova URL na requisição
        construtorRequisicao.url(novaUrl.build())*/

        // Utilizando BEARER TOKEN:
        val requisicao = construtorRequisicao.addHeader(
            "Authorization", "Bearer ${RetrofitService.TOKEN}"
        ).build()
         // .addHeader("", "")

        //return chain.proceed(construtorRequisicao.build())//Response

        // Usando o BEARER TOKEN:
        return chain.proceed(requisicao)//Response
    }


}