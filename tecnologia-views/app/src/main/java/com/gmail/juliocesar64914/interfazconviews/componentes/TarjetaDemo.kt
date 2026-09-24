package com.gmail.juliocesar64914.interfazconviews.componentes

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import androidx.core.widget.TextViewCompat
import com.gmail.juliocesar64914.interfazconviews.R
import com.google.android.material.card.MaterialCardView
import com.google.android.material.color.MaterialColors
import com.google.android.material.divider.MaterialDivider

/**
 * Tarjeta estándar del catálogo: nombre del elemento, explicación breve
 * y debajo la demostración interactiva.
 *
 * Se usa en XML así:
 *   <...TarjetaDemo app:nombre="..." app:descripcion="...">
 *       (vistas de la demostración)
 *   </...TarjetaDemo>
 */
class TarjetaDemo @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = com.google.android.material.R.attr.materialCardViewElevatedStyle
) : MaterialCardView(context, attrs, defStyleAttr) {

    /** Aquí se colocan las vistas hijas que se escriban dentro de la tarjeta en XML. */
    private var contenedor: LinearLayout? = null

    init {
        val base = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(context.dp(16), context.dp(16), context.dp(16), context.dp(16))
        }

        val textoNombre = TextView(context).apply {
            TextViewCompat.setTextAppearance(this, com.google.android.material.R.style.TextAppearance_Material3_TitleMedium)
            setTextColor(MaterialColors.getColor(this, androidx.appcompat.R.attr.colorPrimary))
        }
        val textoDescripcion = TextView(context).apply {
            TextViewCompat.setTextAppearance(this, com.google.android.material.R.style.TextAppearance_Material3_BodyMedium)
            setTextColor(MaterialColors.getColor(this, com.google.android.material.R.attr.colorOnSurfaceVariant))
            setPadding(0, context.dp(4), 0, 0)
        }
        val divisor = MaterialDivider(context)
        val cont = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            dividerDrawable = ContextCompat.getDrawable(context, R.drawable.espacio_8dp)
            showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
        }

        base.addView(textoNombre)
        base.addView(textoDescripcion)
        base.addView(
            divisor,
            LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
                topMargin = context.dp(12)
                bottomMargin = context.dp(12)
            }
        )
        base.addView(cont, LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))
        super.addView(base, -1, FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))
        contenedor = cont

        context.withStyledAttributes(attrs, R.styleable.TarjetaDemo) {
            textoNombre.text = getString(R.styleable.TarjetaDemo_nombre)
            textoDescripcion.text = getString(R.styleable.TarjetaDemo_descripcion)
        }
    }

    /** Redirige las vistas escritas en XML al contenedor de la demostración. */
    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        val c = contenedor
        if (c == null) super.addView(child, index, params) else c.addView(child, params)
    }
}
