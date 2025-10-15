package com.example.myapplication.ui.filmes

import androidx.lifecycle.ViewModel
import com.example.myapplication.data.local.Filmes
import com.example.myapplication.data.repository.FilmesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class FilmesUiState(
    val listaDeFilmes: List<Filmes> = emptyList(),
    val nome: String = "",
    val descricao: String = "",
    val filmeEmEdicao: Filmes? = null
)

class FilmesViewModel(private  val repository: FilmesRepository): ViewModel() {
    private val _uiState = MutableStateFlow(FilmesUiState())
    val uiState: StateFlow<FilmesUiState> = _uiState.asStateFlow()
}