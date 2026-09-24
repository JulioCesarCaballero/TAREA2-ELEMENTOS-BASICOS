package com.gmail.juliocesar64914.interfazconcompose.pantallas

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.selection.triStateToggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gmail.juliocesar64914.interfazconcompose.componentes.DemoElemento
import com.gmail.juliocesar64914.interfazconcompose.componentes.PantallaCatalogo
import com.gmail.juliocesar64914.interfazconcompose.datos.CatalogoViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.roundToInt

@Composable
fun Seccion3Seleccion(viewModel: CatalogoViewModel) {
    PantallaCatalogo {
        item { CasillasVerificacion() }
        item { BotonesDeOpcion() }
        item { Interruptores() }
        item { DeslizadorUnico(viewModel) }
        item { DeslizadorRango() }
        item { ListaDesplegable() }
        item { SelectorFecha() }
        item { SelectorHora() }
        item { ChipsDeFiltro() }
    }
}

@Composable
private fun Respuesta(texto: String) {
    Text(
        texto,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colorScheme.primary
    )
}

// ---------- 1. Casillas de verificación (con estado indeterminado) ----------
@Composable
private fun CasillasVerificacion() {
    val productos = listOf("Leche", "Pan", "Huevos")
    val marcados = remember { mutableStateListOf(true, false, false) }

    val estadoPadre = when {
        marcados.all { it } -> ToggleableState.On
        marcados.none { it } -> ToggleableState.Off
        else -> ToggleableState.Indeterminate
    }

    DemoElemento(
        nombre = "Casilla de verificación",
        descripcion = "Permite marcar o desmarcar opciones independientes. La casilla " +
                "principal queda indeterminada (con una raya) cuando solo algunas están marcadas."
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .triStateToggleable(
                    state = estadoPadre,
                    role = Role.Checkbox,
                    onClick = {
                        val nuevoValor = estadoPadre != ToggleableState.On
                        for (i in marcados.indices) marcados[i] = nuevoValor
                    }
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TriStateCheckbox(state = estadoPadre, onClick = null)
            Text("Seleccionar todo", Modifier.padding(start = 12.dp), fontWeight = FontWeight.Medium)
        }
        productos.forEachIndexed { i, nombre ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 32.dp)
                    .toggleable(
                        value = marcados[i],
                        role = Role.Checkbox,
                        onValueChange = { marcados[i] = it }
                    )
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked = marcados[i], onCheckedChange = null)
                Text(nombre, Modifier.padding(start = 12.dp))
            }
        }
        val estadoTexto = when (estadoPadre) {
            ToggleableState.On -> "todas marcadas"
            ToggleableState.Off -> "ninguna marcada"
            ToggleableState.Indeterminate -> "indeterminado"
        }
        Respuesta("Marcados: ${marcados.count { it }} de ${productos.size} · Estado: $estadoTexto")
    }
}

// ---------- 2. Botones de opción ----------
@Composable
private fun BotonesDeOpcion() {
    val envios = listOf("Estándar (5 a 7 días)", "Exprés (2 a 3 días)", "Recoger en tienda")
    var elegido by rememberSaveable { mutableIntStateOf(0) }

    DemoElemento(
        nombre = "Botones de opción (radio)",
        descripcion = "Presentan opciones mutuamente excluyentes: al elegir una, las demás " +
                "se desmarcan. Se usan cuando todas las alternativas deben estar a la vista."
    ) {
        Column(Modifier.selectableGroup()) {
            envios.forEachIndexed { i, texto ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = elegido == i,
                            onClick = { elegido = i },
                            role = Role.RadioButton
                        )
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = elegido == i, onClick = null)
                    Text(texto, Modifier.padding(start = 12.dp))
                }
            }
        }
        Respuesta("Tipo de envío: ${envios[elegido]}")
    }
}

// ---------- 3. Interruptores ----------
@Composable
private fun Interruptores() {
    var notificaciones by rememberSaveable { mutableStateOf(true) }
    var modoAvion by rememberSaveable { mutableStateOf(false) }

    DemoElemento(
        nombre = "Interruptor (switch)",
        descripcion = "Activa o desactiva una configuración de forma inmediata, sin necesidad " +
                "de pulsar un botón de guardar. Es ideal para ajustes de encendido y apagado."
    ) {
        FilaInterruptor("Notificaciones", notificaciones) { notificaciones = it }
        HorizontalDivider()
        FilaInterruptor("Modo avión", modoAvion) { modoAvion = it }
        Respuesta(
            buildString {
                append(if (notificaciones) "Recibirás notificaciones. " else "Notificaciones silenciadas. ")
                append(if (modoAvion) "Sin conexión." else "Conectado.")
            }
        )
    }
}

