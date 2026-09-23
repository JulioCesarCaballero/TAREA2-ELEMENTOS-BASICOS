package com.gmail.juliocesar64914.interfazconcompose.pantallas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gmail.juliocesar64914.interfazconcompose.componentes.PantallaCatalogo
import com.gmail.juliocesar64914.interfazconcompose.datos.CatalogoViewModel
import com.gmail.juliocesar64914.interfazconcompose.navegacion.Seccion

@Composable
fun PantallaInicio(
    viewModel: CatalogoViewModel,
    onAbrirSeccion: (Seccion) -> Unit
) {
    PantallaCatalogo {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Catálogo de elementos de interfaz", style = MaterialTheme.typography.headlineSmall)
                Text(
                    "Explora cada categoría. Cada elemento incluye su nombre, para qué sirve " +
                            "y una demostración con la que puedes interactuar.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    "La lista de la Sección 4 tiene ${viewModel.elementos.size} elementos.",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        items(Seccion.entries) { seccion ->
            Card(
                onClick = { onAbrirSeccion(seccion) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                seccion.icono,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                    Column(Modifier.weight(1f)) {
                        Text(seccion.titulo, style = MaterialTheme.typography.titleMedium)
                        Text(
                            seccion.descripcion,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}