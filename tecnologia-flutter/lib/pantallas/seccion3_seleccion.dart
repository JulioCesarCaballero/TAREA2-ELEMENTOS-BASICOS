// En Flutter 3.35+ RadioListTile recomienda el nuevo widget RadioGroup; se usa la forma
// clásica (groupValue/onChanged) para que funcione también en versiones anteriores.
// ignore_for_file: deprecated_member_use

import 'package:flutter/material.dart';

import '../componentes/tarjeta_demo.dart';
import '../datos/catalogo_estado.dart';

class Seccion3Seleccion extends StatelessWidget {
  const Seccion3Seleccion({super.key});

  @override
  Widget build(BuildContext context) {
    return const PantallaCatalogo(
      children: [
        _Casillas(),
        _BotonesOpcion(),
        _Interruptores(),
        _DeslizadorUnico(),
        _DeslizadorRango(),
        _ListaDesplegable(),
        _SelectorFecha(),
        _SelectorHora(),
        _ChipsFiltro(),
      ],
    );
  }
}

// ---------- 1. Casillas (con estado indeterminado) ----------
class _Casillas extends StatefulWidget {
  const _Casillas();

  @override
  State<_Casillas> createState() => _CasillasState();
}

class _CasillasState extends State<_Casillas> {
  static const _productos = ['Leche', 'Pan', 'Huevos'];
  final _marcados = [true, false, false];

  @override
  Widget build(BuildContext context) {
    final cantidad = _marcados.where((m) => m).length;
    // null = indeterminado (solo algunas marcadas)
    final bool? estadoPrincipal = cantidad == _productos.length
        ? true
        : cantidad == 0
            ? false
            : null;
    final textoEstado = switch (estadoPrincipal) {
      true => 'todas marcadas',
      false => 'ninguna marcada',
      null => 'indeterminado',
    };

    return TarjetaDemo(
      nombre: 'Casilla de verificación',
      descripcion: 'Permite marcar o desmarcar opciones independientes. La casilla '
          'principal queda indeterminada (con una raya) cuando solo algunas están marcadas.',
      children: [
        CheckboxListTile(
          contentPadding: EdgeInsets.zero,
          controlAffinity: ListTileControlAffinity.leading,
          tristate: true,
          value: estadoPrincipal,
          title: const Text('Seleccionar todo', style: TextStyle(fontWeight: FontWeight.bold)),
          onChanged: (_) => setState(() {
            final marcar = estadoPrincipal != true;
            for (var i = 0; i < _marcados.length; i++) {
              _marcados[i] = marcar;
            }
          }),
        ),
        for (var i = 0; i < _productos.length; i++)
          CheckboxListTile(
            contentPadding: const EdgeInsets.only(left: 32),
            controlAffinity: ListTileControlAffinity.leading,
            value: _marcados[i],
            title: Text(_productos[i]),
            onChanged: (v) => setState(() => _marcados[i] = v ?? false),
          ),
        Respuesta('Marcados: $cantidad de ${_productos.length} · Estado: $textoEstado'),
      ],
    );
  }
}

// ---------- 2. Botones de opción ----------
class _BotonesOpcion extends StatefulWidget {
  const _BotonesOpcion();

  @override
  State<_BotonesOpcion> createState() => _BotonesOpcionState();
}

class _BotonesOpcionState extends State<_BotonesOpcion> {
  static const _envios = ['Estándar (5 a 7 días)', 'Exprés (2 a 3 días)', 'Recoger en tienda'];
  int _elegido = 0;

  @override
  Widget build(BuildContext context) {
    return TarjetaDemo(
      nombre: 'Botones de opción (radio)',
      descripcion: 'Presentan opciones mutuamente excluyentes: al elegir una, las demás '
          'se desmarcan. Se usan cuando todas las alternativas deben estar a la vista.',
      children: [
        for (var i = 0; i < _envios.length; i++)
          RadioListTile<int>(
            contentPadding: EdgeInsets.zero,
            value: i,
            groupValue: _elegido,
            title: Text(_envios[i]),
            onChanged: (v) => setState(() => _elegido = v ?? 0),
          ),
        Respuesta('Tipo de envío: ${_envios[_elegido]}'),
      ],
    );
  }
}

// ---------- 3. Interruptores ----------
class _Interruptores extends StatefulWidget {
  const _Interruptores();

  @override
  State<_Interruptores> createState() => _InterruptoresState();
}

class _InterruptoresState extends State<_Interruptores> {
  bool _notificaciones = true;
  bool _modoAvion = false;

  // Palomita cuando está encendido y tache cuando está apagado
  static final _iconoPulgar = WidgetStateProperty.resolveWith<Icon>(
    (estados) => Icon(estados.contains(WidgetState.selected) ? Icons.check : Icons.close),
  );

