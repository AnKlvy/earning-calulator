package com.earningformula.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.earningformula.R
import com.earningformula.data.models.TotalCalculationResult
import com.earningformula.data.models.JobInputType
import com.earningformula.data.models.WorkConfiguration
import com.earningformula.data.models.Currency
import com.earningformula.data.models.Language
import com.earningformula.utils.SalaryCalculator
import com.earningformula.utils.LocalizationHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsCard(
    totalResult: TotalCalculationResult?,
    currentLoadedConfiguration: WorkConfiguration?,
    originalLoadedConfiguration: WorkConfiguration?,
    currency: Currency,
    language: Language,
    modifier: Modifier = Modifier
) {
    
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
            // Заголовок
            Text(
                text = LocalizationHelper.getTotalResults(language),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Основные показатели
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    ResultItem(
                        label = LocalizationHelper.getTotalWeeklyHours(language),
                        value = "${SalaryCalculator.formatHours(totalResult.totalWeeklyHours)} ${LocalizationHelper.getHoursShort(language)}",
                        isHighlighted = false
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ResultItem(
                        label = LocalizationHelper.getTotalMonthlyHours(language),
                        value = "${SalaryCalculator.formatHours(totalResult.totalMonthlyHours)} ${LocalizationHelper.getHoursShort(language)}",
                        isHighlighted = false
                    )
                }
                
                Column(modifier = Modifier.weight(1f)) {
                    ResultItem(
                        label = LocalizationHelper.getMonthlySalary(language),
                        value = SalaryCalculator.formatMoney(totalResult.totalMonthlySalary, currency),
                        isHighlighted = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ResultItem(
                        label = LocalizationHelper.getAverageHourlyRate(language),
                        value = "${SalaryCalculator.formatMoney(totalResult.averageHourlyRate, currency)}${LocalizationHelper.getPerHour(language)}",
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
                        text = LocalizationHelper.getBreakdownByDays(language),
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
                                text = LocalizationHelper.getWeekdays(language) + ":",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "${SalaryCalculator.formatHours(totalResult.totalWeekdayHours)} ${LocalizationHelper.getHoursShort(language)}/${LocalizationHelper.getWeekShort(language)}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = LocalizationHelper.getWeekends(language) + ":",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "${SalaryCalculator.formatHours(totalResult.totalWeekendHours)} ${LocalizationHelper.getHoursShort(language)}/${LocalizationHelper.getWeekShort(language)}",
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
                    text = LocalizationHelper.getJobBreakdown(language),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                totalResult.jobResults.forEach { jobResult ->
                    JobResultItem(
                        jobResult = jobResult,
                        currency = currency,
                        language = language,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
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
    currency: Currency,
    language: Language,
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
                        JobInputType.DAILY_HOURS -> "${SalaryCalculator.formatHours(jobResult.weeklyHours)} ${LocalizationHelper.getHoursShort(language)}/${LocalizationHelper.getWeekShort(language)}"
                        JobInputType.TOTAL_MONTHLY_HOURS -> "${SalaryCalculator.formatHours(jobResult.job.totalMonthlyHours)} ${LocalizationHelper.getHoursShort(language)}/${LocalizationHelper.getMonthShort(language)}"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = SalaryCalculator.formatMoney(jobResult.job.monthlySalary, currency),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${SalaryCalculator.formatMoney(jobResult.hourlyRate, currency)}${LocalizationHelper.getPerHour(language)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}


