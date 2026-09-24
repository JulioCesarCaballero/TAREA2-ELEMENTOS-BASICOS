import 'package:flutter/material.dart';

import '../componentes/tarjeta_demo.dart';

class Seccion6Contenedores extends StatelessWidget {
  const Seccion6Contenedores({super.key});

  @override
  Widget build(BuildContext context) {
    return const PantallaCatalogo(
      children: [
        _FilaColumna(),
        _Superpuesta(),
        _ContenedorDesplazable(),
        _BarraSuperior(),
        _NavegacionInferior(),
        _MenuLateral(),
        _DistribucionPesos(),
      ],
    );
  }
}

/// Cuadro de color con una letra, para visualizar las distribuciones.
class _Caja extends StatelessWidget {
  const _Caja(this.letra, this.fondo, this.texto);

  final String letra;
  final Color fondo;
  final Color texto;

  @override
  Widget build(BuildContext context) {
    return Container(
      width: 44,
      height: 44,
      alignment: Alignment.center,
      decoration: BoxDecoration(color: fondo, borderRadius: BorderRadius.circular(8)),
      child: Text(letra, style: TextStyle(color: texto, fontWeight: FontWeight.bold)),
    );
  }
}

List<Widget> _cajasABC(ColorScheme c) => [
      _Caja('A', c.primaryContainer, c.onPrimaryContainer),
      _Caja('B', c.secondaryContainer, c.onSecondaryContainer),
      _Caja('C', c.tertiaryContainer, c.onTertiaryContainer),
    ];

/// Recuadro con borde que simula una pantalla dentro de la tarjeta.
BoxDecoration _marco(BuildContext context) => BoxDecoration(
      border: Border.all(color: Theme.of(context).colorScheme.outlineVariant),
      borderRadius: BorderRadius.circular(12),
    );

// ---------- 1. Fila y columna ----------
class _FilaColumna extends StatefulWidget {
  const _FilaColumna();

  @override
  State<_FilaColumna> createState() => _FilaColumnaState();
}

class _FilaColumnaState extends State<_FilaColumna> {
  static const _arreglos = [
    ('Inicio', MainAxisAlignment.start),
    ('Centro', MainAxisAlignment.center),
    ('Extremos', MainAxisAlignment.spaceBetween),
    ('Uniforme', MainAxisAlignment.spaceEvenly),
  ];
  int _elegido = 0;

  @override
  Widget build(BuildContext context) {
    final c = Theme.of(context).colorScheme;
    final arreglo = _arreglos[_elegido].$2;

    return TarjetaDemo(
      nombre: 'Distribución en fila y en columna',
      descripcion: 'Row acomoda elementos de izquierda a derecha y Column de arriba hacia '
          'abajo. mainAxisAlignment decide cómo se reparte el espacio sobrante.',
      children: [
        Wrap(
          spacing: 8,
          runSpacing: 4,
          children: [
            for (var i = 0; i < _arreglos.length; i++)
              ChoiceChip(
                label: Text(_arreglos[i].$1),
                selected: _elegido == i,
                onSelected: (_) => setState(() => _elegido = i),
              ),
          ],
        ),
        const Etiqueta('Fila (Row)'),
        Container(
          padding: const EdgeInsets.all(8),
          decoration: _marco(context),
          child: Row(mainAxisAlignment: arreglo, spacing: 8, children: _cajasABC(c)),
        ),
        const Etiqueta('Columna (Column)'),
        Container(
          height: 200,
          width: double.infinity,
          padding: const EdgeInsets.all(8),
          decoration: _marco(context),
          child: Column(mainAxisAlignment: arreglo, spacing: 8, children: _cajasABC(c)),
        ),
        Respuesta('Arreglo aplicado: ${_arreglos[_elegido].$1}'),
      ],
    );
  }
}

// ---------- 2. Superpuesta ----------
class _Superpuesta extends StatefulWidget {
  const _Superpuesta();

  @override
  State<_Superpuesta> createState() => _SuperpuestaState();
}

class _SuperpuestaState extends State<_Superpuesta> {
  static const _posiciones = [
    ('Arriba izq.', Alignment.topLeft),
    ('Centro', Alignment.center),
    ('Abajo der.', Alignment.bottomRight),
  ];
  int _elegida = 2;