@Composable
private fun FilaInterruptor(texto: String, activo: Boolean, alCambiar: (Boolean) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .toggleable(value = activo, role = Role.Switch, onValueChange = alCambiar)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(texto, style = MaterialTheme.typography.bodyLarge)
        Switch(
            checked = activo,
            onCheckedChange = null,
            thumbContent = {
                Icon(
                    if (activo) Icons.Filled.Check else Icons.Filled.Close,
                    contentDescription = null,
                    modifier = Modifier.size(SwitchDefaults.IconSize)
                )
            }
        )
    }
}

// ---------- 4. Deslizador de valor único (conectado con la Sección 5) ----------
@Composable
private fun DeslizadorUnico(viewModel: CatalogoViewModel) {
    val escala = viewModel.escalaTexto

    DemoElemento(
        nombre = "Deslizador de valor único",
        descripcion = "Elige un valor dentro de un rango arrastrando el control. Este ajusta " +
                "el tamaño de los textos de la Sección 5, así que tu elección se comparte entre secciones."
    ) {
        Slider(
            value = escala,
            onValueChange = { viewModel.cambiarEscalaTexto(it) },
            valueRange = 0.8f..1.6f,
            steps = 7 // saltos de 10 %
        )
        Text(
            "Tamaño de texto: ${(escala * 100).roundToInt()} %",
            fontSize = (16 * escala).sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

// ---------- 5. Deslizador de rango ----------
@Composable
private fun DeslizadorRango() {
    var rango by remember { mutableStateOf(300f..1500f) }

    DemoElemento(
        nombre = "Deslizador de rango",
        descripcion = "Tiene dos controles para definir un mínimo y un máximo a la vez. Es " +
                "común en filtros de precio, edad o distancia."
    ) {
        RangeSlider(
            value = rango,
            onValueChange = { rango = it },
            valueRange = 0f..2000f,
            steps = 19 // saltos de 100
        )
        Respuesta(
            "Precio: de $${rango.start.roundToInt()} a $${rango.endInclusive.roundToInt()} MXN"
        )
    }
}

// ---------- 6. Lista desplegable ----------
@Composable
private fun ListaDesplegable() {
    val carreras = listOf(
        "Ingeniería en Sistemas Computacionales",
        "Ingeniería en Inteligencia Artificial",
        "Licenciatura en Ciencia de Datos",
        "Ingeniería Mecatrónica",
        "Ingeniería Industrial"
    )
    var seleccion by rememberSaveable { mutableStateOf<String?>(null) }
    var expandido by remember { mutableStateOf(false) }

    DemoElemento(
        nombre = "Lista desplegable de selección",
        descripcion = "Muestra la opción elegida y, al tocarla, despliega todas las " +
                "alternativas. Ahorra espacio cuando hay muchas opciones posibles."
    ) {
        Box(Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = seleccion ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Carrera") },
                placeholder = { Text("Elige una opción") },
                trailingIcon = { Icon(Icons.Filled.ArrowDropDown, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )
            // Capa transparente encima del campo para que todo el campo abra el menú
            Box(
                Modifier
                    .matchParentSize()
                    .clickable { expandido = true }
            )
            DropdownMenu(expanded = expandido, onDismissRequest = { expandido = false }) {
                carreras.forEach { carrera ->
                    DropdownMenuItem(
                        text = { Text(carrera) },
                        onClick = { seleccion = carrera; expandido = false },
                        trailingIcon = {
                            if (carrera == seleccion) Icon(Icons.Filled.Check, contentDescription = "Seleccionada")
                        }
                    )
                }
            }
        }
        Respuesta(seleccion?.let { "Seleccionaste: $it" } ?: "Aún no eliges una carrera.")
    }
}

// ---------- 7. Selector de fecha ----------
private val formatoFecha = SimpleDateFormat(
    "EEEE d 'de' MMMM 'de' yyyy", Locale.forLanguageTag("es-MX")
).apply { timeZone = TimeZone.getTimeZone("UTC") } // el DatePicker entrega la fecha en UTC

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectorFecha() {
    var fechaMillis by rememberSaveable { mutableStateOf<Long?>(null) }
    var mostrar by remember { mutableStateOf(false) }

    DemoElemento(
        nombre = "Selector de fecha",
        descripcion = "Abre un calendario para elegir un día sin escribirlo a mano, lo que " +
                "evita formatos incorrectos. También permite capturar la fecha con el teclado."
    ) {
        OutlinedButton(onClick = { mostrar = true }) {
            Icon(Icons.Filled.CalendarMonth, contentDescription = null)
            Spacer(Modifier.size(8.dp))
            Text("Elegir fecha")
        }
        Respuesta(
            fechaMillis?.let { "Fecha: ${formatoFecha.format(Date(it))}" } ?: "Sin fecha seleccionada."
        )
    }

    if (mostrar) {
        val estado = rememberDatePickerState(initialSelectedDateMillis = fechaMillis)
        DatePickerDialog(
            onDismissRequest = { mostrar = false },
            confirmButton = {
                TextButton(onClick = {
                    fechaMillis = estado.selectedDateMillis
                    mostrar = false
                }) { Text("Aceptar") }
            },
            dismissButton = {
                TextButton(onClick = { mostrar = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = estado)
        }
    }
}

// ---------- 8. Selector de hora ----------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectorHora() {
    var hora by rememberSaveable { mutableStateOf<Int?>(null) }
    var minuto by rememberSaveable { mutableIntStateOf(0) }
    var mostrar by remember { mutableStateOf(false) }

    DemoElemento(
        nombre = "Selector de hora",
        descripcion = "Muestra un reloj para elegir hora y minutos de forma visual. Es útil " +
                "para alarmas, citas o recordatorios."
    ) {
        OutlinedButton(onClick = { mostrar = true }) {
            Icon(Icons.Filled.Schedule, contentDescription = null)
            Spacer(Modifier.size(8.dp))
            Text("Elegir hora")
        }
        Respuesta(
            hora?.let { "Hora: %02d:%02d".format(it, minuto) } ?: "Sin hora seleccionada."
        )
    }

    if (mostrar) {
        val estado = rememberTimePickerState(
            initialHour = hora ?: 12,
            initialMinute = minuto,
            is24Hour = true
        )
        AlertDialog(
            onDismissRequest = { mostrar = false },
            title = { Text("Selecciona la hora") },
            text = { TimePicker(state = estado) },
            confirmButton = {
                TextButton(onClick = {
                    hora = estado.hour
                    minuto = estado.minute
                    mostrar = false
                }) { Text("Aceptar") }
            },
            dismissButton = {
                TextButton(onClick = { mostrar = false }) { Text("Cancelar") }
            }
        )
    }
}

// ---------- 9. Chips de filtro ----------
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ChipsDeFiltro() {
    val productos = mapOf(
        "Frutas" to listOf("Manzana", "Mango", "Fresa"),
        "Verduras" to listOf("Zanahoria", "Nopal", "Brócoli"),
        "Lácteos" to listOf("Leche", "Queso", "Yogur"),
        "Cereales" to listOf("Avena", "Arroz", "Maíz")
    )
    val activos = remember { mutableStateListOf("Frutas") }

    DemoElemento(
        nombre = "Chips de filtro",
        descripcion = "Etiquetas compactas que se activan o desactivan para filtrar " +
                "contenido. Se pueden combinar varias a la vez."
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            productos.keys.forEach { categoria ->
                val seleccionado = categoria in activos
                FilterChip(
                    selected = seleccionado,
                    onClick = { if (seleccionado) activos.remove(categoria) else activos.add(categoria) },
                    label = { Text(categoria) },
                    leadingIcon = if (seleccionado) {
                        { Icon(Icons.Filled.Check, contentDescription = null, Modifier.size(FilterChipDefaults.IconSize)) }
                    } else null
                )
            }
        }
        Spacer(Modifier.height(4.dp))
        val visibles = productos.filterKeys { it in activos }.values.flatten()
        Respuesta(
            if (visibles.isEmpty()) "Activa al menos un filtro para ver productos."
            else "Productos: ${visibles.joinToString(", ")}"
        )
    }
}

