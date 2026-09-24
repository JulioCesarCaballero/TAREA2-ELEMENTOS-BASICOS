package com.gmail.juliocesar64914.interfazconviews.pantallas

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.gmail.juliocesar64914.interfazconviews.R
import com.gmail.juliocesar64914.interfazconviews.databinding.FragmentInicioBinding
import com.gmail.juliocesar64914.interfazconviews.databinding.ItemSeccionBinding
import com.gmail.juliocesar64914.interfazconviews.datos.CatalogoViewModel
import com.gmail.juliocesar64914.interfazconviews.navegacion.Seccion
import com.gmail.juliocesar64914.interfazconviews.navegacion.irASeccion

class InicioFragment : Fragment(R.layout.fragment_inicio) {

    private val viewModel: CatalogoViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val b = FragmentInicioBinding.bind(view)

        // Una tarjeta por sección, creada a partir del enum Seccion
        Seccion.entries.forEach { seccion ->
            val tarjeta = ItemSeccionBinding.inflate(layoutInflater, b.listaSecciones, false)
            tarjeta.icono.setImageResource(seccion.icono)
            tarjeta.titulo.text = seccion.titulo
            tarjeta.descripcion.text = seccion.descripcion
            tarjeta.root.setOnClickListener { findNavController().irASeccion(seccion) }
            b.listaSecciones.addView(tarjeta.root)
        }

        viewModel.elementos.observe(viewLifecycleOwner) { lista ->
            b.contador.text = "La lista de la Sección 4 tiene ${lista.size} elementos."
        }
    }
}
