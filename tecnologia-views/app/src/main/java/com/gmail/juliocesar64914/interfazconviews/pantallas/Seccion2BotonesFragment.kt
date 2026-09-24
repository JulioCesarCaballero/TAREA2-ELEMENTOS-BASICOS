package com.gmail.juliocesar64914.interfazconviews.pantallas

import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.gmail.juliocesar64914.interfazconviews.R
import com.gmail.juliocesar64914.interfazconviews.databinding.FragmentSeccion2BotonesBinding
import com.google.android.material.progressindicator.CircularProgressIndicatorSpec
import com.google.android.material.progressindicator.IndeterminateDrawable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Seccion2BotonesFragment : Fragment(R.layout.fragment_seccion2_botones) {

    private var _binding: FragmentSeccion2BotonesBinding? = null
    private val b get() = _binding!!

    private var pulsaciones = 0
    private var tareas = 0
    private var notas = 0
    private var envios = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentSeccion2BotonesBinding.bind(view)
        configurarBasicos()
        configurarConIcono()
        configurarFlotantes()
        configurarAlternancia()
        configurarSegmentados()
        configurarDeshabilitado()
        configurarCarga()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // ---------- 1. Relleno, contorno y texto ----------
    private fun configurarBasicos() {
        val botones = mapOf(
            b.botonRelleno to "Relleno",
            b.botonContorno to "Con contorno",
            b.botonTexto to "De texto",
            b.botonTonal to "Tonal",
            b.botonElevado to "Elevado"
        )
        botones.forEach { (boton, nombre) ->
            boton.setOnClickListener {
                pulsaciones++
                b.respuestaBasicos.text = "Último pulsado: $nombre · Total: $pulsaciones"
            }
        }
    }

    // ---------- 2. Botones con ícono ----------
    private fun configurarConIcono() {
        val mensajes = mapOf(
            b.botonCompartir to "Compartir (botón de ícono estándar)",
            b.botonEditar to "Editar (botón de ícono relleno)",
            b.botonEliminar to "Eliminar (botón de ícono con contorno)",
            b.botonEnviar to "Mensaje enviado",
            b.botonDescargar to "Descarga iniciada"
        )
        mensajes.forEach { (boton, mensaje) ->
            boton.setOnClickListener { b.respuestaIconos.text = mensaje }
        }
    }

    // ---------- 3. Botones flotantes ----------
    private fun configurarFlotantes() {
        b.fabNormal.setOnClickListener {
            tareas++
            b.textoTareas.text = "Tareas creadas: $tareas"
        }
        b.fabExtendido.setOnClickListener {
            notas++
            b.textoNotas.text = "Notas creadas: $notas"
        }
        b.botonContraer.setOnClickListener {
            if (b.fabExtendido.isExtended) {
                b.fabExtendido.shrink()
                b.botonContraer.text = "Expandir FAB extendido"
            } else {
                b.fabExtendido.extend()
                b.botonContraer.text = "Contraer FAB extendido"
            }
        }
    }

    // ---------- 4. Alternancia ----------
    private fun configurarAlternancia() {
        val actualizar = {
            b.respuestaAlternancia.text =
                "Favorito: ${if (b.botonFavorito.isChecked) "sí" else "no"} · " +
                    "Guardado: ${if (b.botonGuardar.isChecked) "sí" else "no"}"
        }
        b.botonFavorito.addOnCheckedChangeListener { boton, marcado ->
            boton.setIconResource(if (marcado) R.drawable.ic_favorite else R.drawable.ic_favorite_border)
            actualizar()
        }
        b.botonGuardar.addOnCheckedChangeListener { boton, marcado ->
            boton.setIconResource(if (marcado) R.drawable.ic_bookmark else R.drawable.ic_bookmark_border)
            actualizar()
        }
    }

    // ---------- 5. Selector segmentado ----------
    private fun configurarSegmentados() {
        b.grupoVista.addOnButtonCheckedListener { _, id, marcado ->
            if (!marcado) return@addOnButtonCheckedListener
            val nombre = when (id) {
                R.id.opcionCuadricula -> "Cuadrícula"
                R.id.opcionMosaico -> "Mosaico"
                else -> "Lista"
            }
            b.respuestaVista.text = "Vista seleccionada: $nombre"
        }

        b.grupoFormato.addOnButtonCheckedListener { grupo, _, _ ->
            val marcados = grupo.checkedButtonIds
            val negrita = R.id.opcionNegrita in marcados
            val cursiva = R.id.opcionCursiva in marcados
            val estilo = when {
                negrita && cursiva -> Typeface.BOLD_ITALIC
                negrita -> Typeface.BOLD
                cursiva -> Typeface.ITALIC
                else -> Typeface.NORMAL
            }
            b.textoFormato.setTypeface(Typeface.create(b.textoFormato.typeface, Typeface.NORMAL), estilo)
            b.textoFormato.paintFlags =
                if (R.id.opcionSubrayado in marcados) b.textoFormato.paintFlags or Paint.UNDERLINE_TEXT_FLAG
                else b.textoFormato.paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv()
        }
    }

    // ---------- 6. Deshabilitado ----------
    private fun configurarDeshabilitado() {
        b.casillaTerminos.setOnCheckedChangeListener { _, marcado ->
            b.botonCrearCuenta.isEnabled = marcado
            b.respuestaDeshabilitado.text =
                if (marcado) "El botón ya está habilitado." else "El botón está deshabilitado: marca la casilla."
        }
        b.botonCrearCuenta.setOnClickListener {
            b.respuestaDeshabilitado.text = "Cuenta creada correctamente."
        }
    }

    // ---------- 7. Carga ----------
    private fun configurarCarga() {
        val boton = b.botonEnviarFormulario
        boton.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                // El ícono se cambia por un indicador circular que gira
                val spec = CircularProgressIndicatorSpec(
                    requireContext(), null, 0,
                    com.google.android.material.R.style.Widget_Material3_CircularProgressIndicator_ExtraSmall
                )
                boton.icon = IndeterminateDrawable.createCircularDrawable(requireContext(), spec)
                boton.text = "Enviando…"
                boton.isEnabled = false
                b.respuestaCarga.text = "Enviando datos, espera…"

                delay(2000) // simula una operación de red

                envios++
                boton.setIconResource(R.drawable.ic_send)
                boton.text = "Enviar formulario"
                boton.isEnabled = true
                b.respuestaCarga.text = "Formulario enviado ($envios ${if (envios == 1) "vez" else "veces"})."
            }
        }
    }
}
