package com.gmail.juliocesar64914.interfazconviews.pantallas

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.gmail.juliocesar64914.interfazconviews.R
import com.gmail.juliocesar64914.interfazconviews.databinding.FragmentSeccion3SeleccionBinding
import com.gmail.juliocesar64914.interfazconviews.datos.CatalogoViewModel
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.roundToInt

class Seccion3SeleccionFragment : Fragment(R.layout.fragment_seccion3_seleccion) {

    private val viewModel: CatalogoViewModel by activityViewModels()
    private var _binding: FragmentSeccion3SeleccionBinding? = null
    private val b get() = _binding!!

    /** Evita ciclos al cambiar casillas desde el código. */
    private var actualizandoCasillas = false

    private var fechaMillis: Long? = null
    private var hora: Int? = null
    private var minuto = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentSeccion3SeleccionBinding.bind(view)
        configurarCasillas()
        configurarOpciones()
        configurarInterruptores()
        configurarDeslizadorUnico()
        configurarDeslizadorRango()
        configurarDesplegable()
        configurarFecha()
        configurarHora()
        configurarChips()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // ---------- 1. Casillas (con estado indeterminado) ----------
    private fun configurarCasillas() {
        val hijas = listOf(b.casillaLeche, b.casillaPan, b.casillaHuevos)

        fun actualizarPrincipal() {
            val marcadas = hijas.count { it.isChecked }
            val estado = when (marcadas) {
                hijas.size -> MaterialCheckBox.STATE_CHECKED
                0 -> MaterialCheckBox.STATE_UNCHECKED
                else -> MaterialCheckBox.STATE_INDETERMINATE
            }
            actualizandoCasillas = true
            b.casillaTodo.checkedState = estado
            actualizandoCasillas = false

            val texto = when (estado) {
                MaterialCheckBox.STATE_CHECKED -> "todas marcadas"
                MaterialCheckBox.STATE_UNCHECKED -> "ninguna marcada"
                else -> "indeterminado"
            }
            b.respuestaCasillas.text = "Marcados: $marcadas de ${hijas.size} · Estado: $texto"
        }

        hijas.forEach { casilla ->
            casilla.setOnCheckedChangeListener { _, _ ->
                if (!actualizandoCasillas) actualizarPrincipal()
            }
        }
        b.casillaTodo.addOnCheckedStateChangedListener { _, estado ->
            if (actualizandoCasillas) return@addOnCheckedStateChangedListener
            val marcar = estado == MaterialCheckBox.STATE_CHECKED
            actualizandoCasillas = true
            hijas.forEach { it.isChecked = marcar }
            actualizandoCasillas = false
            actualizarPrincipal()
        }
        actualizarPrincipal()
    }

    // ---------- 2. Botones de opción ----------
    private fun configurarOpciones() {
        b.grupoEnvio.setOnCheckedChangeListener { _, id ->
            val texto = when (id) {
                R.id.envioExpres -> "Exprés (2 a 3 días)"
                R.id.envioTienda -> "Recoger en tienda"
                else -> "Estándar (5 a 7 días)"
            }
            b.respuestaEnvio.text = "Tipo de envío: $texto"
        }
    }

    // ---------- 3. Interruptores ----------
    private fun configurarInterruptores() {
        val actualizar = {
            val notificaciones =
                if (b.interruptorNotificaciones.isChecked) "Recibirás notificaciones. " else "Notificaciones silenciadas. "
            val avion = if (b.interruptorAvion.isChecked) "Sin conexión." else "Conectado."
            b.respuestaInterruptores.text = notificaciones + avion
        }
        b.interruptorNotificaciones.setOnCheckedChangeListener { _, _ -> actualizar() }
        b.interruptorAvion.setOnCheckedChangeListener { _, _ -> actualizar() }
        actualizar()
    }

