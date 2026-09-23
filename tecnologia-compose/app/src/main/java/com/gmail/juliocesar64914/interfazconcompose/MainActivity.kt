package com.gmail.juliocesar64914.interfazconcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.gmail.juliocesar64914.interfazconcompose.datos.CatalogoViewModel
import com.gmail.juliocesar64914.interfazconcompose.navegacion.AppNavegacion
import com.gmail.juliocesar64914.interfazconcompose.ui.theme.InterfazConComposeTheme

class MainActivity : ComponentActivity() {

    // Un solo ViewModel compartido por todas las secciones:
    // así un dato de la Sección 1 puede aparecer en la Sección 4.
    private val viewModel: CatalogoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // El tema se adapta solo al modo claro/oscuro del sistema
            InterfazConComposeTheme {
                AppNavegacion(viewModel = viewModel)
            }
        }
    }
}