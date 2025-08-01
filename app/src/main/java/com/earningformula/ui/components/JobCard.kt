package com.earningformula.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.earningformula.R
import com.earningformula.data.models.Job
import com.earningformula.data.models.JobInputType
import com.earningformula.data.models.Currency
import com.earningformula.data.models.Language
import com.earningformula.utils.SalaryCalculator
import com.earningformula.utils.LocalizationHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobCard(
    job: Job,
    currency: Currency,
    language: Language,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Заголовок с кнопками
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = job.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                
                Row {
                    IconButton(onClick = onEditClick) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Редактировать"
                        )
                    }
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(R.string.delete),
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Основная информация
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    InfoItem(
                        label = LocalizationHelper.getMonthlySalary(language) + ":",
                        value = SalaryCalculator.formatMoney(job.monthlySalary, currency)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    if (job.inputType == JobInputType.DAILY_HOURS) {
                        InfoItem(
                            label = LocalizationHelper.getWeeklyHours(language) + ":",
                            value = SalaryCalculator.formatHours(job.getWeeklyHours()) + " " + LocalizationHelper.getHoursShort(language)
                        )
                    } else {
                        InfoItem(
                            label = LocalizationHelper.getTotalWorkHours(language) + ":",
                            value = SalaryCalculator.formatHours(job.totalMonthlyHours) + " " + LocalizationHelper.getHoursShort(language)
                        )
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    if (job.inputType == JobInputType.DAILY_HOURS) {
                        InfoItem(
                            label = LocalizationHelper.getTotalMonthlyHours(language) + ":",
                            value = SalaryCalculator.formatHours(job.getMonthlyHours()) + " " + LocalizationHelper.getHoursShort(language)
                        )
                    } else {
                        InfoItem(
                            label = LocalizationHelper.getWeeklyHours(language) + ":",
                            value = SalaryCalculator.formatHours(job.getWeeklyHours()) + " " + LocalizationHelper.getHoursShort(language)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    InfoItem(
                        label = LocalizationHelper.getHourlyRate(language) + ":",
                        value = SalaryCalculator.formatMoney(job.getHourlyRate(), currency) + LocalizationHelper.getPerHour(language),
                        valueColor = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))

            // Часы по дням недели (только для ежедневного ввода)
            if (job.inputType == JobInputType.DAILY_HOURS) {
                Text(
                    text = "Часы по дням:",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Будние дни
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                listOf("Пн", "Вт", "Ср", "Чт", "Пт").forEachIndexed { index, day ->
                    val dayOfWeek = com.earningformula.data.models.DayOfWeek.values()[index]
                    val hours = job.hoursPerDay[dayOfWeek] ?: 0.0

                    DayHoursChip(
                        day = day,
                        hours = hours,
                        isWeekend = false,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            // Выходные дни
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                listOf("Сб", "Вс").forEachIndexed { index, day ->
                    val dayOfWeek = com.earningformula.data.models.DayOfWeek.values()[index + 5]
                    val hours = job.hoursPerDay[dayOfWeek] ?: 0.0
                    
                    DayHoursChip(
                        day = day,
                        hours = hours,
                        isWeekend = true,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }
            } // Закрытие блока if для часов по дням
        }
    }
    
    // Диалог подтверждения удаления
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Удалить работу?") },
            text = { Text("Вы уверены, что хотите удалить работу \"${job.name}\"?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteClick()
                        showDeleteDialog = false
                    }
                ) {
                    Text(
                        text = stringResource(R.string.delete),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}

@Composable
private fun InfoItem(
    label: String,
    value: String,
    valueColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = valueColor
        )
    }
}

@Composable
private fun DayHoursChip(
    day: String,
    hours: Double,
    isWeekend: Boolean,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isWeekend) {
        MaterialTheme.colorScheme.secondaryContainer
    } else {
        MaterialTheme.colorScheme.primaryContainer
    }
    
    val contentColor = if (isWeekend) {
        MaterialTheme.colorScheme.onSecondaryContainer
    } else {
        MaterialTheme.colorScheme.onPrimaryContainer
    }
    
    Surface(
        modifier = modifier.width(48.dp),
        shape = MaterialTheme.shapes.small,
        color = backgroundColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = day,
                style = MaterialTheme.typography.labelSmall,
                color = contentColor
            )
            Text(
                text = if (hours > 0) SalaryCalculator.formatHours(hours) else "0",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium,
                color = contentColor
            )
        }
    }
}
