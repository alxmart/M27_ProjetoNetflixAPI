package com.jamiltondamasceno.projetonetflixapi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.jamiltondamasceno.projetonetflixapi.adapter.FilmeAdapter
import com.jamiltondamasceno.projetonetflixapi.api.RetrofitService
import com.jamiltondamasceno.projetonetflixapi.databinding.ActivityMainBinding
import com.jamiltondamasceno.projetonetflixapi.model.FilmeRecente
import com.jamiltondamasceno.projetonetflixapi.model.FilmeResposta
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
    var jobFilmesPopulares: Job? = null

    private lateinit var filmeAdapter: FilmeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        inicializarViews()
    }

    private fun inicializarViews() {

        filmeAdapter = FilmeAdapter{ filme ->
            val intent = Intent(this, DetalhesActivity::class.java)
            intent.putExtra("filme", filme)
            startActivity(intent)
        }
        binding.rvPopulares.adapter = filmeAdapter

        binding.rvPopulares.layoutManager = LinearLayoutManager(
        this,
        LinearLayoutManager.VERTICAL,
        false
        )

        //binding.rvPopulares.addOnScrollListener( ScrollCustomizado() )
        // object =>  Classe Anônima", herda de OnScrollListener
        binding.rvPopulares.addOnScrollListener(object : OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                Log.i("recycler_test", "onScrolled: dx: $dx, dy: $dy")
            //super.onScrolled(recyclerView, dx, dy)
            }
            
        } )
        
    }

    /*class ScrollCustomizado : OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {            
        }        
    }*/


    override fun onStart() {
        super.onStart()
        recuperarFilmeRecente()
        recuperarFilmesPopulares()
    }

    private fun recuperarFilmesPopulares() {

        jobFilmesPopulares = CoroutineScope(Dispatchers.IO).launch {

            var resposta: Response<FilmeResposta>? = null

            try {
                resposta = filmeAPI.recuperarFilmespopulares()
            } catch (e: Exception) {
                e.printStackTrace()
                exibirMensagem("Erro ao fazer a requisição")
            }

            if (resposta != null) {

                if (resposta.isSuccessful) {

                    val filmeResposta = resposta.body()
                    val listaFilmes = filmeResposta?.filmes

                    if (listaFilmes != null && listaFilmes.isNotEmpty()) {

                        withContext(Dispatchers.Main) {
                            filmeAdapter.adicionarLista( listaFilmes )
                        }


                        /*Log.i("filmes_api", "Lista filmes:")
                        listaFilmes.forEach { filme ->
                            Log.i("filmes_api", "Título: ${filme.title}")
                        }*/
                    }
                } else {
                    exibirMensagem("Problema ao fazer a requisição CÓDIGO: ${resposta.code()}")
                }

            } else {
                exibirMensagem("Não foi possível fazer a requisição")
            }
        }
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
                    val titulo = filmeRecente?.title
                    val url = RetrofitService.BASE_URL_IMAGEM + "w780" + nomeImagem

                    withContext(Dispatchers.Main) {
                        /*val texto = "Título: $titulo URL: $url"
                        binding.textPopulares.text = texto*/
                        Picasso.get()
                            .load(url)
                            .error(R.drawable.capa) //Se não recuperar imagem de filme recente
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
        jobFilmesPopulares?.cancel()
    }

}