    // ---------- 4. Deslizador único: conectado con la Sección 5 ----------
    private fun configurarDeslizadorUnico() {
        // El deslizador trabaja en porcentaje (80 a 160) y el ViewModel guarda la escala (0.8 a 1.6)
        b.deslizadorTamano.setLabelFormatter { "${it.roundToInt()} %" }
        b.deslizadorTamano.addOnChangeListener { _, valor, desdeUsuario ->
            if (desdeUsuario) viewModel.cambiarEscalaTexto(valor / 100f)
        }
        viewModel.escalaTexto.observe(viewLifecycleOwner) { escala ->
            val porcentaje = ((escala * 10).roundToInt() * 10).toFloat().coerceIn(80f, 160f)
            if (b.deslizadorTamano.value != porcentaje) b.deslizadorTamano.value = porcentaje
            b.textoTamano.text = "Tamaño de texto: ${porcentaje.roundToInt()} %"
            b.textoTamano.textSize = 16f * escala // en sp
        }
    }

    // ---------- 5. Deslizador de rango ----------
    private fun configurarDeslizadorRango() {
        b.deslizadorRango.values = listOf(300f, 1500f)
        b.deslizadorRango.setLabelFormatter { "\$${it.roundToInt()}" }
        val actualizar = {
            val (minimo, maximo) = b.deslizadorRango.values
            b.respuestaRango.text = "Precio: de \$${minimo.roundToInt()} a \$${maximo.roundToInt()} MXN"
        }
        b.deslizadorRango.addOnChangeListener { _, _, _ -> actualizar() }
        actualizar()
    }

    // ---------- 6. Lista desplegable ----------
    private fun configurarDesplegable() {
        b.campoCarrera.setSimpleItems(CARRERAS)
        b.campoCarrera.setOnItemClickListener { _, _, posicion, _ ->
            b.respuestaCarrera.text = "Seleccionaste: ${CARRERAS[posicion]}"
        }
    }

    // ---------- 7. Selector de fecha ----------
    private fun configurarFecha() {
        b.botonFecha.setOnClickListener {
            val selector = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Selecciona una fecha")
                .setSelection(fechaMillis ?: MaterialDatePicker.todayInUtcMilliseconds())
                .build()
            selector.addOnPositiveButtonClickListener { millis ->
                fechaMillis = millis
                b.respuestaFecha.text = "Fecha: ${FORMATO_FECHA.format(Date(millis))}"
            }
            selector.show(childFragmentManager, "selectorFecha")
        }
    }

    // ---------- 8. Selector de hora ----------
    private fun configurarHora() {
        b.botonHora.setOnClickListener {
            val selector = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(hora ?: 12)
                .setMinute(minuto)
                .setTitleText("Selecciona la hora")
                .build()
            selector.addOnPositiveButtonClickListener {
                hora = selector.hour
                minuto = selector.minute
                b.respuestaHora.text = "Hora: %02d:%02d".format(selector.hour, selector.minute)
            }
            selector.show(childFragmentManager, "selectorHora")
        }
    }

    // ---------- 9. Chips de filtro ----------
    private fun configurarChips() {
        val productos = mapOf(
            R.id.chipFrutas to listOf("Manzana", "Mango", "Fresa"),
            R.id.chipVerduras to listOf("Zanahoria", "Nopal", "Brócoli"),
            R.id.chipLacteos to listOf("Leche", "Queso", "Yogur"),
            R.id.chipCereales to listOf("Avena", "Arroz", "Maíz")
        )
        val actualizar = { marcados: List<Int> ->
            val visibles = productos.filterKeys { it in marcados }.values.flatten()
            b.respuestaChips.text =
                if (visibles.isEmpty()) "Activa al menos un filtro para ver productos."
                else "Productos: ${visibles.joinToString(", ")}"
        }
        b.grupoChips.setOnCheckedStateChangeListener { _, marcados -> actualizar(marcados) }
        actualizar(b.grupoChips.checkedChipIds)
    }

    companion object {
        private val CARRERAS = arrayOf(
            "Ingeniería en Sistemas Computacionales",
            "Ingeniería en Inteligencia Artificial",
            "Licenciatura en Ciencia de Datos",
            "Ingeniería Mecatrónica",
            "Ingeniería Industrial"
        )

        // El selector entrega la fecha en UTC, por eso se formatea en UTC
        private val FORMATO_FECHA = SimpleDateFormat(
            "EEEE d 'de' MMMM 'de' yyyy", Locale.forLanguageTag("es-MX")
        ).apply { timeZone = TimeZone.getTimeZone("UTC") }
    }
}
