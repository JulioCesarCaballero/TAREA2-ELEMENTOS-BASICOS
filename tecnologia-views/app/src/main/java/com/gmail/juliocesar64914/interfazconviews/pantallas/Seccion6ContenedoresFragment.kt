package com.gmail.juliocesar64914.interfazconviews.pantallas

import android.os.Bundle
import android.transition.TransitionManager
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView
import androidx.core.widget.TextViewCompat
import androidx.fragment.app.Fragment
import com.gmail.juliocesar64914.interfazconviews.R
import com.gmail.juliocesar64914.interfazconviews.databinding.FragmentSeccion6ContenedoresBinding
import kotlin.math.roundToInt

class Seccion6ContenedoresFragment : Fragment(R.layout.fragment_seccion6_contenedores) {

    private var _binding: FragmentSeccion6ContenedoresBinding? = null
    private val b get() = _binding!!

    private var favorito = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentSeccion6ContenedoresBinding.bind(view)
        configurarFilaColumna()
        configurarSuperpuesta()
        configurarDesplazamiento()
        configurarBarraSuperior()
        configurarNavegacionInferior()
        configurarMenuLateral()
        configurarRestricciones()
        configurarPesos()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // ---------- 1. Fila y columna (LinearLayout) ----------
    private fun configurarFilaColumna() {
        b.grupoGravedad.setOnCheckedStateChangeListener { _, marcados ->
            aplicarGravedad(marcados.firstOrNull() ?: R.id.chipInicio)
        }
        aplicarGravedad(b.grupoGravedad.checkedChipId)
    }

    private fun aplicarGravedad(idChip: Int) {
        val (horizontal, vertical, nombre) = when (idChip) {
            R.id.chipCentro -> Triple(Gravity.CENTER_HORIZONTAL, Gravity.CENTER_VERTICAL, "Centro")
            R.id.chipFinal -> Triple(Gravity.END, Gravity.BOTTOM, "Final")
            else -> Triple(Gravity.START, Gravity.TOP, "Inicio")
        }
        // Anima el cambio de posición
        TransitionManager.beginDelayedTransition(b.fila)
        TransitionManager.beginDelayedTransition(b.columna)
        b.fila.gravity = horizontal or Gravity.CENTER_VERTICAL
        b.columna.gravity = vertical or Gravity.CENTER_HORIZONTAL
        b.respuestaGravedad.text = "Gravedad aplicada: $nombre"
    }

    // ---------- 2. Superpuesta (FrameLayout) ----------
    private fun configurarSuperpuesta() {
        b.grupoCapa.setOnCheckedStateChangeListener { _, marcados ->
            aplicarPosicionCapa(marcados.firstOrNull() ?: R.id.chipAbajoDer)
        }
        aplicarPosicionCapa(b.grupoCapa.checkedChipId)
    }

    private fun aplicarPosicionCapa(idChip: Int) {
        val (gravedad, nombre) = when (idChip) {
            R.id.chipArribaIzq -> (Gravity.TOP or Gravity.START) to "Arriba izq."
            R.id.chipCapaCentro -> Gravity.CENTER to "Centro"
            else -> (Gravity.BOTTOM or Gravity.END) to "Abajo der."
        }
        TransitionManager.beginDelayedTransition(b.marcoCapas)
        val parametros = b.etiquetaCapa.layoutParams as FrameLayout.LayoutParams
        parametros.gravity = gravedad
        b.etiquetaCapa.layoutParams = parametros
        b.respuestaCapa.text = "La capa superior está en: $nombre"
    }