  @override
  Widget build(BuildContext context) {
    final c = Theme.of(context).colorScheme;
    return TarjetaDemo(
      nombre: 'Distribución superpuesta',
      descripcion: 'Stack coloca elementos unos encima de otros. Sirve para poner texto '
          'sobre una imagen o un distintivo en una esquina.',
      children: [
        Wrap(
          spacing: 8,
          children: [
            for (var i = 0; i < _posiciones.length; i++)
              ChoiceChip(
                label: Text(_posiciones[i].$1),
                selected: _elegida == i,
                onSelected: (_) => setState(() => _elegida = i),
              ),
          ],
        ),
        Container(
          height: 160,
          decoration: BoxDecoration(color: c.primaryContainer, borderRadius: BorderRadius.circular(12)),
          child: Stack(
            children: [
              // Capa 1: fondo
              Center(
                child: Text(
                  'Capa de fondo',
                  style: Theme.of(context)
                      .textTheme
                      .headlineSmall
                      ?.copyWith(color: c.onPrimaryContainer.withValues(alpha: 0.35)),
                ),
              ),
              // Capa 2: etiqueta que se mueve con animación
              AnimatedAlign(
                duration: const Duration(milliseconds: 300),
                curve: Curves.easeInOut,
                alignment: _posiciones[_elegida].$2,
                child: Padding(
                  padding: const EdgeInsets.all(12),
                  child: Container(
                    padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
                    decoration: BoxDecoration(color: c.primary, borderRadius: BorderRadius.circular(8)),
                    child: Row(
                      mainAxisSize: MainAxisSize.min,
                      spacing: 6,
                      children: [
                        Icon(Icons.star, size: 18, color: c.onPrimary),
                        Text('Capa superior', style: TextStyle(color: c.onPrimary)),
                      ],
                    ),
                  ),
                ),
              ),
            ],
          ),
        ),
        Respuesta('La capa superior está en: ${_posiciones[_elegida].$1}'),
      ],
    );
  }
}

// ---------- 3. Contenedor con desplazamiento ----------
class _ContenedorDesplazable extends StatefulWidget {
  const _ContenedorDesplazable();

  @override
  State<_ContenedorDesplazable> createState() => _ContenedorDesplazableState();
}

class _ContenedorDesplazableState extends State<_ContenedorDesplazable> {
  final _desplazamiento = ScrollController();
  int _porcentaje = 0;

  @override
  void initState() {
    super.initState();
    _desplazamiento.addListener(() {
      final maximo = _desplazamiento.position.maxScrollExtent;
      final nuevo = maximo > 0 ? (_desplazamiento.offset / maximo * 100).round().clamp(0, 100) : 0;
      if (nuevo != _porcentaje) setState(() => _porcentaje = nuevo.toInt());
    });
  }

  @override
  void dispose() {
    _desplazamiento.dispose();
    super.dispose();
  }

  void _irA(double posicion) => _desplazamiento.animateTo(
        posicion,
        duration: const Duration(milliseconds: 500),
        curve: Curves.easeInOut,
      );

  @override
  Widget build(BuildContext context) {
    return TarjetaDemo(
      nombre: 'Contenedor con desplazamiento vertical',
      descripcion: 'Cuando el contenido es más alto que el espacio disponible, permite '
          'recorrerlo deslizando el dedo. A diferencia de una lista, dibuja todo su contenido a la vez.',
      children: [
        Container(
          height: 180,
          decoration: _marco(context),
          child: SingleChildScrollView(
            controller: _desplazamiento,
            padding: const EdgeInsets.all(12),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              spacing: 8,
              children: [
                for (var i = 1; i <= 15; i++)
                  Text('Párrafo $i. Este texto forma parte de un contenido largo que no cabe completo en el recuadro.'),
              ],
            ),
          ),
        ),
        Wrap(
          spacing: 8,
          children: [
            OutlinedButton(onPressed: () => _irA(0), child: const Text('Inicio')),
            OutlinedButton(
              onPressed: () => _irA(_desplazamiento.position.maxScrollExtent),
              child: const Text('Final'),
            ),
          ],
        ),
        Respuesta('Recorrido: $_porcentaje %'),
      ],
    );
  }
}

// ---------- 4. Barra superior ----------
class _BarraSuperior extends StatefulWidget {
  const _BarraSuperior();

  @override
  State<_BarraSuperior> createState() => _BarraSuperiorState();
}

class _BarraSuperiorState extends State<_BarraSuperior> {
  bool _favorito = false;
  String _accion = 'Toca un ícono de la barra.';

