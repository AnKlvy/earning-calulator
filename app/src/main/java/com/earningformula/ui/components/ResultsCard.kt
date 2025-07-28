package com.earningformula.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.earningformula.R
import com.earningformula.data.models.TotalCalculationResult
import com.earningformula.data.models.JobInputType
import com.earningformula.data.models.WorkConfiguration
import com.earningformula.utils.SalaryCalculator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsCard(
    totalResult: TotalCalculationResult?,
    currentLoadedConfiguration: WorkConfiguration?,
    originalLoadedConfiguration: WorkConfiguration?,
    onSaveConfiguration: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showSaveDialog by remember { mutableStateOf(false) }
    
    if (totalResult == null) return
    
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Заголовок с кнопкой сохранения
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.total_results),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                
                IconButton(
                    onClick = { showSaveDialog = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = stringResource(R.string.save_configuration),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Основные показатели
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    ResultItem(
                        label = stringResource(R.string.total_weekly_hours),
                        value = "${SalaryCalculator.formatHours(totalResult.totalWeeklyHours)} ч",
                        isHighlighted = false
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ResultItem(
                        label = stringResource(R.string.total_monthly_hours),
                        value = "${SalaryCalculator.formatHours(totalResult.totalMonthlyHours)} ч",
                        isHighlighted = false
                    )
                }
                
                Column(modifier = Modifier.weight(1f)) {
                    ResultItem(
                        label = stringResource(R.string.total_monthly_salary),
                        value = SalaryCalculator.formatMoney(totalResult.totalMonthlySalary),
                        isHighlighted = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ResultItem(
                        label = "Средняя ставка в час",
                        value = "${SalaryCalculator.formatMoney(totalResult.averageHourlyRate)}/ч",
                        isHighlighted = true
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Разбивка по будням/выходным
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "Разбивка по дням:",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Medium
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Будни:",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "${SalaryCalculator.formatHours(totalResult.totalWeekdayHours)} ч/нед",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Выходные:",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "${SalaryCalculator.formatHours(totalResult.totalWeekendHours)} ч/нед",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Детализация по работам
            if (totalResult.jobResults.isNotEmpty()) {
                Text(
                    text = "Детализация по работам:",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                totalResult.jobResults.forEach { jobResult ->
                    JobResultItem(
                        jobResult = jobResult,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
    
    // Диалог сохранения конфигурации
    if (showSaveDialog) {
        SaveConfigurationDialog(
            currentConfigurationName = originalLoadedConfiguration?.name,
            onDismiss = { showSaveDialog = false },
            onSave = { configName ->
                onSaveConfiguration(configName)
                showSaveDialog = false
            }
        )
    }
}

@Composable
private fun ResultItem(
    label: String,
    value: String,
    isHighlighted: Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            style = if (isHighlighted) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyLarge,
            fontWeight = if (isHighlighted) FontWeight.Bold else FontWeight.Medium,
            color = if (isHighlighted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
private fun JobResultItem(
    jobResult: com.earningformula.data.models.CalculationResult,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = jobResult.job.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = when (jobResult.job.inputType) {
                        JobInputType.DAILY_HOURS -> "${SalaryCalculator.formatHours(jobResult.weeklyHours)} ч/нед"
                        JobInputType.TOTAL_MONTHLY_HOURS -> "${SalaryCalculator.formatHours(jobResult.job.totalMonthlyHours)} ч/мес"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = SalaryCalculator.formatMoney(jobResult.job.monthlySalary),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${SalaryCalculator.formatMoney(jobResult.hourlyRate)}/ч",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun SaveConfigurationDialog(
    currentConfigurationName: String?,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var configName by remember { mutableStateOf(currentConfigurationName ?: "") }
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = stringResource(R.string.save_configuration),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = configName,
                    onValueChange = { configName = it },
                    label = { Text(stringResource(R.string.configuration_name)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(stringResource(R.string.cancel))
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Button(
                        onClick = { 
                            if (configName.isNotBlank()) {
                                onSave(configName.trim())
                            }
                        },
                        enabled = configName.isNotBlank()
                    ) {
                        Text(stringResource(R.string.save))
                    }
                }
            }
        }
    }
}
