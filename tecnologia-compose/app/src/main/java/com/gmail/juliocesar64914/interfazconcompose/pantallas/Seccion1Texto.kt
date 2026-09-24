package com.gmail.juliocesar64914.interfazconcompose.pantallas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.gmail.juliocesar64914.interfazconcompose.componentes.DemoElemento
import com.gmail.juliocesar64914.interfazconcompose.componentes.PantallaCatalogo
import com.gmail.juliocesar64914.interfazconcompose.datos.CatalogoViewModel
import java.text.Normalizer

@Composable
fun Seccion1Texto(viewModel: CatalogoViewModel) {
    PantallaCatalogo {
        item { CampoSimple() }
        item { CampoConValidacion() }
        item { CampoContrasena() }
        item { CamposTiposTeclado() }
        item { CampoMultilinea() }
        item { CampoConSugerencias() }
        item { BarraDeBusqueda() }
        item { AgregarALaLista(viewModel) }
    }
}

// ---------- 1. Campo simple ----------
@Composable
private fun CampoSimple() {
    var texto by rememberSaveable { mutableStateOf("") }

    DemoElemento(
        nombre = "Campo de texto simple",
        descripcion = "Permite escribir una línea de texto. La etiqueta indica qué dato se " +
                "espera y el texto de ayuda muestra un ejemplo mientras el campo está vacío."
    ) {
        OutlinedTextField(
            value = texto,
            onValueChange = { texto = it },
            label = { Text("Nombre") },
            placeholder = { Text("Ej. Julio César") },
            leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            if (texto.isBlank()) "Aún no has escrito nada." else "Hola, ${texto.trim()}.",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

// ---------- 2. Campo con validación ----------
@Composable
private fun CampoConValidacion() {
    var usuario by rememberSaveable { mutableStateOf("") }

    val error = when {
        usuario.isEmpty() -> null
        usuario.length < 4 -> "Debe tener al menos 4 caracteres."
        !usuario.all { it.isLetterOrDigit() || it == '_' } -> "Solo se permiten letras, números y guion bajo."
        else -> null
    }
    val valido = usuario.isNotEmpty() && error == null

    DemoElemento(
        nombre = "Campo con validación",
        descripcion = "Revisa el dato mientras se escribe. Si no cumple las reglas, el campo " +
                "cambia a color de error y muestra un mensaje que explica cómo corregirlo."
    ) {
        OutlinedTextField(
            value = usuario,
            onValueChange = { usuario = it },
            label = { Text("Nombre de usuario") },
            singleLine = true,
            isError = error != null,
            supportingText = {
                Text(
                    when {
                        error != null -> error
                        valido -> "Usuario válido."
                        else -> "Mínimo 4 caracteres: letras, números o guion bajo."
                    }
                )
            },
            trailingIcon = {
                when {
                    error != null -> Icon(Icons.Filled.Error, contentDescription = "Error")
                    valido -> Icon(
                        Icons.Filled.CheckCircle,
                        contentDescription = "Válido",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// ---------- 3. Contraseña ----------
@Composable
private fun CampoContrasena() {
    var contrasena by rememberSaveable { mutableStateOf("") }
    var visible by rememberSaveable { mutableStateOf(false) }

    // Cálculo sencillo de la seguridad de la contraseña (0 a 4 puntos)
    val puntos = listOf(
        contrasena.length >= 8,
        contrasena.any { it.isUpperCase() },
        contrasena.any { it.isDigit() },
        contrasena.any { !it.isLetterOrDigit() }
    ).count { it }
    val nivel = when {
        contrasena.isEmpty() -> "Escribe una contraseña."
        puntos <= 1 -> "Seguridad: débil"
        puntos <= 3 -> "Seguridad: media"
        else -> "Seguridad: fuerte"
    }

    DemoElemento(
        nombre = "Campo de contraseña",
        descripcion = "Oculta los caracteres para proteger datos sensibles. El botón del ojo " +
                "permite mostrar u ocultar el contenido para revisar lo escrito."
    ) {
        OutlinedTextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            label = { Text("Contraseña") },
            singleLine = true,
            visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(onClick = { visible = !visible }) {
                    Icon(
                        if (visible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                        contentDescription = if (visible) "Ocultar contraseña" else "Mostrar contraseña"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        LinearProgressIndicator(
            progress = { puntos / 4f },
            modifier = Modifier.fillMaxWidth()
        )
        Text(nivel, style = MaterialTheme.typography.bodySmall)
    }
}

// ---------- 4. Tipos de teclado ----------
@Composable
private fun CamposTiposTeclado() {
    var edad by rememberSaveable { mutableStateOf("") }
    var correo by rememberSaveable { mutableStateOf("") }
    var telefono by rememberSaveable { mutableStateOf("") }

    DemoElemento(
        nombre = "Tipos de teclado",
        descripcion = "Cada campo abre el teclado más adecuado para su dato: solo números, " +
                "uno con @ para correos o uno de marcación telefónica. Toca cada campo para comparar."
    ) {
        OutlinedTextField(
            value = edad,
            onValueChange = { nuevo -> if (nuevo.length <= 3 && nuevo.all { it.isDigit() }) edad = nuevo },
            label = { Text("Edad (numérico)") },
            leadingIcon = { Icon(Icons.Filled.Pin, contentDescription = null) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it.trim() },
            label = { Text("Correo electrónico") },
            leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = telefono,
            onValueChange = { nuevo -> if (nuevo.length <= 10 && nuevo.all { it.isDigit() }) telefono = nuevo },
            label = { Text("Teléfono (10 dígitos)") },
            leadingIcon = { Icon(Icons.Filled.Phone, contentDescription = null) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Done),
            modifier = Modifier.fillMaxWidth()
        )
        val correoValido = android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()
        Text(
            "Edad: ${edad.ifEmpty { "—" }} · Correo: ${
                if (correo.isEmpty()) "—" else if (correoValido) "válido" else "incompleto"
            } · Teléfono: ${telefono.length}/10",
            style = MaterialTheme.typography.bodySmall
        )
    }
}

// ---------- 5. Multilínea ----------
@Composable
private fun CampoMultilinea() {
    val maximo = 200
    var comentario by rememberSaveable { mutableStateOf("") }

    DemoElemento(
        nombre = "Campo multilínea",
        descripcion = "Sirve para textos largos como comentarios o descripciones. Crece al " +
                "escribir hasta un límite de líneas y aquí incluye un contador de caracteres."
    ) {
        OutlinedTextField(
            value = comentario,
            onValueChange = { if (it.length <= maximo) comentario = it },
            label = { Text("Comentarios") },
            placeholder = { Text("Escribe tu opinión sobre la app…") },
            minLines = 3,
            maxLines = 6,
            supportingText = {
                Text(
                    "${comentario.length} / $maximo",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// ---------- 6. Sugerencias automáticas ----------
@Composable
private fun CampoConSugerencias() {
    val lenguajes = listOf(
        "Kotlin", "Java", "Dart", "Swift", "JavaScript", "TypeScript",
        "Python", "C#", "C++", "Go", "Rust", "Ruby", "PHP"
    )
    var texto by rememberSaveable { mutableStateOf("") }
    var expandido by rememberSaveable { mutableStateOf(false) }

    val sugerencias = lenguajes.filter {
        it.contains(texto, ignoreCase = true) && !it.equals(texto, ignoreCase = true)
    }
    val elegido = lenguajes.firstOrNull { it.equals(texto, ignoreCase = true) }

    DemoElemento(
        nombre = "Campo con sugerencias",
        descripcion = "Muestra opciones que coinciden con lo que se escribe, para completar " +
                "más rápido y evitar errores. La flecha despliega la lista completa."
    ) {
        Box(Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = texto,
                onValueChange = { texto = it; expandido = true },
                label = { Text("Lenguaje de programación favorito") },
                singleLine = true,
                trailingIcon = {
                    IconButton(onClick = { expandido = true }) {
                        Icon(Icons.Filled.ArrowDropDown, contentDescription = "Mostrar opciones")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            DropdownMenu(
                expanded = expandido && sugerencias.isNotEmpty(),
                onDismissRequest = { expandido = false },
                // focusable = false permite seguir escribiendo con el menú abierto
                properties = PopupProperties(focusable = false)
            ) {
                sugerencias.forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(opcion) },
                        onClick = { texto = opcion; expandido = false }
                    )
                }
            }
        }
        Text(
            if (elegido != null) "Elegiste: $elegido" else "Escribe una letra o toca la flecha.",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

// ---------- 7. Barra de búsqueda ----------
private val ESTADOS = listOf(
    "Aguascalientes", "Baja California", "Baja California Sur", "Campeche", "Chiapas",
    "Chihuahua", "Ciudad de México", "Coahuila", "Colima", "Durango", "Estado de México",
    "Guanajuato", "Guerrero", "Hidalgo", "Jalisco", "Michoacán", "Morelos", "Nayarit",
    "Nuevo León", "Oaxaca", "Puebla", "Querétaro", "Quintana Roo", "San Luis Potosí",
    "Sinaloa", "Sonora", "Tabasco", "Tamaulipas", "Tlaxcala", "Veracruz", "Yucatán", "Zacatecas"
)

/** Quita acentos para que "queretaro" encuentre "Querétaro". */
private fun String.sinAcentos(): String =
    Normalizer.normalize(this, Normalizer.Form.NFD).replace(Regex("\\p{Mn}+"), "")

@Composable
private fun BarraDeBusqueda() {
    var consulta by rememberSaveable { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    val resultados = if (consulta.isBlank()) emptyList() else ESTADOS.filter {
        it.sinAcentos().contains(consulta.trim().sinAcentos(), ignoreCase = true)
    }

    DemoElemento(
        nombre = "Barra de búsqueda",
        descripcion = "Filtra un conjunto de datos conforme se escribe. Tiene ícono de lupa, " +
                "botón para borrar y la tecla de búsqueda del teclado cierra el teclado."
    ) {
        OutlinedTextField(
            value = consulta,
            onValueChange = { consulta = it },
            placeholder = { Text("Buscar un estado de México") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
            trailingIcon = {
                if (consulta.isNotEmpty()) {
                    IconButton(onClick = { consulta = "" }) {
                        Icon(Icons.Filled.Clear, contentDescription = "Borrar búsqueda")
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(50),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
            modifier = Modifier.fillMaxWidth()
        )
        when {
            consulta.isBlank() -> Text(
                "Escribe para buscar entre los 32 estados.",
                style = MaterialTheme.typography.bodySmall
            )
            resultados.isEmpty() -> Text(
                "No hay estados que coincidan con «${consulta.trim()}».",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
            else -> Column {
                resultados.take(5).forEach { estado ->
                    ListItem(
                        headlineContent = { Text(estado) },
                        leadingContent = { Icon(Icons.Filled.Place, contentDescription = null) }
                    )
                }
                if (resultados.size > 5) {
                    Text(
                        "y ${resultados.size - 5} resultados más…",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

// ---------- 8. Conexión con la Sección 4 ----------
@Composable
private fun AgregarALaLista(viewModel: CatalogoViewModel) {
    var nuevo by rememberSaveable { mutableStateOf("") }
    var mensaje by rememberSaveable { mutableStateOf<String?>(null) }
    val focusManager = LocalFocusManager.current

    val agregar = {
        if (nuevo.isNotBlank()) {
            viewModel.agregarElemento(nuevo)
            mensaje = "«${nuevo.trim()}» se agregó. La lista de la Sección 4 " +
                    "ahora tiene ${viewModel.elementos.size} elementos."
            nuevo = ""
            focusManager.clearFocus()
        }
    }

    DemoElemento(
        nombre = "Conexión con Listas y colecciones",
        descripcion = "Lo que escribas aquí se agrega al inicio de la lista de la Sección 4. " +
                "Ambas secciones comparten los datos a través de un ViewModel."
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = nuevo,
                onValueChange = { nuevo = it },
                label = { Text("Nuevo elemento") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { agregar() }),
                modifier = Modifier.weight(1f)
            )
            FilledIconButton(onClick = { agregar() }, enabled = nuevo.isNotBlank()) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar a la lista")
            }
        }
        mensaje?.let {
            Text(
                it,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}