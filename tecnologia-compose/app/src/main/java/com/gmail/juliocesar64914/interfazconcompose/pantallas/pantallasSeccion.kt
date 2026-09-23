package com.gmail.juliocesar64914.interfazconcompose.pantallas

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gmail.juliocesar64914.interfazconcompose.datos.CatalogoViewModel
import com.gmail.juliocesar64914.interfazconcompose.navegacion.Seccion

/** Decide qué pantalla mostrar para cada sección. */
@Composable
fun PantallaSeccion(seccion: Seccion, viewModel: CatalogoViewModel) {
    when (seccion) {
        // Iremos reemplazando cada línea conforme construyamos las secciones
        Seccion.TEXTO -> EnConstruccion(seccion)
        Seccion.BOTONES -> EnConstruccion(seccion)
        Seccion.SELECCION -> EnConstruccion(seccion)
        Seccion.LISTAS -> EnConstruccion(seccion)
        Seccion.INFORMACION -> EnConstruccion(seccion)
        Seccion.CONTENEDORES -> EnConstruccion(seccion)
    }
}

@Composable
private fun EnConstruccion(seccion: Seccion) {
    Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
        Text(
            "La sección «${seccion.titulo}» se agregará en el siguiente paso.",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}