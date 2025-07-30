package com.earningformula.utils

import com.earningformula.data.models.Language

object LocalizationHelper {
    
    fun getAppTitle(language: Language): String {
        return when (language) {
            Language.RUSSIAN -> "Калькулятор зарплаты"
            Language.ENGLISH -> "Salary Calculator"
            Language.SPANISH -> "Calculadora de Salario"
            Language.KAZAKH -> "Жалақы калькуляторы"
            Language.GERMAN -> "Gehaltsrechner"
            Language.FRENCH -> "Calculateur de Salaire"
            Language.CHINESE -> "工资计算器"
            Language.HINDI -> "वेतन कैलकुलेटर"
            Language.ARABIC -> "حاسبة الراتب"
        }
    }
    
    fun getAddJob(language: Language): String {
        return when (language) {
            Language.RUSSIAN -> "Добавить работу"
            Language.ENGLISH -> "Add Job"
            Language.SPANISH -> "Agregar Trabajo"
            Language.KAZAKH -> "Жұмыс қосу"
            Language.GERMAN -> "Job hinzufügen"
            Language.FRENCH -> "Ajouter un Emploi"
            Language.CHINESE -> "添加工作"
            Language.HINDI -> "नौकरी जोड़ें"
            Language.ARABIC -> "إضافة وظيفة"
        }
    }
    
    fun getNewConfiguration(language: Language): String {
        return when (language) {
            Language.RUSSIAN -> "Новая конфигурация"
            Language.ENGLISH -> "New Configuration"
            Language.SPANISH -> "Nueva Configuración"
            Language.KAZAKH -> "Жаңа конфигурация"
            Language.GERMAN -> "Neue Konfiguration"
            Language.FRENCH -> "Nouvelle Configuration"
            Language.CHINESE -> "新配置"
            Language.HINDI -> "नई कॉन्फ़िगरेशन"
            Language.ARABIC -> "تكوين جديد"
        }
    }
    
    fun getTotalResults(language: Language): String {
        return when (language) {
            Language.RUSSIAN -> "Общие результаты"
            Language.ENGLISH -> "Total Results"
            Language.SPANISH -> "Resultados Totales"
            Language.KAZAKH -> "Жалпы нәтижелер"
            Language.GERMAN -> "Gesamtergebnisse"
            Language.FRENCH -> "Résultats Totaux"
            Language.CHINESE -> "总结果"
            Language.HINDI -> "कुल परिणाम"
            Language.ARABIC -> "النتائج الإجمالية"
        }
    }
    
    fun getMonthlySalary(language: Language): String {
        return when (language) {
            Language.RUSSIAN -> "Зарплата в месяц"
            Language.ENGLISH -> "Monthly Salary"
            Language.SPANISH -> "Salario Mensual"
            Language.KAZAKH -> "Айлық жалақы"
            Language.GERMAN -> "Monatsgehalt"
            Language.FRENCH -> "Salaire Mensuel"
            Language.CHINESE -> "月薪"
            Language.HINDI -> "मासिक वेतन"
            Language.ARABIC -> "الراتب الشهري"
        }
    }
    
    fun getHourlyRate(language: Language): String {
        return when (language) {
            Language.RUSSIAN -> "Ставка в час"
            Language.ENGLISH -> "Hourly Rate"
            Language.SPANISH -> "Tarifa por Hora"
            Language.KAZAKH -> "Сағаттық ставка"
            Language.GERMAN -> "Stundenlohn"
            Language.FRENCH -> "Taux Horaire"
            Language.CHINESE -> "时薪"
            Language.HINDI -> "घंटे की दर"
            Language.ARABIC -> "الأجر بالساعة"
        }
    }
    
    fun getFavorites(language: Language): String {
        return when (language) {
            Language.RUSSIAN -> "Избранное"
            Language.ENGLISH -> "Favorites"
            Language.SPANISH -> "Favoritos"
            Language.KAZAKH -> "Таңдаулылар"
            Language.GERMAN -> "Favoriten"
            Language.FRENCH -> "Favoris"
            Language.CHINESE -> "收藏夹"
            Language.HINDI -> "पसंदीदा"
            Language.ARABIC -> "المفضلة"
        }
    }
    
    fun getCancel(language: Language): String {
        return when (language) {
            Language.RUSSIAN -> "Отмена"
            Language.ENGLISH -> "Cancel"
            Language.SPANISH -> "Cancelar"
            Language.KAZAKH -> "Болдырмау"
            Language.GERMAN -> "Abbrechen"
            Language.FRENCH -> "Annuler"
            Language.CHINESE -> "取消"
            Language.HINDI -> "रद्द करें"
            Language.ARABIC -> "إلغاء"
        }
    }
    
