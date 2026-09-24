package com.gmail.juliocesar64914.interfazconcompose.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.FormatUnderlined
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material.icons.filled.ViewModule
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.gmail.juliocesar64914.interfazconcompose.componentes.DemoElemento
import com.gmail.juliocesar64914.interfazconcompose.componentes.PantallaCatalogo
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Seccion2Botones() {
    PantallaCatalogo {
        item { BotonesBasicos() }
        item { BotonesConIcono() }
        item { BotonesFlotantes() }
        item { BotonesAlternancia() }
        item { BotonesSegmentados() }
        item { BotonDeshabilitado() }
        item { BotonCargando() }
    }
}

/** Texto que muestra la respuesta visible de cada botón. */
@Composable
private fun Respuesta(texto: String) {
    Text(
        texto,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colorScheme.primary
    )
}

// ---------- 1. Relleno, contorno y texto ----------
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BotonesBasicos() {
    var ultimo by rememberSaveable { mutableStateOf("Ninguno") }
    var pulsaciones by rememberSaveable { mutableIntStateOf(0) }
    fun pulsar(nombre: String) { ultimo = nombre; pulsaciones++ }

    DemoElemento(
        nombre = "Botón relleno, con contorno y de texto",
        descripcion = "Indican distinta importancia: el relleno es la acción principal, el de " +
                "contorno una secundaria y el de texto la de menor énfasis. El tonal y el elevado " +
                "son variantes intermedias."
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = { pulsar("Relleno") }) { Text("Relleno") }
            OutlinedButton(onClick = { pulsar("Con contorno") }) { Text("Con contorno") }
            TextButton(onClick = { pulsar("De texto") }) { Text("De texto") }
            FilledTonalButton(onClick = { pulsar("Tonal") }) { Text("Tonal") }
            ElevatedButton(onClick = { pulsar("Elevado") }) { Text("Elevado") }
        }
        Respuesta("Último pulsado: $ultimo · Total: $pulsaciones")
    }
}

// ---------- 2. Botones con ícono ----------
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BotonesConIcono() {
    var mensaje by rememberSaveable { mutableStateOf("Pulsa un botón.") }

    DemoElemento(
        nombre = "Botones con ícono",
        descripcion = "Los de solo ícono ahorran espacio en acciones conocidas como compartir " +
                "o borrar. Con ícono y texto, la acción es más clara para el usuario."
    ) {
        Text("Solo ícono", style = MaterialTheme.typography.labelLarge)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IconButton(onClick = { mensaje = "Compartir (botón de ícono estándar)" }) {
                Icon(Icons.Filled.Share, contentDescription = "Compartir")
            }
            FilledIconButton(onClick = { mensaje = "Editar (botón de ícono relleno)" }) {
                Icon(Icons.Filled.Edit, contentDescription = "Editar")
            }
            OutlinedIconButton(onClick = { mensaje = "Eliminar (botón de ícono con contorno)" }) {
                Icon(Icons.Filled.Delete, contentDescription = "Eliminar")
            }
        }
        Text("Ícono y texto", style = MaterialTheme.typography.labelLarge)
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = { mensaje = "Mensaje enviado" }) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, Modifier.size(ButtonDefaults.IconSize))
                Spacer(Modifier.width(ButtonDefaults.IconSpacing))
                Text("Enviar")
            }
            OutlinedButton(onClick = { mensaje = "Descarga iniciada" }) {
                Icon(Icons.Filled.Download, contentDescription = null, Modifier.size(ButtonDefaults.IconSize))
                Spacer(Modifier.width(ButtonDefaults.IconSpacing))
                Text("Descargar")
            }
        }
        Respuesta(mensaje)
    }
}

// ---------- 3. Botones de acción flotante ----------
@Composable
private fun BotonesFlotantes() {
    var tareas by rememberSaveable { mutableIntStateOf(0) }
    var notas by rememberSaveable { mutableIntStateOf(0) }
    var extendido by rememberSaveable { mutableStateOf(true) }

    DemoElemento(
        nombre = "Botón de acción flotante (FAB)",
        descripcion = "Representa la acción más importante de una pantalla y flota sobre el " +
                "contenido. La versión extendida añade texto y puede contraerse a solo ícono."
    ) {
        // Área que simula una pantalla para colocar los FAB en las esquinas
        Box(
            Modifier
                .fillMaxWidth()
                .height(190.dp)
                .background(
                    MaterialTheme.colorScheme.surfaceVariant,
                    RoundedCornerShape(12.dp)
                )
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Tareas creadas: $tareas", style = MaterialTheme.typography.bodyLarge)
                Text("Notas creadas: $notas", style = MaterialTheme.typography.bodyLarge)
                TextButton(onClick = { extendido = !extendido }) {
                    Text(if (extendido) "Contraer FAB extendido" else "Expandir FAB extendido")
                }
            }
            ExtendedFloatingActionButton(
                text = { Text("Nueva nota") },
                icon = { Icon(Icons.Filled.Edit, contentDescription = null) },
                expanded = extendido,
                onClick = { notas++ },
                modifier = Modifier.align(Alignment.BottomStart)
            )
            FloatingActionButton(
                onClick = { tareas++ },
                modifier = Modifier.align(Alignment.BottomEnd)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Nueva tarea")
            }
        }
    }
}