  @override
  Widget build(BuildContext context) {
    final c = Theme.of(context).colorScheme;
    return TarjetaDemo(
      nombre: 'Barra superior con título y acciones',
      descripcion: 'Muestra el nombre de la pantalla y las acciones más usadas. Las '
          'acciones menos frecuentes se guardan en el menú de tres puntos.',
      children: [
        ClipRRect(
          borderRadius: BorderRadius.circular(12),
          child: AppBar(
            primary: false, // está dentro de una tarjeta: sin espacio para la barra de estado
            backgroundColor: c.primaryContainer,
            leading: IconButton(
              icon: const Icon(Icons.arrow_back),
              tooltip: 'Regresar',
              onPressed: () => setState(() => _accion = 'Acción: Regresar'),
            ),
            title: const Text('Mis notas'),
            actions: [
              IconButton(
                icon: const Icon(Icons.search),
                tooltip: 'Buscar',
                onPressed: () => setState(() => _accion = 'Acción: Buscar'),
              ),
              IconButton(
                isSelected: _favorito,
                icon: const Icon(Icons.favorite_border),
                selectedIcon: const Icon(Icons.favorite),
                tooltip: 'Favorito',
                onPressed: () => setState(() {
                  _favorito = !_favorito;
                  _accion = _favorito ? 'Acción: agregado a favoritos' : 'Acción: quitado de favoritos';
                }),
              ),
              PopupMenuButton<String>(
                tooltip: 'Más opciones',
                onSelected: (opcion) => setState(() => _accion = 'Menú: $opcion'),
                itemBuilder: (_) => [
                  for (final opcion in const ['Ordenar', 'Compartir', 'Ajustes'])
                    PopupMenuItem(value: opcion, child: Text(opcion)),
                ],
              ),
            ],
          ),
        ),
        Respuesta(_accion),
      ],
    );
  }
}

// ---------- 5. Barra de navegación inferior ----------
class _NavegacionInferior extends StatefulWidget {
  const _NavegacionInferior();

  @override
  State<_NavegacionInferior> createState() => _NavegacionInferiorState();
}

class _NavegacionInferiorState extends State<_NavegacionInferior> {
  static const _destinos = [
    ('Inicio', Icons.home),
    ('Buscar', Icons.search),
    ('Perfil', Icons.person),
  ];
  int _actual = 0;
  int _avisosPerfil = 2;

  @override
  Widget build(BuildContext context) {
    final tema = Theme.of(context);
    return TarjetaDemo(
      nombre: 'Barra de navegación inferior',
      descripcion: 'Permite cambiar entre las pantallas principales de una app con un '
          'toque. Se recomienda para 3 a 5 destinos.',
      children: [
        Container(
          height: 240,
          clipBehavior: Clip.antiAlias,
          decoration: _marco(context),
          child: Column(
            children: [
              Expanded(
                child: Center(
                  child: Column(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      Icon(_destinos[_actual].$2, size: 40, color: tema.colorScheme.primary),
                      Text('Pantalla: ${_destinos[_actual].$1}', style: tema.textTheme.titleMedium),
                    ],
                  ),
                ),
              ),
              // Se quita el margen inferior del sistema porque está dentro de una tarjeta
              MediaQuery.removePadding(
                context: context,
                removeBottom: true,
                child: NavigationBar(
                  selectedIndex: _actual,
                  onDestinationSelected: (i) => setState(() {
                    _actual = i;
                    if (_destinos[i].$1 == 'Perfil') _avisosPerfil = 0;
                  }),
                  destinations: [
                    for (final (nombre, icono) in _destinos)
                      NavigationDestination(
                        label: nombre,
                        icon: Badge(
                          isLabelVisible: nombre == 'Perfil' && _avisosPerfil > 0,
                          label: Text('$_avisosPerfil'),
                          child: Icon(icono),
                        ),
                      ),
                  ],
                ),
              ),
            ],
          ),
        ),
      ],
    );
  }
}

// ---------- 6. Menú lateral ----------
class _MenuLateral extends StatefulWidget {
  const _MenuLateral();

  @override
  State<_MenuLateral> createState() => _MenuLateralState();
}

class _MenuLateralState extends State<_MenuLateral> {
  static const _opciones = [
    ('Inicio', Icons.home),
    ('Favoritos', Icons.favorite),
    ('Ajustes', Icons.settings),
  ];
  final _claveScaffold = GlobalKey<ScaffoldState>();
  int _actual = 0;

