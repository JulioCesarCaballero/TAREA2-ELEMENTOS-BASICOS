import 'package:flutter/material.dart';

/// Tarjeta estándar del catálogo: nombre del elemento, explicación breve
/// y debajo la demostración interactiva.
class TarjetaDemo extends StatelessWidget {
  const TarjetaDemo({
    super.key,
    required this.nombre,
    required this.descripcion,
    required this.children,
  });

  final String nombre;
  final String descripcion;
  final List<Widget> children;

  @override
  Widget build(BuildContext context) {
    final tema = Theme.of(context);
    return Card(
      margin: EdgeInsets.zero,
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          spacing: 8,
          children: [
            Text(
              nombre,
              style: tema.textTheme.titleMedium?.copyWith(color: tema.colorScheme.primary),
            ),
            Text(
              descripcion,
              style: tema.textTheme.bodyMedium?.copyWith(color: tema.colorScheme.onSurfaceVariant),
            ),
            const Divider(height: 16),
            ...children,
          ],
        ),
      ),
    );
  }
}

/// Texto que muestra la respuesta visible de cada demo.
class Respuesta extends StatelessWidget {
  const Respuesta(this.texto, {super.key, this.color});

  final String texto;
  final Color? color;

  @override
  Widget build(BuildContext context) {
    final tema = Theme.of(context);
    return Text(
      texto,
      style: tema.textTheme.bodyMedium?.copyWith(
        color: color ?? tema.colorScheme.primary,
        fontWeight: FontWeight.w500,
      ),
    );
  }
}

/// Subtítulo pequeño dentro de una demo.
class Etiqueta extends StatelessWidget {
  const Etiqueta(this.texto, {super.key});

  final String texto;

  @override
  Widget build(BuildContext context) =>
      Text(texto, style: Theme.of(context).textTheme.labelLarge);
}

/// Lista desplazable con el espaciado común de todas las secciones.
class PantallaCatalogo extends StatelessWidget {
  const PantallaCatalogo({super.key, required this.children});

  final List<Widget> children;

  @override
  Widget build(BuildContext context) {
    return ListView.separated(
      padding: const EdgeInsets.all(16),
      itemCount: children.length,
      itemBuilder: (_, i) => children[i],
      separatorBuilder: (_, __) => const SizedBox(height: 16),
    );
  }
}

/// Decoración con borde para los campos de texto, igual en toda la app.
InputDecoration decoracion({
  String? etiqueta,
  String? pista,
  String? ayuda,
  String? error,
  IconData? icono,
  Widget? sufijo,
}) {
  return InputDecoration(
    border: const OutlineInputBorder(),
    labelText: etiqueta,
    hintText: pista,
    helperText: ayuda,
    errorText: error,
    prefixIcon: icono == null ? null : Icon(icono),
    suffixIcon: sufijo,
  );
}
