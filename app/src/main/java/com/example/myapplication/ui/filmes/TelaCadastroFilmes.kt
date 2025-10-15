package com.example.myapplication.ui.filmes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.local.Filmes
import com.example.myapplication.data.repository.atualizarFilme
import com.example.myapplication.data.repository.buscarFilmes
import com.example.myapplication.data.repository.deletarFilme
import com.example.myapplication.data.repository.inserirFilme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun TelaCadastroFilmes() {

    var nome by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var filmes by remember { mutableStateOf<List<Filmes>>(emptyList()) }

    // Estado para controlar qual filme está sendo editado
    var filmeEmEdicao by remember { mutableStateOf<Filmes?>(null) }

    // Texto do botão que muda dependendo da ação (adicionar ou atualizar)
    val textoBotao = if (filmeEmEdicao == null) "Adicionar Filme" else "Atualizar Filme"

    val contex = LocalContext.current
    val db = AppDatabase.getDatabase(contex)
    val filmesDao = db.filmesDAO()

    // Carrega os filmes na primeira vez que a tela é exibida
    LaunchedEffect(Unit) {
        filmes = buscarFilmes(filmesDao)
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Card(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            border = BorderStroke(1.dp, Color.Gray),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0))
        ) {

            Column(modifier = Modifier.padding(16.dp)) {
                TextField(
                    value = nome,
                    onValueChange = { nome = it },
                    label = { Text("Nome do Filme") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                TextField(
                    value = descricao,
                    onValueChange = { descricao = it },
                    label = { Text("Descrição") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    modifier = Modifier.align(Alignment.End),
                    onClick = {
                        if (nome.isNotBlank() && descricao.isNotBlank()) {
                            CoroutineScope(Dispatchers.IO).launch {
                                // Verifica se está editando ou adicionando
                                if (filmeEmEdicao == null) {
                                    // Adiciona um novo filme
                                    inserirFilme(nome, descricao, filmesDao)
                                } else {
                                    // Atualiza um filme existente
                                    val filmeAtualizado = filmeEmEdicao!!.copy(nome = nome, desc = descricao)
                                    atualizarFilme(filmeAtualizado, filmesDao)
                                }

                                // Limpa os campos, reseta o estado de edição e recarrega a lista
                                nome = ""
                                descricao = ""
                                filmeEmEdicao = null
                                filmes = buscarFilmes(filmesDao)
                            }
                        }
                    }
                ) {
                    Text(textoBotao) // Usa o texto dinâmico
                }
            }
        }

        LazyColumn {
            items(filmes) { filme ->
                umFilme(
                    filme = filme,

                    // Ação para o botão de editar
                    onEdit = { filmeParaEditar ->
                        nome = filmeParaEditar.nome
                        descricao = filmeParaEditar.desc
                        filmeEmEdicao = filmeParaEditar
                    },

                    // Ação para o botão de deletar
                    onDelete = { filmeParaDeletar ->
                        CoroutineScope(Dispatchers.IO).launch {
                            deletarFilme(filmeParaDeletar, filmesDao)
                            filmes = buscarFilmes(filmesDao)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun umFilme(filme: Filmes, onEdit: (Filmes) -> Unit, onDelete: (Filmes) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        border = BorderStroke(1.dp, Color.LightGray)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            // Coluna para nome e descrição
            Column(modifier = Modifier.weight(1f)) {
                Text(text = filme.nome, style = MaterialTheme.typography.titleMedium)
                Text(text = filme.desc, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Ícones de ação
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Editar",
                modifier = Modifier.size(24.dp).clickable {
                    onEdit(filme)
                }
            )
            Spacer(modifier = Modifier.width(16.dp))

            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Deletar",
                modifier = Modifier.size(24.dp).clickable {
                    onDelete(filme)
                }
            )
        }
    }
}