  @override
  Widget build(BuildContext context) {
    final tema = Theme.of(context);
    return TarjetaDemo(
      nombre: 'Menú lateral (drawer)',
      descripcion: 'Panel que se desliza desde el borde izquierdo con las secciones de la '
          'app. Se abre con el ícono de menú o arrastrando desde el borde.',
      children: [
        Container(
          height: 280,
          clipBehavior: Clip.antiAlias,
          decoration: _marco(context),
          // Un Scaffold dentro de la tarjeta: tiene su propio drawer
          child: MediaQuery.removePadding(
            context: context,
            removeTop: true,
            removeBottom: true,
            child: Scaffold(
              key: _claveScaffold,
              drawer: Drawer(
                width: 240,
                child: ListView(
                  padding: const EdgeInsets.symmetric(vertical: 12),
                  children: [
                    Padding(
                      padding: const EdgeInsets.fromLTRB(28, 8, 16, 12),
                      child: Text('Mi app', style: tema.textTheme.titleMedium),
                    ),
                    for (var i = 0; i < _opciones.length; i++)
                      Padding(
                        padding: const EdgeInsets.symmetric(horizontal: 12),
                        child: ListTile(
                          leading: Icon(_opciones[i].$2),
                          title: Text(_opciones[i].$1),
                          selected: _actual == i,
                          selectedTileColor: tema.colorScheme.secondaryContainer,
                          shape: const StadiumBorder(),
                          onTap: () {
                            setState(() => _actual = i);
                            _claveScaffold.currentState?.closeDrawer();
                          },
                        ),
                      ),
                  ],
                ),
              ),
              // Con drawer, la AppBar muestra sola el ícono ☰
              appBar: AppBar(
                primary: false,
                backgroundColor: tema.colorScheme.secondaryContainer,
                title: Text(_opciones[_actual].$1),
              ),
              body: Center(
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    Icon(_opciones[_actual].$2, size: 40, color: tema.colorScheme.primary),
                    const Text('Toca ☰ para cambiar de sección'),
                  ],
                ),
              ),
            ),
          ),
        ),
      ],
    );
  }
}

// ---------- 7. Distribución con pesos ----------
class _DistribucionPesos extends StatefulWidget {
  const _DistribucionPesos();

  @override
  State<_DistribucionPesos> createState() => _DistribucionPesosState();
}

class _DistribucionPesosState extends State<_DistribucionPesos> {
  int _pesoA = 1;
  int _pesoB = 2;
  static const _pesoC = 1;

  @override
  Widget build(BuildContext context) {
    final c = Theme.of(context).colorScheme;
    final total = _pesoA + _pesoB + _pesoC;

    Widget bloque(String nombre, int peso, Color fondo, Color texto) => Expanded(
          flex: peso, // el peso: parte proporcional del espacio
          child: AnimatedContainer(
            duration: const Duration(milliseconds: 250),
            alignment: Alignment.center,
            decoration: BoxDecoration(color: fondo, borderRadius: BorderRadius.circular(8)),
            child: Text(
              '$nombre\n${(peso / total * 100).round()} %',
              textAlign: TextAlign.center,
              style: TextStyle(color: texto, fontWeight: FontWeight.bold),
            ),
          ),
        );

    return TarjetaDemo(
      nombre: 'Distribución con pesos proporcionales',
      descripcion: 'Con Expanded(flex) cada elemento recibe una parte del espacio según su '
          'peso: uno con peso 2 ocupa el doble que uno con peso 1. Así la interfaz se adapta a cualquier ancho.',
      children: [
        SizedBox(
          height: 64,
          child: Row(
            spacing: 4,
            children: [
              bloque('A', _pesoA, c.primaryContainer, c.onPrimaryContainer),
              bloque('B', _pesoB, c.secondaryContainer, c.onSecondaryContainer),
              bloque('C', _pesoC, c.tertiaryContainer, c.onTertiaryContainer),
            ],
          ),
        ),
        Etiqueta('Peso de A: $_pesoA'),
        Slider(
          value: _pesoA.toDouble(),
          min: 1,
          max: 4,
          divisions: 3,
          label: '$_pesoA',
          onChanged: (v) => setState(() => _pesoA = v.round()),
        ),
        Etiqueta('Peso de B: $_pesoB'),
        Slider(
          value: _pesoB.toDouble(),
          min: 1,
          max: 4,
          divisions: 3,
          label: '$_pesoB',
          onChanged: (v) => setState(() => _pesoB = v.round()),
        ),
        Respuesta('C tiene peso fijo de 1. Total de pesos: $total'),
      ],
    );
  }
}
