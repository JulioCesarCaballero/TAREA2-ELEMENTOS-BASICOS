import 'package:flutter/material.dart';

import '../datos/catalogo_estado.dart';

/// Sección 4: pestañas (TabBar) + páginas deslizables (TabBarView).
class Seccion4Listas extends StatelessWidget {
  const Seccion4Listas({super.key});

  @override
  Widget build(BuildContext context) {
    return DefaultTabController(
      length: 3,
      child: Column(
        children: [
          const Padding(
            padding: EdgeInsets.fromLTRB(16, 8, 16, 8),
            child: _Guia(
              nombre: 'Pestañas con contenido deslizable',
              texto: 'Organizan vistas relacionadas en páginas. Toca una pestaña o desliza a '
                  'la izquierda o a la derecha para cambiar de página.',
            ),
          ),
          // Pestañas secundarias: se distinguen de las pestañas principales de la app
          const TabBar.secondary(
            tabs: [Tab(text: 'Lista'), Tab(text: 'Cuadrícula'), Tab(text: 'Encabezados')],
          ),
          const Expanded(
            child: TabBarView(
              children: [_PaginaLista(), _PaginaCuadricula(), _PaginaEncabezados()],
            ),
          ),
        ],
      ),
    );
  }
}

// ================= Pestaña 1: lista vertical =================
class _PaginaLista extends StatelessWidget {
  const _PaginaLista();

  @override
  Widget build(BuildContext context) {
    final estado = EstadoScope.of(context);
    final elementos = estado.elementos;
    final tema = Theme.of(context);

    // RefreshIndicator: jalar hacia abajo para actualizar
    return RefreshIndicator(
      onRefresh: () async {
        await Future.delayed(const Duration(milliseconds: 1500)); // simula una descarga
        estado.reiniciarLista();
      },
      child: ListView(
        // Permite jalar aunque la lista esté vacía
        physics: const AlwaysScrollableScrollPhysics(),
        padding: const EdgeInsets.only(bottom: 16),
        children: [
          Card(
            margin: const EdgeInsets.all(16),
            child: Padding(
              padding: const EdgeInsets.all(16),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                spacing: 12,
                children: [
                  const _Guia(
                    nombre: 'Lista vertical',
                    texto: 'Muestra elementos uno debajo de otro y solo dibuja los visibles, '
                        'por lo que funciona bien con listas largas.',
                  ),
                  const _Guia(nombre: 'Ver detalle', texto: 'Toca un elemento para abrir su información completa.'),
                  const _Guia(
                    nombre: 'Deslizar para eliminar',
                    texto: 'Arrastra un elemento hacia un lado para borrarlo de la lista.',
                  ),
                  const _Guia(
                    nombre: 'Actualizar arrastrando hacia abajo',
                    texto: 'Desde el inicio de la lista, jala hacia abajo para recargar los datos.',
                  ),
                  const _Guia(
                    nombre: 'Estado vacío',
                    texto: 'Cuando no hay elementos se muestra un mensaje con ilustración. '
                        'Pruébalo con el botón de vaciar.',
                  ),
                  OutlinedButton.icon(
                    onPressed: elementos.isEmpty ? null : estado.vaciarLista,
                    icon: const Icon(Icons.delete_sweep),
                    label: const Text('Vaciar lista'),
                  ),
                  Text(
                    '${elementos.length} elementos',
                    style: tema.textTheme.labelLarge?.copyWith(color: tema.colorScheme.primary),
                  ),
                ],
              ),
            ),
          ),
          if (elementos.isEmpty)
            const _EstadoVacio()
          else
            for (final elemento in elementos) ...[
              // Dismissible: deslizar para eliminar
              Dismissible(
                key: ValueKey(elemento.id),
                background: const _FondoEliminar(alineacion: Alignment.centerLeft),
                secondaryBackground: const _FondoEliminar(alineacion: Alignment.centerRight),
                onDismissed: (_) => estado.eliminarElemento(elemento),
                child: ListTile(
                  leading: _Avatar(elemento),
                  title: Text(elemento.titulo),
                  subtitle: Text(elemento.subtitulo),
                  onTap: () => _mostrarDetalle(context, elemento),
                ),
              ),
              const Divider(height: 1),
            ],
        ],
      ),
    );
  }
}

class _FondoEliminar extends StatelessWidget {
  const _FondoEliminar({required this.alineacion});

  final Alignment alineacion;