  @override
  Widget build(BuildContext context) {
    return TarjetaDemo(
      nombre: 'Interruptor (switch)',
      descripcion: 'Activa o desactiva una configuración de forma inmediata, sin necesidad '
          'de pulsar un botón de guardar. Es ideal para ajustes de encendido y apagado.',
      children: [
        SwitchListTile(
          contentPadding: EdgeInsets.zero,
          title: const Text('Notificaciones'),
          value: _notificaciones,
          thumbIcon: _iconoPulgar,
          onChanged: (v) => setState(() => _notificaciones = v),
        ),
        const Divider(height: 1),
        SwitchListTile(
          contentPadding: EdgeInsets.zero,
          title: const Text('Modo avión'),
          value: _modoAvion,
          thumbIcon: _iconoPulgar,
          onChanged: (v) => setState(() => _modoAvion = v),
        ),
        Respuesta(
          '${_notificaciones ? 'Recibirás notificaciones.' : 'Notificaciones silenciadas.'} '
          '${_modoAvion ? 'Sin conexión.' : 'Conectado.'}',
        ),
      ],
    );
  }
}

// ---------- 4. Deslizador único: conectado con la Sección 5 ----------
class _DeslizadorUnico extends StatelessWidget {
  const _DeslizadorUnico();

  @override
  Widget build(BuildContext context) {
    final estado = EstadoScope.of(context);
    final porcentaje = (estado.escalaTexto * 100).roundToDouble().clamp(80.0, 160.0).toDouble();

    return TarjetaDemo(
      nombre: 'Deslizador de valor único',
      descripcion: 'Elige un valor dentro de un rango arrastrando el control. Este ajusta '
          'el tamaño de los textos de la Sección 5, así que tu elección se comparte entre secciones.',
      children: [
        Slider(
          value: porcentaje,
          min: 80,
          max: 160,
          divisions: 8, // saltos de 10 %
          label: '${porcentaje.round()} %',
          onChanged: (v) => estado.cambiarEscalaTexto(v / 100),
        ),
        Text(
          'Tamaño de texto: ${porcentaje.round()} %',
          style: TextStyle(
            fontSize: 16 * estado.escalaTexto,
            fontWeight: FontWeight.w500,
            color: Theme.of(context).colorScheme.primary,
          ),
        ),
      ],
    );
  }
}

// ---------- 5. Deslizador de rango ----------
class _DeslizadorRango extends StatefulWidget {
  const _DeslizadorRango();

  @override
  State<_DeslizadorRango> createState() => _DeslizadorRangoState();
}

class _DeslizadorRangoState extends State<_DeslizadorRango> {
  RangeValues _rango = const RangeValues(300, 1500);

  @override
  Widget build(BuildContext context) {
    final minimo = _rango.start.round();
    final maximo = _rango.end.round();
    return TarjetaDemo(
      nombre: 'Deslizador de rango',
      descripcion: 'Tiene dos controles para definir un mínimo y un máximo a la vez. Es '
          'común en filtros de precio, edad o distancia.',
      children: [
        RangeSlider(
          values: _rango,
          min: 0,
          max: 2000,
          divisions: 20, // saltos de 100
          labels: RangeLabels('\$$minimo', '\$$maximo'),
          onChanged: (v) => setState(() => _rango = v),
        ),
        Respuesta('Precio: de \$$minimo a \$$maximo MXN'),
      ],
    );
  }
}

// ---------- 6. Lista desplegable ----------
class _ListaDesplegable extends StatefulWidget {
  const _ListaDesplegable();

  @override
  State<_ListaDesplegable> createState() => _ListaDesplegableState();
}

class _ListaDesplegableState extends State<_ListaDesplegable> {
  static const _carreras = [
    'Ingeniería en Sistemas Computacionales',
    'Ingeniería en Inteligencia Artificial',
    'Licenciatura en Ciencia de Datos',
    'Ingeniería Mecatrónica',
    'Ingeniería Industrial',
  ];
  String? _seleccion;

  @override
  Widget build(BuildContext context) {
    return TarjetaDemo(
      nombre: 'Lista desplegable de selección',
      descripcion: 'Muestra la opción elegida y, al tocarla, despliega todas las '
          'alternativas. Ahorra espacio cuando hay muchas opciones posibles.',
      children: [
        DropdownMenu<String>(
          label: const Text('Carrera'),
          hintText: 'Elige una opción',
          expandedInsets: EdgeInsets.zero, // ocupa todo el ancho
          requestFocusOnTap: false, // solo se elige, no se escribe
          dropdownMenuEntries: [
            for (final c in _carreras) DropdownMenuEntry(value: c, label: c),
          ],
          onSelected: (c) => setState(() => _seleccion = c),
        ),
        Respuesta(_seleccion == null ? 'Aún no eliges una carrera.' : 'Seleccionaste: $_seleccion'),
      ],
    );
  }
}

