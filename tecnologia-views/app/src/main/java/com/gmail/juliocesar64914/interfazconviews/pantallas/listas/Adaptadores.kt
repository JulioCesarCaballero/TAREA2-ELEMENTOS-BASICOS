package com.gmail.juliocesar64914.interfazconviews.pantallas.listas

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gmail.juliocesar64914.interfazconviews.R
import com.gmail.juliocesar64914.interfazconviews.databinding.ItemElementoCuadriculaBinding
import com.gmail.juliocesar64914.interfazconviews.databinding.ItemElementoListaBinding
import com.gmail.juliocesar64914.interfazconviews.databinding.ItemEncabezadoBinding
import com.gmail.juliocesar64914.interfazconviews.datos.CATEGORIA_USUARIO
import com.gmail.juliocesar64914.interfazconviews.datos.CatalogoViewModel
import com.gmail.juliocesar64914.interfazconviews.datos.ElementoLista
import com.google.android.material.color.MaterialColors
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.R as MR

// ============ Funciones compartidas ============

/** Círculo con la inicial; el color depende de la categoría. */
fun TextView.mostrarAvatar(elemento: ElementoLista) {
    text = elemento.titulo.take(1).uppercase()
    val (fondo, contenido) = when (elemento.categoria) {
        "Frutas" -> MR.attr.colorPrimaryContainer to MR.attr.colorOnPrimaryContainer
        "Verduras" -> MR.attr.colorTertiaryContainer to MR.attr.colorOnTertiaryContainer
        CATEGORIA_USUARIO -> MR.attr.colorSecondaryContainer to MR.attr.colorOnSecondaryContainer
        else -> MR.attr.colorSurfaceVariant to MR.attr.colorOnSurfaceVariant
    }
    backgroundTintList = ColorStateList.valueOf(MaterialColors.getColor(this, fondo))
    setTextColor(MaterialColors.getColor(this, contenido))
}

/** Diálogo con el detalle del elemento seleccionado. */
fun Fragment.mostrarDetalle(elemento: ElementoLista, viewModel: CatalogoViewModel) {
    MaterialAlertDialogBuilder(requireContext())
        .setIcon(R.drawable.ic_info)
        .setTitle(elemento.titulo)
        .setMessage(
            "Categoría: ${elemento.categoria}\n" +
                "Descripción: ${elemento.subtitulo}\n" +
                "Identificador: #${elemento.id}"
        )
        .setPositiveButton("Cerrar", null)
        .setNegativeButton("Eliminar") { _, _ -> viewModel.eliminarElemento(elemento) }
        .show()
}

// ============ Adaptador de elementos (lista o cuadrícula) ============

private object DiffElementos : DiffUtil.ItemCallback<ElementoLista>() {
    override fun areItemsTheSame(a: ElementoLista, b: ElementoLista) = a.id == b.id
    override fun areContentsTheSame(a: ElementoLista, b: ElementoLista) = a == b
}

/**
 * ListAdapter calcula con DiffUtil qué cambió en la lista y anima solo esas filas.
 * @param enCuadricula usa el diseño de tarjeta en lugar del de fila.
 */
class AdaptadorElementos(
    private val enCuadricula: Boolean,
    private val alTocar: (ElementoLista) -> Unit
) : ListAdapter<ElementoLista, AdaptadorElementos.Vista>(DiffElementos) {

    class Vista(
        raiz: View,
        val avatar: TextView,
        val titulo: TextView,
        val subtitulo: TextView
    ) : RecyclerView.ViewHolder(raiz)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vista {
        val inflador = LayoutInflater.from(parent.context)
        return if (enCuadricula) {
            val b = ItemElementoCuadriculaBinding.inflate(inflador, parent, false)
            Vista(b.root, b.avatar, b.titulo, b.subtitulo)
        } else {
            val b = ItemElementoListaBinding.inflate(inflador, parent, false)
            Vista(b.root, b.avatar, b.titulo, b.subtitulo)
        }
    }

    override fun onBindViewHolder(vista: Vista, posicion: Int) {
        val elemento = getItem(posicion)
        vista.avatar.mostrarAvatar(elemento)
        vista.titulo.text = elemento.titulo
        vista.subtitulo.text = if (enCuadricula) elemento.categoria else elemento.subtitulo
        vista.itemView.setOnClickListener { alTocar(elemento) }
    }

    fun elementoEn(posicion: Int): ElementoLista = getItem(posicion)
}

// ============ Adaptador de un solo elemento (guía o estado vacío) ============

/**
 * Muestra una sola vista fija. Se combina con otros adaptadores usando ConcatAdapter.
 * Con [visible] = false desaparece (por ejemplo, el estado vacío cuando hay datos).
 */
class AdaptadorUnico(
    private val crearVista: (ViewGroup) -> View
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var visible = true
        set(valor) {
            if (field == valor) return
            field = valor
            if (valor) notifyItemInserted(0) else notifyItemRemoved(0)
        }

    override fun getItemCount() = if (visible) 1 else 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        object : RecyclerView.ViewHolder(crearVista(parent)) {}

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = Unit
}

// ============ Adaptador con encabezados (dos tipos de elemento) ============

sealed interface Fila {
    data class Encabezado(val categoria: String, val cantidad: Int) : Fila
    data class Elemento(val elemento: ElementoLista) : Fila
}

/** Convierte la lista en: encabezado de categoría, sus elementos, siguiente encabezado… */
fun agruparPorCategoria(lista: List<ElementoLista>): List<Fila> =
    lista.groupBy { it.categoria }.flatMap { (categoria, elementos) ->
        listOf(Fila.Encabezado(categoria, elementos.size)) + elementos.map { Fila.Elemento(it) }
    }

private object DiffFilas : DiffUtil.ItemCallback<Fila>() {
    override fun areItemsTheSame(a: Fila, b: Fila) = when {
        a is Fila.Encabezado && b is Fila.Encabezado -> a.categoria == b.categoria
        a is Fila.Elemento && b is Fila.Elemento -> a.elemento.id == b.elemento.id
        else -> false
    }
    override fun areContentsTheSame(a: Fila, b: Fila) = a == b
}

class AdaptadorAgrupado(
    private val alTocar: (ElementoLista) -> Unit
) : ListAdapter<Fila, RecyclerView.ViewHolder>(DiffFilas) {

    private class VistaEncabezado(val b: ItemEncabezadoBinding) : RecyclerView.ViewHolder(b.root)
    private class VistaFila(val b: ItemElementoListaBinding) : RecyclerView.ViewHolder(b.root)

    override fun getItemViewType(position: Int) =
        if (getItem(position) is Fila.Encabezado) TIPO_ENCABEZADO else TIPO_ELEMENTO

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflador = LayoutInflater.from(parent.context)
        return if (viewType == TIPO_ENCABEZADO) {
            VistaEncabezado(ItemEncabezadoBinding.inflate(inflador, parent, false))
        } else {
            VistaFila(ItemElementoListaBinding.inflate(inflador, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val fila = getItem(position)) {
            is Fila.Encabezado -> (holder as VistaEncabezado).b.textoEncabezado.text =
                "${fila.categoria} (${fila.cantidad})"
            is Fila.Elemento -> {
                val b = (holder as VistaFila).b
                b.avatar.mostrarAvatar(fila.elemento)
                b.titulo.text = fila.elemento.titulo
                b.subtitulo.text = fila.elemento.subtitulo
                b.root.setOnClickListener { alTocar(fila.elemento) }
            }
        }
    }

    private companion object {
        const val TIPO_ENCABEZADO = 0
        const val TIPO_ELEMENTO = 1
    }
}
