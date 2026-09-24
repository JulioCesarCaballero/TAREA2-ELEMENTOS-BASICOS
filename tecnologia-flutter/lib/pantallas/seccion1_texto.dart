import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import '../componentes/tarjeta_demo.dart';
import '../datos/catalogo_estado.dart';

class Seccion1Texto extends StatelessWidget {
  const Seccion1Texto({super.key});

  @override
  Widget build(BuildContext context) {
    return const PantallaCatalogo(
      children: [
        _CampoSimple(),
        _CampoValidacion(),
        _CampoContrasena(),
        _TiposTeclado(),
        _CampoMultilinea(),
        _CampoSugerencias(),
        _BarraBusqueda(),
        _AgregarALista(),
      ],
    );
  }
}

// ---------- 1. Campo simple ----------
class _CampoSimple extends StatefulWidget {
  const _CampoSimple();

  @override
  State<_CampoSimple> createState() => _CampoSimpleState();
}

class _CampoSimpleState extends State<_CampoSimple> {
  String _nombre = '';

  @override
  Widget build(BuildContext context) {
    return TarjetaDemo(
      nombre: 'Campo de texto simple',
      descripcion: 'Permite escribir una línea de texto. La etiqueta indica qué dato se '
          'espera y el texto de ayuda muestra un ejemplo mientras el campo está vacío.',
      children: [
        TextField(
          decoration: decoracion(etiqueta: 'Nombre', pista: 'Ej. Julio César', icono: Icons.person),
          textCapitalization: TextCapitalization.words,
          onChanged: (t) => setState(() => _nombre = t.trim()),
        ),
        Respuesta(_nombre.isEmpty ? 'Aún no has escrito nada.' : 'Hola, $_nombre.'),
      ],
    );
  }
}

// ---------- 2. Campo con validación ----------
class _CampoValidacion extends StatefulWidget {
  const _CampoValidacion();

  @override
  State<_CampoValidacion> createState() => _CampoValidacionState();
}

class _CampoValidacionState extends State<_CampoValidacion> {
  String _usuario = '';

  String? get _error {
    if (_usuario.isEmpty) return null;
    if (_usuario.length < 4) return 'Debe tener al menos 4 caracteres.';
    if (!RegExp(r'^[a-zA-Z0-9_ñÑáéíóúÁÉÍÓÚ]+$').hasMatch(_usuario)) {
      return 'Solo se permiten letras, números y guion bajo.';
    }
    return null;
  }

  @override
  Widget build(BuildContext context) {
    final valido = _usuario.isNotEmpty && _error == null;
    return TarjetaDemo(
      nombre: 'Campo con validación',
      descripcion: 'Revisa el dato mientras se escribe. Si no cumple las reglas, el campo '
          'cambia a color de error y muestra un mensaje que explica cómo corregirlo.',
      children: [
        TextField(
          decoration: decoracion(
            etiqueta: 'Nombre de usuario',
            ayuda: valido ? 'Usuario válido.' : 'Mínimo 4 caracteres: letras, números o guion bajo.',
            error: _error,
            sufijo: valido
                ? Icon(Icons.check_circle, color: Theme.of(context).colorScheme.primary)
                : null,
          ),
          onChanged: (t) => setState(() => _usuario = t),
        ),
      ],
    );
  }
}

// ---------- 3. Contraseña ----------
class _CampoContrasena extends StatefulWidget {
  const _CampoContrasena();

  @override
  State<_CampoContrasena> createState() => _CampoContrasenaState();
}

class _CampoContrasenaState extends State<_CampoContrasena> {
  String _contrasena = '';
  bool _visible = false;

  @override
  Widget build(BuildContext context) {
    final c = _contrasena;
    final puntos = [
      c.length >= 8,
      c.contains(RegExp(r'[A-Z]')),
      c.contains(RegExp(r'[0-9]')),
      c.contains(RegExp(r'[^a-zA-Z0-9]')),
    ].where((cumple) => cumple).length;
    final nivel = c.isEmpty
        ? 'Escribe una contraseña.'
        : puntos <= 1
            ? 'Seguridad: débil'
            : puntos <= 3
                ? 'Seguridad: media'
                : 'Seguridad: fuerte';

    return TarjetaDemo(
      nombre: 'Campo de contraseña',
      descripcion: 'Oculta los caracteres para proteger datos sensibles. El botón del ojo '
          'permite mostrar u ocultar el contenido para revisar lo escrito.',
      children: [
        TextField(
          obscureText: !_visible,
          keyboardType: TextInputType.visiblePassword,
          decoration: decoracion(
            etiqueta: 'Contraseña',
            sufijo: IconButton(
              icon: Icon(_visible ? Icons.visibility_off : Icons.visibility),
              tooltip: _visible ? 'Ocultar contraseña' : 'Mostrar contraseña',
              onPressed: () => setState(() => _visible = !_visible),
            ),
          ),
          onChanged: (t) => setState(() => _contrasena = t),
        ),
        LinearProgressIndicator(value: puntos / 4),
        Text(nivel, style: Theme.of(context).textTheme.bodySmall),
      ],
    );
  }
}