// ---------- 7. Selector de fecha ----------
class _SelectorFecha extends StatefulWidget {
  const _SelectorFecha();

  @override
  State<_SelectorFecha> createState() => _SelectorFechaState();
}

class _SelectorFechaState extends State<_SelectorFecha> {
  DateTime? _fecha;

  Future<void> _elegir() async {
    final hoy = DateTime.now();
    final elegida = await showDatePicker(
      context: context,
      initialDate: _fecha ?? hoy,
      firstDate: DateTime(1900),
      lastDate: DateTime(2100),
      helpText: 'Selecciona una fecha',
    );
    if (elegida != null) setState(() => _fecha = elegida);
  }

  @override
  Widget build(BuildContext context) {
    // MaterialLocalizations da el formato en español: "miércoles, 23 de septiembre de 2026"
    final formato = MaterialLocalizations.of(context);
    return TarjetaDemo(
      nombre: 'Selector de fecha',
      descripcion: 'Abre un calendario para elegir un día sin escribirlo a mano, lo que '
          'evita formatos incorrectos. También permite capturar la fecha con el teclado.',
      children: [
        OutlinedButton.icon(
          onPressed: _elegir,
          icon: const Icon(Icons.calendar_month),
          label: const Text('Elegir fecha'),
        ),
        Respuesta(_fecha == null ? 'Sin fecha seleccionada.' : 'Fecha: ${formato.formatFullDate(_fecha!)}'),
      ],
    );
  }
}

// ---------- 8. Selector de hora ----------
class _SelectorHora extends StatefulWidget {
  const _SelectorHora();

  @override
  State<_SelectorHora> createState() => _SelectorHoraState();
}

class _SelectorHoraState extends State<_SelectorHora> {
  TimeOfDay? _hora;

  Future<void> _elegir() async {
    final elegida = await showTimePicker(
      context: context,
      initialTime: _hora ?? const TimeOfDay(hour: 12, minute: 0),
      helpText: 'Selecciona la hora',
      // Fuerza el formato de 24 horas
      builder: (context, hijo) => MediaQuery(
        data: MediaQuery.of(context).copyWith(alwaysUse24HourFormat: true),
        child: hijo!,
      ),
    );
    if (elegida != null) setState(() => _hora = elegida);
  }

  @override
  Widget build(BuildContext context) {
    String dosDigitos(int n) => n.toString().padLeft(2, '0');
    return TarjetaDemo(
      nombre: 'Selector de hora',
      descripcion: 'Muestra un reloj para elegir hora y minutos de forma visual. Es útil '
          'para alarmas, citas o recordatorios.',
      children: [
        OutlinedButton.icon(
          onPressed: _elegir,
          icon: const Icon(Icons.schedule),
          label: const Text('Elegir hora'),
        ),
        Respuesta(
          _hora == null
              ? 'Sin hora seleccionada.'
              : 'Hora: ${dosDigitos(_hora!.hour)}:${dosDigitos(_hora!.minute)}',
        ),
      ],
    );
  }
}

// ---------- 9. Chips de filtro ----------
class _ChipsFiltro extends StatefulWidget {
  const _ChipsFiltro();

  @override
  State<_ChipsFiltro> createState() => _ChipsFiltroState();
}

class _ChipsFiltroState extends State<_ChipsFiltro> {
  static const _productos = {
    'Frutas': ['Manzana', 'Mango', 'Fresa'],
    'Verduras': ['Zanahoria', 'Nopal', 'Brócoli'],
    'Lácteos': ['Leche', 'Queso', 'Yogur'],
    'Cereales': ['Avena', 'Arroz', 'Maíz'],
  };
  final _activos = <String>{'Frutas'};

  @override
  Widget build(BuildContext context) {
    final visibles = [
      for (final entrada in _productos.entries)
        if (_activos.contains(entrada.key)) ...entrada.value,
    ];
    return TarjetaDemo(
      nombre: 'Chips de filtro',
      descripcion: 'Etiquetas compactas que se activan o desactivan para filtrar '
          'contenido. Se pueden combinar varias a la vez.',
      children: [
        Wrap(
          spacing: 8,
          runSpacing: 4,
          children: [
            for (final categoria in _productos.keys)
              FilterChip(
                label: Text(categoria),
                selected: _activos.contains(categoria),
                onSelected: (activo) => setState(
                  () => activo ? _activos.add(categoria) : _activos.remove(categoria),
                ),
              ),
          ],
        ),
        Respuesta(
          visibles.isEmpty
              ? 'Activa al menos un filtro para ver productos.'
              : 'Productos: ${visibles.join(', ')}',
        ),
      ],
    );
  }
}