    fun getSelectCurrency(language: Language): String {
        return when (language) {
            Language.RUSSIAN -> "Выберите валюту"
            Language.ENGLISH -> "Select Currency"
            Language.SPANISH -> "Seleccionar Moneda"
            Language.KAZAKH -> "Валютаны таңдаңыз"
            Language.GERMAN -> "Währung auswählen"
            Language.FRENCH -> "Sélectionner la Devise"
            Language.CHINESE -> "选择货币"
            Language.HINDI -> "मुद्रा चुनें"
            Language.ARABIC -> "اختر العملة"
        }
    }
    
    fun getSelectLanguage(language: Language): String {
        return when (language) {
            Language.RUSSIAN -> "Выберите язык"
            Language.ENGLISH -> "Select Language"
            Language.SPANISH -> "Seleccionar Idioma"
            Language.KAZAKH -> "Тілді таңдаңыз"
            Language.GERMAN -> "Sprache auswählen"
            Language.FRENCH -> "Sélectionner la Langue"
            Language.CHINESE -> "选择语言"
            Language.HINDI -> "भाषा चुनें"
            Language.ARABIC -> "اختر اللغة"
        }
    }

    fun getTotalWeeklyHours(language: Language): String {
        return when (language) {
            Language.RUSSIAN -> "Общие часы в неделю"
            Language.ENGLISH -> "Total Weekly Hours"
            Language.SPANISH -> "Horas Semanales Totales"
            Language.KAZAKH -> "Жалпы апталық сағаттар"
            Language.GERMAN -> "Gesamte Wochenstunden"
            Language.FRENCH -> "Heures Hebdomadaires Totales"
            Language.CHINESE -> "每周总小时数"
            Language.HINDI -> "कुल साप्ताहिक घंटे"
            Language.ARABIC -> "إجمالي الساعات الأسبوعية"
        }
    }

    fun getTotalMonthlyHours(language: Language): String {
        return when (language) {
            Language.RUSSIAN -> "Общие часы в месяц"
            Language.ENGLISH -> "Total Monthly Hours"
            Language.SPANISH -> "Horas Mensuales Totales"
            Language.KAZAKH -> "Жалпы айлық сағаттар"
            Language.GERMAN -> "Gesamte Monatsstunden"
            Language.FRENCH -> "Heures Mensuelles Totales"
            Language.CHINESE -> "每月总小时数"
            Language.HINDI -> "कुल मासिक घंटे"
            Language.ARABIC -> "إجمالي الساعات الشهرية"
        }
    }

    fun getAverageHourlyRate(language: Language): String {
        return when (language) {
            Language.RUSSIAN -> "Средняя ставка в час"
            Language.ENGLISH -> "Average Hourly Rate"
            Language.SPANISH -> "Tarifa Promedio por Hora"
            Language.KAZAKH -> "Орташа сағаттық ставка"
            Language.GERMAN -> "Durchschnittlicher Stundenlohn"
            Language.FRENCH -> "Taux Horaire Moyen"
            Language.CHINESE -> "平均时薪"
            Language.HINDI -> "औसत घंटे की दर"
            Language.ARABIC -> "متوسط الأجر بالساعة"
        }
    }

    fun getWeeklyHours(language: Language): String {
        return when (language) {
            Language.RUSSIAN -> "Часов в неделю"
            Language.ENGLISH -> "Weekly Hours"
            Language.SPANISH -> "Horas Semanales"
            Language.KAZAKH -> "Апталық сағаттар"
            Language.GERMAN -> "Wochenstunden"
            Language.FRENCH -> "Heures Hebdomadaires"
            Language.CHINESE -> "每周小时数"
            Language.HINDI -> "साप्ताहिक घंटे"
            Language.ARABIC -> "الساعات الأسبوعية"
        }
    }

    fun getTotalWorkHours(language: Language): String {
        return when (language) {
            Language.RUSSIAN -> "Общие часы работы"
            Language.ENGLISH -> "Total Work Hours"
            Language.SPANISH -> "Horas Totales de Trabajo"
            Language.KAZAKH -> "Жалпы жұмыс сағаттары"
            Language.GERMAN -> "Gesamte Arbeitsstunden"
            Language.FRENCH -> "Heures de Travail Totales"
            Language.CHINESE -> "总工作小时数"
            Language.HINDI -> "कुल कार्य घंटे"
            Language.ARABIC -> "إجمالي ساعات العمل"
        }
    }

