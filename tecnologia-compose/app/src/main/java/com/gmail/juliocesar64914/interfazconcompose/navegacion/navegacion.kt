package com.gmail.juliocesar64914.interfazconcompose.navegacion

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.gmail.juliocesar64914.interfazconcompose.datos.CatalogoViewModel
import com.gmail.juliocesar64914.interfazconcompose.datos.ModoTema
import com.gmail.juliocesar64914.interfazconcompose.pantallas.PantallaInicio
import com.gmail.juliocesar64914.interfazconcompose.pantallas.PantallaSeccion

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavegacion(viewModel: CatalogoViewModel) {
    val navController = rememberNavController()
    val entradaActual by navController.currentBackStackEntryAsState()
    val seccionActual = Seccion.entries.find { it.ruta == entradaActual?.destination?.route }

    // Pestaña 0 = Inicio, pestañas 1 a 6 = secciones
    val pestanaSeleccionada = seccionActual?.let { it.ordinal + 1 } ?: 0

    fun irAInicio() {
        navController.popBackStack(RUTA_INICIO, inclusive = false)
    }

    fun irASeccion(seccion: Seccion) {
        navController.navigate(seccion.ruta) {
            popUpTo(RUTA_INICIO)       // Inicio siempre queda como base
            launchSingleTop = true
        }
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text(seccionActual?.titulo ?: "Catálogo de elementos") },
                    navigationIcon = {
                        if (seccionActual != null) {
                            IconButton(onClick = { irAInicio() }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar al inicio")
                            }
                        }
                    },
                    actions = {
                        SelectorTema(
                            actual = viewModel.modoTema,
                            onElegir = { viewModel.cambiarModoTema(it) }
                        )
                    }
                )
                // Menú de navegación: pestañas para moverse entre las seis secciones
                PrimaryScrollableTabRow(
                    selectedTabIndex = pestanaSeleccionada,
                    edgePadding = 8.dp
                ) {
                    Tab(
                        selected = pestanaSeleccionada == 0,
                        onClick = { irAInicio() },
                        text = { Text("Inicio") },
                        icon = { Icon(Icons.Filled.Home, contentDescription = null) }
                    )
                    Seccion.entries.forEach { seccion ->
                        Tab(
                            selected = seccion == seccionActual,
                            onClick = { irASeccion(seccion) },
                            text = { Text(seccion.tituloCorto) },
                            icon = { Icon(seccion.icono, contentDescription = null) }
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = RUTA_INICIO,
            modifier = Modifier
                .padding(padding)
                .consumeWindowInsets(padding)
                .imePadding() // evita que el teclado tape los campos de texto
        ) {
            composable(RUTA_INICIO) {
                PantallaInicio(
                    viewModel = viewModel,
                    onAbrirSeccion = { irASeccion(it) }
                )
            }
            Seccion.entries.forEach { seccion ->
                composable(seccion.ruta) {
                    PantallaSeccion(seccion = seccion, viewModel = viewModel)
                }
            }
        }
    }
}

private fun iconoTema(modo: ModoTema): ImageVector = when (modo) {
    ModoTema.SISTEMA -> Icons.Filled.BrightnessAuto
    ModoTema.CLARO -> Icons.Filled.LightMode
    ModoTema.OSCURO -> Icons.Filled.DarkMode
}

/** Botón de la barra superior para elegir tema: según el sistema, claro u oscuro. */
@Composable
private fun SelectorTema(actual: ModoTema, onElegir: (ModoTema) -> Unit) {
    var abierto by remember { mutableStateOf(false) }
    Box {
        IconButton(onClick = { abierto = true }) {
            Icon(iconoTema(actual), contentDescription = "Cambiar tema: ${actual.nombre}")
        }
        DropdownMenu(expanded = abierto, onDismissRequest = { abierto = false }) {
            ModoTema.entries.forEach { modo ->
                DropdownMenuItem(
                    text = { Text(modo.nombre) },
                    leadingIcon = { Icon(iconoTema(modo), contentDescription = null) },
                    trailingIcon = {
                        if (modo == actual) Icon(Icons.Filled.Check, contentDescription = "Seleccionado")
                    },
                    onClick = {
                        onElegir(modo)
                        abierto = false
                    }
                )
            }
        }
    }
}