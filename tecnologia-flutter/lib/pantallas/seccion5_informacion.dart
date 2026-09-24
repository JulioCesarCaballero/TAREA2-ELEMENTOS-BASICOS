import 'dart:async';
import 'dart:math' as math;

import 'package:flutter/material.dart';

import '../componentes/tarjeta_demo.dart';
import '../datos/catalogo_estado.dart';

class Seccion5Informacion extends StatelessWidget {
  const Seccion5Informacion({super.key});

  @override
  Widget build(BuildContext context) {
    final estado = EstadoScope.of(context);

    // Conexión con la Sección 3: todos los textos de esta sección se escalan
    // con el valor elegido en el deslizador.
    return _ConEscala(
      escala: estado.escalaTexto,
      child: const PantallaCatalogo(
        children: [
          _AvisoEscala(),
          _EstilosTexto(),
          _Imagenes(),
          _IndicadoresProgreso(),
          _ToastYSnackbar(),
          _DialogoConfirmacion(),
          _HojaInferior(),
          _TarjetasSeparadoresBadges(),
        ],
      ),
    );
  }
}

/// Multiplica el tamaño de todos los textos que están dentro de [child].
class _ConEscala extends StatelessWidget {
  const _ConEscala({required this.escala, required this.child});

  final double escala;
  final Widget child;

  @override
  Widget build(BuildContext context) {
    final datos = MediaQuery.of(context);
    return MediaQuery(
      data: datos.copyWith(textScaler: TextScaler.linear(datos.textScaler.scale(1) * escala)),
      child: child,
    );
  }
}

// ---------- Aviso de conexión con la Sección 3 ----------
class _AvisoEscala extends StatelessWidget {
  const _AvisoEscala();

  @override
  Widget build(BuildContext context) {
    final porcentaje = (EstadoScope.of(context).escalaTexto * 100).round();
    return Card.filled(
      margin: EdgeInsets.zero,
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Row(
          spacing: 12,
          children: [
            const Icon(Icons.text_fields),
            Expanded(
              child: Text(
                'Tamaño de texto: $porcentaje %. Cámbialo con el deslizador de la sección '
                'Elementos de selección.',
              ),
            ),
          ],
        ),
      ),
    );
  }
}

// ---------- 1. Estilos de texto ----------
enum _Enfasis { negrita, cursiva, subrayado, tachado, color }

class _EstilosTexto extends StatefulWidget {
  const _EstilosTexto();

  @override
  State<_EstilosTexto> createState() => _EstilosTextoState();
}

class _EstilosTextoState extends State<_EstilosTexto> {
  static const _nombres = {
    _Enfasis.negrita: 'Negrita',
    _Enfasis.cursiva: 'Cursiva',
    _Enfasis.subrayado: 'Subrayado',
    _Enfasis.tachado: 'Tachado',
    _Enfasis.color: 'Color',
  };

  final _controlador = TextEditingController(text: 'Hola, mundo');
  final _enfasis = <_Enfasis>{_Enfasis.negrita};
  bool _expandido = false;

