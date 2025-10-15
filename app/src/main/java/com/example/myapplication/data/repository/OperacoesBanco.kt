package com.example.myapplication.data.repository

import android.util.Log
import com.example.myapplication.data.local.Filmes
import com.example.myapplication.data.local.FilmesDAO

suspend fun buscarFilmes(filmesDao: FilmesDAO): List<Filmes> {
    return try {
        filmesDao.buscarTodos()
    } catch (e: Exception) {
        Log.e("Erro ao buscar", "${e.message}")
        emptyList()
    }
}

suspend fun inserirFilme(nome: String, desc: String, filmesDao: FilmesDAO) {
    try {
        filmesDao.inserir(Filmes(nome = nome, desc = desc))
    } catch (e: Exception) {
        Log.e("Erro ao adicionar", "Msg: ${e.message}")
    }
}

suspend fun deletarFilme(filme: Filmes, filmesDao: FilmesDAO) {
    try {
        filmesDao.deletar(filme)
    } catch (e: Exception) {
        Log.e("Erro ao deletar", "Msg: ${e.message}")
    }
}

suspend fun atualizarFilme(filme: Filmes, filmesDao: FilmesDAO) {
    try {
        filmesDao.atualizar(filme)
    } catch (e: Exception) {
        Log.e("Erro ao atualizar", "Msg: ${e.message}")
    }
}