  @override
  Widget build(BuildContext context) {
    final colores = Theme.of(context).colorScheme;
    return Container(
      color: colores.errorContainer,
      alignment: alineacion,
      padding: const EdgeInsets.symmetric(horizontal: 24),
      child: Icon(Icons.delete, color: colores.onErrorContainer, semanticLabel: 'Eliminar'),
    );
  }
}

// ================= Pestaña 2: cuadrícula =================
class _PaginaCuadricula extends StatelessWidget {
  const _PaginaCuadricula();

  @override
  Widget build(BuildContext context) {
    final elementos = EstadoScope.of(context).elementos;
    final tema = Theme.of(context);

    return CustomScrollView(
      slivers: [
        const SliverPadding(
          padding: EdgeInsets.all(16),
          sliver: SliverToBoxAdapter(
            child: _Guia(
              nombre: 'Cuadrícula de elementos',
              texto: 'Acomoda los elementos en filas y columnas. El número de columnas se '
                  'ajusta al ancho de la pantalla. Toca una tarjeta para ver su detalle.',
            ),
          ),
        ),
        if (elementos.isEmpty)
          const SliverToBoxAdapter(child: _EstadoVacio())
        else
          SliverPadding(
            padding: const EdgeInsets.fromLTRB(16, 0, 16, 16),
            // Cada tarjeta mide como máximo 120 de ancho: las columnas se calculan solas
            sliver: SliverGrid(
              gridDelegate: const SliverGridDelegateWithMaxCrossAxisExtent(
                maxCrossAxisExtent: 120,
                mainAxisSpacing: 12,
                crossAxisSpacing: 12,
                childAspectRatio: 0.9,
              ),
              delegate: SliverChildBuilderDelegate(
                childCount: elementos.length,
                (context, i) {
                  final elemento = elementos[i];
                  return Card.filled(
                    margin: EdgeInsets.zero,
                    clipBehavior: Clip.antiAlias,
                    child: InkWell(
                      onTap: () => _mostrarDetalle(context, elemento),
                      child: Padding(
                        padding: const EdgeInsets.all(8),
                        child: Column(
                          mainAxisAlignment: MainAxisAlignment.center,
                          spacing: 6,
                          children: [
                            _Avatar(elemento, radio: 24),
                            Text(
                              elemento.titulo,
                              maxLines: 1,
                              overflow: TextOverflow.ellipsis,
                              style: tema.textTheme.titleSmall,
                            ),
                            Text(
                              elemento.categoria,
                              style: tema.textTheme.labelSmall
                                  ?.copyWith(color: tema.colorScheme.onSurfaceVariant),
                            ),
                          ],
                        ),
                      ),
                    ),
                  );
                },
              ),
            ),
          ),
      ],
    );
  }
}

// ================= Pestaña 3: lista con encabezados =================
class _PaginaEncabezados extends StatelessWidget {
  const _PaginaEncabezados();

  @override
  Widget build(BuildContext context) {
    final elementos = EstadoScope.of(context).elementos;

    // Agrupa por categoría conservando el orden de aparición
    final grupos = <String, List<ElementoLista>>{};
    for (final e in elementos) {
      grupos.putIfAbsent(e.categoria, () => []).add(e);
    }

    return CustomScrollView(
      slivers: [
        const SliverPadding(
          padding: EdgeInsets.all(16),
          sliver: SliverToBoxAdapter(
            child: _Guia(
              nombre: 'Lista con encabezados de sección',
              texto: 'Combina dos tipos de elemento: encabezados que agrupan por categoría y '
                  'filas de contenido. El encabezado se queda fijo arriba mientras recorres su grupo.',
            ),
          ),
        ),
        if (grupos.isEmpty) const SliverToBoxAdapter(child: _EstadoVacio()),
        for (final grupo in grupos.entries)
          // SliverMainAxisGroup + encabezado fijo (pinned): el encabezado solo
          // permanece arriba mientras se ven los elementos de su grupo
          SliverMainAxisGroup(
            slivers: [
              SliverPersistentHeader(
                pinned: true,
                delegate: _EncabezadoFijo('${grupo.key} (${grupo.value.length})'),
              ),
              SliverList.list(
                children: [
                  for (final elemento in grupo.value)
                    ListTile(
                      leading: _Avatar(elemento),
                      title: Text(elemento.titulo),
                      subtitle: Text(elemento.subtitulo),
                      onTap: () => _mostrarDetalle(context, elemento),
                    ),
                ],
              ),
            ],
          ),
        const SliverToBoxAdapter(child: SizedBox(height: 16)),
      ],
    );
  }
}

class _EncabezadoFijo extends SliverPersistentHeaderDelegate {
  _EncabezadoFijo(this.texto);

