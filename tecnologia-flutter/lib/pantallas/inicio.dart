import 'package:flutter/material.dart';

import '../datos/catalogo_estado.dart';
import '../navegacion/seccion.dart';

class PantallaInicio extends StatelessWidget {
  const PantallaInicio({super.key, required this.onAbrirSeccion});

  final ValueChanged<Seccion> onAbrirSeccion;

  @override
  Widget build(BuildContext context) {
    final tema = Theme.of(context);
    final estado = EstadoScope.of(context);

    return ListView(
      padding: const EdgeInsets.all(16),
      children: [
        Text('Catálogo de elementos de interfaz', style: tema.textTheme.headlineSmall),
        const SizedBox(height: 4),
        Text(
          'Elige una categoría aquí o en las pestañas de arriba. Cada elemento incluye '
          'su nombre, para qué sirve y una demostración interactiva.',
          style: tema.textTheme.bodyMedium?.copyWith(color: tema.colorScheme.onSurfaceVariant),
        ),
        const SizedBox(height: 4),
        Text(
          'La lista de la Sección 4 tiene ${estado.elementos.length} elementos.',
          style: tema.textTheme.labelMedium?.copyWith(color: tema.colorScheme.primary),
        ),
        const SizedBox(height: 12),
        for (final seccion in Seccion.values) ...[
          Card.filled(
            margin: EdgeInsets.zero,
            clipBehavior: Clip.antiAlias,
            child: InkWell(
              onTap: () => onAbrirSeccion(seccion),
              child: Padding(
                padding: const EdgeInsets.all(16),
                child: Row(
                  children: [
                    CircleAvatar(
                      radius: 24,
                      backgroundColor: tema.colorScheme.primaryContainer,
                      foregroundColor: tema.colorScheme.onPrimaryContainer,
                      child: Icon(seccion.icono),
                    ),
                    const SizedBox(width: 16),
                    Expanded(
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Text(seccion.titulo, style: tema.textTheme.titleMedium),
                          Text(
                            seccion.descripcion,
                            style: tema.textTheme.bodySmall
                                ?.copyWith(color: tema.colorScheme.onSurfaceVariant),
                          ),
                        ],
                      ),
                    ),
                  ],
                ),
              ),
            ),
          ),
          const SizedBox(height: 12),
        ],
      ],
    );
  }
}
