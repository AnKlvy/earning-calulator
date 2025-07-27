package com.earningformula.data.local

import android.content.Context
import android.content.SharedPreferences
import com.earningformula.data.models.DayOfWeek
import com.earningformula.data.models.Job
import com.earningformula.data.models.WorkConfiguration
import org.json.JSONArray
import org.json.JSONObject

class SharedPreferencesManager {
    companion object {
        private const val PREFS_NAME = "earning_formula_prefs"
        private const val KEY_CONFIGURATIONS = "configurations"
        private const val KEY_LAST_CONFIGURATION_ID = "last_configuration_id"

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
    
    /**
     * Сохраняет конфигурацию
     */
    fun saveConfiguration(configuration: WorkConfiguration) {
        val configurations = getAllConfigurations().toMutableList()
        
        // Удаляем существующую конфигурацию с тем же ID, если есть
        configurations.removeAll { it.id == configuration.id }
        
        // Добавляем новую конфигурацию
        configurations.add(configuration)
        
        // Сортируем по дате создания (новые сверху)
        configurations.sortByDescending { it.createdAt }
        
        saveAllConfigurations(configurations)
    }
    
    /**
     * Получает все сохраненные конфигурации
     */
    fun getAllConfigurations(): List<WorkConfiguration> {
        val configurationsJson = sharedPreferences.getString(KEY_CONFIGURATIONS, "[]")
        return parseConfigurationsFromJson(configurationsJson ?: "[]")
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
        val configurations = getAllConfigurations().toMutableList()
        configurations.removeAll { it.id == id }
        saveAllConfigurations(configurations)
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
     * Сохраняет все конфигурации
     */
    private fun saveAllConfigurations(configurations: List<WorkConfiguration>) {
        val json = convertConfigurationsToJson(configurations)
        sharedPreferences.edit()
            .putString(KEY_CONFIGURATIONS, json)
            .apply()
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
                    
                    val job = Job(
                        id = jobJson.getString("id"),
                        name = jobJson.getString("name"),
                        monthlySalary = jobJson.getDouble("monthlySalary"),
                        hoursPerDay = hoursPerDay
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
}
