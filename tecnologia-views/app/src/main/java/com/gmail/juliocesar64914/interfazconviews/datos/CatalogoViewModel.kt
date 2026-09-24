package com.gmail.juliocesar64914.interfazconviews.datos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

const val CATEGORIA_USUARIO = "Del usuario"

data class ElementoLista(
    val id: Long,
    val titulo: String,
    val subtitulo: String,
    val categoria: String
)

/**
 * Datos compartidos por todas las secciones. Cada Fragment lo obtiene con
 * activityViewModels(), así que todos ven la misma instancia.
 */
class CatalogoViewModel : ViewModel() {

    private var siguienteId = 0L

    /** Lista de la Sección 4. La Sección 1 puede agregar elementos. */
    private val _elementos = MutableLiveData<List<ElementoLista>>(emptyList())
    val elementos: LiveData<List<ElementoLista>> get() = _elementos

    /** Preferencia elegida en la Sección 3 que modifica los textos de la Sección 5. */
    private val _escalaTexto = MutableLiveData(1f)
    val escalaTexto: LiveData<Float> get() = _escalaTexto

    val cantidad: Int get() = _elementos.value.orEmpty().size

    init {
        reiniciarLista()
    }

    fun agregarElemento(titulo: String) {
        if (titulo.isBlank()) return
        val nuevo = ElementoLista(siguienteId++, titulo.trim(), "Agregado desde Entrada de texto", CATEGORIA_USUARIO)
        _elementos.value = listOf(nuevo) + _elementos.value.orEmpty()
    }

    fun eliminarElemento(elemento: ElementoLista) {
        _elementos.value = _elementos.value.orEmpty() - elemento
    }

    /** Restaura las frutas y verduras originales, conservando lo que agregó el usuario. */
    fun reiniciarLista() {
        val delUsuario = _elementos.value.orEmpty().filter { it.categoria == CATEGORIA_USUARIO }
        val frutas = FRUTAS.map { ElementoLista(siguienteId++, it, "Fruta de temporada", "Frutas") }
        val verduras = VERDURAS.map { ElementoLista(siguienteId++, it, "Verdura fresca", "Verduras") }
        _elementos.value = delUsuario + frutas + verduras
    }

    fun vaciarLista() {
        _elementos.value = emptyList()
    }

    fun cambiarEscalaTexto(valor: Float) {
        _escalaTexto.value = valor
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