    // ---------- 3. Contenedor con desplazamiento ----------
    private fun configurarDesplazamiento() {
        repeat(15) { i ->
            val parrafo = TextView(requireContext()).apply {
                text = "Párrafo ${i + 1}. Este texto forma parte de un contenido largo que no cabe completo en el recuadro."
                TextViewCompat.setTextAppearance(this, com.google.android.material.R.style.TextAppearance_Material3_BodyMedium)
            }
            b.contenidoDesplazable.addView(parrafo)
        }

        val desplazable = b.desplazable
        fun maximo() = (b.contenidoDesplazable.height - desplazable.height).coerceAtLeast(0)

        desplazable.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, y, _, _ ->
            val max = maximo()
            val porcentaje = if (max > 0) y * 100 / max else 0
            _binding?.respuestaDesplazamiento?.text = "Recorrido: $porcentaje %"
        })
        b.botonIrInicio.setOnClickListener { desplazable.smoothScrollTo(0, 0) }
        b.botonIrFinal.setOnClickListener { desplazable.smoothScrollTo(0, maximo()) }
    }

    // ---------- 4. Barra superior ----------
    private fun configurarBarraSuperior() {
        b.barraDemo.setNavigationOnClickListener { b.respuestaBarra.text = "Acción: Regresar" }
        b.barraDemo.setOnMenuItemClickListener { item ->
            b.respuestaBarra.text = when (item.itemId) {
                R.id.accionBuscar -> "Acción: Buscar"
                R.id.accionFavorito -> {
                    favorito = !favorito
                    item.setIcon(if (favorito) R.drawable.ic_favorite else R.drawable.ic_favorite_border)
                    if (favorito) "Acción: agregado a favoritos" else "Acción: quitado de favoritos"
                }
                R.id.accionOrdenar -> "Menú: Ordenar"
                R.id.accionCompartir -> "Menú: Compartir"
                else -> "Menú: Ajustes"
            }
            true
        }
    }

    // ---------- 5. Barra de navegación inferior ----------
    private fun configurarNavegacionInferior() {
        val nav = b.navInferior
        // Está dentro de una tarjeta: se ignoran los márgenes de la barra del sistema
        ViewCompat.setOnApplyWindowInsetsListener(nav) { _, insets -> insets }

        nav.getOrCreateBadge(R.id.navPerfil).number = 2

        nav.setOnItemSelectedListener { item ->
            val (icono, nombre) = when (item.itemId) {
                R.id.navBuscar -> R.drawable.ic_search to "Buscar"
                R.id.navPerfil -> R.drawable.ic_person to "Perfil"
                else -> R.drawable.ic_home to "Inicio"
            }
            if (item.itemId == R.id.navPerfil) nav.removeBadge(R.id.navPerfil)
            b.iconoDestino.setImageResource(icono)
            b.textoDestino.text = "Pantalla: $nombre"
            true
        }
    }

    // ---------- 6. Menú lateral ----------
    private fun configurarMenuLateral() {
        val cajon = b.cajonDemo
        // Tampoco debe dejar espacio para la barra de estado
        ViewCompat.setOnApplyWindowInsetsListener(b.navLateral) { _, insets -> insets }
        b.cajonDemo.clipToOutline = true

        b.botonAbrirMenu.setOnClickListener { cajon.openDrawer(GravityCompat.START) }
        b.navLateral.setNavigationItemSelectedListener { item ->
            item.isChecked = true
            val icono = when (item.itemId) {
                R.id.ladoFavoritos -> R.drawable.ic_favorite
                R.id.ladoAjustes -> R.drawable.ic_settings
                else -> R.drawable.ic_home
            }
            b.tituloCajon.text = item.title
            b.iconoCajon.setImageResource(icono)
            cajon.closeDrawer(GravityCompat.START)
            true
        }
    }

    // ---------- 7. Restricciones (ConstraintLayout) ----------
    private fun configurarRestricciones() {
        b.grupoCadena.setOnCheckedStateChangeListener { _, marcados ->
            aplicarCadena(marcados.firstOrNull() ?: R.id.chipExtendida)
        }
        aplicarCadena(b.grupoCadena.checkedChipId)

        b.deslizadorGuia.setLabelFormatter { "${it.roundToInt()} %" }
        b.deslizadorGuia.addOnChangeListener { _, valor, _ -> aplicarGuia(valor) }
        aplicarGuia(b.deslizadorGuia.value)
    }

    private fun aplicarCadena(idChip: Int) {
        val (estilo, explicacion) = when (idChip) {
            R.id.chipInterna -> ConstraintSet.CHAIN_SPREAD_INSIDE to
                "Interna (spread_inside): los extremos tocan los bordes y el espacio queda entre ellos."
            R.id.chipAgrupada -> ConstraintSet.CHAIN_PACKED to
                "Agrupada (packed): las vistas se juntan en el centro."
            else -> ConstraintSet.CHAIN_SPREAD to
                "Extendida (spread): el espacio se reparte igual alrededor de cada vista."
        }
        // ConstraintSet modifica las restricciones desde el código
        val conjunto = ConstraintSet()
        conjunto.clone(b.restriccionesCadena)
        conjunto.setHorizontalChainStyle(R.id.cadenaA, estilo)
        TransitionManager.beginDelayedTransition(b.restriccionesCadena)
        conjunto.applyTo(b.restriccionesCadena)
        b.respuestaCadena.text = explicacion
    }

    private fun aplicarGuia(porcentaje: Float) {
        b.guia.setGuidelinePercent(porcentaje / 100f)
        b.bloqueIzquierdo.text = "${porcentaje.roundToInt()} %"
        b.bloqueDerecho.text = "${100 - porcentaje.roundToInt()} %"
    }

    // ---------- 8. Pesos proporcionales ----------
    private fun configurarPesos() {
        b.deslizadorPesoA.addOnChangeListener { _, _, _ -> aplicarPesos() }
        b.deslizadorPesoB.addOnChangeListener { _, _, _ -> aplicarPesos() }
        aplicarPesos()
    }

    private fun aplicarPesos() {
        val pesoA = b.deslizadorPesoA.value
        val pesoB = b.deslizadorPesoB.value
        val pesoC = 1f
        val total = pesoA + pesoB + pesoC

        TransitionManager.beginDelayedTransition(b.filaPesos)
        listOf(b.bloqueA to pesoA, b.bloqueB to pesoB, b.bloqueC to pesoC).forEachIndexed { i, (bloque, peso) ->
            val parametros = bloque.layoutParams as LinearLayout.LayoutParams
            parametros.weight = peso
            bloque.layoutParams = parametros
            bloque.text = "${"ABC"[i]}\n${(peso / total * 100).roundToInt()} %"
        }
        b.etiquetaPesoA.text = "Peso de A: ${pesoA.roundToInt()}"
        b.etiquetaPesoB.text = "Peso de B: ${pesoB.roundToInt()}"
        b.respuestaPesos.text = "C tiene peso fijo de 1. Total de pesos: ${total.roundToInt()}"
    }
}
