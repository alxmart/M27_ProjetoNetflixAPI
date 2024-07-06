package com.jamiltondamasceno.projetonetflixapi

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jamiltondamasceno.projetonetflixapi.api.RetrofitService
import com.jamiltondamasceno.projetonetflixapi.databinding.ActivityMainBinding
import com.jamiltondamasceno.projetonetflixapi.model.FilmeRecente
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val TAG = "info_filme"

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val filmeAPI by lazy {
        RetrofitService.filmeAPI
    }

    var jobFilmeRecente: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }

    override fun onStart() {
        super.onStart()
        recuperarFilmeRecente()
    }

    private fun recuperarFilmeRecente() {

        jobFilmeRecente = CoroutineScope(Dispatchers.IO).launch {

            var resposta: Response<FilmeRecente>? = null

            try {
                resposta = filmeAPI.recuperarFilmeRecente()

            } catch (e: Exception) {
                e.printStackTrace()
                exibirMensagem("Erro ao fazer a requisição")
            }

            if (resposta != null) {

                if (resposta.isSuccessful) {

                    val filmeRecente = resposta.body()
                    val nomeImagem = filmeRecente?.poster_path
                    val url = RetrofitService.BASE_URL_IMAGEM + "w780" + nomeImagem

                    withContext(Dispatchers.Main) {
                        Picasso.get()
                            .load(url)
                            .into(binding.imgCapa)
                    }

                } else {
                    exibirMensagem("Problema ao fazer a requisição CÓDIGO: ${resposta.code()}")
                }

            } else {
                exibirMensagem("Não foi possível fazer a requisição")
            }


        }
    }

    private fun exibirMensagem(mensagem: String) {

        Toast.makeText(
            applicationContext,
            mensagem,
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onStop() {
        super.onStop()
        jobFilmeRecente?.cancel()
    }


}


}