package com.earningformula.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.earningformula.R
import com.earningformula.data.models.DayOfWeek
import com.earningformula.data.models.Job
import com.earningformula.data.models.JobInputType
import com.earningformula.data.models.Language
import com.earningformula.utils.SalaryCalculator
import com.earningformula.utils.LocalizationHelper
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddJobDialog(
    job: Job? = null,
    language: Language,
    onDismiss: () -> Unit,
    onSave: (Job) -> Unit
) {
    var jobName by remember { mutableStateOf(job?.name ?: "") }
    var monthlySalary by remember { mutableStateOf(job?.monthlySalary?.toString() ?: "") }
    var inputType by remember { mutableStateOf(job?.inputType ?: JobInputType.DAILY_HOURS) }
    var hoursPerDay by remember {
        mutableStateOf(
            job?.hoursPerDay?.mapValues { it.value.toString() } ?: DayOfWeek.values().associateWith { "0" }
        )
    }
    var totalMonthlyHours by remember { mutableStateOf(job?.totalMonthlyHours?.toString() ?: "") }
    var errors by remember { mutableStateOf<List<String>>(emptyList()) }
    
    val isEditing = job != null
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Заголовок
                Text(
                    text = if (isEditing) LocalizationHelper.getEdit(language) + " " + LocalizationHelper.getAddJob(language).lowercase() else LocalizationHelper.getAddJob(language),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Название работы
                OutlinedTextField(
                    value = jobName,
                    onValueChange = { jobName = it },
                    label = { Text(LocalizationHelper.getJobName(language)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Зарплата в месяц
                OutlinedTextField(
                    value = monthlySalary,
                    onValueChange = { monthlySalary = it },
                    label = { Text(LocalizationHelper.getSalary(language)) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(16.dp))

                // Переключатель типа ввода
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = LocalizationHelper.getTotalMonthlyHoursInput(language),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )
                    Switch(
                        checked = inputType == JobInputType.TOTAL_MONTHLY_HOURS,
                        onCheckedChange = { isChecked ->
                            inputType = if (isChecked) JobInputType.TOTAL_MONTHLY_HOURS else JobInputType.DAILY_HOURS
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (inputType == JobInputType.TOTAL_MONTHLY_HOURS) {
                    // Общие часы в месяц
                    Text(
                        text = LocalizationHelper.getTotalWorkHours(language),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = totalMonthlyHours,
                        onValueChange = { totalMonthlyHours = it },
                        label = { Text(LocalizationHelper.getTotalMonthlyHours(language)) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        suffix = { Text(LocalizationHelper.getHoursShort(language)) }
                    )
                } else {
                    // Часы по дням недели
                    Text(
                        text = LocalizationHelper.getDailyHours(language),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Будние дни
                Text(
                    text = LocalizationHelper.getWeekdays(language) + ":",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                DayOfWeek.values().take(5).forEach { day ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = LocalizationHelper.getDayOfWeekShort(day, language),
                            modifier = Modifier.width(40.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        OutlinedTextField(
                            value = hoursPerDay[day]?.let { if (it == "0") "" else it } ?: "",
                            onValueChange = { newValue ->
                                hoursPerDay = hoursPerDay.toMutableMap().apply {
                                    this[day] = if (newValue.isEmpty()) "0" else newValue
                                }
                            },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true,
                            suffix = { Text(LocalizationHelper.getHoursShort(language)) },
                            placeholder = { Text("0") }
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Выходные дни
                Text(
                    text = LocalizationHelper.getWeekends(language) + ":",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                DayOfWeek.values().drop(5).forEach { day ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = LocalizationHelper.getDayOfWeekShort(day, language),
                            modifier = Modifier.width(40.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        OutlinedTextField(
                            value = hoursPerDay[day]?.let { if (it == "0") "" else it } ?: "",
                            onValueChange = { newValue ->
                                hoursPerDay = hoursPerDay.toMutableMap().apply {
                                    this[day] = if (newValue.isEmpty()) "0" else newValue
                                }
                            },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true,
                            suffix = { Text(LocalizationHelper.getHoursShort(language)) },
                            placeholder = { Text("0") }
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                }
                } // Закрытие блока else для часов по дням

                // Показ ошибок
                if (errors.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            errors.forEach { error ->
                                Text(
                                    text = "• $error",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onErrorContainer
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Кнопки
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(LocalizationHelper.getCancel(language))
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Button(
                        onClick = {
                            val newJob = try {
                                Job(
                                    id = job?.id ?: UUID.randomUUID().toString(),
                                    name = jobName.trim(),
                                    monthlySalary = monthlySalary.toDoubleOrNull() ?: 0.0,
                                    inputType = inputType,
                                    hoursPerDay = if (inputType == JobInputType.DAILY_HOURS) {
                                        hoursPerDay.mapValues { it.value.toDoubleOrNull() ?: 0.0 }
                                    } else {
                                        DayOfWeek.values().associateWith { 0.0 }
                                    },
                                    totalMonthlyHours = if (inputType == JobInputType.TOTAL_MONTHLY_HOURS) {
                                        totalMonthlyHours.toDoubleOrNull() ?: 0.0
                                    } else {
                                        0.0
                                    }
                                )
                            } catch (e: Exception) {
                                errors = listOf("Ошибка при создании работы: ${e.message}")
                                return@Button
                            }
                            
                            val validationErrors = SalaryCalculator.validateJob(newJob)
                            if (validationErrors.isNotEmpty()) {
                                errors = validationErrors
                            } else {
                                onSave(newJob)
                            }
                        }
                    ) {
                        Text(LocalizationHelper.getSave(language))
                    }
                }
            }
        }
    }
}
