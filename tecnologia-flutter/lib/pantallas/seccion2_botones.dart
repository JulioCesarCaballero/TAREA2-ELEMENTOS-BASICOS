import 'package:flutter/material.dart';

import '../componentes/tarjeta_demo.dart';

class Seccion2Botones extends StatelessWidget {
  const Seccion2Botones({super.key});

  @override
  Widget build(BuildContext context) {
    return const PantallaCatalogo(
      children: [
        _BotonesBasicos(),
        _BotonesConIcono(),
        _BotonesFlotantes(),
        _BotonesAlternancia(),
        _BotonesSegmentados(),
        _BotonDeshabilitado(),
        _BotonCargando(),
      ],
    );
  }
}

// ---------- 1. Relleno, contorno y texto ----------
class _BotonesBasicos extends StatefulWidget {
  const _BotonesBasicos();

  @override
  State<_BotonesBasicos> createState() => _BotonesBasicosState();
}

class _BotonesBasicosState extends State<_BotonesBasicos> {
  String _ultimo = 'Ninguno';
  int _total = 0;

  void _pulsar(String nombre) => setState(() {
        _ultimo = nombre;
        _total++;
      });

  @override
  Widget build(BuildContext context) {
    return TarjetaDemo(
      nombre: 'Botón relleno, con contorno y de texto',
      descripcion: 'Indican distinta importancia: el relleno es la acción principal, el de '
          'contorno una secundaria y el de texto la de menor énfasis. El tonal y el elevado '
          'son variantes intermedias.',
      children: [
        // Wrap acomoda los botones y los pasa a otra línea si no caben
        Wrap(
          spacing: 8,
          runSpacing: 8,
          children: [
            FilledButton(onPressed: () => _pulsar('Relleno'), child: const Text('Relleno')),
            OutlinedButton(onPressed: () => _pulsar('Con contorno'), child: const Text('Con contorno')),
            TextButton(onPressed: () => _pulsar('De texto'), child: const Text('De texto')),
            FilledButton.tonal(onPressed: () => _pulsar('Tonal'), child: const Text('Tonal')),
            ElevatedButton(onPressed: () => _pulsar('Elevado'), child: const Text('Elevado')),
          ],
        ),
        Respuesta('Último pulsado: $_ultimo · Total: $_total'),
      ],
    );
  }
}

// ---------- 2. Botones con ícono ----------
class _BotonesConIcono extends StatefulWidget {
  const _BotonesConIcono();

  @override
  State<_BotonesConIcono> createState() => _BotonesConIconoState();
}

class _BotonesConIconoState extends State<_BotonesConIcono> {
  String _mensaje = 'Pulsa un botón.';

  void _mostrar(String texto) => setState(() => _mensaje = texto);

  @override
  Widget build(BuildContext context) {
    return TarjetaDemo(
      nombre: 'Botones con ícono',
      descripcion: 'Los de solo ícono ahorran espacio en acciones conocidas como compartir '
          'o borrar. Con ícono y texto, la acción es más clara para el usuario.',
      children: [
        const Etiqueta('Solo ícono'),
        Row(
          spacing: 8,
          children: [
            IconButton(
              icon: const Icon(Icons.share),
              tooltip: 'Compartir',
              onPressed: () => _mostrar('Compartir (botón de ícono estándar)'),
            ),
            IconButton.filled(
              icon: const Icon(Icons.edit),
              tooltip: 'Editar',
              onPressed: () => _mostrar('Editar (botón de ícono relleno)'),
            ),
            IconButton.outlined(
              icon: const Icon(Icons.delete),
              tooltip: 'Eliminar',
              onPressed: () => _mostrar('Eliminar (botón de ícono con contorno)'),
            ),
          ],
        ),
        const Etiqueta('Ícono y texto'),
        Wrap(
          spacing: 8,
          runSpacing: 8,
          children: [
            FilledButton.icon(
              onPressed: () => _mostrar('Mensaje enviado'),
              icon: const Icon(Icons.send),
              label: const Text('Enviar'),
            ),
            OutlinedButton.icon(
              onPressed: () => _mostrar('Descarga iniciada'),
              icon: const Icon(Icons.download),
              label: const Text('Descargar'),
            ),
          ],
        ),
        Respuesta(_mensaje),
      ],
    );
  }
}

// ---------- 3. Botones de acción flotante ----------
class _BotonesFlotantes extends StatefulWidget {
  const _BotonesFlotantes();

  @override
  State<_BotonesFlotantes> createState() => _BotonesFlotantesState();
}

class _BotonesFlotantesState extends State<_BotonesFlotantes> {
  int _tareas = 0;
  int _notas = 0;
  bool _extendido = true;