  @override
  void dispose() {
    _controlador.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final tema = Theme.of(context);
    final texto = _controlador.text.trim().isEmpty ? 'Texto de ejemplo' : _controlador.text;

    final decoraciones = [
      if (_enfasis.contains(_Enfasis.subrayado)) TextDecoration.underline,
      if (_enfasis.contains(_Enfasis.tachado)) TextDecoration.lineThrough,
    ];

    return TarjetaDemo(
      nombre: 'Textos con distintos estilos',
      descripcion: 'El tamaño, el grosor y el color crean jerarquía: indican qué leer '
          'primero. El énfasis resalta palabras clave. Escribe un texto y cambia su énfasis.',
      children: [
        TextField(
          controller: _controlador,
          maxLength: 30,
          decoration: decoracion(etiqueta: 'Texto de ejemplo'),
          onChanged: (_) => setState(() {}),
        ),
        const Etiqueta('Tamaños'),
        Text(texto, style: tema.textTheme.headlineMedium),
        Text(texto, style: tema.textTheme.titleLarge),
        Text(texto, style: tema.textTheme.bodyLarge),
        Text(texto, style: tema.textTheme.labelSmall),
        const Divider(),
        const Etiqueta('Énfasis'),
        Wrap(
          spacing: 8,
          runSpacing: 4,
          children: [
            for (final e in _Enfasis.values)
              FilterChip(
                label: Text(_nombres[e]!),
                selected: _enfasis.contains(e),
                onSelected: (activo) => setState(() => activo ? _enfasis.add(e) : _enfasis.remove(e)),
              ),
          ],
        ),
        // Text.rich + TextSpan: aplica estilos solo a una parte del texto
        Text.rich(
          TextSpan(
            text: 'Resultado: ',
            children: [
              TextSpan(
                text: texto,
                style: TextStyle(
                  fontWeight: _enfasis.contains(_Enfasis.negrita) ? FontWeight.bold : FontWeight.normal,
                  fontStyle: _enfasis.contains(_Enfasis.cursiva) ? FontStyle.italic : FontStyle.normal,
                  decoration: TextDecoration.combine(decoraciones),
                  color: _enfasis.contains(_Enfasis.color) ? tema.colorScheme.error : null,
                ),
              ),
            ],
          ),
          style: tema.textTheme.titleMedium,
        ),
        const Divider(),
        const Etiqueta('Texto largo'),
        GestureDetector(
          onTap: () => setState(() => _expandido = !_expandido),
          child: Text(
            'Este párrafo es demasiado largo para caber en una sola línea. Cuando está '
            'contraído se corta con puntos suspensivos; al expandirlo se muestra completo '
            'y ocupa todas las líneas que necesite.',
            maxLines: _expandido ? null : 1,
            overflow: _expandido ? TextOverflow.visible : TextOverflow.ellipsis,
            style: tema.textTheme.bodyMedium,
          ),
        ),
        TextButton(
          onPressed: () => setState(() => _expandido = !_expandido),
          child: Text(_expandido ? 'Ver menos' : 'Ver más'),
        ),
      ],
    );
  }
}

// ---------- 2. Imágenes ----------
class _Imagenes extends StatefulWidget {
  const _Imagenes();

  @override
  State<_Imagenes> createState() => _ImagenesState();
}

class _ImagenesState extends State<_Imagenes> {
  static const _modos = [
    ('Ajustar', BoxFit.contain, 'contain: se ve completa, puede dejar espacios vacíos.'),
    ('Recortar', BoxFit.cover, 'cover: llena el espacio y recorta lo que sobra.'),
    ('Estirar', BoxFit.fill, 'fill: llena el espacio deformando la imagen.'),
    ('Original', BoxFit.none, 'none: tamaño real, sin escalar.'),
  ];

  int _modo = 0;

