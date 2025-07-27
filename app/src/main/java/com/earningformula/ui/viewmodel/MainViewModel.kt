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
        // Загружаем пример конфигурации для демонстрации
        loadSampleData()
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
                
                val configuration = WorkConfiguration(
                    id = UUID.randomUUID().toString(),
                    name = name.trim(),
                    jobs = _uiState.value.jobs
                )
                
                configurationRepository.saveConfiguration(configuration)
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
        viewModelScope.launch {
            try {
                updateJobsAndRecalculate(configuration.jobs)
                
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
                configurationRepository.deleteConfiguration(configId)
                loadSavedConfigurations()
                
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
        val configuration = WorkConfiguration(
            id = "current",
            name = "Текущая конфигурация",
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
     * Загружает пример данных для демонстрации
     */
    private fun loadSampleData() {
        viewModelScope.launch {
            try {
                // Проверяем, есть ли уже сохраненные конфигурации
                val savedConfigs = configurationRepository.getAllConfigurations()
                
                // Если нет сохраненных конфигураций, загружаем пример
                if (savedConfigs.isEmpty()) {
                    val sampleConfig = SalaryCalculator.createSampleConfiguration()
                    configurationRepository.saveConfiguration(sampleConfig)
                    loadSavedConfigurations()
                }
                
            } catch (e: Exception) {
                // Игнорируем ошибки при загрузке примера
            }
        }
    }
}
