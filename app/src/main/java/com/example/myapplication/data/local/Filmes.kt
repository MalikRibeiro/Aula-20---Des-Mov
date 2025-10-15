package com.example.myapplication.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "filmes")
data class Filmes(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nome: String,
    val desc: String
)