  final String texto;

  @override
  double get minExtent => 44;

  @override
  double get maxExtent => 44;

  @override
  Widget build(BuildContext context, double shrinkOffset, bool overlapsContent) {
    final tema = Theme.of(context);
    return Container(
      color: tema.colorScheme.secondaryContainer,
      alignment: Alignment.centerLeft,
      padding: const EdgeInsets.symmetric(horizontal: 16),
      child: Text(
        texto,
        style: tema.textTheme.titleSmall?.copyWith(
          fontWeight: FontWeight.bold,
          color: tema.colorScheme.onSecondaryContainer,
        ),
      ),
    );
  }

  @override
  bool shouldRebuild(_EncabezadoFijo anterior) => anterior.texto != texto;
}

// ================= Componentes compartidos =================

/// Nombre y explicación breve de un elemento, en formato compacto.
class _Guia extends StatelessWidget {
  const _Guia({required this.nombre, required this.texto});

  final String nombre;
  final String texto;

  @override
  Widget build(BuildContext context) {
    final tema = Theme.of(context);
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(nombre, style: tema.textTheme.titleSmall?.copyWith(color: tema.colorScheme.primary)),
        Text(
          texto,
          style: tema.textTheme.bodySmall?.copyWith(color: tema.colorScheme.onSurfaceVariant),
        ),
      ],
    );
  }
}

/// Círculo con la inicial; el color depende de la categoría.
class _Avatar extends StatelessWidget {
  const _Avatar(this.elemento, {this.radio = 20});

  final ElementoLista elemento;
  final double radio;

  @override
  Widget build(BuildContext context) {
    final c = Theme.of(context).colorScheme;
    final (fondo, texto) = switch (elemento.categoria) {
      'Frutas' => (c.primaryContainer, c.onPrimaryContainer),
      'Verduras' => (c.tertiaryContainer, c.onTertiaryContainer),
      categoriaUsuario => (c.secondaryContainer, c.onSecondaryContainer),
      _ => (c.surfaceContainerHighest, c.onSurfaceVariant),
    };
    return CircleAvatar(
      radius: radio,
      backgroundColor: fondo,
      foregroundColor: texto,
      child: Text(elemento.titulo.characters.first.toUpperCase()),
    );
  }
}

/// Estado vacío con ilustración, mensaje y acción para recuperar los datos.
class _EstadoVacio extends StatelessWidget {
  const _EstadoVacio();

  @override
  Widget build(BuildContext context) {
    final tema = Theme.of(context);
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 32, vertical: 40),
      child: Column(
        spacing: 12,
        children: [
          CircleAvatar(
            radius: 60,
            backgroundColor: tema.colorScheme.surfaceContainerHighest,
            child: Icon(Icons.inventory_2, size: 56, color: tema.colorScheme.onSurfaceVariant),
          ),
          Text('La lista está vacía', style: tema.textTheme.titleMedium),
          Text(
            'No hay elementos para mostrar. Restaura la lista original o agrega uno nuevo '
            'desde la sección Entrada de texto.',
            textAlign: TextAlign.center,
            style: tema.textTheme.bodyMedium?.copyWith(color: tema.colorScheme.onSurfaceVariant),
          ),
          FilledButton.icon(
            onPressed: EstadoScope.leer(context).reiniciarLista,
            icon: const Icon(Icons.refresh),
            label: const Text('Restaurar lista'),
          ),
        ],
      ),
    );
  }
}

/// Diálogo con el detalle del elemento seleccionado.
void _mostrarDetalle(BuildContext context, ElementoLista elemento) {
  final estado = EstadoScope.leer(context);
  showDialog<void>(
    context: context,
    builder: (contextoDialogo) => AlertDialog(
      icon: _Avatar(elemento, radio: 28),
      title: Text(elemento.titulo),
      content: Column(
        mainAxisSize: MainAxisSize.min,
        crossAxisAlignment: CrossAxisAlignment.start,
        spacing: 4,
        children: [
          Text('Categoría: ${elemento.categoria}'),
          Text('Descripción: ${elemento.subtitulo}'),
          Text('Identificador: #${elemento.id}'),
        ],
      ),
      actions: [
        TextButton(
          onPressed: () {
            estado.eliminarElemento(elemento);
            Navigator.pop(contextoDialogo);
          },
          child: const Text('Eliminar'),
        ),
        TextButton(
          onPressed: () => Navigator.pop(contextoDialogo),
          child: const Text('Cerrar'),
        ),
      ],
    ),
  );
}