// ---------- 4. Tipos de teclado ----------
class _TiposTeclado extends StatefulWidget {
  const _TiposTeclado();

  @override
  State<_TiposTeclado> createState() => _TiposTecladoState();
}

class _TiposTecladoState extends State<_TiposTeclado> {
  String _edad = '';
  String _correo = '';
  String _telefono = '';

  @override
  Widget build(BuildContext context) {
    final estadoCorreo = _correo.isEmpty
        ? '—'
        : RegExp(r'^[^@\s]+@[^@\s]+\.[^@\s]+$').hasMatch(_correo)
            ? 'válido'
            : 'incompleto';

    return TarjetaDemo(
      nombre: 'Tipos de teclado',
      descripcion: 'Cada campo abre el teclado más adecuado para su dato: solo números, '
          'uno con @ para correos o uno de marcación telefónica. Toca cada campo para comparar.',
      children: [
        TextField(
          keyboardType: TextInputType.number,
          textInputAction: TextInputAction.next,
          inputFormatters: [
            FilteringTextInputFormatter.digitsOnly,
            LengthLimitingTextInputFormatter(3),
          ],
          decoration: decoracion(etiqueta: 'Edad (numérico)', icono: Icons.pin),
          onChanged: (t) => setState(() => _edad = t),
        ),
        TextField(
          keyboardType: TextInputType.emailAddress,
          textInputAction: TextInputAction.next,
          decoration: decoracion(etiqueta: 'Correo electrónico', icono: Icons.email),
          onChanged: (t) => setState(() => _correo = t.trim()),
        ),
        TextField(
          keyboardType: TextInputType.phone,
          textInputAction: TextInputAction.done,
          inputFormatters: [
            FilteringTextInputFormatter.digitsOnly,
            LengthLimitingTextInputFormatter(10),
          ],
          decoration: decoracion(etiqueta: 'Teléfono (10 dígitos)', icono: Icons.phone),
          onChanged: (t) => setState(() => _telefono = t),
        ),
        Respuesta(
          'Edad: ${_edad.isEmpty ? '—' : _edad} · Correo: $estadoCorreo · '
          'Teléfono: ${_telefono.length}/10',
        ),
      ],
    );
  }
}

// ---------- 5. Multilínea ----------
class _CampoMultilinea extends StatelessWidget {
  const _CampoMultilinea();

  @override
  Widget build(BuildContext context) {
    return TarjetaDemo(
      nombre: 'Campo multilínea',
      descripcion: 'Sirve para textos largos como comentarios o descripciones. Crece al '
          'escribir hasta un límite de líneas y aquí incluye un contador de caracteres.',
      children: [
        TextField(
          minLines: 3,
          maxLines: 6,
          maxLength: 200, // muestra el contador automáticamente
          keyboardType: TextInputType.multiline,
          textCapitalization: TextCapitalization.sentences,
          decoration: decoracion(etiqueta: 'Comentarios', pista: 'Escribe tu opinión sobre la app…'),
        ),
      ],
    );
  }
}

// ---------- 6. Sugerencias ----------
class _CampoSugerencias extends StatefulWidget {
  const _CampoSugerencias();

  @override
  State<_CampoSugerencias> createState() => _CampoSugerenciasState();
}

class _CampoSugerenciasState extends State<_CampoSugerencias> {
  static const _lenguajes = [
    'Kotlin', 'Java', 'Dart', 'Swift', 'JavaScript', 'TypeScript',
    'Python', 'C#', 'C++', 'Go', 'Rust', 'Ruby', 'PHP',
  ];

  String? _elegido;

  String? _buscarExacto(String texto) {
    final t = texto.trim().toLowerCase();
    return _lenguajes.where((l) => l.toLowerCase() == t).firstOrNull;
  }

  @override
  Widget build(BuildContext context) {
    return TarjetaDemo(
      nombre: 'Campo con sugerencias',
      descripcion: 'Muestra opciones que coinciden con lo que se escribe, para completar '
          'más rápido y evitar errores. Al tocar el campo se despliega la lista.',
      children: [
        // Autocomplete: widget de Flutter que filtra y muestra las opciones
        Autocomplete<String>(
          optionsBuilder: (valor) {
            final t = valor.text.trim().toLowerCase();
            return _lenguajes.where((l) => l.toLowerCase().contains(t) && l.toLowerCase() != t);
          },
          onSelected: (l) => setState(() => _elegido = l),
          fieldViewBuilder: (context, controlador, foco, alEnviar) => TextField(
            controller: controlador,
            focusNode: foco,
            decoration: decoracion(
              etiqueta: 'Lenguaje de programación favorito',
              sufijo: IconButton(
                icon: const Icon(Icons.arrow_drop_down),
                tooltip: 'Mostrar opciones',
                onPressed: foco.requestFocus,
              ),
            ),
            onChanged: (t) => setState(() => _elegido = _buscarExacto(t)),
            onSubmitted: (_) => alEnviar(),
          ),
        ),
        Respuesta(_elegido != null ? 'Elegiste: $_elegido' : 'Escribe una letra o toca el campo.'),
      ],
    );
  }
}

