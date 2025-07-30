package com.earningformula.data.models

import java.util.Locale

/**
 * Поддерживаемые языки приложения
 */
enum class Language(
    val code: String,
    val displayName: String,
    val nativeName: String,
    val locale: Locale
) {
    RUSSIAN("ru", "Russian", "Русский", Locale("ru", "RU")),
    ENGLISH("en", "English", "English", Locale("en", "US")),
    SPANISH("es", "Spanish", "Español", Locale("es", "ES")),
    KAZAKH("kk", "Kazakh", "Қазақша", Locale("kk", "KZ")),
    GERMAN("de", "German", "Deutsch", Locale("de", "DE")),
    FRENCH("fr", "French", "Français", Locale("fr", "FR")),
    CHINESE("zh", "Chinese", "中文", Locale("zh", "CN")),
    HINDI("hi", "Hindi", "हिन्दी", Locale("hi", "IN")),
    ARABIC("ar", "Arabic", "العربية", Locale("ar", "SA"));

    companion object {
        fun fromCode(code: String): Language {
            return values().find { it.code == code } ?: RUSSIAN
        }
        
        fun getSystemLanguage(): Language {
            val systemLocale = Locale.getDefault()
            return values().find { 
                it.locale.language == systemLocale.language 
            } ?: RUSSIAN
        }
    }
}
