package com.gmail.juliocesar64914.interfazconviews.pantallas.listas

import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gmail.juliocesar64914.interfazconviews.R
import com.gmail.juliocesar64914.interfazconviews.componentes.dp
import com.gmail.juliocesar64914.interfazconviews.databinding.GuiaListaBinding
import com.gmail.juliocesar64914.interfazconviews.databinding.ItemEstadoVacioBinding
import com.gmail.juliocesar64914.interfazconviews.databinding.PaginaListaBinding
import com.gmail.juliocesar64914.interfazconviews.datos.CatalogoViewModel
import com.google.android.material.color.MaterialColors
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.google.android.material.R as MR

/** Pestaña "Lista": lista vertical, detalle, deslizar para eliminar, actualizar y estado vacío. */
class PaginaListaFragment : Fragment(R.layout.pagina_lista) {

    private val viewModel: CatalogoViewModel by activityViewModels()
    private var guia: GuiaListaBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val b = PaginaListaBinding.bind(view)

        val elementos = AdaptadorElementos(enCuadricula = false) { mostrarDetalle(it, viewModel) }
        val cabecera = AdaptadorUnico { padre ->
            GuiaListaBinding.inflate(layoutInflater, padre, false).also { g ->
                guia = g
                g.botonVaciar.setOnClickListener { viewModel.vaciarLista() }
                actualizarGuia(viewModel.cantidad)
            }.root
        }
        val vacio = AdaptadorUnico { padre ->
            ItemEstadoVacioBinding.inflate(layoutInflater, padre, false).also {
                it.botonRestaurar.setOnClickListener { viewModel.reiniciarLista() }
            }.root
        }.apply { visible = false }

        // ConcatAdapter une varios adaptadores en una sola lista: guía + vacío + elementos
        b.lista.layoutManager = LinearLayoutManager(requireContext())
        b.lista.adapter = ConcatAdapter(cabecera, vacio, elementos)

        configurarDeslizarParaEliminar(b.lista, elementos)
        evitarConflictoConPestanas(b.lista)

        // Actualizar arrastrando hacia abajo
        b.actualizador.setColorSchemeColors(MaterialColors.getColor(view, androidx.appcompat.R.attr.colorPrimary))
        b.actualizador.setProgressBackgroundColorSchemeColor(MaterialColors.getColor(view, MR.attr.colorSurfaceVariant))
        b.actualizador.setOnRefreshListener {
            viewLifecycleOwner.lifecycleScope.launch {
                delay(1500) // simula una descarga
                viewModel.reiniciarLista()
                b.actualizador.isRefreshing = false
            }
        }

        viewModel.elementos.observe(viewLifecycleOwner) { lista ->
            elementos.submitList(lista)
            vacio.visible = lista.isEmpty()
            actualizarGuia(lista.size)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        guia = null
    }

    private fun actualizarGuia(cantidad: Int) {
        guia?.let {
            it.contador.text = "$cantidad elementos"
            it.botonVaciar.isEnabled = cantidad > 0
        }
    }

    /** ItemTouchHelper detecta el gesto de deslizar y dibuja el fondo rojo con el ícono. */
    private fun configurarDeslizarParaEliminar(lista: RecyclerView, adaptador: AdaptadorElementos) {
        val fondo = Paint().apply { color = MaterialColors.getColor(lista, MR.attr.colorErrorContainer) }
        val icono = ContextCompat.getDrawable(requireContext(), R.drawable.ic_delete)!!.mutate().apply {
            setTint(MaterialColors.getColor(lista, MR.attr.colorOnErrorContainer))
        }
        val tamano = requireContext().dp(24)
        val margen = requireContext().dp(24)

        val gesto = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(rv: RecyclerView, origen: RecyclerView.ViewHolder, destino: RecyclerView.ViewHolder) = false

            // Solo las filas de elementos se pueden deslizar (no la guía ni el estado vacío)
            override fun getSwipeDirs(rv: RecyclerView, vista: RecyclerView.ViewHolder) =
                if (vista is AdaptadorElementos.Vista) super.getSwipeDirs(rv, vista) else 0

            override fun onSwiped(vista: RecyclerView.ViewHolder, direccion: Int) {
                val posicion = vista.bindingAdapterPosition
                if (posicion != RecyclerView.NO_POSITION) {
                    viewModel.eliminarElemento(adaptador.elementoEn(posicion))
                }
            }

            override fun onChildDraw(
                c: Canvas, rv: RecyclerView, vista: RecyclerView.ViewHolder,
                dX: Float, dY: Float, estado: Int, activo: Boolean
            ) {
                val v = vista.itemView
                if (dX != 0f) {
                    if (dX > 0) c.drawRect(v.left.toFloat(), v.top.toFloat(), v.left + dX, v.bottom.toFloat(), fondo)
                    else c.drawRect(v.right + dX, v.top.toFloat(), v.right.toFloat(), v.bottom.toFloat(), fondo)
                    val arriba = v.top + (v.height - tamano) / 2
                    val izquierda = if (dX > 0) v.left + margen else v.right - margen - tamano
                    icono.setBounds(izquierda, arriba, izquierda + tamano, arriba + tamano)
                    icono.draw(c)
                }
                super.onChildDraw(c, rv, vista, dX, dY, estado, activo)
            }
        }
        ItemTouchHelper(gesto).attachToRecyclerView(lista)
    }

    /**
     * El paginador de pestañas también reacciona al deslizar horizontal. Cuando el dedo
     * empieza sobre una fila, se le pide al paginador que no intercepte el gesto, para que
     * la fila se pueda deslizar y eliminar. En la guía, deslizar sigue cambiando de pestaña.
     */
    private fun evitarConflictoConPestanas(lista: RecyclerView) {
        lista.addOnItemTouchListener(object : RecyclerView.SimpleOnItemTouchListener() {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                if (e.actionMasked == MotionEvent.ACTION_DOWN) {
                    val hijo = rv.findChildViewUnder(e.x, e.y)
                    val esFila = hijo != null && rv.getChildViewHolder(hijo) is AdaptadorElementos.Vista
                    rv.parent.requestDisallowInterceptTouchEvent(esFila)
                }
                return false
            }
        })
    }
}
