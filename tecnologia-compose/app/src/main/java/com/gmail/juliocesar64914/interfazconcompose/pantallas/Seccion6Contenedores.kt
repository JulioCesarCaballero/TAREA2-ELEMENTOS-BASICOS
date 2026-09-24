package com.gmail.juliocesar64914.interfazconcompose.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gmail.juliocesar64914.interfazconcompose.componentes.DemoElemento
import com.gmail.juliocesar64914.interfazconcompose.componentes.PantallaCatalogo
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun Seccion6Contenedores() {
    PantallaCatalogo {
        item { DistribucionFilaColumna() }
        item { DistribucionSuperpuesta() }
        item { ContenedorDesplazable() }
        item { BarraSuperior() }
        item { BarraNavegacionInferior() }
        item { MenuLateral() }
        item { DistribucionConPesos() }
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

/** Cuadro de color con una letra, para visualizar las distribuciones. */
@Composable
private fun Caja(texto: String, fondo: Color, contenido: Color, modifier: Modifier = Modifier) {
    Surface(
        color = fondo,
        contentColor = contenido,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier.size(44.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(texto, fontWeight = FontWeight.Bold)
        }
    }
}

/** Recuadro que simula una pantalla dentro de la tarjeta. */
private fun Modifier.marco(color: Color) = this
    .clip(RoundedCornerShape(12.dp))
    .border(1.dp, color, RoundedCornerShape(12.dp))

// ---------- 1. Fila y columna ----------
private data class OpcionArreglo(val nombre: String, val horizontal: Arrangement.Horizontal, val vertical: Arrangement.Vertical)

private val ARREGLOS = listOf(
    OpcionArreglo("Inicio", Arrangement.Start, Arrangement.Top),
    OpcionArreglo("Centro", Arrangement.Center, Arrangement.Center),
    OpcionArreglo("Extremos", Arrangement.SpaceBetween, Arrangement.SpaceBetween),
    OpcionArreglo("Uniforme", Arrangement.SpaceEvenly, Arrangement.SpaceEvenly)
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DistribucionFilaColumna() {
    var elegido by rememberSaveable { mutableIntStateOf(0) }
    val c = MaterialTheme.colorScheme
    val arreglo = ARREGLOS[elegido]

    DemoElemento(
        nombre = "Distribución en fila y en columna",
        descripcion = "La fila (Row) acomoda elementos de izquierda a derecha y la columna " +
                "(Column) de arriba hacia abajo. El arreglo decide cómo se reparte el espacio sobrante."
    ) {
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ARREGLOS.forEachIndexed { i, opcion ->
                FilterChip(
                    selected = elegido == i,
                    onClick = { elegido = i },
                    label = { Text(opcion.nombre) }
                )
            }
        }
        Text("Fila", style = MaterialTheme.typography.labelLarge)
        Row(
            Modifier
                .fillMaxWidth()
                .marco(c.outlineVariant)
                .padding(8.dp),
            horizontalArrangement = arreglo.horizontal
        ) {
            Caja("A", c.primaryContainer, c.onPrimaryContainer)
            Caja("B", c.secondaryContainer, c.onSecondaryContainer)
            Caja("C", c.tertiaryContainer, c.onTertiaryContainer)
        }
        Text("Columna", style = MaterialTheme.typography.labelLarge)
        Column(
            Modifier
                .fillMaxWidth()
                .height(200.dp)
                .marco(c.outlineVariant)
                .padding(8.dp),
            verticalArrangement = arreglo.vertical,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Caja("A", c.primaryContainer, c.onPrimaryContainer)
            Caja("B", c.secondaryContainer, c.onSecondaryContainer)
            Caja("C", c.tertiaryContainer, c.onTertiaryContainer)
        }
        Respuesta("Arreglo aplicado: ${arreglo.nombre}")
    }
}

// ---------- 2. Superpuesta ----------
private data class OpcionAlineacion(val nombre: String, val alineacion: Alignment)

private val ALINEACIONES = listOf(
    OpcionAlineacion("Arriba izq.", Alignment.TopStart),
    OpcionAlineacion("Centro", Alignment.Center),
    OpcionAlineacion("Abajo der.", Alignment.BottomEnd)
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DistribucionSuperpuesta() {
    var elegido by rememberSaveable { mutableIntStateOf(2) }
    val c = MaterialTheme.colorScheme

    DemoElemento(
        nombre = "Distribución superpuesta",
        descripcion = "El contenedor Box coloca elementos unos encima de otros. Sirve para " +
                "poner texto sobre una imagen o un distintivo en una esquina."
    ) {
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ALINEACIONES.forEachIndexed { i, opcion ->
                FilterChip(
                    selected = elegido == i,
                    onClick = { elegido = i },
                    label = { Text(opcion.nombre) }
                )
            }
        }
        Box(
            Modifier
                .fillMaxWidth()
                .height(160.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(c.primaryContainer)
        ) {
            // Capa 1: fondo con un texto grande
            Text(
                "Capa de fondo",
                style = MaterialTheme.typography.headlineSmall,
                color = c.onPrimaryContainer.copy(alpha = 0.35f),
                modifier = Modifier.align(Alignment.Center)
            )
            // Capa 2: etiqueta que se mueve según la alineación elegida
            Surface(
                color = c.primary,
                contentColor = c.onPrimary,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .align(ALINEACIONES[elegido].alineacion)
                    .padding(12.dp)
            ) {
                Row(
                    Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.Star, contentDescription = null, Modifier.size(18.dp))
                    Spacer(Modifier.size(6.dp))
                    Text("Capa superior")
                }
            }
        }
        Respuesta("La capa superior está en: ${ALINEACIONES[elegido].nombre}")
    }
}

// ---------- 3. Contenedor con desplazamiento ----------
@Composable
private fun ContenedorDesplazable() {
    val scroll = rememberScrollState()
    val scope = rememberCoroutineScope()
    val porcentaje = if (scroll.maxValue > 0) scroll.value * 100 / scroll.maxValue else 0

    DemoElemento(
        nombre = "Contenedor con desplazamiento vertical",
        descripcion = "Cuando el contenido es más alto que el espacio disponible, permite " +
                "recorrerlo deslizando el dedo. A diferencia de una lista, dibuja todo su contenido a la vez."
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .height(180.dp)
                .marco(MaterialTheme.colorScheme.outlineVariant)
                .verticalScroll(scroll)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(15) { i ->
                Text(
                    "Párrafo ${i + 1}. Este texto forma parte de un contenido largo que no " +
                            "cabe completo en el recuadro.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(onClick = { scope.launch { scroll.animateScrollTo(0) } }) { Text("Inicio") }
            OutlinedButton(onClick = { scope.launch { scroll.animateScrollTo(scroll.maxValue) } }) { Text("Final") }
        }
        Respuesta("Recorrido: $porcentaje %")
    }
}

// ---------- 4. Barra superior ----------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BarraSuperior() {
    var favorito by rememberSaveable { mutableStateOf(false) }
    var menuAbierto by remember { mutableStateOf(false) }
    var accion by rememberSaveable { mutableStateOf("Toca un ícono de la barra.") }

    DemoElemento(
        nombre = "Barra superior con título y acciones",
        descripcion = "Muestra el nombre de la pantalla y las acciones más usadas. Las " +
                "acciones menos frecuentes se guardan en el menú de tres puntos."
    ) {
        TopAppBar(
            title = { Text("Mis notas") },
            navigationIcon = {
                IconButton(onClick = { accion = "Regresar" }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                }
            },
            actions = {
                IconButton(onClick = { accion = "Buscar" }) {
                    Icon(Icons.Filled.Search, contentDescription = "Buscar")
                }
                IconButton(onClick = {
                    favorito = !favorito
                    accion = if (favorito) "Agregado a favoritos" else "Quitado de favoritos"
                }) {
                    Icon(
                        if (favorito) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Favorito"
                    )
                }
                Box {
                    IconButton(onClick = { menuAbierto = true }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "Más opciones")
                    }
                    DropdownMenu(expanded = menuAbierto, onDismissRequest = { menuAbierto = false }) {
                        listOf("Ordenar", "Compartir", "Ajustes").forEach { opcion ->
                            DropdownMenuItem(
                                text = { Text(opcion) },
                                onClick = { accion = "Menú: $opcion"; menuAbierto = false }
                            )
                        }
                    }
                }
            },
            // Sin márgenes de la barra de estado, porque está dentro de una tarjeta
            windowInsets = WindowInsets(0, 0, 0, 0),
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier.clip(RoundedCornerShape(12.dp))
        )
        Respuesta("Acción: $accion")
    }
}

// ---------- 5. Barra de navegación inferior ----------
private data class Destino(val nombre: String, val icono: ImageVector)

private val DESTINOS = listOf(
    Destino("Inicio", Icons.Filled.Home),
    Destino("Buscar", Icons.Filled.Search),
    Destino("Perfil", Icons.Filled.Person)
)

@Composable
private fun BarraNavegacionInferior() {
    var actual by rememberSaveable { mutableIntStateOf(0) }
    var avisosPerfil by rememberSaveable { mutableIntStateOf(2) }

    DemoElemento(
        nombre = "Barra de navegación inferior",
        descripcion = "Permite cambiar entre las pantallas principales de una app con un " +
                "toque. Se recomienda para 3 a 5 destinos."
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .height(240.dp)
                .marco(MaterialTheme.colorScheme.outlineVariant)
        ) {
            Box(
                Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        DESTINOS[actual].icono,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text("Pantalla: ${DESTINOS[actual].nombre}", style = MaterialTheme.typography.titleMedium)
                }
            }
            NavigationBar(windowInsets = WindowInsets(0, 0, 0, 0)) {
                DESTINOS.forEachIndexed { i, destino ->
                    NavigationBarItem(
                        selected = actual == i,
                        onClick = {
                            actual = i
                            if (destino.nombre == "Perfil") avisosPerfil = 0
                        },
                        icon = {
                            BadgedBox(badge = {
                                if (destino.nombre == "Perfil" && avisosPerfil > 0) Badge { Text("$avisosPerfil") }
                            }) {
                                Icon(destino.icono, contentDescription = null)
                            }
                        },
                        label = { Text(destino.nombre) }
                    )
                }
            }
        }
    }
}

