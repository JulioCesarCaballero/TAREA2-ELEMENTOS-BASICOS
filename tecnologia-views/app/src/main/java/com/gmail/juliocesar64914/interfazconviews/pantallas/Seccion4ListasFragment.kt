package com.gmail.juliocesar64914.interfazconviews.pantallas

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.gmail.juliocesar64914.interfazconviews.R
import com.gmail.juliocesar64914.interfazconviews.databinding.FragmentSeccion4ListasBinding
import com.gmail.juliocesar64914.interfazconviews.pantallas.listas.PaginaCuadriculaFragment
import com.gmail.juliocesar64914.interfazconviews.pantallas.listas.PaginaEncabezadosFragment
import com.gmail.juliocesar64914.interfazconviews.pantallas.listas.PaginaListaFragment
import com.google.android.material.tabs.TabLayoutMediator

/** Sección 4: pestañas (TabLayout) + páginas deslizables (ViewPager2). */
class Seccion4ListasFragment : Fragment(R.layout.fragment_seccion4_listas) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val b = FragmentSeccion4ListasBinding.bind(view)

        b.paginador.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = PESTANAS.size
            override fun createFragment(position: Int): Fragment = when (position) {
                0 -> PaginaListaFragment()
                1 -> PaginaCuadriculaFragment()
                else -> PaginaEncabezadosFragment()
            }
        }

        // Une las pestañas con el paginador: tocar una pestaña cambia de página y viceversa
        TabLayoutMediator(b.pestanasListas, b.paginador) { pestana, posicion ->
            pestana.text = PESTANAS[posicion]
        }.attach()
    }

    companion object {
        private val PESTANAS = listOf("Lista", "Cuadrícula", "Encabezados")
    }
}
