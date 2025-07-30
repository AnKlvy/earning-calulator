package com.earningformula.utils

import com.earningformula.data.models.Language
import com.earningformula.data.models.DayOfWeek
import com.earningformula.data.models.Currency

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

    // Переводы для форм
    fun getJobName(language: Language): String {
        return when (language) {
            Language.RUSSIAN -> "Название работы"
            Language.ENGLISH -> "Job Name"
            Language.SPANISH -> "Nombre del Trabajo"
            Language.KAZAKH -> "Жұмыс атауы"
            Language.GERMAN -> "Job-Name"
            Language.FRENCH -> "Nom du Travail"
            Language.CHINESE -> "工作名称"
            Language.HINDI -> "नौकरी का नाम"
            Language.ARABIC -> "اسم الوظيفة"
        }
    }

    fun getSalary(language: Language): String {
        return when (language) {
            Language.RUSSIAN -> "Зарплата"
            Language.ENGLISH -> "Salary"
            Language.SPANISH -> "Salario"
            Language.KAZAKH -> "Жалақы"
            Language.GERMAN -> "Gehalt"
            Language.FRENCH -> "Salaire"
            Language.CHINESE -> "工资"
            Language.HINDI -> "वेतन"
            Language.ARABIC -> "الراتب"
        }
    }

    fun getInputType(language: Language): String {
        return when (language) {
            Language.RUSSIAN -> "Тип ввода"
            Language.ENGLISH -> "Input Type"
            Language.SPANISH -> "Tipo de Entrada"
            Language.KAZAKH -> "Енгізу түрі"
            Language.GERMAN -> "Eingabetyp"
            Language.FRENCH -> "Type de Saisie"
            Language.CHINESE -> "输入类型"
            Language.HINDI -> "इनपुट प्रकार"
            Language.ARABIC -> "نوع الإدخال"
        }
    }

    fun getDailyHours(language: Language): String {
        return when (language) {
            Language.RUSSIAN -> "Часы по дням"
            Language.ENGLISH -> "Daily Hours"
            Language.SPANISH -> "Horas Diarias"
            Language.KAZAKH -> "Күндік сағаттар"
            Language.GERMAN -> "Tägliche Stunden"
            Language.FRENCH -> "Heures Quotidiennes"
            Language.CHINESE -> "每日小时数"
            Language.HINDI -> "दैनिक घंटे"
            Language.ARABIC -> "الساعات اليومية"
        }
    }

    fun getTotalMonthlyHoursInput(language: Language): String {
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

    fun getSave(language: Language): String {
        return when (language) {
            Language.RUSSIAN -> "Сохранить"
            Language.ENGLISH -> "Save"
            Language.SPANISH -> "Guardar"
            Language.KAZAKH -> "Сақтау"
            Language.GERMAN -> "Speichern"
            Language.FRENCH -> "Enregistrer"
            Language.CHINESE -> "保存"
            Language.HINDI -> "सेव करें"
            Language.ARABIC -> "حفظ"
        }
    }

    fun getEdit(language: Language): String {
        return when (language) {
            Language.RUSSIAN -> "Редактировать"
            Language.ENGLISH -> "Edit"
            Language.SPANISH -> "Editar"
            Language.KAZAKH -> "Өңдеу"
            Language.GERMAN -> "Bearbeiten"
            Language.FRENCH -> "Modifier"
            Language.CHINESE -> "编辑"
            Language.HINDI -> "संपादित करें"
            Language.ARABIC -> "تحرير"
        }
    }

    fun getConfigurationName(language: Language): String {
        return when (language) {
            Language.RUSSIAN -> "Название конфигурации"
            Language.ENGLISH -> "Configuration Name"
            Language.SPANISH -> "Nombre de Configuración"
            Language.KAZAKH -> "Конфигурация атауы"
            Language.GERMAN -> "Konfigurationsname"
            Language.FRENCH -> "Nom de Configuration"
            Language.CHINESE -> "配置名称"
            Language.HINDI -> "कॉन्फ़िगरेशन नाम"
            Language.ARABIC -> "اسم التكوين"
        }
    }

    fun getCreate(language: Language): String {
        return when (language) {
            Language.RUSSIAN -> "Создать"
            Language.ENGLISH -> "Create"
            Language.SPANISH -> "Crear"
            Language.KAZAKH -> "Жасау"
            Language.GERMAN -> "Erstellen"
            Language.FRENCH -> "Créer"
            Language.CHINESE -> "创建"
            Language.HINDI -> "बनाएं"
            Language.ARABIC -> "إنشاء"
        }
    }

    // Дни недели
    fun getMonday(language: Language): String {
        return when (language) {
            Language.RUSSIAN -> "Пн"
            Language.ENGLISH -> "Mon"
            Language.SPANISH -> "Lun"
            Language.KAZAKH -> "Дс"
            Language.GERMAN -> "Mo"
            Language.FRENCH -> "Lun"
            Language.CHINESE -> "周一"
            Language.HINDI -> "सोम"
            Language.ARABIC -> "الإثنين"
        }
    }

    fun getTuesday(language: Language): String {
        return when (language) {
            Language.RUSSIAN -> "Вт"
            Language.ENGLISH -> "Tue"
            Language.SPANISH -> "Mar"
            Language.KAZAKH -> "Сс"
            Language.GERMAN -> "Di"
            Language.FRENCH -> "Mar"
            Language.CHINESE -> "周二"
            Language.HINDI -> "मंगल"
            Language.ARABIC -> "الثلاثاء"
        }
    }

    fun getWednesday(language: Language): String {
        return when (language) {
            Language.RUSSIAN -> "Ср"
            Language.ENGLISH -> "Wed"
            Language.SPANISH -> "Mié"
            Language.KAZAKH -> "Ср"
            Language.GERMAN -> "Mi"
            Language.FRENCH -> "Mer"
            Language.CHINESE -> "周三"
            Language.HINDI -> "बुध"
            Language.ARABIC -> "الأربعاء"
        }
    }

    fun getThursday(language: Language): String {
        return when (language) {
            Language.RUSSIAN -> "Чт"
            Language.ENGLISH -> "Thu"
            Language.SPANISH -> "Jue"
            Language.KAZAKH -> "Бс"
            Language.GERMAN -> "Do"
            Language.FRENCH -> "Jeu"
            Language.CHINESE -> "周四"
            Language.HINDI -> "गुरु"
            Language.ARABIC -> "الخميس"
        }
    }

    fun getFriday(language: Language): String {
        return when (language) {
            Language.RUSSIAN -> "Пт"
            Language.ENGLISH -> "Fri"
            Language.SPANISH -> "Vie"
            Language.KAZAKH -> "Жм"
            Language.GERMAN -> "Fr"
            Language.FRENCH -> "Ven"
            Language.CHINESE -> "周五"
            Language.HINDI -> "शुक्र"
            Language.ARABIC -> "الجمعة"
        }
    }

    fun getSaturday(language: Language): String {
        return when (language) {
            Language.RUSSIAN -> "Сб"
            Language.ENGLISH -> "Sat"
            Language.SPANISH -> "Sáb"
            Language.KAZAKH -> "Сб"
            Language.GERMAN -> "Sa"
            Language.FRENCH -> "Sam"
            Language.CHINESE -> "周六"
            Language.HINDI -> "शनि"
            Language.ARABIC -> "السبت"
        }
    }

    fun getSunday(language: Language): String {
        return when (language) {
            Language.RUSSIAN -> "Вс"
            Language.ENGLISH -> "Sun"
            Language.SPANISH -> "Dom"
            Language.KAZAKH -> "Жк"
            Language.GERMAN -> "So"
            Language.FRENCH -> "Dim"
            Language.CHINESE -> "周日"
            Language.HINDI -> "रवि"
            Language.ARABIC -> "الأحد"
        }
    }

    fun getDayOfWeekShort(dayOfWeek: DayOfWeek, language: Language): String {
        return when (dayOfWeek) {
            DayOfWeek.MONDAY -> getMonday(language)
            DayOfWeek.TUESDAY -> getTuesday(language)
            DayOfWeek.WEDNESDAY -> getWednesday(language)
            DayOfWeek.THURSDAY -> getThursday(language)
            DayOfWeek.FRIDAY -> getFriday(language)
            DayOfWeek.SATURDAY -> getSaturday(language)
            DayOfWeek.SUNDAY -> getSunday(language)
        }
    }

    // Переводы для диалогов
    fun getNoSavedConfigurations(language: Language): String {
        return when (language) {
            Language.RUSSIAN -> "Нет сохраненных конфигураций"
            Language.ENGLISH -> "No saved configurations"
            Language.SPANISH -> "No hay configuraciones guardadas"
            Language.KAZAKH -> "Сақталған конфигурациялар жоқ"
            Language.GERMAN -> "Keine gespeicherten Konfigurationen"
            Language.FRENCH -> "Aucune configuration sauvegardée"
            Language.CHINESE -> "没有保存的配置"
            Language.HINDI -> "कोई सहेजा गया कॉन्फ़िगरेशन नहीं"
            Language.ARABIC -> "لا توجد تكوينات محفوظة"
        }
    }

    fun getLoad(language: Language): String {
        return when (language) {
            Language.RUSSIAN -> "Загрузить"
            Language.ENGLISH -> "Load"
            Language.SPANISH -> "Cargar"
            Language.KAZAKH -> "Жүктеу"
            Language.GERMAN -> "Laden"
            Language.FRENCH -> "Charger"
            Language.CHINESE -> "加载"
            Language.HINDI -> "लोड करें"
            Language.ARABIC -> "تحميل"
        }
    }

    fun getDelete(language: Language): String {
        return when (language) {
            Language.RUSSIAN -> "Удалить"
            Language.ENGLISH -> "Delete"
            Language.SPANISH -> "Eliminar"
            Language.KAZAKH -> "Жою"
            Language.GERMAN -> "Löschen"
            Language.FRENCH -> "Supprimer"
            Language.CHINESE -> "删除"
            Language.HINDI -> "हटाएं"
            Language.ARABIC -> "حذف"
        }
    }

    fun getJobs(language: Language): String {
        return when (language) {
            Language.RUSSIAN -> "работ"
            Language.ENGLISH -> "jobs"
            Language.SPANISH -> "trabajos"
            Language.KAZAKH -> "жұмыс"
            Language.GERMAN -> "Jobs"
            Language.FRENCH -> "emplois"
            Language.CHINESE -> "工作"
            Language.HINDI -> "नौकरियां"
            Language.ARABIC -> "وظائف"
        }
    }

    fun getHours(language: Language): String {
        return when (language) {
            Language.RUSSIAN -> "часов"
            Language.ENGLISH -> "hours"
            Language.SPANISH -> "horas"
            Language.KAZAKH -> "сағат"
            Language.GERMAN -> "Stunden"
            Language.FRENCH -> "heures"
            Language.CHINESE -> "小时"
            Language.HINDI -> "घंटे"
            Language.ARABIC -> "ساعات"
        }
    }

    // Переводы валют
    fun getCurrencyName(currency: Currency, language: Language): String {
        return when (currency) {
            Currency.RUB -> when (language) {
                Language.RUSSIAN -> "Рубли"
                Language.ENGLISH -> "Rubles"
                Language.SPANISH -> "Rublos"
                Language.KAZAKH -> "Рублдер"
                Language.GERMAN -> "Rubel"
                Language.FRENCH -> "Roubles"
                Language.CHINESE -> "卢布"
                Language.HINDI -> "रूबल"
                Language.ARABIC -> "روبل"
            }
            Currency.USD -> when (language) {
                Language.RUSSIAN -> "Доллары"
                Language.ENGLISH -> "Dollars"
                Language.SPANISH -> "Dólares"
                Language.KAZAKH -> "Доллар"
                Language.GERMAN -> "Dollar"
                Language.FRENCH -> "Dollars"
                Language.CHINESE -> "美元"
                Language.HINDI -> "डॉलर"
                Language.ARABIC -> "دولار"
            }
            Currency.KZT -> when (language) {
                Language.RUSSIAN -> "Тенге"
                Language.ENGLISH -> "Tenge"
                Language.SPANISH -> "Tenge"
                Language.KAZAKH -> "Теңге"
                Language.GERMAN -> "Tenge"
                Language.FRENCH -> "Tenge"
                Language.CHINESE -> "坚戈"
                Language.HINDI -> "तेंगे"
                Language.ARABIC -> "تنغي"
            }
        }
    }

    fun getEmptyConfiguration(language: Language): String {
        return when (language) {
            Language.RUSSIAN -> "Пустая конфигурация"
            Language.ENGLISH -> "Empty configuration"
            Language.SPANISH -> "Configuración vacía"
            Language.KAZAKH -> "Бос конфигурация"
            Language.GERMAN -> "Leere Konfiguration"
            Language.FRENCH -> "Configuration vide"
            Language.CHINESE -> "空配置"
            Language.HINDI -> "खाली कॉन्फ़िगरेशन"
            Language.ARABIC -> "تكوين فارغ"
        }
    }

    fun getReadyToFill(language: Language): String {
        return when (language) {
            Language.RUSSIAN -> "Готова к заполнению"
            Language.ENGLISH -> "Ready to fill"
            Language.SPANISH -> "Lista para llenar"
            Language.KAZAKH -> "Толтыруға дайын"
            Language.GERMAN -> "Bereit zum Ausfüllen"
            Language.FRENCH -> "Prête à remplir"
            Language.CHINESE -> "准备填写"
            Language.HINDI -> "भरने के लिए तैयार"
            Language.ARABIC -> "جاهز للملء"
        }
    }

    fun getJobs2(language: Language): String {
        return when (language) {
            Language.RUSSIAN -> "Работы"
            Language.ENGLISH -> "Jobs"
            Language.SPANISH -> "Trabajos"
            Language.KAZAKH -> "Жұмыстар"
            Language.GERMAN -> "Jobs"
            Language.FRENCH -> "Emplois"
            Language.CHINESE -> "工作"
            Language.HINDI -> "नौकरियां"
            Language.ARABIC -> "وظائف"
        }
    }
}