    fun getBreakdownByDays(language: Language): String {
        return when (language) {
            Language.RUSSIAN -> "Разбивка по дням"
            Language.ENGLISH -> "Breakdown by Days"
            Language.SPANISH -> "Desglose por Días"
            Language.KAZAKH -> "Күндер бойынша бөлу"
            Language.GERMAN -> "Aufschlüsselung nach Tagen"
            Language.FRENCH -> "Répartition par Jours"
            Language.CHINESE -> "按天分解"
            Language.HINDI -> "दिनों के अनुसार विभाजन"
            Language.ARABIC -> "التفصيل حسب الأيام"
        }
    }

    fun getWeekdays(language: Language): String {
        return when (language) {
            Language.RUSSIAN -> "Будни"
            Language.ENGLISH -> "Weekdays"
            Language.SPANISH -> "Días Laborables"
            Language.KAZAKH -> "Жұмыс күндері"
            Language.GERMAN -> "Wochentage"
            Language.FRENCH -> "Jours de Semaine"
            Language.CHINESE -> "工作日"
            Language.HINDI -> "सप्ताह के दिन"
            Language.ARABIC -> "أيام الأسبوع"
        }
    }

    fun getWeekends(language: Language): String {
        return when (language) {
            Language.RUSSIAN -> "Выходные"
            Language.ENGLISH -> "Weekends"
            Language.SPANISH -> "Fines de Semana"
            Language.KAZAKH -> "Демалыс күндері"
            Language.GERMAN -> "Wochenenden"
            Language.FRENCH -> "Week-ends"
            Language.CHINESE -> "周末"
            Language.HINDI -> "सप्ताहांत"
            Language.ARABIC -> "عطلات نهاية الأسبوع"
        }
    }

    fun getJobBreakdown(language: Language): String {
        return when (language) {
            Language.RUSSIAN -> "Детализация по работам"
            Language.ENGLISH -> "Job Breakdown"
            Language.SPANISH -> "Desglose por Trabajos"
            Language.KAZAKH -> "Жұмыстар бойынша детализация"
            Language.GERMAN -> "Aufschlüsselung nach Jobs"
            Language.FRENCH -> "Répartition par Emplois"
            Language.CHINESE -> "工作明细"
            Language.HINDI -> "नौकरी का विवरण"
            Language.ARABIC -> "تفصيل الوظائف"
        }
    }

    // Единицы измерения времени
    fun getHoursShort(language: Language): String {
        return when (language) {
            Language.RUSSIAN -> "ч"
            Language.ENGLISH -> "h"
            Language.SPANISH -> "h"
            Language.KAZAKH -> "сағ"
            Language.GERMAN -> "Std"
            Language.FRENCH -> "h"
            Language.CHINESE -> "小时"
            Language.HINDI -> "घं"
            Language.ARABIC -> "س"
        }
    }

    fun getWeekShort(language: Language): String {
        return when (language) {
            Language.RUSSIAN -> "нед"
            Language.ENGLISH -> "week"
            Language.SPANISH -> "sem"
            Language.KAZAKH -> "апт"
            Language.GERMAN -> "Wo"
            Language.FRENCH -> "sem"
            Language.CHINESE -> "周"
            Language.HINDI -> "सप्ताह"
            Language.ARABIC -> "أسبوع"
        }
    }

    fun getMonthShort(language: Language): String {
        return when (language) {
            Language.RUSSIAN -> "мес"
            Language.ENGLISH -> "month"
            Language.SPANISH -> "mes"
            Language.KAZAKH -> "ай"
            Language.GERMAN -> "Mon"
            Language.FRENCH -> "mois"
            Language.CHINESE -> "月"
            Language.HINDI -> "महीना"
            Language.ARABIC -> "شهر"
        }
    }

    fun getPerHour(language: Language): String {
        return when (language) {
            Language.RUSSIAN -> "/ч"
            Language.ENGLISH -> "/h"
            Language.SPANISH -> "/h"
            Language.KAZAKH -> "/сағ"
            Language.GERMAN -> "/Std"
            Language.FRENCH -> "/h"
            Language.CHINESE -> "/小时"
            Language.HINDI -> "/घं"
            Language.ARABIC -> "/س"
        }
    }
}
