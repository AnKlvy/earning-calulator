package com.earningformula.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.earningformula.data.models.Job
import com.earningformula.data.models.WorkConfiguration
import com.earningformula.data.models.TotalCalculationResult
import com.earningformula.data.models.Currency
import com.earningformula.data.models.Language
import com.earningformula.data.repository.ConfigurationRepository
import com.earningformula.utils.SalaryCalculator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log
import java.util.*

data class MainUiState(
    val jobs: List<Job> = emptyList(),
    val totalResult: TotalCalculationResult? = null,
    val savedConfigurations: List<WorkConfiguration> = emptyList(),
    val currentLoadedConfiguration: WorkConfiguration? = null,
    val originalLoadedConfiguration: WorkConfiguration? = null, // Оригинальная конфигурация до изменений
    val selectedCurrency: Currency = Currency.RUB,
    val selectedLanguage: Language = Language.RUSSIAN,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class MainViewModel(
    private val configurationRepository: ConfigurationRepository = ConfigurationRepository()
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()
    
    init {
        loadSavedConfigurations()
        loadSavedCurrency()
        loadSavedLanguage()
        // Загружаем последнюю конфигурацию или пример для демонстрации
        loadLastOrSampleConfiguration()
    }
    
    /**
     * Добавляет новую работу
     */
    fun addJob(job: Job) {
        viewModelScope.launch {
            try {
                val validationErrors = SalaryCalculator.validateJob(job)
                if (validationErrors.isNotEmpty()) {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = validationErrors.joinToString("\n")
                    )
                    return@launch
                }

                Log.d("MainViewModel", "Добавляем работу: ${job.name}")

                val currentJobs = _uiState.value.jobs.toMutableList()
                currentJobs.add(job)

                updateJobsAndRecalculate(currentJobs)
                updateCurrentConfiguration() // Используем единый метод

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Ошибка при добавлении работы: ${e.message}"
                )
            }
        }
    }
    
    /**
     * Обновляет существующую работу
     */
    fun updateJob(updatedJob: Job) {
        viewModelScope.launch {
            try {
                val validationErrors = SalaryCalculator.validateJob(updatedJob)
                if (validationErrors.isNotEmpty()) {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = validationErrors.joinToString("\n")
                    )
                    return@launch
                }

                Log.d("MainViewModel", "Обновляем работу: ${updatedJob.name}")

                val currentJobs = _uiState.value.jobs.toMutableList()
                val index = currentJobs.indexOfFirst { it.id == updatedJob.id }

                if (index != -1) {
                    currentJobs[index] = updatedJob
                    updateJobsAndRecalculate(currentJobs)
                    updateCurrentConfiguration() // Используем единый метод
                } else {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Работа не найдена для обновления"
                    )
                }

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Ошибка при обновлении работы: ${e.message}"
                )
            }
        }
    }
    
    /**
     * Удаляет работу по ID
     */
    fun deleteJob(jobId: String) {
        viewModelScope.launch {
            try {
                Log.d("MainViewModel", "Удаляем работу с ID: $jobId")

                val currentJobs = _uiState.value.jobs.toMutableList()
                currentJobs.removeAll { it.id == jobId }

                updateJobsAndRecalculate(currentJobs)
                updateCurrentConfiguration() // Используем единый метод

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Ошибка при удалении работы: ${e.message}"
                )
            }
        }
    }
    
    /**
     * Сохраняет текущую конфигурацию в избранное
     */
    fun saveConfiguration(name: String) {
        viewModelScope.launch {
            try {
                if (name.isBlank()) {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Название конфигурации не может быть пустым"
                    )
                    return@launch
                }

                if (_uiState.value.jobs.isEmpty()) {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Нельзя сохранить пустую конфигурацию"
                    )
                    return@launch
                }

                val originalConfig = _uiState.value.originalLoadedConfiguration
                val trimmedName = name.trim()

                // Проверяем, нужно ли обновить существующую конфигурацию
                val shouldUpdateExisting = originalConfig != null &&
                    originalConfig.name == trimmedName

                val configuration = if (shouldUpdateExisting) {
                    // Обновляем существующую конфигурацию (используем оригинальный ID)
                    originalConfig!!.copy(
                        jobs = _uiState.value.jobs,
                        createdAt = System.currentTimeMillis() // Обновляем время изменения
                    )
                } else {
                    // Создаем новую конфигурацию
                    WorkConfiguration(
                        id = UUID.randomUUID().toString(),
                        name = trimmedName,
                        jobs = _uiState.value.jobs
                    )
                }

                configurationRepository.saveConfiguration(configuration)

                // Обновляем информацию о текущей загруженной конфигурации
                _uiState.value = _uiState.value.copy(
                    currentLoadedConfiguration = configuration,
                    originalLoadedConfiguration = configuration // Обновляем и оригинал
                )

                loadSavedConfigurations()

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Ошибка при сохранении конфигурации: ${e.message}"
                )
            }
        }
    }
    
    /**
     * Загружает конфигурацию из избранного
     */
    fun loadConfiguration(configuration: WorkConfiguration) {
        try {
            Log.d("MainViewModel", "Загружаем конфигурацию: ${configuration.name} (${configuration.id})")

            // Используем единый метод установки конфигурации
            setCurrentConfiguration(configuration)

        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Ошибка при загрузке конфигурации: ${e.message}"
            )
        }
    }
    
    /**
     * Удаляет сохраненную конфигурацию
     */
    fun deleteConfiguration(configId: String) {
        viewModelScope.launch {
            try {
                // Проверяем, удаляем ли мы текущую загруженную конфигурацию
                val currentConfig = _uiState.value.currentLoadedConfiguration
                val originalConfig = _uiState.value.originalLoadedConfiguration

                configurationRepository.deleteConfiguration(configId)
                loadSavedConfigurations()

                // Если удаляем текущую конфигурацию, сбрасываем состояние
                if (currentConfig?.id == configId || originalConfig?.id == configId) {
                    _uiState.value = _uiState.value.copy(
                        currentLoadedConfiguration = null,
                        originalLoadedConfiguration = null
                    )
                }

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Ошибка при удалении конфигурации: ${e.message}"
                )
            }
        }
    }
    
    /**
     * Очищает сообщение об ошибке
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
    
    /**
     * Очищает все работы
     */
    fun clearAllJobs() {
        viewModelScope.launch {
            Log.d("MainViewModel", "Очищаем все работы")
            updateJobsAndRecalculate(emptyList())
            updateCurrentConfiguration() // Используем единый метод
            // Сбрасываем информацию о загруженной конфигурации
            _uiState.value = _uiState.value.copy(
                currentLoadedConfiguration = null,
                originalLoadedConfiguration = null
            )
        }
    }

    /**
     * Создает новую конфигурацию (очищает текущую)
     */
    fun createNewConfiguration() {
        viewModelScope.launch {
            try {
                // ВАЖНО: Сначала сохраняем текущие изменения, если есть работы
                val currentJobs = _uiState.value.jobs
                if (currentJobs.isNotEmpty()) {
                    updateCurrentConfiguration()
                }

                // Создаем пустую "текущую" конфигурацию
                val emptyCurrentConfig = WorkConfiguration(
                    id = "current",
                    name = "Новая конфигурация",
                    jobs = emptyList()
                )

                // Очищаем все работы и сбрасываем конфигурацию
                updateJobsAndRecalculate(emptyList())
                _uiState.value = _uiState.value.copy(
                    currentLoadedConfiguration = emptyCurrentConfig,
                    originalLoadedConfiguration = emptyCurrentConfig
                )

                // Сохраняем пустую текущую конфигурацию и устанавливаем как последнюю
                configurationRepository.saveCurrentConfiguration(emptyCurrentConfig)
                configurationRepository.saveLastConfigurationId("current")

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Ошибка при создании новой конфигурации: ${e.message}"
                )
            }
        }
    }

    /**
     * Создает новую конфигурацию с заданным именем и сохраняет в избранное
     */
    fun createNewConfigurationWithName(name: String) {
        viewModelScope.launch {
            try {
                Log.d("MainViewModel", "Создание новой конфигурации с именем: $name")

                if (name.isBlank()) {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Название конфигурации не может быть пустым"
                    )
                    return@launch
                }

                // При создании новой конфигурации НЕ сохраняем текущие изменения
                // Пользователь явно хочет создать новую конфигурацию, значит текущие изменения не нужны
                Log.d("MainViewModel", "Создаем новую конфигурацию, текущие изменения будут сброшены")

                val trimmedName = name.trim()

                // Создаем новую конфигурацию с заданным именем
                val newConfiguration = WorkConfiguration(
                    id = UUID.randomUUID().toString(),
                    name = trimmedName,
                    jobs = emptyList()
                )

                Log.d("MainViewModel", "Создана конфигурация: ${newConfiguration.id}, имя: ${newConfiguration.name}")

                // Сохраняем новую конфигурацию в избранное
                configurationRepository.saveConfiguration(newConfiguration)
                Log.d("MainViewModel", "Конфигурация сохранена в репозиторий")

                // Устанавливаем как текущую конфигурацию (единый метод)
                setCurrentConfiguration(newConfiguration)

                // Обновляем список сохраненных конфигураций
                loadSavedConfigurations()

                Log.d("MainViewModel", "Список конфигураций обновлен. Всего: ${_uiState.value.savedConfigurations.size}")

            } catch (e: Exception) {
                Log.e("MainViewModel", "Ошибка при создании конфигурации", e)
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Ошибка при создании новой конфигурации: ${e.message}"
                )
            }
        }
    }

    /**
     * Принудительно загружает текущую сохраненную конфигурацию (для восстановления данных)
     */
    fun loadCurrentConfiguration() {
        viewModelScope.launch {
            try {
                val currentConfig = configurationRepository.getLastConfiguration()
                if (currentConfig != null) {
                    updateJobsAndRecalculate(currentConfig.jobs)
                    _uiState.value = _uiState.value.copy(
                        currentLoadedConfiguration = currentConfig,
                        originalLoadedConfiguration = currentConfig
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Ошибка при загрузке текущей конфигурации: ${e.message}"
                )
            }
        }
    }
    
    /**
     * Загружает сохраненные конфигурации
     */
    private fun loadSavedConfigurations() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)

                val configurations = configurationRepository.getAllConfigurations()
                Log.d("MainViewModel", "Загружено конфигураций: ${configurations.size}")
                configurations.forEach { config ->
                    Log.d("MainViewModel", "Конфигурация: ${config.name} (${config.id}), работ: ${config.jobs.size}")
                }

                _uiState.value = _uiState.value.copy(
                    savedConfigurations = configurations,
                    isLoading = false
                )

            } catch (e: Exception) {
                Log.e("MainViewModel", "Ошибка при загрузке конфигураций", e)
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Ошибка при загрузке сохраненных конфигураций: ${e.message}",
                    isLoading = false
                )
            }
        }
    }
    
    /**
     * Обновляет список работ и пересчитывает результаты
     */
    private fun updateJobsAndRecalculate(jobs: List<Job>) {
        // Используем название оригинальной загруженной конфигурации или дефолтное
        val originalConfig = _uiState.value.originalLoadedConfiguration
        val configName = originalConfig?.name ?: "Текущая конфигурация"

        val configuration = WorkConfiguration(
            id = "current",
            name = configName,
            jobs = jobs
        )

        val totalResult = if (jobs.isNotEmpty()) {
            SalaryCalculator.calculateTotalResults(configuration)
        } else {
            null
        }

        _uiState.value = _uiState.value.copy(
            jobs = jobs,
            totalResult = totalResult
        )
    }
    
    /**
     * Загружает последнюю конфигурацию или пример данных для демонстрации
     */
    private fun loadLastOrSampleConfiguration() {
        viewModelScope.launch {
            try {
                // Сначала пытаемся загрузить последнюю конфигурацию
                val lastConfig = configurationRepository.getLastConfiguration()

                if (lastConfig != null) {
                    // Загружаем последнюю конфигурацию
                    updateJobsAndRecalculate(lastConfig.jobs)
                    _uiState.value = _uiState.value.copy(
                        currentLoadedConfiguration = lastConfig,
                        originalLoadedConfiguration = lastConfig
                    )
                } else {
                    // Проверяем, есть ли уже сохраненные конфигурации
                    val savedConfigs = configurationRepository.getAllConfigurations()

                    if (savedConfigs.isEmpty()) {
                        // Если нет сохраненных конфигураций, создаем и загружаем пример
                        val sampleConfig = SalaryCalculator.createSampleConfiguration()
                        configurationRepository.saveConfiguration(sampleConfig)
                        loadSavedConfigurations()
                        updateJobsAndRecalculate(sampleConfig.jobs)
                        _uiState.value = _uiState.value.copy(
                            currentLoadedConfiguration = sampleConfig,
                            originalLoadedConfiguration = sampleConfig
                        )
                        configurationRepository.saveLastConfigurationId(sampleConfig.id)
                    } else {
                        // Если есть сохраненные конфигурации, загружаем первую
                        val firstConfig = savedConfigs.first()
                        updateJobsAndRecalculate(firstConfig.jobs)
                        _uiState.value = _uiState.value.copy(
                            currentLoadedConfiguration = firstConfig,
                            originalLoadedConfiguration = firstConfig
                        )
                        configurationRepository.saveLastConfigurationId(firstConfig.id)
                    }
                }

            } catch (e: Exception) {
                // Игнорируем ошибки при загрузке
            }
        }
    }

    /**
     * ЕДИНЫЙ МЕТОД ДЛЯ УСТАНОВКИ ТЕКУЩЕЙ КОНФИГУРАЦИИ
     * Устанавливает конфигурацию как текущую и обновляет UI
     */
    private fun setCurrentConfiguration(configuration: WorkConfiguration) {
        Log.d("MainViewModel", "Устанавливаем текущую конфигурацию: ${configuration.name} (${configuration.id})")

        // Обновляем состояние UI
        _uiState.value = _uiState.value.copy(
            currentLoadedConfiguration = configuration,
            originalLoadedConfiguration = configuration
        )

        // Загружаем работы из конфигурации
        updateJobsAndRecalculate(configuration.jobs)

        // Сохраняем как последнюю конфигурацию
        viewModelScope.launch {
            try {
                configurationRepository.saveLastConfigurationId(configuration.id)
            } catch (e: Exception) {
                Log.e("MainViewModel", "Ошибка при сохранении ID последней конфигурации", e)
            }
        }
    }

    /**
     * ЕДИНЫЙ МЕТОД ОБНОВЛЕНИЯ ТЕКУЩЕЙ КОНФИГУРАЦИИ
     * Все изменения работ должны идти через этот метод
     */
    private fun updateCurrentConfiguration() {
        viewModelScope.launch {
            try {
                val currentJobs = _uiState.value.jobs
                val currentConfig = _uiState.value.currentLoadedConfiguration

                if (currentConfig != null) {
                    Log.d("MainViewModel", "Обновляем конфигурацию: ${currentConfig.name} (${currentConfig.id}), работ: ${currentJobs.size}")

                    // Создаем обновленную конфигурацию
                    val updatedConfig = currentConfig.copy(
                        jobs = currentJobs
                    )

                    // Сохраняем в базу данных
                    if (currentConfig.id == "current") {
                        configurationRepository.saveCurrentConfiguration(updatedConfig)
                        configurationRepository.saveLastConfigurationId("current")
                    } else {
                        configurationRepository.updateConfiguration(updatedConfig)
                        configurationRepository.saveLastConfigurationId(updatedConfig.id)
                    }

                    // Обновляем состояние UI
                    _uiState.value = _uiState.value.copy(
                        currentLoadedConfiguration = updatedConfig,
                        originalLoadedConfiguration = updatedConfig
                    )

                    // Обновляем список сохраненных конфигураций
                    loadSavedConfigurations()

                    Log.d("MainViewModel", "Конфигурация успешно обновлена")
                } else {
                    Log.w("MainViewModel", "Нет текущей конфигурации для обновления")
                }
            } catch (e: Exception) {
                Log.e("MainViewModel", "Ошибка при обновлении конфигурации", e)
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Ошибка при сохранении изменений: ${e.message}"
                )
            }
        }
    }



    /**
     * Помечает конфигурацию как измененную (сбрасывает связь с загруженной конфигурацией)
     */
    private fun markConfigurationAsModified() {
        val currentConfig = _uiState.value.currentLoadedConfiguration
        if (currentConfig != null && currentConfig.id != "current") {
            // Создаем копию конфигурации с ID "current" чтобы показать, что она изменена
            val modifiedConfig = currentConfig.copy(
                id = "current",
                jobs = _uiState.value.jobs
            )
            _uiState.value = _uiState.value.copy(
                currentLoadedConfiguration = modifiedConfig
            )
        }
    }

    /**
     * Изменяет валюту приложения
     */
    fun changeCurrency(currency: Currency) {
        viewModelScope.launch {
            try {
                Log.d("MainViewModel", "Изменяем валюту на: ${currency.displayName}")

                // Сохраняем в настройки
                configurationRepository.saveCurrency(currency)

                // Обновляем UI состояние
                _uiState.value = _uiState.value.copy(
                    selectedCurrency = currency
                )

                Log.d("MainViewModel", "Валюта успешно изменена")
            } catch (e: Exception) {
                Log.e("MainViewModel", "Ошибка при изменении валюты", e)
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Ошибка при изменении валюты: ${e.message}"
                )
            }
        }
    }

    /**
     * Загружает сохраненную валюту при инициализации
     */
    private fun loadSavedCurrency() {
        viewModelScope.launch {
            try {
                val savedCurrency = configurationRepository.getCurrency()
                _uiState.value = _uiState.value.copy(
                    selectedCurrency = savedCurrency
                )
                Log.d("MainViewModel", "Загружена валюта: ${savedCurrency.displayName}")
            } catch (e: Exception) {
                Log.e("MainViewModel", "Ошибка при загрузке валюты", e)
                // Используем валюту по умолчанию
            }
        }
    }

    /**
     * Изменяет язык приложения
     */
    fun changeLanguage(language: Language) {
        viewModelScope.launch {
            try {
                Log.d("MainViewModel", "Изменяем язык на: ${language.displayName}")

                // Сохраняем в настройки
                configurationRepository.saveLanguage(language)

                // Обновляем UI состояние
                _uiState.value = _uiState.value.copy(
                    selectedLanguage = language
                )

                Log.d("MainViewModel", "Язык успешно изменен")
            } catch (e: Exception) {
                Log.e("MainViewModel", "Ошибка при изменении языка", e)
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Ошибка при изменении языка: ${e.message}"
                )
            }
        }
    }

    /**
     * Загружает сохраненный язык при инициализации
     */
    private fun loadSavedLanguage() {
        viewModelScope.launch {
            try {
                val savedLanguage = configurationRepository.getLanguage()
                _uiState.value = _uiState.value.copy(
                    selectedLanguage = savedLanguage
                )
                Log.d("MainViewModel", "Загружен язык: ${savedLanguage.displayName}")
            } catch (e: Exception) {
                Log.e("MainViewModel", "Ошибка при загрузке языка", e)
                // Используем язык по умолчанию
            }
        }
    }
}
