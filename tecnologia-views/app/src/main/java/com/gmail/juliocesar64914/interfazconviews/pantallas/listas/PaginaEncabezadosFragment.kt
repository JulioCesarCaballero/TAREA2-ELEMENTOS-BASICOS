package com.gmail.juliocesar64914.interfazconviews.pantallas.listas

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.gmail.juliocesar64914.interfazconviews.R
import com.gmail.juliocesar64914.interfazconviews.databinding.GuiaSimpleBinding
import com.gmail.juliocesar64914.interfazconviews.databinding.ItemEstadoVacioBinding
import com.gmail.juliocesar64914.interfazconviews.databinding.PaginaRecyclerBinding
import com.gmail.juliocesar64914.interfazconviews.datos.CatalogoViewModel

/** Pestaña "Encabezados": lista con dos tipos de elemento (encabezado y fila). */
class PaginaEncabezadosFragment : Fragment(R.layout.pagina_recycler) {

    private val viewModel: CatalogoViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val b = PaginaRecyclerBinding.bind(view)

        val filas = AdaptadorAgrupado { mostrarDetalle(it, viewModel) }
        val cabecera = AdaptadorUnico { padre ->
            GuiaSimpleBinding.inflate(layoutInflater, padre, false).also {
                it.nombre.text = "Lista con encabezados de sección"
                it.texto.text = "Combina dos tipos de elemento: encabezados que agrupan por " +
                    "categoría y filas de contenido. Cada tipo usa su propio diseño."
            }.root
        }
        val vacio = AdaptadorUnico { padre ->
            ItemEstadoVacioBinding.inflate(layoutInflater, padre, false).also {
                it.botonRestaurar.setOnClickListener { viewModel.reiniciarLista() }
            }.root
        }.apply { visible = false }

        b.lista.layoutManager = LinearLayoutManager(requireContext())
        b.lista.adapter = ConcatAdapter(cabecera, vacio, filas)

        viewModel.elementos.observe(viewLifecycleOwner) { lista ->
            filas.submitList(agruparPorCategoria(lista))
            vacio.visible = lista.isEmpty()
        }
    }
}
