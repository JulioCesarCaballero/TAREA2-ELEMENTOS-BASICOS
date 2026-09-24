package com.gmail.juliocesar64914.interfazconviews.pantallas.listas

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import com.gmail.juliocesar64914.interfazconviews.R
import com.gmail.juliocesar64914.interfazconviews.componentes.dp
import com.gmail.juliocesar64914.interfazconviews.databinding.GuiaSimpleBinding
import com.gmail.juliocesar64914.interfazconviews.databinding.ItemEstadoVacioBinding
import com.gmail.juliocesar64914.interfazconviews.databinding.PaginaRecyclerBinding
import com.gmail.juliocesar64914.interfazconviews.datos.CatalogoViewModel

/** Pestaña "Cuadrícula": RecyclerView con GridLayoutManager. */
class PaginaCuadriculaFragment : Fragment(R.layout.pagina_recycler) {

    private val viewModel: CatalogoViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val b = PaginaRecyclerBinding.bind(view)
        val contexto = requireContext()

        val elementos = AdaptadorElementos(enCuadricula = true) { mostrarDetalle(it, viewModel) }
        val cabecera = AdaptadorUnico { padre ->
            GuiaSimpleBinding.inflate(layoutInflater, padre, false).also {
                it.nombre.text = "Cuadrícula de elementos"
                it.texto.text = "Acomoda los elementos en filas y columnas. El número de columnas " +
                    "se ajusta al ancho de la pantalla. Toca una tarjeta para ver su detalle."
                it.root.setPadding(contexto.dp(6), contexto.dp(6), contexto.dp(6), contexto.dp(10))
            }.root
        }
        val vacio = AdaptadorUnico { padre ->
            ItemEstadoVacioBinding.inflate(layoutInflater, padre, false).also {
                it.botonRestaurar.setOnClickListener { viewModel.reiniciarLista() }
            }.root
        }.apply { visible = false }

        // Columnas según el ancho de la pantalla (mínimo 2)
        val columnas = ((resources.configuration.screenWidthDp - 20) / 116).coerceAtLeast(2)
        val gestor = GridLayoutManager(contexto, columnas)
        // La guía y el estado vacío ocupan todo el ancho; cada tarjeta ocupa una columna
        gestor.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(posicion: Int) =
                if (posicion == 0 || elementos.itemCount == 0) columnas else 1
        }

        b.lista.setPadding(contexto.dp(10), contexto.dp(10), contexto.dp(10), contexto.dp(16))
        b.lista.layoutManager = gestor
        b.lista.adapter = ConcatAdapter(cabecera, vacio, elementos)

        viewModel.elementos.observe(viewLifecycleOwner) { lista ->
            elementos.submitList(lista)
            vacio.visible = lista.isEmpty()
        }
    }
}
