import 'package:flutter/material.dart';

import '../datos/catalogo_estado.dart';
import '../pantallas/inicio.dart';
import '../pantallas/pendiente.dart';
import '../pantallas/seccion1_texto.dart';
import 'seccion.dart';

/// Estructura principal: barra superior, pestañas (Inicio + 6 secciones) y contenido.
class Estructura extends StatefulWidget {
  const Estructura({super.key});

  @override
  State<Estructura> createState() => _EstructuraState();
}

class _EstructuraState extends State<Estructura> with SingleTickerProviderStateMixin {
  late final TabController _pestanas =
      TabController(length: Seccion.values.length + 1, vsync: this);

  /// null = pantalla de inicio
  Seccion? _actual;

  void _ir(Seccion? seccion) {
    setState(() => _actual = seccion);
    final indice = seccion == null ? 0 : seccion.index + 1;
    if (_pestanas.index != indice) _pestanas.animateTo(indice);
  }

  @override
  void dispose() {
    _pestanas.dispose();
    super.dispose();
  }

  Widget _contenido() {
    return switch (_actual) {
      null => PantallaInicio(onAbrirSeccion: _ir),
      Seccion.texto => const Seccion1Texto(),
      final Seccion s => PantallaPendiente(seccion: s),
    };
  }

  @override
  Widget build(BuildContext context) {
    // En una sección, el botón atrás del teléfono regresa al inicio en vez de cerrar la app
    return PopScope(
      canPop: _actual == null,
      onPopInvokedWithResult: (seCerro, _) {
        if (!seCerro) _ir(null);
      },
      child: Scaffold(
        appBar: AppBar(
          automaticallyImplyLeading: false,
          leading: _actual == null
              ? null
              : IconButton(
                  icon: const Icon(Icons.arrow_back),
                  tooltip: 'Regresar al inicio',
                  onPressed: () => _ir(null),
                ),
          title: Text(_actual?.titulo ?? 'Catálogo de elementos'),
          actions: const [SelectorTema()],
          // Menú de navegación: pestañas para moverse entre las seis secciones
          bottom: TabBar(
            controller: _pestanas,
            isScrollable: true,
            tabAlignment: TabAlignment.start,
            onTap: (i) => _ir(i == 0 ? null : Seccion.values[i - 1]),
            tabs: [
              const Tab(icon: Icon(Icons.home), text: 'Inicio'),
              for (final s in Seccion.values) Tab(icon: Icon(s.icono), text: s.tituloCorto),
            ],
          ),
        ),
        // La clave hace que cada sección empiece con su estado limpio
        body: KeyedSubtree(key: ValueKey(_actual), child: _contenido()),
      ),
    );
  }
}

/// Botón de la barra superior para elegir tema: según el sistema, claro u oscuro.
class SelectorTema extends StatelessWidget {
  const SelectorTema({super.key});

  @override
  Widget build(BuildContext context) {
    final estado = EstadoScope.of(context);
    return PopupMenuButton<ModoTema>(
      icon: Icon(estado.modoTema.icono),
      tooltip: 'Cambiar tema: ${estado.modoTema.nombre}',
      onSelected: estado.cambiarModoTema,
      itemBuilder: (_) => [
        for (final modo in ModoTema.values)
          CheckedPopupMenuItem(
            value: modo,
            checked: modo == estado.modoTema,
            child: Text(modo.nombre),
          ),
      ],
    );
  }
}