// ---------- 6. Menú lateral ----------
private val OPCIONES_MENU = listOf(
    Destino("Inicio", Icons.Filled.Home),
    Destino("Favoritos", Icons.Filled.Favorite),
    Destino("Ajustes", Icons.Filled.Settings)
)

@Composable
private fun MenuLateral() {
    val estadoMenu = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var actual by rememberSaveable { mutableIntStateOf(0) }

    DemoElemento(
        nombre = "Menú lateral (drawer)",
        descripcion = "Panel que se desliza desde el borde izquierdo con las secciones de la " +
                "app. Se abre con el ícono de menú o arrastrando desde el borde."
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(280.dp)
                .marco(MaterialTheme.colorScheme.outlineVariant)
        ) {
            ModalNavigationDrawer(
                drawerState = estadoMenu,
                drawerContent = {
                    ModalDrawerSheet(windowInsets = WindowInsets(0, 0, 0, 0)) {
                        Text(
                            "Mi app",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(horizontal = 28.dp, vertical = 16.dp)
                        )
                        OPCIONES_MENU.forEachIndexed { i, opcion ->
                            NavigationDrawerItem(
                                label = { Text(opcion.nombre) },
                                icon = { Icon(opcion.icono, contentDescription = null) },
                                selected = actual == i,
                                onClick = {
                                    actual = i
                                    scope.launch { estadoMenu.close() }
                                },
                                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                            )
                        }
                    }
                }
            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { scope.launch { estadoMenu.open() } }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Abrir menú")
                        }
                        Text(OPCIONES_MENU[actual].nombre, style = MaterialTheme.typography.titleMedium)
                    }
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                OPCIONES_MENU[actual].icono,
                                contentDescription = null,
                                modifier = Modifier.size(40.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text("Toca ☰ para cambiar de sección")
                        }
                    }
                }
            }
        }
    }
}

