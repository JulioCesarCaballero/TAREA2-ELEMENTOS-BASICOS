package com.gmail.juliocesar64914.interfazconcompose

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.DisposableEffect
import com.gmail.juliocesar64914.interfazconcompose.datos.CatalogoViewModel
import com.gmail.juliocesar64914.interfazconcompose.datos.ModoTema
import com.gmail.juliocesar64914.interfazconcompose.navegacion.AppNavegacion
import com.gmail.juliocesar64914.interfazconcompose.ui.theme.InterfazConComposeTheme
import java.util.Locale

class MainActivity : ComponentActivity() {

    // Un solo ViewModel compartido por todas las secciones:
    // así un dato de la Sección 1 puede aparecer en la Sección 4.
    private val viewModel: CatalogoViewModel by viewModels()

    // Fuerza el español aunque el teléfono esté en otro idioma, para que los
    // textos del sistema (calendario, reloj, botones de diálogos) salgan en español.
    override fun attachBaseContext(newBase: Context) {
        val configuracion = Configuration(newBase.resources.configuration)
        configuracion.setLocale(Locale.forLanguageTag("es-MX"))
        super.attachBaseContext(newBase.createConfigurationContext(configuracion))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val sistemaOscuro = isSystemInDarkTheme()
            val oscuro = when (viewModel.modoTema) {
                ModoTema.SISTEMA -> sistemaOscuro
                ModoTema.CLARO -> false
                ModoTema.OSCURO -> true
            }

            // Ajusta el color de los íconos de la barra de estado (hora, batería)
            // para que se lean bien con el tema elegido.
            DisposableEffect(oscuro) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT) { oscuro },
                    navigationBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT) { oscuro }
                )
                onDispose {}
            }

            InterfazConComposeTheme(darkTheme = oscuro) {
                AppNavegacion(viewModel = viewModel)
            }
        }
    }
}