package com.gmail.juliocesar64914.interfazconviews

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.google.android.material.color.DynamicColors

/** Se ejecuta una vez al abrir la app, antes que cualquier pantalla. */
class CatalogoApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Colores dinámicos (Material You) en Android 12+, igual que la versión Compose
        DynamicColors.applyToActivitiesIfAvailable(this)
        // Fuerza el español aunque el teléfono esté en otro idioma
        // (calendario, reloj y botones de diálogos del sistema)
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("es-MX"))
    }
}
