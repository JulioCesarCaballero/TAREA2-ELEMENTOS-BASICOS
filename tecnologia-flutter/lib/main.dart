import 'package:flutter/material.dart';
import 'package:flutter_localizations/flutter_localizations.dart';

import 'datos/catalogo_estado.dart';
import 'navegacion/estructura.dart';

void main() => runApp(const CatalogoApp());

class CatalogoApp extends StatefulWidget {
  const CatalogoApp({super.key});

  @override
  State<CatalogoApp> createState() => _CatalogoAppState();
}

class _CatalogoAppState extends State<CatalogoApp> {
  /// Estado compartido por todas las secciones (equivale al ViewModel de Android).
  final _estado = CatalogoEstado();

  @override
  void dispose() {
    _estado.dispose();
    super.dispose();
  }

  ThemeData _tema(Brightness brillo) => ThemeData(
        useMaterial3: true,
        colorScheme: ColorScheme.fromSeed(
          seedColor: const Color(0xFF3F51B5),
          brightness: brillo,
        ),
      );

  @override
  Widget build(BuildContext context) {
    return EstadoScope(
      notifier: _estado,
      // Se reconstruye cuando cambia el modo de tema
      child: ListenableBuilder(
        listenable: _estado,
        builder: (context, _) => MaterialApp(
          title: 'Catálogo de elementos',
          debugShowCheckedModeBanner: false,
          // Tema claro y oscuro; por defecto sigue el modo del sistema
          theme: _tema(Brightness.light),
          darkTheme: _tema(Brightness.dark),
          themeMode: _estado.modoTema.themeMode,
          // Todo en español, incluidos calendario, reloj y diálogos
          locale: const Locale('es', 'MX'),
          supportedLocales: const [Locale('es', 'MX'), Locale('es')],
          localizationsDelegates: const [
            GlobalMaterialLocalizations.delegate,
            GlobalWidgetsLocalizations.delegate,
            GlobalCupertinoLocalizations.delegate,
          ],
          home: const Estructura(),
        ),
      ),
    );
  }
}