  @override
  Widget build(BuildContext context) {
    final tema = Theme.of(context);
    return TarjetaDemo(
      nombre: 'Botón de acción flotante (FAB)',
      descripcion: 'Representa la acción más importante de una pantalla y flota sobre el '
          'contenido. La versión extendida añade texto y puede contraerse a solo ícono.',
      children: [
        // Recuadro que simula una pantalla; Stack permite colocar los FAB en las esquinas
        Container(
          height: 190,
          padding: const EdgeInsets.all(16),
          decoration: BoxDecoration(
            color: tema.colorScheme.surfaceContainerHighest,
            borderRadius: BorderRadius.circular(12),
          ),
          child: Stack(
            children: [
              Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text('Tareas creadas: $_tareas', style: tema.textTheme.bodyLarge),
                  Text('Notas creadas: $_notas', style: tema.textTheme.bodyLarge),
                  TextButton(
                    onPressed: () => setState(() => _extendido = !_extendido),
                    child: Text(_extendido ? 'Contraer FAB extendido' : 'Expandir FAB extendido'),
                  ),
                ],
              ),
              Positioned(
                left: 0,
                bottom: 0,
                child: FloatingActionButton.extended(
                  heroTag: null, // evita conflictos al haber varios FAB
                  isExtended: _extendido,
                  onPressed: () => setState(() => _notas++),
                  icon: const Icon(Icons.edit),
                  label: const Text('Nueva nota'),
                ),
              ),
              Positioned(
                right: 0,
                bottom: 0,
                child: FloatingActionButton(
                  heroTag: null,
                  tooltip: 'Nueva tarea',
                  onPressed: () => setState(() => _tareas++),
                  child: const Icon(Icons.add),
                ),
              ),
            ],
          ),
        ),
      ],
    );
  }
}

// ---------- 4. Alternancia ----------
class _BotonesAlternancia extends StatefulWidget {
  const _BotonesAlternancia();

  @override
  State<_BotonesAlternancia> createState() => _BotonesAlternanciaState();
}

class _BotonesAlternanciaState extends State<_BotonesAlternancia> {
  bool _favorito = false;
  bool _guardado = false;

  @override
  Widget build(BuildContext context) {
    return TarjetaDemo(
      nombre: 'Botón de alternancia (toggle)',
      descripcion: 'Cambia entre dos estados, activado y desactivado, con cada pulsación. '
          'Se usa para marcar favoritos, guardar elementos o activar una opción.',
      children: [
        Row(
          spacing: 8,
          children: [
            // isSelected + selectedIcon: el botón cambia de ícono según su estado
            IconButton(
              isSelected: _favorito,
              icon: const Icon(Icons.favorite_border),
              selectedIcon: Icon(Icons.favorite, color: Theme.of(context).colorScheme.error),
              tooltip: 'Favorito',
              onPressed: () => setState(() => _favorito = !_favorito),
            ),
            IconButton(
              isSelected: _guardado,
              icon: const Icon(Icons.bookmark_border),
              selectedIcon: const Icon(Icons.bookmark),
              tooltip: 'Guardar',
              onPressed: () => setState(() => _guardado = !_guardado),
            ),
          ],
        ),
        Respuesta('Favorito: ${_favorito ? 'sí' : 'no'} · Guardado: ${_guardado ? 'sí' : 'no'}'),
      ],
    );
  }
}

// ---------- 5. Selector segmentado ----------
enum _Formato { negrita, cursiva, subrayado }

class _BotonesSegmentados extends StatefulWidget {
  const _BotonesSegmentados();

  @override
  State<_BotonesSegmentados> createState() => _BotonesSegmentadosState();
}

class _BotonesSegmentadosState extends State<_BotonesSegmentados> {
  static const _vistas = ['Lista', 'Cuadrícula', 'Mosaico'];
  static const _iconosVista = [Icons.view_agenda, Icons.grid_view, Icons.view_module];

  int _vista = 0;
  Set<_Formato> _formato = {};

