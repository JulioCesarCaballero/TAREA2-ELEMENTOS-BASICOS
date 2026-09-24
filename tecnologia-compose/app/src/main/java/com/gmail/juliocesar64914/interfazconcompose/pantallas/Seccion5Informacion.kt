package com.gmail.juliocesar64914.interfazconcompose.pantallas

import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.gmail.juliocesar64914.interfazconcompose.R
import com.gmail.juliocesar64914.interfazconcompose.componentes.DemoElemento
import com.gmail.juliocesar64914.interfazconcompose.componentes.PantallaCatalogo
import com.gmail.juliocesar64914.interfazconcompose.datos.CatalogoViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun Seccion5Informacion(viewModel: CatalogoViewModel) {
    val snackbarHostState = remember { SnackbarHostState() }
    val densidad = LocalDensity.current

    // Conexión con la Sección 3: todos los textos (sp) de esta sección se
    // multiplican por la escala elegida en el deslizador.
    CompositionLocalProvider(
        LocalDensity provides Density(densidad.density, densidad.fontScale * viewModel.escalaTexto)
    ) {
        Box(Modifier.fillMaxSize()) {
            PantallaCatalogo {
                item { AvisoEscala(viewModel) }
                item { EstilosDeTexto() }
                item { Imagenes() }
                item { IndicadoresProgreso() }
                item { ToastYSnackbar(snackbarHostState) }
                item { DialogoConfirmacion(viewModel) }
                item { HojaInferior() }
                item { TarjetasSeparadoresBadges() }
                item { Spacer(Modifier.height(64.dp)) } // espacio para el snackbar
            }
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
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

// ---------- Aviso de conexión con la Sección 3 ----------
@Composable
private fun AvisoEscala(viewModel: CatalogoViewModel) {
    Card(Modifier.fillMaxWidth()) {
        Row(
            Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(Icons.Filled.TextFields, contentDescription = null)
            Text(
                "Tamaño de texto: ${(viewModel.escalaTexto * 100).roundToInt()} %. " +
                        "Cámbialo con el deslizador de la sección Elementos de selección.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

// ---------- 1. Estilos de texto ----------
private val NOMBRES_ENFASIS = listOf("Negrita", "Cursiva", "Subrayado", "Tachado", "Color")

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun EstilosDeTexto() {
    var muestra by rememberSaveable { mutableStateOf("Hola, mundo") }
    val enfasis = remember { mutableStateListOf(true, false, false, false, false) }
    var expandido by rememberSaveable { mutableStateOf(false) }
    val texto = muestra.ifBlank { "Texto de ejemplo" }

    DemoElemento(
        nombre = "Textos con distintos estilos",
        descripcion = "El tamaño, el grosor y el color crean jerarquía: indican qué leer " +
                "primero. El énfasis resalta palabras clave. Escribe un texto y cambia su énfasis."
    ) {
        OutlinedTextField(
            value = muestra,
            onValueChange = { muestra = it.take(30) },
            label = { Text("Texto de ejemplo") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Text("Tamaños", style = MaterialTheme.typography.labelLarge)
        Text(texto, style = MaterialTheme.typography.headlineMedium)
        Text(texto, style = MaterialTheme.typography.titleLarge)
        Text(texto, style = MaterialTheme.typography.bodyLarge)
        Text(texto, style = MaterialTheme.typography.labelSmall)

        HorizontalDivider(Modifier.padding(vertical = 4.dp))
        Text("Énfasis", style = MaterialTheme.typography.labelLarge)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            NOMBRES_ENFASIS.forEachIndexed { i, nombre ->
                FilterChip(
                    selected = enfasis[i],
                    onClick = { enfasis[i] = !enfasis[i] },
                    label = { Text(nombre) }
                )
            }
        }
        val decoraciones = buildList {
            if (enfasis[2]) add(TextDecoration.Underline)
            if (enfasis[3]) add(TextDecoration.LineThrough)
        }
        Text(
            buildAnnotatedString {
                append("Resultado: ")
                withStyle(
                    SpanStyle(
                        fontWeight = if (enfasis[0]) FontWeight.Bold else FontWeight.Normal,
                        fontStyle = if (enfasis[1]) FontStyle.Italic else FontStyle.Normal,
                        textDecoration = TextDecoration.combine(decoraciones),
                        color = if (enfasis[4]) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                    )
                ) { append(texto) }
            },
            style = MaterialTheme.typography.titleMedium
        )

        HorizontalDivider(Modifier.padding(vertical = 4.dp))
        Text("Texto largo", style = MaterialTheme.typography.labelLarge)
        Text(
            "Este párrafo es demasiado largo para caber en una sola línea. Cuando está " +
                    "contraído se corta con puntos suspensivos; al expandirlo se muestra completo " +
                    "y ocupa todas las líneas que necesite.",
            maxLines = if (expandido) Int.MAX_VALUE else 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.clickable { expandido = !expandido }
        )
        TextButton(onClick = { expandido = !expandido }) {
            Text(if (expandido) "Ver menos" else "Ver más")
        }
    }
}

// ---------- 2. Imágenes ----------
private data class ModoEscala(val nombre: String, val escala: ContentScale, val explicacion: String)

private val MODOS = listOf(
    ModoEscala("Ajustar", ContentScale.Fit, "Fit: se ve completa, puede dejar espacios vacíos."),
    ModoEscala("Recortar", ContentScale.Crop, "Crop: llena el espacio y recorta lo que sobra."),
    ModoEscala("Estirar", ContentScale.FillBounds, "FillBounds: llena el espacio deformando la imagen."),
    ModoEscala("Original", ContentScale.None, "None: tamaño real, sin escalar.")
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Imagenes() {
    var modo by rememberSaveable { mutableIntStateOf(0) }
    var estadoUrl by remember { mutableStateOf("Cargando imagen desde internet…") }

    DemoElemento(
        nombre = "Imagen local y desde URL",
        descripcion = "La imagen local viene dentro de la app; la otra se descarga de " +
                "internet. El modo de escalado decide cómo se acomodan en su espacio."
    ) {
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            MODOS.forEachIndexed { i, m ->
                FilterChip(
                    selected = modo == i,
                    onClick = { modo = i },
                    label = { Text(m.nombre) }
                )
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                MarcoImagen {
                    Image(
                        painter = painterResource(R.drawable.paisaje_local),
                        contentDescription = "Paisaje de montañas (imagen local)",
                        contentScale = MODOS[modo].escala,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Text("Local", style = MaterialTheme.typography.labelMedium)
            }
            Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                MarcoImagen {
                    AsyncImage(
                        model = "https://picsum.photos/id/1018/800/500",
                        contentDescription = "Paisaje descargado de internet",
                        contentScale = MODOS[modo].escala,
                        placeholder = ColorPainter(MaterialTheme.colorScheme.surfaceVariant),
                        onLoading = { estadoUrl = "Cargando imagen desde internet…" },
                        onSuccess = { estadoUrl = "Imagen de internet cargada." },
                        onError = { estadoUrl = "No se pudo cargar: revisa la conexión a internet." },
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Text("Desde URL", style = MaterialTheme.typography.labelMedium)
            }
        }
        Respuesta(MODOS[modo].explicacion)
        Text(estadoUrl, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun MarcoImagen(contenido: @Composable () -> Unit) {
    val forma = RoundedCornerShape(12.dp)
    Box(
        Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(forma)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(1.dp, MaterialTheme.colorScheme.outline, forma)
    ) { contenido() }
}

// ---------- 3. Indicadores de progreso ----------
@Composable
private fun IndicadoresProgreso() {
    var progreso by remember { mutableFloatStateOf(0.3f) }
    var descargando by remember { mutableStateOf(false) }
    var cargaActiva by rememberSaveable { mutableStateOf(true) }
    val animado by animateFloatAsState(targetValue = progreso, label = "progreso")
    val scope = rememberCoroutineScope()

    DemoElemento(
        nombre = "Indicadores de progreso",
        descripcion = "El modo determinado muestra cuánto falta para terminar una tarea. El " +
                "indeterminado solo indica que algo está en proceso, sin saber cuánto tardará."
    ) {
        Text("Determinado: ${(animado * 100).roundToInt()} %", style = MaterialTheme.typography.labelLarge)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LinearProgressIndicator(progress = { animado }, modifier = Modifier.weight(1f))
            CircularProgressIndicator(progress = { animado })
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(
                onClick = { progreso = (progreso - 0.1f).coerceAtLeast(0f) },
                enabled = !descargando
            ) { Text("−10 %") }
            OutlinedButton(
                onClick = { progreso = (progreso + 0.1f).coerceAtMost(1f) },
                enabled = !descargando
            ) { Text("+10 %") }
            FilledTonalButton(
                onClick = {
                    scope.launch {
                        descargando = true
                        progreso = 0f
                        while (progreso < 1f) {
                            delay(150)
                            progreso = (progreso + 0.05f).coerceAtMost(1f)
                        }
                        descargando = false
                    }
                },
                enabled = !descargando
            ) { Text("Simular") }
        }
        Spacer(Modifier.height(4.dp))
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Indeterminado", style = MaterialTheme.typography.labelLarge)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Carga en curso", style = MaterialTheme.typography.bodySmall)
                Spacer(Modifier.size(8.dp))
                Switch(checked = cargaActiva, onCheckedChange = { cargaActiva = it })
            }
        }
        if (cargaActiva) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                LinearProgressIndicator(Modifier.weight(1f))
                CircularProgressIndicator()
            }
        } else {
            Respuesta("Carga detenida. Activa el interruptor para ver los indicadores.")
        }
    }
}

// ---------- 4. Toast y snackbar ----------
@Composable
private fun ToastYSnackbar(snackbarHostState: SnackbarHostState) {
    val contexto = LocalContext.current
    val scope = rememberCoroutineScope()
    var archivados by rememberSaveable { mutableIntStateOf(0) }

    DemoElemento(
        nombre = "Toast y snackbar",
        descripcion = "El toast es un aviso breve del sistema que desaparece solo. El " +
                "snackbar aparece en la parte inferior de la app y puede incluir una acción, como deshacer."
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = {
                Toast.makeText(contexto, "Esto es un toast", Toast.LENGTH_SHORT).show()
            }) { Text("Mostrar toast") }
            Button(onClick = {
                archivados++
                scope.launch {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    val resultado = snackbarHostState.showSnackbar(
                        message = "Mensaje archivado",
                        actionLabel = "Deshacer",
                        duration = SnackbarDuration.Short
                    )
                    if (resultado == SnackbarResult.ActionPerformed) archivados--
                }
            }) { Text("Archivar") }
        }
        Respuesta("Mensajes archivados: $archivados")
    }
}

// ---------- 5. Diálogo de confirmación (conectado con la Sección 3) ----------
@Composable
private fun DialogoConfirmacion(viewModel: CatalogoViewModel) {
    var mostrar by remember { mutableStateOf(false) }
    var resultado by rememberSaveable { mutableStateOf("Aún no has abierto el diálogo.") }

    DemoElemento(
        nombre = "Diálogo de confirmación",
        descripcion = "Interrumpe al usuario para confirmar una acción importante antes de " +
                "realizarla. Aquí restablece el tamaño de texto que se eligió en la Sección 3."
    ) {
        Button(onClick = { mostrar = true }) { Text("Restablecer tamaño de texto") }
        Respuesta(resultado)
    }

    if (mostrar) {
        AlertDialog(
            onDismissRequest = { mostrar = false; resultado = "Diálogo cerrado sin elegir." },
            title = { Text("¿Restablecer el tamaño?") },
            text = { Text("Los textos volverán al 100 %. Puedes cambiarlo de nuevo en Elementos de selección.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.cambiarEscalaTexto(1f)
                    resultado = "Confirmado: el tamaño volvió al 100 %."
                    mostrar = false
                }) { Text("Restablecer") }
            },
            dismissButton = {
                TextButton(onClick = {
                    resultado = "Cancelado: no se hizo ningún cambio."
                    mostrar = false
                }) { Text("Cancelar") }
            }
        )
    }
}

// ---------- 6. Hoja inferior ----------
private data class OpcionHoja(val texto: String, val icono: ImageVector)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HojaInferior() {
    var mostrar by remember { mutableStateOf(false) }
    var elegido by rememberSaveable { mutableStateOf("Ninguna opción elegida.") }
    val estadoHoja = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    val opciones = listOf(
        OpcionHoja("Compartir", Icons.Filled.Share),
        OpcionHoja("Copiar enlace", Icons.Filled.Link),
        OpcionHoja("Descargar", Icons.Filled.Download)
    )

    DemoElemento(
        nombre = "Hoja inferior (bottom sheet)",
        descripcion = "Panel que sube desde la parte inferior con opciones o contenido " +
                "adicional. Se cierra al deslizarla hacia abajo o al tocar fuera de ella."
    ) {
        Button(onClick = { mostrar = true }) { Text("Abrir hoja inferior") }
        Respuesta(elegido)
    }

    if (mostrar) {
        ModalBottomSheet(
            onDismissRequest = { mostrar = false },
            sheetState = estadoHoja
        ) {
            Column(Modifier.navigationBarsPadding().padding(bottom = 16.dp)) {
                Text(
                    "¿Qué quieres hacer?",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                )
                opciones.forEach { opcion ->
                    ListItem(
                        headlineContent = { Text(opcion.texto) },
                        leadingContent = { Icon(opcion.icono, contentDescription = null) },
                        modifier = Modifier.clickable {
                            elegido = "Elegiste: ${opcion.texto}"
                            scope.launch { estadoHoja.hide() }.invokeOnCompletion {
                                if (!estadoHoja.isVisible) mostrar = false
                            }
                        }
                    )
                }
            }
        }
    }
}

// ---------- 7. Tarjeta, separador y badge ----------
@Composable
private fun TarjetasSeparadoresBadges() {
    var notificaciones by rememberSaveable { mutableIntStateOf(3) }
    var carrito by rememberSaveable { mutableIntStateOf(0) }
    var correoNuevo by rememberSaveable { mutableStateOf(true) }
    var toquesTarjeta by rememberSaveable { mutableIntStateOf(0) }

    DemoElemento(
        nombre = "Tarjeta, separador y distintivo (badge)",
        descripcion = "La tarjeta agrupa información relacionada. El separador divide " +
                "contenido con una línea. El badge muestra un número o punto sobre un ícono para avisar novedades."
    ) {
        Text("Tarjetas", style = MaterialTheme.typography.labelLarge)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Card(onClick = { toquesTarjeta++ }, modifier = Modifier.weight(1f)) {
                Text("Rellena", Modifier.padding(12.dp))
            }
            ElevatedCard(onClick = { toquesTarjeta++ }, modifier = Modifier.weight(1f)) {
                Text("Elevada", Modifier.padding(12.dp))
            }
            OutlinedCard(onClick = { toquesTarjeta++ }, modifier = Modifier.weight(1f)) {
                Text("Contorno", Modifier.padding(12.dp))
            }
        }
        Respuesta("Tarjetas tocadas: $toquesTarjeta")

        HorizontalDivider(Modifier.padding(vertical = 8.dp))

        Text("Separador vertical", style = MaterialTheme.typography.labelLarge)
        Row(
            Modifier.height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Inicio")
            VerticalDivider(Modifier.fillMaxHeight())
            Text("Perfil")
            VerticalDivider(Modifier.fillMaxHeight())
            Text("Ajustes")
        }

        HorizontalDivider(Modifier.padding(vertical = 8.dp))

        Text("Distintivos (badges)", style = MaterialTheme.typography.labelLarge)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(32.dp),
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            BadgedBox(badge = {
                if (notificaciones > 0) Badge { Text(if (notificaciones > 99) "99+" else "$notificaciones") }
            }) {
                Icon(Icons.Filled.Notifications, contentDescription = "Notificaciones: $notificaciones")
            }
            BadgedBox(badge = { if (carrito > 0) Badge { Text("$carrito") } }) {
                Icon(Icons.Filled.ShoppingCart, contentDescription = "Carrito: $carrito")
            }
            // Toca el sobre para marcar o desmarcar el correo como nuevo
            IconButton(onClick = { correoNuevo = !correoNuevo }) {
                BadgedBox(badge = { if (correoNuevo) Badge() }) {
                    Icon(Icons.Filled.Mail, contentDescription = "Correo")
                }
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = { notificaciones++ }) { Text("+ Aviso") }
            OutlinedButton(onClick = { carrito++ }) { Text("+ Carrito") }
            TextButton(onClick = {
                notificaciones = 0; carrito = 0; correoNuevo = false
            }) { Text("Limpiar") }
        }
    }
}