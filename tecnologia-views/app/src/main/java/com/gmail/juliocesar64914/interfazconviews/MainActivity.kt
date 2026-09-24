package com.gmail.juliocesar64914.interfazconviews

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.gmail.juliocesar64914.interfazconviews.databinding.ActivityMainBinding
import com.gmail.juliocesar64914.interfazconviews.navegacion.Seccion
import com.gmail.juliocesar64914.interfazconviews.navegacion.irAInicio
import com.gmail.juliocesar64914.interfazconviews.navegacion.irASeccion
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import kotlin.math.max

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    /** Evita que al seleccionar una pestaña desde el código se dispare otra navegación. */
    private var sincronizandoPestanas = false

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ajustarMargenesDelSistema()

        val navHost = supportFragmentManager.findFragmentById(R.id.contenedorNav) as NavHostFragment
        navController = navHost.navController

        configurarBarraSuperior()
        configurarPestanas()

        // Cada vez que cambia la pantalla, se actualizan título, flecha y pestaña
        navController.addOnDestinationChangedListener { _, destino, _ ->
            val seccion = Seccion.entries.find { it.destino == destino.id }
            binding.barraSuperior.title = seccion?.titulo ?: "Catálogo de elementos"
            if (seccion != null) {
                binding.barraSuperior.setNavigationIcon(R.drawable.ic_arrow_back)
                binding.barraSuperior.navigationContentDescription = "Regresar al inicio"
            } else {
                binding.barraSuperior.navigationIcon = null
            }
            val indice = seccion?.let { it.ordinal + 1 } ?: 0
            if (binding.pestanas.selectedTabPosition != indice) {
                sincronizandoPestanas = true
                binding.pestanas.getTabAt(indice)?.select()
                sincronizandoPestanas = false
            }
        }
    }

    /** La app se dibuja detrás de las barras del sistema; aquí se deja el espacio necesario. */
    private fun ajustarMargenesDelSistema() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.raiz) { vista, insets ->
            val barras = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val teclado = insets.getInsets(WindowInsetsCompat.Type.ime())
            vista.updatePadding(left = barras.left, right = barras.right)
            binding.appBar.updatePadding(top = barras.top)
            // Evita que el teclado tape los campos de texto
            binding.contenedorNav.updatePadding(bottom = max(barras.bottom, teclado.bottom))
            insets
        }
    }

    private fun configurarBarraSuperior() {
        binding.barraSuperior.inflateMenu(R.menu.menu_principal)
        binding.barraSuperior.setNavigationOnClickListener { navController.irAInicio() }
        binding.barraSuperior.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.accionTema) {
                mostrarSelectorTema()
                true
            } else false
        }
    }

    private fun configurarPestanas() {
        val pestanas = binding.pestanas
        pestanas.addTab(pestanas.newTab().setText("Inicio").setIcon(R.drawable.ic_home))
        Seccion.entries.forEach { seccion ->
            pestanas.addTab(pestanas.newTab().setText(seccion.tituloCorto).setIcon(seccion.icono))
        }
        pestanas.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (!sincronizandoPestanas) navegarAPestana(tab.position)
            }
            override fun onTabUnselected(tab: TabLayout.Tab) = Unit
            override fun onTabReselected(tab: TabLayout.Tab) {
                if (!sincronizandoPestanas) navegarAPestana(tab.position)
            }
        })
    }

    private fun navegarAPestana(posicion: Int) {
        if (posicion == 0) navController.irAInicio()
        else navController.irASeccion(Seccion.entries[posicion - 1])
    }

    /** Selector de tema: según el sistema (predeterminado), claro u oscuro. */
    private fun mostrarSelectorTema() {
        val opciones = arrayOf("Según el sistema", "Claro", "Oscuro")
        val modos = intArrayOf(
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM,
            AppCompatDelegate.MODE_NIGHT_NO,
            AppCompatDelegate.MODE_NIGHT_YES
        )
        val actual = modos.indexOf(AppCompatDelegate.getDefaultNightMode()).coerceAtLeast(0)

        MaterialAlertDialogBuilder(this)
            .setTitle("Tema de la aplicación")
            .setSingleChoiceItems(opciones, actual) { dialogo, elegido ->
                AppCompatDelegate.setDefaultNightMode(modos[elegido])
                dialogo.dismiss()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