// ---------- 7. Barra de búsqueda ----------
class _BarraBusqueda extends StatefulWidget {
  const _BarraBusqueda();

  @override
  State<_BarraBusqueda> createState() => _BarraBusquedaState();
}

class _BarraBusquedaState extends State<_BarraBusqueda> {
  static const _estados = [
    'Aguascalientes', 'Baja California', 'Baja California Sur', 'Campeche', 'Chiapas',
    'Chihuahua', 'Ciudad de México', 'Coahuila', 'Colima', 'Durango', 'Estado de México',
    'Guanajuato', 'Guerrero', 'Hidalgo', 'Jalisco', 'Michoacán', 'Morelos', 'Nayarit',
    'Nuevo León', 'Oaxaca', 'Puebla', 'Querétaro', 'Quintana Roo', 'San Luis Potosí',
    'Sinaloa', 'Sonora', 'Tabasco', 'Tamaulipas', 'Tlaxcala', 'Veracruz', 'Yucatán', 'Zacatecas',
  ];

  final _controlador = TextEditingController();

  /// Quita acentos para que "queretaro" encuentre "Querétaro".
  static String _sinAcentos(String texto) {
    const conAcento = 'áéíóúü';
    const sinAcento = 'aeiouu';
    var resultado = texto.toLowerCase();
    for (var i = 0; i < conAcento.length; i++) {
      resultado = resultado.replaceAll(conAcento[i], sinAcento[i]);
    }
    return resultado;
  }

  @override
  void dispose() {
    _controlador.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final consulta = _controlador.text.trim();
    final resultados = consulta.isEmpty
        ? <String>[]
        : _estados.where((e) => _sinAcentos(e).contains(_sinAcentos(consulta))).toList();

    return TarjetaDemo(
      nombre: 'Barra de búsqueda',
      descripcion: 'Filtra un conjunto de datos conforme se escribe. Tiene ícono de lupa, '
          'botón para borrar y la tecla de búsqueda del teclado cierra el teclado.',
      children: [
        SearchBar(
          controller: _controlador,
          hintText: 'Buscar un estado de México',
          leading: const Icon(Icons.search),
          elevation: const WidgetStatePropertyAll(1),
          textInputAction: TextInputAction.search,
          trailing: [
            if (consulta.isNotEmpty)
              IconButton(
                icon: const Icon(Icons.clear),
                tooltip: 'Borrar búsqueda',
                onPressed: () => setState(_controlador.clear),
              ),
          ],
          onChanged: (_) => setState(() {}),
          onSubmitted: (_) => FocusScope.of(context).unfocus(),
        ),
        if (consulta.isEmpty)
          const Respuesta('Escribe para buscar entre los 32 estados.')
        else if (resultados.isEmpty)
          Respuesta(
            'No hay estados que coincidan con «$consulta».',
            color: Theme.of(context).colorScheme.error,
          )
        else ...[
          for (final estado in resultados.take(5))
            ListTile(
              contentPadding: EdgeInsets.zero,
              leading: const Icon(Icons.place),
              title: Text(estado),
            ),
          if (resultados.length > 5) Respuesta('y ${resultados.length - 5} resultados más…'),
        ],
      ],
    );
  }
}

// ---------- 8. Conexión con la Sección 4 ----------
class _AgregarALista extends StatefulWidget {
  const _AgregarALista();

  @override
  State<_AgregarALista> createState() => _AgregarAListaState();
}

class _AgregarAListaState extends State<_AgregarALista> {
  final _controlador = TextEditingController();
  String? _mensaje;

  @override
  void dispose() {
    _controlador.dispose();
    super.dispose();
  }

  void _agregar() {
    final texto = _controlador.text.trim();
    if (texto.isEmpty) return;
    final estado = EstadoScope.leer(context);
    estado.agregarElemento(texto);
    setState(() {
      _mensaje = '«$texto» se agregó. La lista de la Sección 4 ahora tiene '
          '${estado.elementos.length} elementos.';
      _controlador.clear();
    });
    FocusScope.of(context).unfocus();
  }

  @override
  Widget build(BuildContext context) {
    return TarjetaDemo(
      nombre: 'Conexión con Listas y colecciones',
      descripcion: 'Lo que escribas aquí se agrega al inicio de la lista de la Sección 4. '
          'Ambas secciones comparten los datos a través de un estado común.',
      children: [
        Row(
          children: [
            Expanded(
              child: TextField(
                controller: _controlador,
                textCapitalization: TextCapitalization.sentences,
                textInputAction: TextInputAction.done,
                decoration: decoracion(etiqueta: 'Nuevo elemento'),
                onChanged: (_) => setState(() {}),
                onSubmitted: (_) => _agregar(),
              ),
            ),
            const SizedBox(width: 8),
            IconButton.filled(
              icon: const Icon(Icons.add),
              tooltip: 'Agregar a la lista',
              onPressed: _controlador.text.trim().isEmpty ? null : _agregar,
            ),
          ],
        ),
        if (_mensaje != null) Respuesta(_mensaje!),
      ],
    );
  }
}
