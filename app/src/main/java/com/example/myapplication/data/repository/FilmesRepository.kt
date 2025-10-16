package com.example.myapplication.data.repository

import com.example.myapplication.data.local.Filmes
import com.example.myapplication.data.local.FilmesDAO
import kotlinx.coroutines.flow.Flow

class FilmesRepository (private val filmesDAO: FilmesDAO) {

    suspend fun getAllFilmes(): Flow<List<Filmes>> = filmesDAO.buscarTodos()

    suspend fun inserirFilme(filme: Filmes) {
        filmesDAO.inserir(filme)
    }

    suspend fun atualizarFilme(filme: Filmes) {
        filmesDAO.atualizar(filme)
    }

    suspend fun deleterFilme(filme: Filmes) {
        filmesDAO.deletar(filme)
    }
}