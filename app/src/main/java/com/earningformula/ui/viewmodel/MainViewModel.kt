package com.earningformula.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.earningformula.data.models.Job
import com.earningformula.data.models.WorkConfiguration
import com.earningformula.data.models.TotalCalculationResult
import com.earningformula.data.repository.ConfigurationRepository
import com.earningformula.utils.SalaryCalculator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*

data class MainUiState(
    val jobs: List<Job> = emptyList(),
    val totalResult: TotalCalculationResult? = null,
    val savedConfigurations: List<WorkConfiguration> = emptyList(),
    val currentLoadedConfiguration: WorkConfiguration? = null,
    val originalLoadedConfiguration: WorkConfiguration? = null, // Оригинальная конфигурация до изменений
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
                
                val currentJobs = _uiState.value.jobs.toMutableList()
                currentJobs.add(job)

                updateJobsAndRecalculate(currentJobs)
                saveCurrentConfigurationChanges()
                
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
                
                val currentJobs = _uiState.value.jobs.toMutableList()
                val index = currentJobs.indexOfFirst { it.id == updatedJob.id }
                
                if (index != -1) {
                    currentJobs[index] = updatedJob
                    updateJobsAndRecalculate(currentJobs)
                    saveCurrentConfigurationChanges()
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
                val currentJobs = _uiState.value.jobs.toMutableList()
                currentJobs.removeAll { it.id == jobId }

                updateJobsAndRecalculate(currentJobs)
                saveCurrentConfigurationChanges()
                
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
        // Сначала сохраняем текущие изменения (если есть)
        saveCurrentConfigurationChanges()

        viewModelScope.launch {
            try {
                // Затем загружаем новую конфигурацию
                updateJobsAndRecalculate(configuration.jobs)
                // Сохраняем информацию о загруженной конфигурации
                _uiState.value = _uiState.value.copy(
                    currentLoadedConfiguration = configuration,
                    originalLoadedConfiguration = configuration // Сохраняем оригинал
                )
                // Сохраняем ID последней загруженной конфигурации
                configurationRepository.saveLastConfigurationId(configuration.id)

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Ошибка при загрузке конфигурации: ${e.message}"
                )
            }
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
            updateJobsAndRecalculate(emptyList())
            saveCurrentConfigurationChanges()
            // Сбрасываем информацию о загруженной конфигурации
            _uiState.value = _uiState.value.copy(
                currentLoadedConfiguration = null,
                originalLoadedConfiguration = null
            )
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
                
                _uiState.value = _uiState.value.copy(
                    savedConfigurations = configurations,
                    isLoading = false
                )
                
            } catch (e: Exception) {
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
     * Сохраняет изменения в текущую конфигурацию
     */
    private fun saveCurrentConfigurationChanges() {
        viewModelScope.launch {
            try {
                val currentJobs = _uiState.value.jobs
                if (currentJobs.isNotEmpty()) {
                    val currentConfig = _uiState.value.currentLoadedConfiguration

                    if (currentConfig != null) {
                        // Обновляем конфигурацию с текущими работами
                        val updatedConfig = currentConfig.copy(
                            jobs = currentJobs
                        )

                        if (currentConfig.id == "current") {
                            // Сохраняем как текущую конфигурацию
                            configurationRepository.saveCurrentConfiguration(updatedConfig)
                            configurationRepository.saveLastConfigurationId("current")
                        } else {
                            // Обновляем существующую сохраненную конфигурацию
                            configurationRepository.updateConfiguration(updatedConfig)
                            configurationRepository.saveLastConfigurationId(updatedConfig.id)
                            // Обновляем список сохраненных конфигураций, чтобы показать актуальные данные
                            loadSavedConfigurations()
                        }

                        // Обновляем состояние - ВАЖНО: обновляем и originalLoadedConfiguration
                        _uiState.value = _uiState.value.copy(
                            currentLoadedConfiguration = updatedConfig,
                            originalLoadedConfiguration = updatedConfig // Обновляем оригинал после сохранения
                        )
                    } else {
                        // Если нет загруженной конфигурации, создаем новую "текущую"
                        val originalConfig = _uiState.value.originalLoadedConfiguration
                        val configName = originalConfig?.name ?: "Текущая конфигурация"

                        val newCurrentConfig = WorkConfiguration(
                            id = "current",
                            name = configName,
                            jobs = currentJobs
                        )

                        configurationRepository.saveCurrentConfiguration(newCurrentConfig)
                        configurationRepository.saveLastConfigurationId("current")

                        // Обновляем состояние
                        _uiState.value = _uiState.value.copy(
                            currentLoadedConfiguration = newCurrentConfig,
                            originalLoadedConfiguration = newCurrentConfig // Устанавливаем как оригинал
                        )
                    }
                }
            } catch (e: Exception) {
                // Игнорируем ошибки автосохранения
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
}
