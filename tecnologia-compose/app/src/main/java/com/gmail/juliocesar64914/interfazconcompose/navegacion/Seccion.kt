package com.gmail.juliocesar64914.interfazconcompose.navegacion

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.SmartButton
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.ui.graphics.vector.ImageVector

const val RUTA_INICIO = "inicio"

enum class Seccion(
    val ruta: String,
    val titulo: String,
    val tituloCorto: String,
    val descripcion: String,
    val icono: ImageVector
) {
    TEXTO(
        "texto", "Entrada de texto", "Texto",
        "Campos simples, validación, contraseñas, tipos de teclado, autocompletado y búsqueda.",
        Icons.Filled.TextFields
    ),
    BOTONES(
        "botones", "Botones y acciones", "Botones",
        "Botones rellenos, con contorno, de texto, con ícono, flotantes, alternancia y carga.",
        Icons.Filled.SmartButton
    ),
    SELECCION(
        "seleccion", "Elementos de selección", "Selección",
        "Casillas, opciones, interruptores, deslizadores, desplegables, fecha, hora y chips.",
        Icons.Filled.CheckBox
    ),
    LISTAS(
        "listas", "Listas y colecciones", "Listas",
        "Listas, cuadrículas, encabezados, deslizar para eliminar, actualizar y pestañas.",
        Icons.AutoMirrored.Filled.List
    ),
    INFORMACION(
        "informacion", "Información y retroalimentación", "Información",
        "Textos, imágenes, progreso, toast, snackbar, diálogos, hoja inferior y badges.",
        Icons.Filled.Info
    ),
    CONTENEDORES(
        "contenedores", "Contenedores y estructura", "Contenedores",
        "Filas, columnas, superposición, desplazamiento, barras y distribución con pesos.",
        Icons.Filled.Dashboard
    )
}