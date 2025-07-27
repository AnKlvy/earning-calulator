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
import com.earningformula.utils.SalaryCalculator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobCard(
    job: Job,
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
                        label = "Зарплата в месяц:",
                        value = SalaryCalculator.formatMoney(job.monthlySalary)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    InfoItem(
                        label = stringResource(R.string.weekly_hours) + ":",
                        value = SalaryCalculator.formatHours(job.getWeeklyHours()) + " ч"
                    )
                }
                
                Column(modifier = Modifier.weight(1f)) {
                    InfoItem(
                        label = stringResource(R.string.monthly_hours) + ":",
                        value = SalaryCalculator.formatHours(job.getMonthlyHours()) + " ч"
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    InfoItem(
                        label = stringResource(R.string.hourly_rate) + ":",
                        value = SalaryCalculator.formatMoney(job.getHourlyRate()) + "/ч",
                        valueColor = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Часы по дням недели
            Text(
                text = "Часы по дням:",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Будние дни
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("Пн", "Вт", "Ср", "Чт", "Пт").forEachIndexed { index, day ->
                    val dayOfWeek = com.earningformula.data.models.DayOfWeek.values()[index]
                    val hours = job.hoursPerDay[dayOfWeek] ?: 0.0
                    
                    DayHoursChip(
                        day = day,
                        hours = hours,
                        isWeekend = false
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
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        color = backgroundColor
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
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
