package com.gmail.juliocesar64914.interfazconcompose.navegacion

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.gmail.juliocesar64914.interfazconcompose.datos.CatalogoViewModel
import com.gmail.juliocesar64914.interfazconcompose.pantallas.PantallaInicio
import com.gmail.juliocesar64914.interfazconcompose.pantallas.PantallaSeccion

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavegacion(viewModel: CatalogoViewModel) {
    val navController = rememberNavController()
    val entradaActual by navController.currentBackStackEntryAsState()
    val seccionActual = Seccion.entries.find { it.ruta == entradaActual?.destination?.route }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(seccionActual?.titulo ?: "Catálogo de UI") },
                navigationIcon = {
                    // La flecha solo aparece dentro de una sección y regresa al inicio
                    if (seccionActual != null) {
                        IconButton(onClick = { navController.popBackStack(RUTA_INICIO, inclusive = false) }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar al inicio")
                        }
                    }
                }
            )
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
                    onAbrirSeccion = { seccion ->
                        navController.navigate(seccion.ruta) { launchSingleTop = true }
                    }
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