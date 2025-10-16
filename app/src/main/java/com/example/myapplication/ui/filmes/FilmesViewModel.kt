package com.example.myapplication.ui.filmes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.Filmes
import com.example.myapplication.data.repository.FilmesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FilmesUiState(
    val listaDeFilmes: List<Filmes> = emptyList(),
    val nome: String = "",
    val descricao: String = "",
    val filmeEmEdicao: Filmes? = null
){
    val textoBotao: String
        get() = if (filmeEmEdicao == null) "Add" else "Atualizar"
}

class FilmesViewModel(private  val repository: FilmesRepository): ViewModel() {
    private val _uiState = MutableStateFlow(FilmesUiState())
    val uiState: StateFlow<FilmesUiState> = _uiState.asStateFlow()

    init {
        // Coleta o Flow do repositório
        viewModelScope.launch {
            repository.getAllFilmes().collect { filmes ->
                _uiState.update { currentState ->
                    currentState.copy(listaDeFilmes = filmes)
                }
            }
        }
    }


    fun onNomeChance(novoNome: String) {
        _uiState.update { it.copy(nome = novoNome) }
    }

    fun onDescricaoChange(novaDescricao: String) {
        _uiState.update { it.copy(descricao = novaDescricao) }
    }

    fun onEditar(filme: Filmes) {
        _uiState.update {
            it.copy(
                nome = filme.nome,
                descricao = filme.desc,
                filmeEmEdicao = filme
            )
        }
    }

    fun onDeletar(filme: Filmes) {
        viewModelScope.launch { repository.deleterFilme(filme) }
    }

    fun onSalvar() {

        val state = _uiState.value

        if (state.nome.isBlank() || state.descricao.isBlank()) return;

        val filmeParaSalvar = state.filmeEmEdicao?.copy(
            nome = state.nome,
            desc = state.descricao
        ) ?: Filmes(
            nome = state.nome,
            desc = state.descricao
        )

        viewModelScope.launch {
            if(state.filmeEmEdicao == null){
                repository.inserirFilme(filmeParaSalvar)
            }else{
                repository.atualizarFilme(filmeParaSalvar)
            }
        }
    }
    private fun limparCampos(){
        _uiState.update {
            it.copy(
                nome = "",
                descricao = "",
                filmeEmEdicao = null
            )
        }
    }
}

// 4. Factory para injetar o Repositório no ViewModel
class FilmesViewModelFactory(private val repository: FilmesRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FilmesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FilmesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
