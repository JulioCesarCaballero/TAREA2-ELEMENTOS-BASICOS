package com.gmail.juliocesar64914.interfazconviews.pantallas

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.TextViewCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.gmail.juliocesar64914.interfazconviews.componentes.dp

/** Pantalla temporal para las secciones que todavía no se construyen. */
class PendienteFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        TextView(requireContext()).apply {
            gravity = Gravity.CENTER
            val p = context.dp(24)
            setPadding(p, p, p, p)
            TextViewCompat.setTextAppearance(this, com.google.android.material.R.style.TextAppearance_Material3_BodyLarge)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val nombre = findNavController().currentDestination?.label ?: "esta sección"
        (view as TextView).text = "La sección «$nombre» se agregará en el siguiente paso."
    }
}
