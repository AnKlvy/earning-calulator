package com.earningformula.data.repository

import com.earningformula.data.local.SharedPreferencesManager
import com.earningformula.data.models.WorkConfiguration
import com.earningformula.data.models.Currency
import com.earningformula.data.models.Language
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ConfigurationRepository(
    private val sharedPreferencesManager: SharedPreferencesManager = SharedPreferencesManager.getInstance()
) {
    
    /**
     * Сохраняет конфигурацию в локальное хранилище
     */
    suspend fun saveConfiguration(configuration: WorkConfiguration) {
        withContext(Dispatchers.IO) {
            try {
                sharedPreferencesManager.saveConfiguration(configuration)
            } catch (e: Exception) {
                throw ConfigurationException("Ошибка при сохранении конфигурации: ${e.message}", e)
            }
        }
    }
    
    /**
     * Получает все сохраненные конфигурации
     */
    suspend fun getAllConfigurations(): List<WorkConfiguration> {
        return withContext(Dispatchers.IO) {
            try {
                sharedPreferencesManager.getAllConfigurations()
            } catch (e: Exception) {
                throw ConfigurationException("Ошибка при загрузке конфигураций: ${e.message}", e)
            }
        }
    }
    
    /**
     * Получает конфигурацию по ID
     */
    suspend fun getConfigurationById(id: String): WorkConfiguration? {
        return withContext(Dispatchers.IO) {
            try {
                sharedPreferencesManager.getConfigurationById(id)
            } catch (e: Exception) {
                throw ConfigurationException("Ошибка при загрузке конфигурации: ${e.message}", e)
            }
        }
    }
    
    /**
     * Удаляет конфигурацию по ID
     */
    suspend fun deleteConfiguration(id: String) {
        withContext(Dispatchers.IO) {
            try {
                sharedPreferencesManager.deleteConfiguration(id)
            } catch (e: Exception) {
                throw ConfigurationException("Ошибка при удалении конфигурации: ${e.message}", e)
            }
        }
    }
    
    /**
     * Удаляет все конфигурации
     */
    suspend fun deleteAllConfigurations() {
        withContext(Dispatchers.IO) {
            try {
                sharedPreferencesManager.deleteAllConfigurations()
            } catch (e: Exception) {
                throw ConfigurationException("Ошибка при удалении всех конфигураций: ${e.message}", e)
            }
        }
    }
    
    /**
     * Проверяет, существует ли конфигурация с данным именем
     */
    suspend fun isConfigurationNameExists(name: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val configurations = sharedPreferencesManager.getAllConfigurations()
                configurations.any { it.name.equals(name, ignoreCase = true) }
            } catch (e: Exception) {
                false
            }
        }
    }
    
    /**
     * Обновляет существующую конфигурацию
     */
    suspend fun updateConfiguration(configuration: WorkConfiguration) {
        withContext(Dispatchers.IO) {
            try {
                // Проверяем, существует ли конфигурация
                val existing = sharedPreferencesManager.getConfigurationById(configuration.id)
                if (existing == null) {
                    throw ConfigurationException("Конфигурация с ID ${configuration.id} не найдена")
                }
                
                sharedPreferencesManager.saveConfiguration(configuration)
            } catch (e: Exception) {
                if (e is ConfigurationException) throw e
                throw ConfigurationException("Ошибка при обновлении конфигурации: ${e.message}", e)
            }
        }
    }
    
    /**
     * Получает количество сохраненных конфигураций
     */
    suspend fun getConfigurationsCount(): Int {
        return withContext(Dispatchers.IO) {
            try {
                sharedPreferencesManager.getConfigurationsCount()
            } catch (e: Exception) {
                0
            }
        }
    }
    
    /**
     * Экспортирует все конфигурации в JSON строку
     */
    suspend fun exportConfigurations(): String {
        return withContext(Dispatchers.IO) {
            try {
                sharedPreferencesManager.exportConfigurations()
            } catch (e: Exception) {
                throw ConfigurationException("Ошибка при экспорте конфигураций: ${e.message}", e)
            }
        }
    }
    
    /**
     * Импортирует конфигурации из JSON строки
     */
    suspend fun importConfigurations(jsonData: String, replaceExisting: Boolean = false) {
        withContext(Dispatchers.IO) {
            try {
                if (replaceExisting) {
                    sharedPreferencesManager.deleteAllConfigurations()
                }
                sharedPreferencesManager.importConfigurations(jsonData)
            } catch (e: Exception) {
                throw ConfigurationException("Ошибка при импорте конфигураций: ${e.message}", e)
            }
        }
    }

    /**
     * Сохраняет ID последней использованной конфигурации
     */
    suspend fun saveLastConfigurationId(configId: String) {
        withContext(Dispatchers.IO) {
            try {
                sharedPreferencesManager.saveLastConfigurationId(configId)
            } catch (e: Exception) {
                throw ConfigurationException("Ошибка при сохранении ID последней конфигурации: ${e.message}", e)
            }
        }
    }

    /**
     * Получает последнюю использованную конфигурацию
     */
    suspend fun getLastConfiguration(): WorkConfiguration? {
        return withContext(Dispatchers.IO) {
            try {
                val lastConfigId = sharedPreferencesManager.getLastConfigurationId()
                if (lastConfigId != null) {
                    if (lastConfigId == "current") {
                        // Загружаем текущую конфигурацию
                        sharedPreferencesManager.getCurrentConfiguration()
                    } else {
                        // Загружаем сохраненную конфигурацию
                        sharedPreferencesManager.getConfigurationById(lastConfigId)
                    }
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
    }

    /**
     * Сохраняет текущую конфигурацию
     */
    suspend fun saveCurrentConfiguration(configuration: WorkConfiguration) {
        withContext(Dispatchers.IO) {
            try {
                sharedPreferencesManager.saveCurrentConfiguration(configuration)
            } catch (e: Exception) {
                throw ConfigurationException("Ошибка при сохранении текущей конфигурации: ${e.message}", e)
            }
        }
    }
    /**
     * Сохраняет выбранную валюту
     */
    suspend fun saveCurrency(currency: Currency) {
        withContext(Dispatchers.IO) {
            try {
                sharedPreferencesManager.saveCurrency(currency)
            } catch (e: Exception) {
                throw ConfigurationException("Ошибка при сохранении валюты: ${e.message}", e)
            }
        }
    }

    /**
     * Получает сохраненную валюту
     */
    suspend fun getCurrency(): Currency {
        return withContext(Dispatchers.IO) {
            try {
                sharedPreferencesManager.getCurrency()
            } catch (e: Exception) {
                throw ConfigurationException("Ошибка при загрузке валюты: ${e.message}", e)
            }
        }
    }

    /**
     * Сохраняет выбранный язык
     */
    suspend fun saveLanguage(language: Language) {
        withContext(Dispatchers.IO) {
            try {
                sharedPreferencesManager.saveLanguage(language)
            } catch (e: Exception) {
                throw ConfigurationException("Ошибка при сохранении языка: ${e.message}", e)
            }
        }
    }

    /**
     * Получает сохраненный язык
     */
    suspend fun getLanguage(): Language {
        return withContext(Dispatchers.IO) {
            try {
                sharedPreferencesManager.getLanguage()
            } catch (e: Exception) {
                throw ConfigurationException("Ошибка при загрузке языка: ${e.message}", e)
            }
        }
    }
}

/**
 * Исключение для ошибок работы с конфигурациями
 */
class ConfigurationException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause)
