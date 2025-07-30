package com.earningformula.data.local

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.earningformula.data.models.DayOfWeek
import com.earningformula.data.models.Job
import com.earningformula.data.models.JobInputType
import com.earningformula.data.models.WorkConfiguration
import com.earningformula.data.models.Currency
import com.earningformula.data.models.Language
import org.json.JSONArray
import org.json.JSONObject

class SharedPreferencesManager {
    companion object {
        private const val PREFS_NAME = "earning_formula_prefs"
        private const val KEY_CONFIGURATIONS = "configurations"
        private const val KEY_LAST_CONFIGURATION_ID = "last_configuration_id"
        private const val KEY_CURRENT_CONFIGURATION = "current_configuration"
        private const val KEY_CURRENCY = "currency"
        private const val KEY_LANGUAGE = "language"

        @Volatile
        private var INSTANCE: SharedPreferencesManager? = null
        private var appContext: Context? = null

        fun initialize(context: Context) {
            appContext = context.applicationContext
        }

        fun getInstance(): SharedPreferencesManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SharedPreferencesManager().also { INSTANCE = it }
            }
        }
    }

    private val sharedPreferences: SharedPreferences by lazy {
        appContext?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            ?: throw IllegalStateException("SharedPreferencesManager не инициализирован. Вызовите initialize(context) в Application классе.")
    }

    // Объект для синхронизации операций с конфигурациями
    private val configurationLock = Any()
    
    /**
     * Сохраняет конфигурацию
     */
    fun saveConfiguration(configuration: WorkConfiguration) {
        synchronized(configurationLock) {
            Log.d("SharedPreferencesManager", "Сохранение конфигурации: ${configuration.name} (${configuration.id})")

            val configurations = getAllConfigurationsUnsafe().toMutableList()
            Log.d("SharedPreferencesManager", "Текущее количество конфигураций: ${configurations.size}")

            // Удаляем существующую конфигурацию с тем же ID, если есть
            configurations.removeAll { it.id == configuration.id }

            // Добавляем новую конфигурацию
            configurations.add(configuration)

            // Сортируем по дате создания (новые сверху)
            configurations.sortByDescending { it.createdAt }

            Log.d("SharedPreferencesManager", "Сохраняем ${configurations.size} конфигураций")
            saveAllConfigurations(configurations)

            // Проверяем что сохранилось
            val savedConfigs = getAllConfigurationsUnsafe()
            Log.d("SharedPreferencesManager", "После сохранения: ${savedConfigs.size} конфигураций")
        }
    }
    
    /**
     * Получает все сохраненные конфигурации
     */
    fun getAllConfigurations(): List<WorkConfiguration> {
        synchronized(configurationLock) {
            return getAllConfigurationsUnsafe()
        }
    }

    /**
     * Получает все сохраненные конфигурации без синхронизации (для внутреннего использования)
     */
    private fun getAllConfigurationsUnsafe(): List<WorkConfiguration> {
        val configurationsJson = sharedPreferences.getString(KEY_CONFIGURATIONS, "[]")
        Log.d("SharedPreferencesManager", "Загружаем конфигурации из JSON: $configurationsJson")
        val configs = parseConfigurationsFromJson(configurationsJson ?: "[]")
        Log.d("SharedPreferencesManager", "Загружено ${configs.size} конфигураций")
        return configs
    }
    
    /**
     * Получает конфигурацию по ID
     */
    fun getConfigurationById(id: String): WorkConfiguration? {
        return getAllConfigurations().find { it.id == id }
    }
    
    /**
     * Удаляет конфигурацию по ID
     */
    fun deleteConfiguration(id: String) {
        synchronized(configurationLock) {
            val configurations = getAllConfigurationsUnsafe().toMutableList()
            configurations.removeAll { it.id == id }
            saveAllConfigurations(configurations)
        }
    }
    
    /**
     * Удаляет все конфигурации
     */
    fun deleteAllConfigurations() {
        sharedPreferences.edit()
            .remove(KEY_CONFIGURATIONS)
            .apply()
    }
    
    /**
     * Получает количество сохраненных конфигураций
     */
    fun getConfigurationsCount(): Int {
        return getAllConfigurations().size
    }
    
    /**
     * Экспортирует все конфигурации в JSON
     */
    fun exportConfigurations(): String {
        val configurations = getAllConfigurations()
        return convertConfigurationsToJson(configurations)
    }
    
    /**
     * Импортирует конфигурации из JSON
     */
    fun importConfigurations(jsonData: String) {
        val importedConfigurations = parseConfigurationsFromJson(jsonData)
        val existingConfigurations = getAllConfigurations().toMutableList()
        
        // Добавляем импортированные конфигурации, избегая дубликатов по ID
        importedConfigurations.forEach { imported ->
            existingConfigurations.removeAll { it.id == imported.id }
            existingConfigurations.add(imported)
        }
        
        existingConfigurations.sortByDescending { it.createdAt }
        saveAllConfigurations(existingConfigurations)
    }
    
    /**
     * Сохраняет ID последней использованной конфигурации
     */
    fun saveLastConfigurationId(configId: String) {
        sharedPreferences.edit()
            .putString(KEY_LAST_CONFIGURATION_ID, configId)
            .apply()
    }
    
    /**
     * Получает ID последней использованной конфигурации
     */
    fun getLastConfigurationId(): String? {
        return sharedPreferences.getString(KEY_LAST_CONFIGURATION_ID, null)
    }

    /**
     * Сохраняет текущую конфигурацию
     */
    fun saveCurrentConfiguration(configuration: WorkConfiguration) {
        val json = convertConfigurationToJson(configuration)
        sharedPreferences.edit()
            .putString(KEY_CURRENT_CONFIGURATION, json)
            .apply()
    }

    /**
     * Получает текущую конфигурацию
     */
    fun getCurrentConfiguration(): WorkConfiguration? {
        val json = sharedPreferences.getString(KEY_CURRENT_CONFIGURATION, null)
        return if (json != null) {
            try {
                parseConfigurationFromJson(json)
            } catch (e: Exception) {
                null
            }
        } else {
            null
        }
    }
    
    /**
     * Сохраняет все конфигурации
     */
    private fun saveAllConfigurations(configurations: List<WorkConfiguration>) {
        val json = convertConfigurationsToJson(configurations)
        sharedPreferences.edit()
            .putString(KEY_CONFIGURATIONS, json)
            .apply()
    }

    /**
     * Конвертирует одну конфигурацию в JSON
     */
    private fun convertConfigurationToJson(configuration: WorkConfiguration): String {
        val configJson = JSONObject().apply {
            put("id", configuration.id)
            put("name", configuration.name)
            put("createdAt", configuration.createdAt)

            val jobsArray = JSONArray()
            configuration.jobs.forEach { job ->
                val jobJson = JSONObject().apply {
                    put("id", job.id)
                    put("name", job.name)
                    put("monthlySalary", job.monthlySalary)
                    put("inputType", job.inputType.name)
                    put("totalMonthlyHours", job.totalMonthlyHours)

                    val hoursJson = JSONObject()
                    job.hoursPerDay.forEach { (day, hours) ->
                        hoursJson.put(day.name, hours)
                    }
                    put("hoursPerDay", hoursJson)
                }
                jobsArray.put(jobJson)
            }
            put("jobs", jobsArray)
        }

        return configJson.toString()
    }

    /**
     * Парсит одну конфигурацию из JSON
     */
    private fun parseConfigurationFromJson(json: String): WorkConfiguration {
        val configJson = JSONObject(json)

        val jobs = mutableListOf<Job>()
        val jobsArray = configJson.getJSONArray("jobs")

        for (j in 0 until jobsArray.length()) {
            val jobJson = jobsArray.getJSONObject(j)

            val hoursPerDay = mutableMapOf<DayOfWeek, Double>()
            val hoursJson = jobJson.getJSONObject("hoursPerDay")

            DayOfWeek.values().forEach { day ->
                hoursPerDay[day] = hoursJson.optDouble(day.name, 0.0)
            }

            val inputTypeString = jobJson.optString("inputType", JobInputType.DAILY_HOURS.name)
            val inputType = try {
                JobInputType.valueOf(inputTypeString)
            } catch (e: Exception) {
                JobInputType.DAILY_HOURS
            }

            val job = Job(
                id = jobJson.getString("id"),
                name = jobJson.getString("name"),
                monthlySalary = jobJson.getDouble("monthlySalary"),
                inputType = inputType,
                hoursPerDay = hoursPerDay,
                totalMonthlyHours = jobJson.optDouble("totalMonthlyHours", 0.0)
            )
            jobs.add(job)
        }

        return WorkConfiguration(
            id = configJson.getString("id"),
            name = configJson.getString("name"),
            jobs = jobs,
            createdAt = configJson.getLong("createdAt")
        )
    }
    
    /**
     * Конвертирует список конфигураций в JSON
     */
    private fun convertConfigurationsToJson(configurations: List<WorkConfiguration>): String {
        val jsonArray = JSONArray()
        
        configurations.forEach { config ->
            val configJson = JSONObject().apply {
                put("id", config.id)
                put("name", config.name)
                put("createdAt", config.createdAt)
                
                val jobsArray = JSONArray()
                config.jobs.forEach { job ->
                    val jobJson = JSONObject().apply {
                        put("id", job.id)
                        put("name", job.name)
                        put("monthlySalary", job.monthlySalary)
                        put("inputType", job.inputType.name)
                        put("totalMonthlyHours", job.totalMonthlyHours)

                        val hoursJson = JSONObject()
                        job.hoursPerDay.forEach { (day, hours) ->
                            hoursJson.put(day.name, hours)
                        }
                        put("hoursPerDay", hoursJson)
                    }
                    jobsArray.put(jobJson)
                }
                put("jobs", jobsArray)
            }
            jsonArray.put(configJson)
        }
        
        return jsonArray.toString()
    }
    
    /**
     * Парсит конфигурации из JSON
     */
    private fun parseConfigurationsFromJson(json: String): List<WorkConfiguration> {
        try {
            val jsonArray = JSONArray(json)
            val configurations = mutableListOf<WorkConfiguration>()
            
            for (i in 0 until jsonArray.length()) {
                val configJson = jsonArray.getJSONObject(i)
                
                val jobs = mutableListOf<Job>()
                val jobsArray = configJson.getJSONArray("jobs")
                
                for (j in 0 until jobsArray.length()) {
                    val jobJson = jobsArray.getJSONObject(j)
                    
                    val hoursPerDay = mutableMapOf<DayOfWeek, Double>()
                    val hoursJson = jobJson.getJSONObject("hoursPerDay")
                    
                    DayOfWeek.values().forEach { day ->
                        hoursPerDay[day] = hoursJson.optDouble(day.name, 0.0)
                    }
                    
                    val inputTypeString = jobJson.optString("inputType", JobInputType.DAILY_HOURS.name)
                    val inputType = try {
                        JobInputType.valueOf(inputTypeString)
                    } catch (e: Exception) {
                        JobInputType.DAILY_HOURS
                    }

                    val job = Job(
                        id = jobJson.getString("id"),
                        name = jobJson.getString("name"),
                        monthlySalary = jobJson.getDouble("monthlySalary"),
                        inputType = inputType,
                        hoursPerDay = hoursPerDay,
                        totalMonthlyHours = jobJson.optDouble("totalMonthlyHours", 0.0)
                    )
                    jobs.add(job)
                }
                
                val configuration = WorkConfiguration(
                    id = configJson.getString("id"),
                    name = configJson.getString("name"),
                    jobs = jobs,
                    createdAt = configJson.getLong("createdAt")
                )
                configurations.add(configuration)
            }
            
            return configurations
        } catch (e: Exception) {
            throw IllegalArgumentException("Ошибка при парсинге JSON: ${e.message}", e)
        }
    }

    /**
     * Сохраняет выбранную валюту
     */
    fun saveCurrency(currency: Currency) {
        sharedPreferences.edit()
            .putString(KEY_CURRENCY, currency.code)
            .apply()
    }

    /**
     * Получает сохраненную валюту
     */
    fun getCurrency(): Currency {
        val currencyCode = sharedPreferences.getString(KEY_CURRENCY, Currency.RUB.code)
        return Currency.fromCode(currencyCode ?: Currency.RUB.code)
    }

    /**
     * Сохраняет выбранный язык
     */
    fun saveLanguage(language: Language) {
        sharedPreferences.edit()
            .putString(KEY_LANGUAGE, language.code)
            .apply()
    }

    /**
     * Получает сохраненный язык
     */
    fun getLanguage(): Language {
        val languageCode = sharedPreferences.getString(KEY_LANGUAGE, null)
        return if (languageCode != null) {
            Language.fromCode(languageCode)
        } else {
            // Если язык не сохранен, используем системный
            Language.getSystemLanguage()
        }
    }
}
