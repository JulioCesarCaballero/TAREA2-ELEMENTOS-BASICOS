package com.gmail.juliocesar64914.interfazconcompose.navegacion

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.gmail.juliocesar64914.interfazconcompose.datos.CatalogoViewModel
import com.gmail.juliocesar64914.interfazconcompose.pantallas.PantallaInicio
import com.gmail.juliocesar64914.interfazconcompose.pantallas.PantallaSeccion
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavegacion(viewModel: CatalogoViewModel) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val entradaActual by navController.currentBackStackEntryAsState()
    val rutaActual = entradaActual?.destination?.route ?: RUTA_INICIO
    val seccionActual = Seccion.entries.find { it.ruta == rutaActual }

    fun navegar(ruta: String) {
        navController.navigate(ruta) {
            // Siempre deja "Inicio" como base; el botón atrás regresa ahí
            popUpTo(RUTA_INICIO)
            launchSingleTop = true
        }
        scope.launch { drawerState.close() }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    "Catálogo de UI",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 28.dp, vertical = 20.dp)
                )
                NavigationDrawerItem(
                    label = { Text("Inicio") },
                    icon = { Icon(Icons.Filled.Home, contentDescription = null) },
                    selected = rutaActual == RUTA_INICIO,
                    onClick = { navegar(RUTA_INICIO) },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                HorizontalDivider(Modifier.padding(horizontal = 28.dp, vertical = 8.dp))
                Seccion.entries.forEach { seccion ->
                    NavigationDrawerItem(
                        label = { Text(seccion.titulo) },
                        icon = { Icon(seccion.icono, contentDescription = null) },
                        selected = seccion == seccionActual,
                        onClick = { navegar(seccion.ruta) },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
                Spacer(Modifier.height(12.dp))
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(seccionActual?.titulo ?: "Catálogo de UI") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Abrir menú")
                        }
                    },
                    actions = {
                        if (seccionActual != null) {
                            IconButton(onClick = { navegar(RUTA_INICIO) }) {
                                Icon(Icons.Filled.Home, contentDescription = "Ir al inicio")
                            }
                        }
                    }
                )
            }
        ) { padding ->
            NavHost(
                navController = navController,
                startDestination = RUTA_INICIO,
                modifier = Modifier.padding(padding)
            ) {
                composable(RUTA_INICIO) {
                    PantallaInicio(
                        viewModel = viewModel,
                        onAbrirSeccion = { navegar(it.ruta) }
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
}