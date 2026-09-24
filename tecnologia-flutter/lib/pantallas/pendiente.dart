import 'package:flutter/material.dart';

import '../navegacion/seccion.dart';

/// Pantalla temporal para las secciones que todavía no se construyen.
class PantallaPendiente extends StatelessWidget {
  const PantallaPendiente({super.key, required this.seccion});

  final Seccion seccion;

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Padding(
        padding: const EdgeInsets.all(24),
        child: Text(
          'La sección «${seccion.titulo}» se agregará en el siguiente paso.',
          textAlign: TextAlign.center,
          style: Theme.of(context).textTheme.bodyLarge,
        ),
      ),
    );
  }
}
