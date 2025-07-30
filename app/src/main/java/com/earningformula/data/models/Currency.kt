package com.earningformula.data.models

/**
 * Поддерживаемые валюты
 */
enum class Currency(
    val code: String,
    val symbol: String,
    val displayName: String
) {
    RUB("RUB", "₽", "Рубли"),
    USD("USD", "$", "Доллары"),
    KZT("KZT", "₸", "Тенге");

    companion object {
        fun fromCode(code: String): Currency {
            return values().find { it.code == code } ?: RUB
        }
    }
}