// ---------- 4. Botones de alternancia ----------
@Composable
private fun BotonesAlternancia() {
    var favorito by rememberSaveable { mutableStateOf(false) }
    var guardado by rememberSaveable { mutableStateOf(false) }

    DemoElemento(
        nombre = "Botón de alternancia (toggle)",
        descripcion = "Cambia entre dos estados, activado y desactivado, con cada pulsación. " +
                "Se usa para marcar favoritos, guardar elementos o activar una opción."
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconToggleButton(checked = favorito, onCheckedChange = { favorito = it }) {
                Icon(
                    if (favorito) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = "Favorito",
                    tint = if (favorito) MaterialTheme.colorScheme.error else LocalContentColor.current
                )
            }
            IconToggleButton(checked = guardado, onCheckedChange = { guardado = it }) {
                Icon(
                    if (guardado) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                    contentDescription = "Guardar"
                )
            }
        }
        Respuesta(
            "Favorito: ${if (favorito) "sí" else "no"} · Guardado: ${if (guardado) "sí" else "no"}"
        )
    }
}

// ---------- 5. Selector segmentado ----------
private data class OpcionVista(val nombre: String, val icono: ImageVector)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BotonesSegmentados() {
    val vistas = listOf(
        OpcionVista("Lista", Icons.Filled.ViewAgenda),
        OpcionVista("Cuadrícula", Icons.Filled.GridView),
        OpcionVista("Mosaico", Icons.Filled.ViewModule)
    )
    var vistaElegida by rememberSaveable { mutableIntStateOf(0) }

    var negrita by rememberSaveable { mutableStateOf(false) }
    var cursiva by rememberSaveable { mutableStateOf(false) }
    var subrayado by rememberSaveable { mutableStateOf(false) }

    DemoElemento(
        nombre = "Selector segmentado",
        descripcion = "Agrupa opciones relacionadas en una sola barra. Puede permitir elegir " +
                "solo una opción, como el modo de vista, o varias a la vez, como el formato de texto."
    ) {
        Text("Una sola opción", style = MaterialTheme.typography.labelLarge)
        SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
            vistas.forEachIndexed { indice, opcion ->
                SegmentedButton(
                    selected = vistaElegida == indice,
                    onClick = { vistaElegida = indice },
                    shape = SegmentedButtonDefaults.itemShape(indice, vistas.size),
                    icon = { Icon(opcion.icono, contentDescription = null, Modifier.size(18.dp)) },
                    label = { Text(opcion.nombre) }
                )
            }
        }
        Respuesta("Vista seleccionada: ${vistas[vistaElegida].nombre}")

        Spacer(Modifier.height(8.dp))
        Text("Varias opciones", style = MaterialTheme.typography.labelLarge)
        val formatos = listOf(
            Triple(Icons.Filled.FormatBold, "Negrita", negrita),
            Triple(Icons.Filled.FormatItalic, "Cursiva", cursiva),
            Triple(Icons.Filled.FormatUnderlined, "Subrayado", subrayado)
        )
        MultiChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
            formatos.forEachIndexed { indice, (icono, nombre, activo) ->
                SegmentedButton(
                    checked = activo,
                    onCheckedChange = {
                        when (indice) {
                            0 -> negrita = it
                            1 -> cursiva = it
                            else -> subrayado = it
                        }
                    },
                    shape = SegmentedButtonDefaults.itemShape(indice, formatos.size),
                    icon = {},
                    label = { Icon(icono, contentDescription = nombre) }
                )
            }
        }
        Text(
            "Este texto cambia con el formato elegido.",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = if (negrita) FontWeight.Bold else FontWeight.Normal,
            fontStyle = if (cursiva) FontStyle.Italic else FontStyle.Normal,
            textDecoration = if (subrayado) TextDecoration.Underline else TextDecoration.None
        )
    }
}

// ---------- 6. Botón deshabilitado ----------
@Composable
private fun BotonDeshabilitado() {
    var acepta by rememberSaveable { mutableStateOf(false) }
    var registrado by rememberSaveable { mutableStateOf(false) }

    DemoElemento(
        nombre = "Botón deshabilitado",
        descripcion = "Se muestra atenuado y no responde mientras falte un requisito. Aquí el " +
                "botón se habilita solo cuando aceptas los términos."
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = acepta, onCheckedChange = { acepta = it; registrado = false })
            Text("Acepto los términos y condiciones")
        }
        Button(onClick = { registrado = true }, enabled = acepta) {
            Text("Crear cuenta")
        }
        Respuesta(
            when {
                registrado -> "Cuenta creada correctamente."
                acepta -> "El botón ya está habilitado."
                else -> "El botón está deshabilitado: marca la casilla."
            }
        )
    }
}

// ---------- 7. Botón en estado de carga ----------
@Composable
private fun BotonCargando() {
    var cargando by remember { mutableStateOf(false) }
    var envios by rememberSaveable { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()

    DemoElemento(
        nombre = "Botón en estado de carga",
        descripcion = "Mientras una operación está en curso, el botón muestra un indicador de " +
                "progreso y se bloquea para evitar que se pulse dos veces."
    ) {
        Button(
            onClick = {
                scope.launch {
                    cargando = true
                    delay(2000) // simula una operación de red
                    cargando = false
                    envios++
                }
            },
            enabled = !cargando
        ) {
            if (cargando) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    strokeWidth = 2.dp,
                    color = LocalContentColor.current
                )
                Spacer(Modifier.width(ButtonDefaults.IconSpacing))
                Text("Enviando…")
            } else {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, Modifier.size(ButtonDefaults.IconSize))
                Spacer(Modifier.width(ButtonDefaults.IconSpacing))
                Text("Enviar formulario")
            }
        }
        Respuesta(
            when {
                cargando -> "Enviando datos, espera…"
                envios == 0 -> "Pulsa el botón para simular un envío."
                else -> "Formulario enviado ($envios ${if (envios == 1) "vez" else "veces"})."
            }
        )
    }
}