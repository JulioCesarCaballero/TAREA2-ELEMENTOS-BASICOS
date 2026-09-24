package com.gmail.juliocesar64914.interfazconcompose.pantallas

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gmail.juliocesar64914.interfazconcompose.datos.CATEGORIA_USUARIO
import com.gmail.juliocesar64914.interfazconcompose.datos.CatalogoViewModel
import com.gmail.juliocesar64914.interfazconcompose.datos.ElementoLista
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private data class Pestana(val titulo: String, val icono: ImageVector)

private val PESTANAS = listOf(
    Pestana("Lista", Icons.AutoMirrored.Filled.ViewList),
    Pestana("Cuadrícula", Icons.Filled.GridView),
    Pestana("Encabezados", Icons.Filled.ViewAgenda)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Seccion4Listas(viewModel: CatalogoViewModel) {
    val pagerState = rememberPagerState(pageCount = { PESTANAS.size })
    val scope = rememberCoroutineScope()
    var detalle by remember { mutableStateOf<ElementoLista?>(null) }

    Column(Modifier.fillMaxSize()) {
        // Documentación de las pestañas
        Guia(
            nombre = "Pestañas con contenido deslizable",
            texto = "Organizan vistas relacionadas en páginas. Toca una pestaña o desliza " +
                    "a la izquierda o a la derecha para cambiar de página.",
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        PrimaryTabRow(selectedTabIndex = pagerState.currentPage) {
            PESTANAS.forEachIndexed { indice, pestana ->
                Tab(
                    selected = pagerState.currentPage == indice,
                    onClick = { scope.launch { pagerState.animateScrollToPage(indice) } },
                    text = { Text(pestana.titulo) },
                    icon = { Icon(pestana.icono, contentDescription = null) }
                )
            }
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { pagina ->
            when (pagina) {
                0 -> PaginaLista(viewModel, onVerDetalle = { detalle = it })
                1 -> PaginaCuadricula(viewModel, onVerDetalle = { detalle = it })
                else -> PaginaEncabezados(viewModel, onVerDetalle = { detalle = it })
            }
        }
    }

    // Detalle del elemento seleccionado
    detalle?.let { elemento ->
        AlertDialog(
            onDismissRequest = { detalle = null },
            icon = { Avatar(elemento, 56.dp) },
            title = { Text(elemento.titulo) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Categoría: ${elemento.categoria}")
                    Text("Descripción: ${elemento.subtitulo}")
                    Text("Identificador: #${elemento.id}")
                }
            },
            confirmButton = {
                TextButton(onClick = { detalle = null }) { Text("Cerrar") }
            },
            dismissButton = {
                TextButton(onClick = {
                    viewModel.eliminarElemento(elemento)
                    detalle = null
                }) { Text("Eliminar") }
            }
        )
    }
}

// ================= Pestaña 1: lista vertical =================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PaginaLista(viewModel: CatalogoViewModel, onVerDetalle: (ElementoLista) -> Unit) {
    var actualizando by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    PullToRefreshBox(
        isRefreshing = actualizando,
        onRefresh = {
            scope.launch {
                actualizando = true
                delay(1500) // simula una descarga
                viewModel.reiniciarLista()
                actualizando = false
            }
        },
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item {
                ElevatedCard(Modifier.fillMaxWidth().padding(16.dp)) {
                    Column(
                        Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Guia(
                            "Lista vertical",
                            "Muestra elementos uno debajo de otro y solo dibuja los visibles, " +
                                    "por lo que funciona bien con listas largas."
                        )
                        Guia(
                            "Ver detalle",
                            "Toca un elemento para abrir su información completa."
                        )
                        Guia(
                            "Deslizar para eliminar",
                            "Arrastra un elemento hacia un lado para borrarlo de la lista."
                        )
                        Guia(
                            "Actualizar arrastrando hacia abajo",
                            "Desde el inicio de la lista, jala hacia abajo para recargar los datos."
                        )
                        Guia(
                            "Estado vacío",
                            "Cuando no hay elementos se muestra un mensaje con ilustración. " +
                                    "Pruébalo con el botón de vaciar."
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(
                                onClick = { viewModel.vaciarLista() },
                                enabled = viewModel.elementos.isNotEmpty()
                            ) {
                                Icon(Icons.Filled.DeleteSweep, contentDescription = null)
                                Spacer(Modifier.size(8.dp))
                                Text("Vaciar lista")
                            }
                        }
                        Text(
                            "${viewModel.elementos.size} elementos",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            if (viewModel.elementos.isEmpty()) {
                item { EstadoVacio(onRestaurar = { viewModel.reiniciarLista() }) }
            } else {
                items(viewModel.elementos, key = { it.id }) { elemento ->
                    FilaDeslizable(
                        elemento = elemento,
                        onClick = { onVerDetalle(elemento) },
                        onEliminar = { viewModel.eliminarElemento(elemento) }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
private fun FilaDeslizable(
    elemento: ElementoLista,
    onClick: () -> Unit,
    onEliminar: () -> Unit
) {
    val estado = rememberSwipeToDismissBoxState()

    // Cuando el elemento termina de deslizarse, se elimina
    LaunchedEffect(estado.currentValue) {
        if (estado.currentValue != SwipeToDismissBoxValue.Settled) onEliminar()
    }

    SwipeToDismissBox(
        state = estado,
        backgroundContent = {
            val alineacion =
                if (estado.dismissDirection == SwipeToDismissBoxValue.StartToEnd) Alignment.CenterStart
                else Alignment.CenterEnd
            Box(
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.errorContainer)
                    .padding(horizontal = 24.dp),
                contentAlignment = alineacion
            ) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "Eliminar",
                    tint = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    ) {
        ListItem(
            headlineContent = { Text(elemento.titulo) },
            supportingContent = { Text(elemento.subtitulo) },
            leadingContent = { Avatar(elemento, 40.dp) },
            modifier = Modifier.clickable(onClick = onClick)
        )
    }
}

// ================= Pestaña 2: cuadrícula =================
@Composable
private fun PaginaCuadricula(viewModel: CatalogoViewModel, onVerDetalle: (ElementoLista) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 104.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Guia(
                "Cuadrícula de elementos",
                "Acomoda los elementos en filas y columnas. El número de columnas se " +
                        "ajusta al ancho de la pantalla. Toca una tarjeta para ver su detalle."
            )
        }
        if (viewModel.elementos.isEmpty()) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                EstadoVacio(onRestaurar = { viewModel.reiniciarLista() })
            }
        } else {
            items(viewModel.elementos, key = { it.id }) { elemento ->
                Card(onClick = { onVerDetalle(elemento) }) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Avatar(elemento, 48.dp)
                        Text(
                            elemento.titulo,
                            style = MaterialTheme.typography.titleSmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            elemento.categoria,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

// ================= Pestaña 3: lista con encabezados =================
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PaginaEncabezados(viewModel: CatalogoViewModel, onVerDetalle: (ElementoLista) -> Unit) {
    val grupos = viewModel.elementos.groupBy { it.categoria }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item {
            Guia(
                "Lista con encabezados de sección",
                "Combina dos tipos de elemento: encabezados que agrupan por categoría y " +
                        "filas de contenido. El encabezado se queda fijo arriba mientras recorres su grupo.",
                modifier = Modifier.padding(16.dp)
            )
        }
        if (grupos.isEmpty()) {
            item { EstadoVacio(onRestaurar = { viewModel.reiniciarLista() }) }
        }
        grupos.forEach { (categoria, lista) ->
            stickyHeader(key = "encabezado-$categoria", contentType = "encabezado") {
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "$categoria (${lista.size})",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                    )
                }
            }
            items(lista, key = { it.id }, contentType = { "fila" }) { elemento ->
                ListItem(
                    headlineContent = { Text(elemento.titulo) },
                    supportingContent = { Text(elemento.subtitulo) },
                    leadingContent = { Avatar(elemento, 40.dp) },
                    modifier = Modifier.clickable { onVerDetalle(elemento) }
                )
            }
        }
    }
}

// ================= Componentes compartidos =================

/** Nombre y explicación breve de un elemento, en formato compacto. */
@Composable
private fun Guia(nombre: String, texto: String, modifier: Modifier = Modifier) {
    Column(modifier) {
        Text(
            nombre,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            texto,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/** Círculo con la inicial del elemento; el color depende de la categoría. */
@Composable
private fun Avatar(elemento: ElementoLista, tamano: Dp) {
    val (fondo: Color, texto: Color) = when (elemento.categoria) {
        "Frutas" -> MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.onPrimaryContainer
        "Verduras" -> MaterialTheme.colorScheme.tertiaryContainer to MaterialTheme.colorScheme.onTertiaryContainer
        CATEGORIA_USUARIO -> MaterialTheme.colorScheme.secondaryContainer to MaterialTheme.colorScheme.onSecondaryContainer
        else -> MaterialTheme.colorScheme.surfaceVariant to MaterialTheme.colorScheme.onSurfaceVariant
    }
    Surface(shape = CircleShape, color = fondo, modifier = Modifier.size(tamano)) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                elemento.titulo.take(1).uppercase(),
                color = texto,
                style = if (tamano >= 48.dp) MaterialTheme.typography.titleLarge
                else MaterialTheme.typography.titleMedium
            )
        }
    }
}

/** Estado vacío con ilustración, mensaje y acción para recuperar los datos. */
@Composable
private fun EstadoVacio(onRestaurar: () -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.size(120.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    Icons.Filled.Inventory2,
                    contentDescription = null,
                    modifier = Modifier.size(56.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Text("La lista está vacía", style = MaterialTheme.typography.titleMedium)
        Text(
            "No hay elementos para mostrar. Restaura la lista original o agrega uno " +
                    "nuevo desde la sección Entrada de texto.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(4.dp))
        Button(onClick = onRestaurar) {
            Icon(Icons.Filled.Refresh, contentDescription = null)
            Spacer(Modifier.size(8.dp))
            Text("Restaurar lista")
        }
    }
}