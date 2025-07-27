package com.earningformula.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WorkConfiguration(
    val id: String = "",
    val name: String = "",
    val jobs: List<Job> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
) : Parcelable {
    
    fun getTotalWeeklyHours(): Double {
        return jobs.sumOf { it.getWeeklyHours() }
    }
    
    fun getTotalMonthlyHours(): Double {
        return jobs.sumOf { it.getMonthlyHours() }
    }
    
    fun getTotalMonthlySalary(): Double {
        return jobs.sumOf { it.monthlySalary }
    }
    
    fun getAverageHourlyRate(): Double {
        val totalHours = getTotalMonthlyHours()
        val totalSalary = getTotalMonthlySalary()
        return if (totalHours > 0) totalSalary / totalHours else 0.0
    }
    
    fun getTotalWeekdayHours(): Double {
        return jobs.sumOf { it.getWeekdayHours() }
    }
    
    fun getTotalWeekendHours(): Double {
        return jobs.sumOf { it.getWeekendHours() }
    }
}
