package com.gmail.juliocesar64914.interfazconviews.navegacion

import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.navOptions
import com.gmail.juliocesar64914.interfazconviews.R

enum class Seccion(
    @IdRes val destino: Int,
    val titulo: String,
    val tituloCorto: String,
    val descripcion: String,
    @DrawableRes val icono: Int
) {
    TEXTO(
        R.id.seccion1Fragment, "Entrada de texto", "Texto",
        "Campos simples, validación, contraseñas, tipos de teclado, autocompletado y búsqueda.",
        R.drawable.ic_text_fields
    ),
    BOTONES(
        R.id.seccion2Fragment, "Botones y acciones", "Botones",
        "Botones rellenos, con contorno, de texto, con ícono, flotantes, alternancia y carga.",
        R.drawable.ic_botones
    ),
    SELECCION(
        R.id.seccion3Fragment, "Elementos de selección", "Selección",
        "Casillas, opciones, interruptores, deslizadores, desplegables, fecha, hora y chips.",
        R.drawable.ic_check_box
    ),
    LISTAS(
        R.id.seccion4Fragment, "Listas y colecciones", "Listas",
        "Listas, cuadrículas, encabezados, deslizar para eliminar, actualizar y pestañas.",
        R.drawable.ic_list
    ),
    INFORMACION(
        R.id.seccion5Fragment, "Información y retroalimentación", "Información",
        "Textos, imágenes, progreso, toast, snackbar, diálogos, hoja inferior y badges.",
        R.drawable.ic_info
    ),
    CONTENEDORES(
        R.id.seccion6Fragment, "Contenedores y estructura", "Contenedores",
        "Filas, columnas, superposición, desplazamiento, barras y distribución con restricciones.",
        R.drawable.ic_dashboard
    )
}

/** Abre una sección dejando siempre Inicio como base del historial. */
fun NavController.irASeccion(seccion: Seccion) {
    navigate(seccion.destino, null, navOptions {
        popUpTo(R.id.inicioFragment) { inclusive = false }
        launchSingleTop = true
    })
}

fun NavController.irAInicio() {
    popBackStack(R.id.inicioFragment, false)
}
