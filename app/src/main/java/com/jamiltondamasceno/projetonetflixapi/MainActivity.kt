package com.jamiltondamasceno.projetonetflixapi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.jamiltondamasceno.projetonetflixapi.adapter.FilmeAdapter
import com.jamiltondamasceno.projetonetflixapi.api.RetrofitService
import com.jamiltondamasceno.projetonetflixapi.databinding.ActivityMainBinding
import com.jamiltondamasceno.projetonetflixapi.model.FilmeRecente
import com.jamiltondamasceno.projetonetflixapi.model.FilmeResposta
import com.jamiltondamasceno.projetonetflixapi.model.Genero
import com.jamiltondamasceno.projetonetflixapi.model.Usuario
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private var paginaAtual = 1
    private val TAG = "info_filme"
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val filmeAPI by lazy {
        RetrofitService.filmeAPI
    }

    var jobFilmeRecente: Job? = null
    var jobFilmesPopulares: Job? = null

    //var linearLayoutManager: LinearLayoutManager? = null
    var gridLayoutManager: GridLayoutManager? = null
    private lateinit var filmeAdapter: FilmeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        inicializarViews()

        // Preciso ter várias instâncias
        //===========================================
        /*val genero1 = Genero(1, "Comédia")
        val genero2 = Genero(2, "Ação")
        Log.i("api_filme","genero1: $genero1 - genero2: $genero2")*/

       /* val usuario1 = Usuario()
        usuario1.nome = "Homer"

        val usuario2 = Usuario()
        usuario2.nome = "Lisa"
        // Uma única instância. Ex. B.D. , Retrofit
        //val retro = RetrofitSingleton.APIGO
        Log.i("api_filme","retrofit: $retro")
        //Log.i("api_filme","genero1: $genero1 - genero2: $genero2")
        Log.i("api_filme","usuario1: $usuario1 - usuario2: $usuario2")
*/
    }

    private fun inicializarViews() {

        filmeAdapter = FilmeAdapter { filme ->
            val intent = Intent(this, DetalhesActivity::class.java)
            intent.putExtra("filme", filme)
            startActivity(intent)
        }
        binding.rvPopulares.adapter = filmeAdapter

        gridLayoutManager = GridLayoutManager(
            this,
            2
        )

        /*linearLayoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )*/

        //binding.rvPopulares.layoutManager = linearLayoutManager

        //binding.rvPopulares.addOnScrollListener( ScrollCustomizado() )
        // object =>  Classe Anônima", herda de OnScrollListener

        binding.rvPopulares.layoutManager = gridLayoutManager

        binding.rvPopulares.addOnScrollListener(object : OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                val podeDescerVerticalmente = recyclerView.canScrollVertically(1)
                // 1) Chegar ao final da lista
                //if ( podeDescerVerticalmente == false ) {
                if ( !podeDescerVerticalmente ) {  // Não NÃO PUDER descer
                    // Carregar mais 20 itens
                    Log.i("recycler_api", "paginaAtual: $paginaAtual")
                    recuperarFilmesPopularesProximaPagina()
                    //binding.fabAdicionar.show()
                }
                /*} else {
                    binding.fabAdicionar.hide()
                }*/

                //Log.i("recycler_api", "podeDescerVerticalmente: $podeDescerVerticalmente")

                /*
                => HIDING FAB on LAST ITEM:
                ==============================
                val ultimoItemVisivel = linearLayoutManager?.findLastVisibleItemPosition()
                val totalItens = linearLayoutManager?.itemCount
                //Log.i("recycler_test", "Ultimo: $ultimoItemVisivel Total $totalItens")
                if (ultimoItemVisivel != null && totalItens != null) {
                    if (totalItens - 1 == ultimoItemVisivel) {//chegou no último item
                        binding.fabAdicionar.hide()
                    } else {//não chegou no último item
                        binding.fabAdicionar.show()
                    }
                }*/

                /*Log.i("recycler_test", "onScrolled: dx: $dx, dy: $dy")
                if (dy > 0) { //
                    binding.fabAdicionar.hide()
                } else {
                    binding.fabAdicionar.show()
                }*/
            }
        })
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

    private fun recuperarFilmesPopularesProximaPagina() {
        if (paginaAtual < 1000) {
            paginaAtual++
            recuperarFilmesPopulares(paginaAtual)
        }
    }

    private fun recuperarFilmesPopulares(pagina: Int = 1) {

        jobFilmesPopulares = CoroutineScope(Dispatchers.IO).launch {

            var resposta: Response<FilmeResposta>? = null

            try {
                resposta = filmeAPI.recuperarFilmespopulares(pagina)
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
                            filmeAdapter.adicionarLista(listaFilmes)
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
