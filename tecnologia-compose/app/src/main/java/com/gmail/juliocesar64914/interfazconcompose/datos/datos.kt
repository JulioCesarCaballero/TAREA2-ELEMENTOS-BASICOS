package com.gmail.juliocesar64914.interfazconcompose.datos

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

data class ElementoLista(
    val id: Long,
    val titulo: String,
    val subtitulo: String,
    val categoria: String
)

class CatalogoViewModel : ViewModel() {

    private var siguienteId = 0L

    /** Lista que se muestra en la Sección 4. La Sección 1 puede agregar elementos. */
    val elementos = mutableStateListOf<ElementoLista>()

    /** Preferencia elegida en la Sección 3 que modifica los textos de la Sección 5. */
    var escalaTexto by mutableFloatStateOf(1f)
        private set

    init {
        reiniciarLista()
    }

    fun agregarElemento(titulo: String) {
        if (titulo.isBlank()) return
        elementos.add(
            0,
            ElementoLista(siguienteId++, titulo.trim(), "Agregado desde Entrada de texto", "Del usuario")
        )
    }

    fun eliminarElemento(elemento: ElementoLista) {
        elementos.remove(elemento)
    }

    fun reiniciarLista() {
        elementos.clear()
        FRUTAS.forEach {
            elementos.add(ElementoLista(siguienteId++, it, "Fruta de temporada", "Frutas"))
        }
        VERDURAS.forEach {
            elementos.add(ElementoLista(siguienteId++, it, "Verdura fresca", "Verduras"))
        }
    }

    fun vaciarLista() {
        elementos.clear()
    }

    fun cambiarEscalaTexto(valor: Float) {
        escalaTexto = valor
    }

    companion object {
        private val FRUTAS = listOf(
            "Manzana", "Plátano", "Mango", "Fresa", "Uva",
            "Piña", "Sandía", "Papaya", "Guayaba", "Kiwi"
        )
        private val VERDURAS = listOf(
            "Zanahoria", "Brócoli", "Calabacita", "Jitomate", "Espinaca",
            "Chayote", "Nopal", "Pepino", "Lechuga", "Elote"
        )
    }
}