  @override
  Widget build(BuildContext context) {
    return TarjetaDemo(
      nombre: 'Selector segmentado',
      descripcion: 'Agrupa opciones relacionadas en una sola barra. Puede permitir elegir '
          'solo una opción, como el modo de vista, o varias a la vez, como el formato de texto.',
      children: [
        const Etiqueta('Una sola opción'),
        SizedBox(
          width: double.infinity,
          child: SegmentedButton<int>(
            segments: [
              for (var i = 0; i < _vistas.length; i++)
                ButtonSegment(value: i, label: Text(_vistas[i]), icon: Icon(_iconosVista[i])),
            ],
            selected: {_vista},
            onSelectionChanged: (seleccion) => setState(() => _vista = seleccion.first),
          ),
        ),
        Respuesta('Vista seleccionada: ${_vistas[_vista]}'),
        const SizedBox(height: 8),
        const Etiqueta('Varias opciones'),
        SizedBox(
          width: double.infinity,
          child: SegmentedButton<_Formato>(
            multiSelectionEnabled: true,
            emptySelectionAllowed: true,
            showSelectedIcon: false,
            segments: const [
              ButtonSegment(value: _Formato.negrita, icon: Icon(Icons.format_bold), tooltip: 'Negrita'),
              ButtonSegment(value: _Formato.cursiva, icon: Icon(Icons.format_italic), tooltip: 'Cursiva'),
              ButtonSegment(
                value: _Formato.subrayado,
                icon: Icon(Icons.format_underlined),
                tooltip: 'Subrayado',
              ),
            ],
            selected: _formato,
            onSelectionChanged: (seleccion) => setState(() => _formato = seleccion),
          ),
        ),
        Text(
          'Este texto cambia con el formato elegido.',
          style: Theme.of(context).textTheme.bodyLarge?.copyWith(
                fontWeight: _formato.contains(_Formato.negrita) ? FontWeight.bold : FontWeight.normal,
                fontStyle: _formato.contains(_Formato.cursiva) ? FontStyle.italic : FontStyle.normal,
                decoration:
                    _formato.contains(_Formato.subrayado) ? TextDecoration.underline : TextDecoration.none,
              ),
        ),
      ],
    );
  }
}

// ---------- 6. Botón deshabilitado ----------
class _BotonDeshabilitado extends StatefulWidget {
  const _BotonDeshabilitado();

  @override
  State<_BotonDeshabilitado> createState() => _BotonDeshabilitadoState();
}

class _BotonDeshabilitadoState extends State<_BotonDeshabilitado> {
  bool _acepta = false;
  bool _registrado = false;

  @override
  Widget build(BuildContext context) {
    return TarjetaDemo(
      nombre: 'Botón deshabilitado',
      descripcion: 'Se muestra atenuado y no responde mientras falte un requisito. Aquí el '
          'botón se habilita solo cuando aceptas los términos.',
      children: [
        CheckboxListTile(
          contentPadding: EdgeInsets.zero,
          controlAffinity: ListTileControlAffinity.leading,
          value: _acepta,
          title: const Text('Acepto los términos y condiciones'),
          onChanged: (v) => setState(() {
            _acepta = v ?? false;
            _registrado = false;
          }),
        ),
        // onPressed: null deshabilita el botón
        FilledButton(
          onPressed: _acepta ? () => setState(() => _registrado = true) : null,
          child: const Text('Crear cuenta'),
        ),
        Respuesta(
          _registrado
              ? 'Cuenta creada correctamente.'
              : _acepta
                  ? 'El botón ya está habilitado.'
                  : 'El botón está deshabilitado: marca la casilla.',
        ),
      ],
    );
  }
}

// ---------- 7. Botón en estado de carga ----------
class _BotonCargando extends StatefulWidget {
  const _BotonCargando();

  @override
  State<_BotonCargando> createState() => _BotonCargandoState();
}

class _BotonCargandoState extends State<_BotonCargando> {
  bool _cargando = false;
  int _envios = 0;

  Future<void> _enviar() async {
    setState(() => _cargando = true);
    await Future.delayed(const Duration(seconds: 2)); // simula una operación de red
    if (!mounted) return; // la pantalla pudo cerrarse mientras tanto
    setState(() {
      _cargando = false;
      _envios++;
    });
  }

  @override
  Widget build(BuildContext context) {
    return TarjetaDemo(
      nombre: 'Botón en estado de carga',
      descripcion: 'Mientras una operación está en curso, el botón muestra un indicador de '
          'progreso y se bloquea para evitar que se pulse dos veces.',
      children: [
        FilledButton.icon(
          onPressed: _cargando ? null : _enviar,
          icon: _cargando
              ? const SizedBox(
                  width: 18,
                  height: 18,
                  child: CircularProgressIndicator(strokeWidth: 2),
                )
              : const Icon(Icons.send),
          label: Text(_cargando ? 'Enviando…' : 'Enviar formulario'),
        ),
        Respuesta(
          _cargando
              ? 'Enviando datos, espera…'
              : _envios == 0
                  ? 'Pulsa el botón para simular un envío.'
                  : 'Formulario enviado ($_envios ${_envios == 1 ? 'vez' : 'veces'}).',
        ),
      ],
    );
  }
}
