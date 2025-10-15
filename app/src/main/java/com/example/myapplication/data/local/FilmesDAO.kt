package com.example.myapplication.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FilmesDAO{

    @Insert
    suspend fun inserir(filme: Filmes)

    @Query("SELECT * FROM filmes")
    suspend fun buscarTodos() : Flow<List<Filmes>>

    @Delete
    suspend fun deletar(filmes: Filmes)

    @Update
    suspend fun atualizar(filmes: Filmes)

}
