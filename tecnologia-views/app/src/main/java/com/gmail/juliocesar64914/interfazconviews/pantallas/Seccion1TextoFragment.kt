package com.gmail.juliocesar64914.interfazconviews.pantallas

import android.os.Bundle
import android.util.Patterns
import android.view.Gravity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.widget.TextViewCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.gmail.juliocesar64914.interfazconviews.R
import com.gmail.juliocesar64914.interfazconviews.componentes.dp
import com.gmail.juliocesar64914.interfazconviews.componentes.ocultarTeclado
import com.gmail.juliocesar64914.interfazconviews.componentes.sinAcentos
import com.gmail.juliocesar64914.interfazconviews.databinding.FragmentSeccion1TextoBinding
import com.gmail.juliocesar64914.interfazconviews.datos.CatalogoViewModel

class Seccion1TextoFragment : Fragment(R.layout.fragment_seccion1_texto) {

    private val viewModel: CatalogoViewModel by activityViewModels()
    private var _binding: FragmentSeccion1TextoBinding? = null
    private val b get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentSeccion1TextoBinding.bind(view)
        configurarCampoSimple()
        configurarValidacion()
        configurarContrasena()
        configurarTeclados()
        configurarSugerencias()
        configurarBusqueda()
        configurarConexion()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // ---------- 1. Campo simple ----------
    private fun configurarCampoSimple() {
        b.campoNombre.doAfterTextChanged { texto ->
            val nombre = texto?.toString()?.trim().orEmpty()
            b.respuestaNombre.text = if (nombre.isEmpty()) "Aún no has escrito nada." else "Hola, $nombre."
        }
    }

    // ---------- 2. Campo con validación ----------
    private fun configurarValidacion() {
        b.capaUsuario.isEndIconVisible = false
        b.campoUsuario.doAfterTextChanged { texto ->
            val usuario = texto?.toString().orEmpty()
            val error = when {
                usuario.isEmpty() -> null
                usuario.length < 4 -> "Debe tener al menos 4 caracteres."
                !usuario.all { it.isLetterOrDigit() || it == '_' } -> "Solo se permiten letras, números y guion bajo."
                else -> null
            }
            val valido = usuario.isNotEmpty() && error == null
            b.capaUsuario.error = error
            b.capaUsuario.isEndIconVisible = valido
            b.capaUsuario.helperText =
                if (valido) "Usuario válido." else "Mínimo 4 caracteres: letras, números o guion bajo."
        }
    }

    // ---------- 3. Contraseña ----------
    private fun configurarContrasena() {
        b.campoContrasena.doAfterTextChanged { texto ->
            val c = texto?.toString().orEmpty()
            val puntos = listOf(
                c.length >= 8,
                c.any { it.isUpperCase() },
                c.any { it.isDigit() },
                c.any { !it.isLetterOrDigit() }
            ).count { it }
            b.barraSeguridad.setProgressCompat(puntos, true)
            b.textoSeguridad.text = when {
                c.isEmpty() -> "Escribe una contraseña."
                puntos <= 1 -> "Seguridad: débil"
                puntos <= 3 -> "Seguridad: media"
                else -> "Seguridad: fuerte"
            }
        }
    }

    // ---------- 4. Tipos de teclado ----------
    private fun configurarTeclados() {
        val actualizar = {
            val edad = b.campoEdad.text?.toString().orEmpty()
            val correo = b.campoCorreo.text?.toString()?.trim().orEmpty()
            val telefono = b.campoTelefono.text?.toString().orEmpty()
            val estadoCorreo = when {
                correo.isEmpty() -> "—"
                Patterns.EMAIL_ADDRESS.matcher(correo).matches() -> "válido"
                else -> "incompleto"
            }
            b.respuestaTeclados.text =
                "Edad: ${edad.ifEmpty { "—" }} · Correo: $estadoCorreo · Teléfono: ${telefono.length}/10"
        }
        listOf(b.campoEdad, b.campoCorreo, b.campoTelefono).forEach { campo ->
            campo.doAfterTextChanged { actualizar() }
        }
        actualizar()
    }

