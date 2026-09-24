package com.gmail.juliocesar64914.interfazconviews.pantallas

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import coil3.load
import com.gmail.juliocesar64914.interfazconviews.R
import com.gmail.juliocesar64914.interfazconviews.databinding.FragmentSeccion5InformacionBinding
import com.gmail.juliocesar64914.interfazconviews.databinding.HojaInferiorBinding
import com.gmail.juliocesar64914.interfazconviews.datos.CatalogoViewModel
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.color.MaterialColors
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt
import com.google.android.material.R as MR

class Seccion5InformacionFragment : Fragment(R.layout.fragment_seccion5_informacion) {

    private val viewModel: CatalogoViewModel by activityViewModels()
    private var _binding: FragmentSeccion5InformacionBinding? = null
    private val b get() = _binding!!

    private var escala = 1f
    private var progreso = 30
    private var archivados = 0
    private var toquesTarjetas = 0
    private var avisos = 3
    private var carrito = 0
    private var correoNuevo = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentSeccion5InformacionBinding.bind(view)
        configurarEstilos()
        configurarImagenes()
        configurarProgreso()
        configurarToastYSnackbar()
        configurarDialogo()
        configurarHojaInferior()
        configurarTarjetasYBadges()

        // Conexión con la Sección 3: la escala elegida cambia el tamaño de todos los textos
        viewModel.escalaTexto.observe(viewLifecycleOwner) { nueva ->
            escala = nueva
            b.avisoEscala.text = "Tamaño de texto: ${(nueva * 100).roundToInt()} %. " +
                "Cámbialo con el deslizador de la sección Elementos de selección."
            aplicarEscala(b.root, nueva)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Recorre todas las vistas y multiplica el tamaño de cada texto por la escala.
     * El tamaño original se guarda en una etiqueta para no acumular cambios.
     */
    private fun aplicarEscala(vista: View, escala: Float) {
        if (vista is TextView) {
            val original = vista.getTag(R.id.tamano_original) as? Float
                ?: vista.textSize.also { vista.setTag(R.id.tamano_original, it) }
            vista.setTextSize(TypedValue.COMPLEX_UNIT_PX, original * escala)
        }
        if (vista is ViewGroup) {
            for (i in 0 until vista.childCount) aplicarEscala(vista.getChildAt(i), escala)
        }
    }

    // ---------- 1. Estilos de texto ----------
    private fun configurarEstilos() {
        b.campoMuestra.doAfterTextChanged { actualizarEstilos() }
        b.grupoEnfasis.setOnCheckedStateChangeListener { _, _ -> actualizarEstilos() }
        actualizarEstilos()

        var expandido = false
        val alternar = {
            expandido = !expandido
            b.textoLargo.maxLines = if (expandido) Int.MAX_VALUE else 1
            b.botonVerMas.text = if (expandido) "Ver menos" else "Ver más"
        }
        b.botonVerMas.setOnClickListener { alternar() }
        b.textoLargo.setOnClickListener { alternar() }
    }

    private fun actualizarEstilos() {
        val texto = b.campoMuestra.text?.toString().orEmpty().ifBlank { "Texto de ejemplo" }
        listOf(b.muestraTitulo, b.muestraSubtitulo, b.muestraCuerpo, b.muestraEtiqueta).forEach { it.text = texto }

        val marcados = b.grupoEnfasis.checkedChipIds
        val negrita = R.id.chipNegrita in marcados
        val cursiva = R.id.chipCursiva in marcados

        // SpannableString: aplica estilos solo a una parte del texto
        val resultado = SpannableStringBuilder("Resultado: ")
        val inicio = resultado.length
        resultado.append(texto)
        val fin = resultado.length
        fun aplicar(estilo: Any) = resultado.setSpan(estilo, inicio, fin, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        when {
            negrita && cursiva -> aplicar(StyleSpan(Typeface.BOLD_ITALIC))
            negrita -> aplicar(StyleSpan(Typeface.BOLD))
            cursiva -> aplicar(StyleSpan(Typeface.ITALIC))
        }
        if (R.id.chipSubrayado in marcados) aplicar(UnderlineSpan())
        if (R.id.chipTachado in marcados) aplicar(StrikethroughSpan())
        if (R.id.chipColor in marcados) aplicar(ForegroundColorSpan(MaterialColors.getColor(b.root, androidx.appcompat.R.attr.colorError)))

        b.resultadoEnfasis.text = resultado
    }

    // ---------- 2. Imágenes ----------
    private fun configurarImagenes() {
        // Recorta las esquinas de las imágenes con la forma redondeada del fondo
        b.imagenLocal.clipToOutline = true
        b.imagenUrl.clipToOutline = true

        b.imagenUrl.load("https://picsum.photos/id/1018/800/500") {
            listener(
                onStart = { _binding?.estadoUrl?.text = "Cargando imagen desde internet…" },
                onSuccess = { _, _ -> _binding?.estadoUrl?.text = "Imagen de internet cargada." },
                onError = { _, _ -> _binding?.estadoUrl?.text = "No se pudo cargar: revisa la conexión a internet." }
            )
        }

        b.grupoEscala.setOnCheckedStateChangeListener { _, marcados -> aplicarModo(marcados.firstOrNull()) }
        aplicarModo(b.grupoEscala.checkedChipId)
    }

    private fun aplicarModo(idChip: Int?) {
        val (modo, explicacion) = when (idChip) {
            R.id.chipRecortar -> ImageView.ScaleType.CENTER_CROP to "CENTER_CROP: llena el espacio y recorta lo que sobra."
            R.id.chipEstirar -> ImageView.ScaleType.FIT_XY to "FIT_XY: llena el espacio deformando la imagen."
            R.id.chipOriginal -> ImageView.ScaleType.CENTER to "CENTER: tamaño real, sin escalar."
            else -> ImageView.ScaleType.FIT_CENTER to "FIT_CENTER: se ve completa, puede dejar espacios vacíos."
        }
        b.imagenLocal.scaleType = modo
        b.imagenUrl.scaleType = modo
        b.respuestaEscala.text = explicacion
    }

    // ---------- 3. Indicadores de progreso ----------
    private fun configurarProgreso() {
        mostrarProgreso(animar = false)
        b.botonMenos.setOnClickListener { progreso = max(0, progreso - 10); mostrarProgreso() }
        b.botonMas.setOnClickListener { progreso = min(100, progreso + 10); mostrarProgreso() }
        b.botonSimular.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                habilitarBotonesProgreso(false)
                progreso = 0
                mostrarProgreso()
                while (progreso < 100) {
                    delay(150)
                    progreso = min(100, progreso + 5)
                    mostrarProgreso()
                }
                habilitarBotonesProgreso(true)
            }
        }
        b.interruptorCarga.setOnCheckedChangeListener { _, activo ->
            b.filaIndeterminada.isVisible = activo
            b.textoCargaDetenida.isVisible = !activo
        }
    }

