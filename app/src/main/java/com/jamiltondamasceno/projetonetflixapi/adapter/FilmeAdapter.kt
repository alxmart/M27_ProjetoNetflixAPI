package com.jamiltondamasceno.projetonetflixapi.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jamiltondamasceno.projetonetflixapi.databinding.ItemFilmeBinding
import com.jamiltondamasceno.projetonetflixapi.model.Filme

class FilmeAdapter() : RecyclerView.Adapter<FilmeAdapter.FilmeViewHolder>() {

    private var listaFilmes: List<Filme> = emptyList()

    fun adicionarLista( lista: List<Filme> ) {

       this.listaFilmes = lista
        notifyDataSetChanged()
    }

    inner class FilmeViewHolder(itemFilme: ItemFilmeBinding)
        : RecyclerView.ViewHolder(itemFilme.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmeViewHolder {

    return

    }

    override fun onBindViewHolder(holder: FilmeViewHolder, position: Int) {


    }

    override fun getItemCount(): Int {
        return listaFilmes.size
    }


}