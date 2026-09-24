package com.gmail.juliocesar64914.interfazconviews.componentes

import android.content.Context
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import java.text.Normalizer

/** Convierte dp a píxeles. */
fun Context.dp(valor: Int): Int = (valor * resources.displayMetrics.density).toInt()

/** Cierra el teclado en pantalla. */
fun Fragment.ocultarTeclado() {
    val vista = view ?: return
    WindowCompat.getInsetsController(requireActivity().window, vista).hide(WindowInsetsCompat.Type.ime())
}

/** Quita acentos para que "queretaro" encuentre "Querétaro". */
fun String.sinAcentos(): String =
    Normalizer.normalize(this, Normalizer.Form.NFD).replace(Regex("\\p{Mn}+"), "")