    // ---------- 6. Sugerencias ----------
    private fun configurarSugerencias() {
        val adaptador = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            LENGUAJES
        )
        b.campoLenguaje.setAdapter(adaptador)
        b.campoLenguaje.doAfterTextChanged { texto ->
            val elegido = LENGUAJES.firstOrNull { it.equals(texto?.toString()?.trim(), ignoreCase = true) }
            b.respuestaLenguaje.text =
                if (elegido != null) "Elegiste: $elegido" else "Escribe una letra o toca la flecha."
        }
    }

    // ---------- 7. Barra de búsqueda ----------
    private fun configurarBusqueda() {
        b.campoBusqueda.doAfterTextChanged { mostrarResultados(it?.toString().orEmpty()) }
        b.campoBusqueda.setOnEditorActionListener { _, accion, _ ->
            if (accion == EditorInfo.IME_ACTION_SEARCH) {
                ocultarTeclado()
                true
            } else false
        }
        mostrarResultados("")
    }

    private fun mostrarResultados(consulta: String) {
        val texto = consulta.trim()
        b.resultadosBusqueda.removeAllViews()
        if (texto.isEmpty()) {
            b.respuestaBusqueda.text = "Escribe para buscar entre los 32 estados."
            return
        }
        val resultados = ESTADOS.filter { it.sinAcentos().contains(texto.sinAcentos(), ignoreCase = true) }
        if (resultados.isEmpty()) {
            b.respuestaBusqueda.text = "No hay estados que coincidan con «$texto»."
            return
        }
        resultados.take(5).forEach { b.resultadosBusqueda.addView(crearFilaResultado(it)) }
        b.respuestaBusqueda.text =
            if (resultados.size > 5) "y ${resultados.size - 5} resultados más…"
            else "${resultados.size} ${if (resultados.size == 1) "resultado" else "resultados"}"
    }

    private fun crearFilaResultado(estado: String) = TextView(requireContext()).apply {
        text = estado
        TextViewCompat.setTextAppearance(this, com.google.android.material.R.style.TextAppearance_Material3_BodyLarge)
        setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_place, 0, 0, 0)
        compoundDrawablePadding = context.dp(16)
        gravity = Gravity.CENTER_VERTICAL
        setPadding(context.dp(8), context.dp(10), context.dp(8), context.dp(10))
    }

    // ---------- 8. Conexión con la Sección 4 ----------
    private fun configurarConexion() {
        b.botonAgregar.isEnabled = false
        b.campoNuevo.doAfterTextChanged { b.botonAgregar.isEnabled = !it.isNullOrBlank() }
        b.botonAgregar.setOnClickListener { agregarALista() }
        b.campoNuevo.setOnEditorActionListener { _, accion, _ ->
            if (accion == EditorInfo.IME_ACTION_DONE) {
                agregarALista()
                true
            } else false
        }
    }

    private fun agregarALista() {
        val texto = b.campoNuevo.text?.toString()?.trim().orEmpty()
        if (texto.isEmpty()) return
        viewModel.agregarElemento(texto)
        b.respuestaAgregar.text =
            "«$texto» se agregó. La lista de la Sección 4 ahora tiene ${viewModel.cantidad} elementos."
        b.respuestaAgregar.isVisible = true
        b.campoNuevo.text?.clear()
        ocultarTeclado()
    }

    companion object {
        private val LENGUAJES = listOf(
            "Kotlin", "Java", "Dart", "Swift", "JavaScript", "TypeScript",
            "Python", "C#", "C++", "Go", "Rust", "Ruby", "PHP"
        )

        private val ESTADOS = listOf(
            "Aguascalientes", "Baja California", "Baja California Sur", "Campeche", "Chiapas",
            "Chihuahua", "Ciudad de México", "Coahuila", "Colima", "Durango", "Estado de México",
            "Guanajuato", "Guerrero", "Hidalgo", "Jalisco", "Michoacán", "Morelos", "Nayarit",
            "Nuevo León", "Oaxaca", "Puebla", "Querétaro", "Quintana Roo", "San Luis Potosí",
            "Sinaloa", "Sonora", "Tabasco", "Tamaulipas", "Tlaxcala", "Veracruz", "Yucatán", "Zacatecas"
        )
    }
}