  Widget _marco(Widget imagen) {
    return AspectRatio(
      aspectRatio: 1,
      child: ClipRRect(
        borderRadius: BorderRadius.circular(12),
        child: ColoredBox(
          color: Theme.of(context).colorScheme.surfaceContainerHighest,
          child: imagen,
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    final ajuste = _modos[_modo].$2;
    final estiloEtiqueta = Theme.of(context).textTheme.labelMedium;

    return TarjetaDemo(
      nombre: 'Imagen local y desde URL',
      descripcion: 'La imagen local viene dentro de la app; la otra se descarga de '
          'internet. El modo de escalado decide cómo se acomodan en su espacio.',
      children: [
        Wrap(
          spacing: 8,
          runSpacing: 4,
          children: [
            for (var i = 0; i < _modos.length; i++)
              ChoiceChip(
                label: Text(_modos[i].$1),
                selected: _modo == i,
                onSelected: (_) => setState(() => _modo = i),
              ),
          ],
        ),
        Row(
          spacing: 12,
          children: [
            Expanded(
              child: Column(
                spacing: 4,
                children: [
                  _marco(
                    Image.asset(
                      'assets/paisaje_local.png',
                      fit: ajuste,
                      semanticLabel: 'Paisaje de montañas (imagen local)',
                    ),
                  ),
                  Text('Local', style: estiloEtiqueta),
                ],
              ),
            ),
            Expanded(
              child: Column(
                spacing: 4,
                children: [
                  _marco(
                    Image.network(
                      'https://picsum.photos/id/1018/800/500',
                      fit: ajuste,
                      semanticLabel: 'Paisaje descargado de internet',
                      // Mientras descarga muestra un indicador de progreso
                      loadingBuilder: (context, hijo, progreso) => progreso == null
                          ? hijo
                          : const Center(child: CircularProgressIndicator()),
                      // Si falla, muestra un aviso en lugar de la imagen
                      errorBuilder: (context, _, __) => const Center(
                        child: Padding(
                          padding: EdgeInsets.all(8),
                          child: Text('No se pudo cargar: revisa la conexión.', textAlign: TextAlign.center),
                        ),
                      ),
                    ),
                  ),
                  Text('Desde URL', style: estiloEtiqueta),
                ],
              ),
            ),
          ],
        ),
        Respuesta(_modos[_modo].$3),
      ],
    );
  }
}

// ---------- 3. Indicadores de progreso ----------
class _IndicadoresProgreso extends StatefulWidget {
  const _IndicadoresProgreso();

  @override
  State<_IndicadoresProgreso> createState() => _IndicadoresProgresoState();
}

class _IndicadoresProgresoState extends State<_IndicadoresProgreso> {
  int _progreso = 30;
  bool _simulando = false;
  bool _cargaActiva = true;
  Timer? _temporizador;

  @override
  void dispose() {
    _temporizador?.cancel();
    super.dispose();
  }

  void _simular() {
    setState(() {
      _simulando = true;
      _progreso = 0;
    });
    _temporizador = Timer.periodic(const Duration(milliseconds: 150), (t) {
      setState(() {
        _progreso = math.min(100, _progreso + 5);
        if (_progreso >= 100) {
          t.cancel();
          _simulando = false;
        }
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    return TarjetaDemo(
      nombre: 'Indicadores de progreso',
      descripcion: 'El modo determinado muestra cuánto falta para terminar una tarea. El '
          'indeterminado solo indica que algo está en proceso, sin saber cuánto tardará.',
      children: [
        Etiqueta('Determinado: $_progreso %'),
        // TweenAnimationBuilder anima el cambio de valor
        TweenAnimationBuilder<double>(
          tween: Tween(end: _progreso / 100),
          duration: const Duration(milliseconds: 250),
          builder: (context, valor, _) => Row(
            spacing: 16,
            children: [
              Expanded(child: LinearProgressIndicator(value: valor)),
              CircularProgressIndicator(value: valor),
            ],
          ),
        ),
        Wrap(
          spacing: 8,
          runSpacing: 8,
          children: [
            OutlinedButton(
              onPressed: _simulando ? null : () => setState(() => _progreso = math.max(0, _progreso - 10)),
              child: const Text('−10 %'),
            ),
            OutlinedButton(
              onPressed: _simulando ? null : () => setState(() => _progreso = math.min(100, _progreso + 10)),
              child: const Text('+10 %'),
            ),
            FilledButton.tonal(onPressed: _simulando ? null : _simular, child: const Text('Simular')),
          ],
        ),
        SwitchListTile(
          contentPadding: EdgeInsets.zero,
          title: const Etiqueta('Indeterminado: carga en curso'),
          value: _cargaActiva,
          onChanged: (v) => setState(() => _cargaActiva = v),
        ),
        if (_cargaActiva)
          const Row(
            spacing: 16,
            children: [
              Expanded(child: LinearProgressIndicator()),
              CircularProgressIndicator(),
            ],
          )
        else
          const Respuesta('Carga detenida. Activa el interruptor para ver los indicadores.'),
      ],
    );
  }
}

// ---------- 4. Toast y snackbar ----------
class _ToastYSnackbar extends StatefulWidget {
  const _ToastYSnackbar();

  @override
  State<_ToastYSnackbar> createState() => _ToastYSnackbarState();
}

class _ToastYSnackbarState extends State<_ToastYSnackbar> {
  int _archivados = 0;

  /// Flutter no tiene un Toast nativo: se construye con un Overlay,
  /// una capa que se dibuja encima de toda la app y se quita sola.
  void _mostrarToast(String mensaje) {
    final capa = Overlay.of(context);
    final colores = Theme.of(context).colorScheme;
    final entrada = OverlayEntry(
      builder: (_) => Positioned(
        left: 24,
        right: 24,
        bottom: 96,
        child: IgnorePointer(
          child: Center(
            child: Material(
              color: colores.inverseSurface,
              borderRadius: BorderRadius.circular(24),
              child: Padding(
                padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 12),
                child: Text(mensaje, style: TextStyle(color: colores.onInverseSurface)),
              ),
            ),
          ),
        ),
      ),
    );
    capa.insert(entrada);
    Timer(const Duration(seconds: 2), entrada.remove);
  }

  void _archivar() {
    setState(() => _archivados++);
    final mensajero = ScaffoldMessenger.of(context);
    mensajero.hideCurrentSnackBar();
    mensajero.showSnackBar(
      SnackBar(
        content: const Text('Mensaje archivado'),
        behavior: SnackBarBehavior.floating,
        action: SnackBarAction(
          label: 'Deshacer',
          onPressed: () {
            if (mounted) setState(() => _archivados = math.max(0, _archivados - 1));
          },
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return TarjetaDemo(
      nombre: 'Toast y snackbar',
      descripcion: 'El toast es un aviso breve que desaparece solo. El snackbar aparece en '
          'la parte inferior de la app y puede incluir una acción, como deshacer.',
      children: [
        Wrap(
          spacing: 8,
          runSpacing: 8,
          children: [
            OutlinedButton(onPressed: () => _mostrarToast('Esto es un toast'), child: const Text('Mostrar toast')),
            FilledButton(onPressed: _archivar, child: const Text('Archivar')),
          ],
        ),
        Respuesta('Mensajes archivados: $_archivados'),
      ],
    );
  }
}

// ---------- 5. Diálogo de confirmación (conectado con la Sección 3) ----------
class _DialogoConfirmacion extends StatefulWidget {
  const _DialogoConfirmacion();

  @override
  State<_DialogoConfirmacion> createState() => _DialogoConfirmacionState();
}

class _DialogoConfirmacionState extends State<_DialogoConfirmacion> {
  String _resultado = 'Aún no has abierto el diálogo.';

  Future<void> _abrir() async {
    final estado = EstadoScope.leer(context);
    // showDialog devuelve lo que se pase a Navigator.pop: true, false o null
    final confirmado = await showDialog<bool>(
      context: context,
      builder: (contexto) => AlertDialog(
        title: const Text('¿Restablecer el tamaño?'),
        content: const Text(
          'Los textos volverán al 100 %. Puedes cambiarlo de nuevo en Elementos de selección.',
        ),
        actions: [
          TextButton(onPressed: () => Navigator.pop(contexto, false), child: const Text('Cancelar')),
          TextButton(onPressed: () => Navigator.pop(contexto, true), child: const Text('Restablecer')),
        ],
      ),
    );
    if (!mounted) return;
    setState(() {
      switch (confirmado) {
        case true:
          estado.cambiarEscalaTexto(1.0);
          _resultado = 'Confirmado: el tamaño volvió al 100 %.';
        case false:
          _resultado = 'Cancelado: no se hizo ningún cambio.';
        case null:
          _resultado = 'Diálogo cerrado sin elegir.';
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return TarjetaDemo(
      nombre: 'Diálogo de confirmación',
      descripcion: 'Interrumpe al usuario para confirmar una acción importante antes de '
          'realizarla. Aquí restablece el tamaño de texto que se eligió en la Sección 3.',
      children: [
        FilledButton(onPressed: _abrir, child: const Text('Restablecer tamaño de texto')),
        Respuesta(_resultado),
      ],
    );
  }
}

// ---------- 6. Hoja inferior ----------
class _HojaInferior extends StatefulWidget {
  const _HojaInferior();

  @override
  State<_HojaInferior> createState() => _HojaInferiorState();
}

class _HojaInferiorState extends State<_HojaInferior> {
  String _elegido = 'Ninguna opción elegida.';

  Future<void> _abrir() async {
    final escala = EstadoScope.leer(context).escalaTexto;
    final opcion = await showModalBottomSheet<String>(
      context: context,
      showDragHandle: true, // barrita para arrastrar
      builder: (contexto) => _ConEscala(
        escala: escala,
        child: SafeArea(
          child: Column(
            mainAxisSize: MainAxisSize.min,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Padding(
                padding: const EdgeInsets.fromLTRB(24, 0, 24, 8),
                child: Text('¿Qué quieres hacer?', style: Theme.of(contexto).textTheme.titleMedium),
              ),
              for (final (texto, icono) in const [
                ('Compartir', Icons.share),
                ('Copiar enlace', Icons.link),
                ('Descargar', Icons.download),
              ])
                ListTile(
                  leading: Icon(icono),
                  title: Text(texto),
                  contentPadding: const EdgeInsets.symmetric(horizontal: 24),
                  onTap: () => Navigator.pop(contexto, texto),
                ),
              const SizedBox(height: 8),
            ],
          ),
        ),
      ),
    );
    if (opcion != null && mounted) setState(() => _elegido = 'Elegiste: $opcion');
  }

  @override
  Widget build(BuildContext context) {
    return TarjetaDemo(
      nombre: 'Hoja inferior (bottom sheet)',
      descripcion: 'Panel que sube desde la parte inferior con opciones o contenido '
          'adicional. Se cierra al deslizarla hacia abajo o al tocar fuera de ella.',
      children: [
        FilledButton(onPressed: _abrir, child: const Text('Abrir hoja inferior')),
        Respuesta(_elegido),
      ],
    );
  }
}

// ---------- 7. Tarjeta, separador y badge ----------
class _TarjetasSeparadoresBadges extends StatefulWidget {
  const _TarjetasSeparadoresBadges();

  @override
  State<_TarjetasSeparadoresBadges> createState() => _TarjetasSeparadoresBadgesState();
}

class _TarjetasSeparadoresBadgesState extends State<_TarjetasSeparadoresBadges> {
  int _toques = 0;
  int _avisos = 3;
  int _carrito = 0;
  bool _correoNuevo = true;

  Widget _tarjeta(Card Function(Widget hijo) crear, String texto) {
    return Expanded(
      child: crear(
        InkWell(
          onTap: () => setState(() => _toques++),
          child: Padding(padding: const EdgeInsets.all(12), child: Text(texto)),
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return TarjetaDemo(
      nombre: 'Tarjeta, separador y distintivo (badge)',
      descripcion: 'La tarjeta agrupa información relacionada. El separador divide '
          'contenido con una línea. El badge muestra un número o punto sobre un ícono para avisar novedades.',
      children: [
        const Etiqueta('Tarjetas'),
        Row(
          spacing: 8,
          children: [
            _tarjeta((h) => Card.filled(margin: EdgeInsets.zero, clipBehavior: Clip.antiAlias, child: h), 'Rellena'),
            _tarjeta((h) => Card(margin: EdgeInsets.zero, clipBehavior: Clip.antiAlias, child: h), 'Elevada'),
            _tarjeta(
              (h) => Card.outlined(margin: EdgeInsets.zero, clipBehavior: Clip.antiAlias, child: h),
              'Contorno',
            ),
          ],
        ),
        Respuesta('Tarjetas tocadas: $_toques'),
        const Divider(),
        const Etiqueta('Separador vertical'),
        // IntrinsicHeight da a los separadores verticales la altura del texto
        const IntrinsicHeight(
          child: Row(
            children: [
              Text('Inicio'),
              VerticalDivider(width: 24),
              Text('Perfil'),
              VerticalDivider(width: 24),
              Text('Ajustes'),
            ],
          ),
        ),
        const Divider(),
        const Etiqueta('Distintivos (badges)'),
        Row(
          spacing: 16,
          children: [
            Padding(
              padding: const EdgeInsets.all(12),
              child: Badge(
                isLabelVisible: _avisos > 0,
                label: Text(_avisos > 99 ? '99+' : '$_avisos'),
                child: Icon(Icons.notifications, semanticLabel: 'Notificaciones: $_avisos'),
              ),
            ),
            Padding(
              padding: const EdgeInsets.all(12),
              child: Badge(
                isLabelVisible: _carrito > 0,
                label: Text('$_carrito'),
                child: Icon(Icons.shopping_cart, semanticLabel: 'Carrito: $_carrito'),
              ),
            ),
            // Toca el sobre para marcar o desmarcar el correo como nuevo
            IconButton(
              tooltip: 'Correo',
              onPressed: () => setState(() => _correoNuevo = !_correoNuevo),
              icon: Badge(isLabelVisible: _correoNuevo, child: const Icon(Icons.mail)),
            ),
          ],
        ),
        Wrap(
          spacing: 8,
          runSpacing: 8,
          children: [
            OutlinedButton(onPressed: () => setState(() => _avisos++), child: const Text('+ Aviso')),
            OutlinedButton(onPressed: () => setState(() => _carrito++), child: const Text('+ Carrito')),
            TextButton(
              onPressed: () => setState(() {
                _avisos = 0;
                _carrito = 0;
                _correoNuevo = false;
              }),
              child: const Text('Limpiar'),
            ),
          ],
        ),
      ],
    );
  }
}