    private fun mostrarProgreso(animar: Boolean = true) {
        b.progresoLineal.setProgressCompat(progreso, animar)
        b.progresoCircular.setProgressCompat(progreso, animar)
        b.textoProgreso.text = "Determinado: $progreso %"
    }

    private fun habilitarBotonesProgreso(habilitar: Boolean) {
        listOf(b.botonMenos, b.botonMas, b.botonSimular).forEach { it.isEnabled = habilitar }
    }

    // ---------- 4. Toast y snackbar ----------
    private fun configurarToastYSnackbar() {
        b.botonToast.setOnClickListener {
            Toast.makeText(requireContext(), "Esto es un toast", Toast.LENGTH_SHORT).show()
        }
        b.botonArchivar.setOnClickListener {
            archivados++
            mostrarArchivados()
            Snackbar.make(requireView(), "Mensaje archivado", Snackbar.LENGTH_SHORT)
                .setAction("Deshacer") {
                    archivados = max(0, archivados - 1)
                    mostrarArchivados()
                }
                .show()
        }
    }

    private fun mostrarArchivados() {
        _binding?.respuestaArchivados?.text = "Mensajes archivados: $archivados"
    }

    // ---------- 5. Diálogo de confirmación (conectado con la Sección 3) ----------
    private fun configurarDialogo() {
        b.botonRestablecer.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("¿Restablecer el tamaño?")
                .setMessage("Los textos volverán al 100 %. Puedes cambiarlo de nuevo en Elementos de selección.")
                .setPositiveButton("Restablecer") { _, _ ->
                    viewModel.cambiarEscalaTexto(1f)
                    b.respuestaDialogo.text = "Confirmado: el tamaño volvió al 100 %."
                }
                .setNegativeButton("Cancelar") { _, _ ->
                    b.respuestaDialogo.text = "Cancelado: no se hizo ningún cambio."
                }
                .setOnCancelListener {
                    _binding?.respuestaDialogo?.text = "Diálogo cerrado sin elegir."
                }
                .show()
        }
    }

    // ---------- 6. Hoja inferior ----------
    private fun configurarHojaInferior() {
        b.botonHoja.setOnClickListener {
            val hoja = BottomSheetDialog(requireContext())
            val contenido = HojaInferiorBinding.inflate(layoutInflater)
            listOf(
                contenido.opcionCompartir to "Compartir",
                contenido.opcionEnlace to "Copiar enlace",
                contenido.opcionDescargar to "Descargar"
            ).forEach { (opcion, nombre) ->
                opcion.setOnClickListener {
                    _binding?.respuestaHoja?.text = "Elegiste: $nombre"
                    hoja.dismiss()
                }
            }
            aplicarEscala(contenido.root, escala)
            hoja.setContentView(contenido.root)
            hoja.show()
        }
    }

    // ---------- 7. Tarjetas, separadores y badges ----------
    @OptIn(ExperimentalBadgeUtils::class)
    private fun configurarTarjetasYBadges() {
        listOf(b.tarjetaRellena, b.tarjetaElevada, b.tarjetaContorno).forEach { tarjeta ->
            tarjeta.setOnClickListener {
                toquesTarjetas++
                b.respuestaTarjetas.text = "Tarjetas tocadas: $toquesTarjetas"
            }
        }

        val badgeAvisos = BadgeDrawable.create(requireContext())
        val badgeCarrito = BadgeDrawable.create(requireContext())
        val badgeCorreo = BadgeDrawable.create(requireContext()) // sin número = punto

        fun actualizarBadges() {
            badgeAvisos.number = avisos
            badgeAvisos.isVisible = avisos > 0
            badgeCarrito.number = carrito
            badgeCarrito.isVisible = carrito > 0
            badgeCorreo.isVisible = correoNuevo
            b.iconoAvisos.contentDescription = "Notificaciones: $avisos"
            b.iconoCarrito.contentDescription = "Carrito: $carrito"
        }

        // Los badges se colocan cuando los íconos ya tienen tamaño en pantalla
        val iconoAvisos = b.iconoAvisos
        val iconoCarrito = b.iconoCarrito
        val iconoCorreo = b.iconoCorreo
        val marcoAvisos = b.marcoAvisos
        val marcoCarrito = b.marcoCarrito
        val marcoCorreo = b.marcoCorreo
        iconoAvisos.post {
            BadgeUtils.attachBadgeDrawable(badgeAvisos, iconoAvisos, marcoAvisos)
            BadgeUtils.attachBadgeDrawable(badgeCarrito, iconoCarrito, marcoCarrito)
            BadgeUtils.attachBadgeDrawable(badgeCorreo, iconoCorreo, marcoCorreo)
        }

        b.botonMasAviso.setOnClickListener { avisos++; actualizarBadges() }
        b.botonMasCarrito.setOnClickListener { carrito++; actualizarBadges() }
        b.marcoCorreo.setOnClickListener { correoNuevo = !correoNuevo; actualizarBadges() }
        b.botonLimpiar.setOnClickListener {
            avisos = 0; carrito = 0; correoNuevo = false
            actualizarBadges()
        }
        actualizarBadges()
    }
}
