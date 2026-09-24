import 'package:flutter/material.dart';

const categoriaUsuario = 'Del usuario';

class ElementoLista {
  const ElementoLista({
    required this.id,
    required this.titulo,
    required this.subtitulo,
    required this.categoria,
  });

  final int id;
  final String titulo;
  final String subtitulo;
  final String categoria;
}

/// Tema elegido por el usuario. [sistema] sigue el modo claro/oscuro del teléfono.
enum ModoTema {
  sistema('Según el sistema', Icons.brightness_auto, ThemeMode.system),
  claro('Claro', Icons.light_mode, ThemeMode.light),
  oscuro('Oscuro', Icons.dark_mode, ThemeMode.dark);

  const ModoTema(this.nombre, this.icono, this.themeMode);

  final String nombre;
  final IconData icono;
  final ThemeMode themeMode;
}

/// Datos compartidos por todas las secciones.
/// ChangeNotifier avisa a las pantallas cuando algo cambia.
class CatalogoEstado extends ChangeNotifier {
  CatalogoEstado() {
    reiniciarLista();
  }

  static const _frutas = [
    'Manzana', 'Plátano', 'Mango', 'Fresa', 'Uva',
    'Piña', 'Sandía', 'Papaya', 'Guayaba', 'Kiwi',
  ];
  static const _verduras = [
    'Zanahoria', 'Brócoli', 'Calabacita', 'Jitomate', 'Espinaca',
    'Chayote', 'Nopal', 'Pepino', 'Lechuga', 'Elote',
  ];

  int _siguienteId = 0;

  /// Lista de la Sección 4. La Sección 1 puede agregar elementos.
  List<ElementoLista> _elementos = [];
  List<ElementoLista> get elementos => List.unmodifiable(_elementos);

  /// Preferencia elegida en la Sección 3 que modifica los textos de la Sección 5.
  double _escalaTexto = 1.0;
  double get escalaTexto => _escalaTexto;

  /// Por defecto la app sigue el modo del sistema.
  ModoTema _modoTema = ModoTema.sistema;
  ModoTema get modoTema => _modoTema;

  void agregarElemento(String titulo) {
    final limpio = titulo.trim();
    if (limpio.isEmpty) return;
    _elementos = [
      ElementoLista(
        id: _siguienteId++,
        titulo: limpio,
        subtitulo: 'Agregado desde Entrada de texto',
        categoria: categoriaUsuario,
      ),
      ..._elementos,
    ];
    notifyListeners();
  }

  void eliminarElemento(ElementoLista elemento) {
    _elementos = _elementos.where((e) => e.id != elemento.id).toList();
    notifyListeners();
  }

  /// Restaura las frutas y verduras originales, conservando lo que agregó el usuario.
  void reiniciarLista() {
    final delUsuario = _elementos.where((e) => e.categoria == categoriaUsuario);
    _elementos = [
      ...delUsuario,
      for (final f in _frutas)
        ElementoLista(id: _siguienteId++, titulo: f, subtitulo: 'Fruta de temporada', categoria: 'Frutas'),
      for (final v in _verduras)
        ElementoLista(id: _siguienteId++, titulo: v, subtitulo: 'Verdura fresca', categoria: 'Verduras'),
    ];
    notifyListeners();
  }

  void vaciarLista() {
    _elementos = [];
    notifyListeners();
  }

  void cambiarEscalaTexto(double valor) {
    _escalaTexto = valor;
    notifyListeners();
  }

  void cambiarModoTema(ModoTema modo) {
    _modoTema = modo;
    notifyListeners();
  }
}

/// Hace que el estado esté disponible en toda la app.
class EstadoScope extends InheritedNotifier<CatalogoEstado> {
  const EstadoScope({
    super.key,
    required CatalogoEstado super.notifier,
    required super.child,
  });

  /// Obtiene el estado y reconstruye el widget cuando cambia.
  static CatalogoEstado of(BuildContext context) =>
      context.dependOnInheritedWidgetOfExactType<EstadoScope>()!.notifier!;

  /// Obtiene el estado sin escuchar cambios (para usar dentro de botones).
  static CatalogoEstado leer(BuildContext context) =>
      context.getInheritedWidgetOfExactType<EstadoScope>()!.notifier!;
}
