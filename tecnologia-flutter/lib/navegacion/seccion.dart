import 'package:flutter/material.dart';

enum Seccion {
  texto(
    'Entrada de texto',
    'Texto',
    'Campos simples, validación, contraseñas, tipos de teclado, autocompletado y búsqueda.',
    Icons.text_fields,
  ),
  botones(
    'Botones y acciones',
    'Botones',
    'Botones rellenos, con contorno, de texto, con ícono, flotantes, alternancia y carga.',
    Icons.smart_button,
  ),
  seleccion(
    'Elementos de selección',
    'Selección',
    'Casillas, opciones, interruptores, deslizadores, desplegables, fecha, hora y chips.',
    Icons.check_box,
  ),
  listas(
    'Listas y colecciones',
    'Listas',
    'Listas, cuadrículas, encabezados, deslizar para eliminar, actualizar y pestañas.',
    Icons.list,
  ),
  informacion(
    'Información y retroalimentación',
    'Información',
    'Textos, imágenes, progreso, toast, snackbar, diálogos, hoja inferior y badges.',
    Icons.info,
  ),
  contenedores(
    'Contenedores y estructura',
    'Contenedores',
    'Filas, columnas, superposición, desplazamiento, barras y distribución con pesos.',
    Icons.dashboard,
  );

  const Seccion(this.titulo, this.tituloCorto, this.descripcion, this.icono);

  final String titulo;
  final String tituloCorto;
  final String descripcion;
  final IconData icono;
}