// ---------- 7. Distribución con pesos ----------
@Composable
private fun DistribucionConPesos() {
    var pesoA by remember { mutableFloatStateOf(1f) }
    var pesoB by remember { mutableFloatStateOf(2f) }
    val pesoC = 1f
    val total = pesoA + pesoB + pesoC
    val c = MaterialTheme.colorScheme

    DemoElemento(
        nombre = "Distribución con pesos proporcionales",
        descripcion = "Cada elemento recibe una parte del espacio según su peso: uno con peso 2 " +
                "ocupa el doble que uno con peso 1. Así la interfaz se adapta a cualquier ancho de pantalla."
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(64.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            BloquePeso("A", pesoA, total, c.primaryContainer, c.onPrimaryContainer, Modifier.weight(pesoA))
            BloquePeso("B", pesoB, total, c.secondaryContainer, c.onSecondaryContainer, Modifier.weight(pesoB))
            BloquePeso("C", pesoC, total, c.tertiaryContainer, c.onTertiaryContainer, Modifier.weight(pesoC))
        }
        Text("Peso de A: ${pesoA.roundToInt()}", style = MaterialTheme.typography.labelLarge)
        Slider(value = pesoA, onValueChange = { pesoA = it }, valueRange = 1f..4f, steps = 2)
        Text("Peso de B: ${pesoB.roundToInt()}", style = MaterialTheme.typography.labelLarge)
        Slider(value = pesoB, onValueChange = { pesoB = it }, valueRange = 1f..4f, steps = 2)
        Respuesta("C tiene peso fijo de 1. Total de pesos: ${total.roundToInt()}")
    }
}

@Composable
private fun BloquePeso(
    nombre: String,
    peso: Float,
    total: Float,
    fondo: Color,
    contenido: Color,
    modifier: Modifier
) {
    Surface(
        color = fondo,
        contentColor = contenido,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier.fillMaxHeight()
    ) {
        Box(contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(nombre, fontWeight = FontWeight.Bold)
                Text("${(peso / total * 100).roundToInt()} %", style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}
