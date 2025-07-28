package com.earningformula.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Job(
    val id: String = "",
    val name: String = "",
    val monthlySalary: Double = 0.0,
    val inputType: JobInputType = JobInputType.DAILY_HOURS,
    val hoursPerDay: Map<DayOfWeek, Double> = mapOf(
        DayOfWeek.MONDAY to 0.0,
        DayOfWeek.TUESDAY to 0.0,
        DayOfWeek.WEDNESDAY to 0.0,
        DayOfWeek.THURSDAY to 0.0,
        DayOfWeek.FRIDAY to 0.0,
        DayOfWeek.SATURDAY to 0.0,
        DayOfWeek.SUNDAY to 0.0
    ),
    val totalMonthlyHours: Double = 0.0
) : Parcelable {
    
    fun getWeeklyHours(): Double {
        return when (inputType) {
            JobInputType.DAILY_HOURS -> hoursPerDay.values.sum()
            JobInputType.TOTAL_MONTHLY_HOURS -> totalMonthlyHours / 4.3
        }
    }

    fun getMonthlyHours(): Double {
        return when (inputType) {
            JobInputType.DAILY_HOURS -> getWeeklyHours() * 4.3
            JobInputType.TOTAL_MONTHLY_HOURS -> totalMonthlyHours
        }
    }
    
    fun getHourlyRate(): Double {
        val monthlyHours = getMonthlyHours()
        return if (monthlyHours > 0) monthlySalary / monthlyHours else 0.0
    }
    
    fun getWeekdayHours(): Double {
        return when (inputType) {
            JobInputType.DAILY_HOURS -> listOf(
                DayOfWeek.MONDAY,
                DayOfWeek.TUESDAY,
                DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY,
                DayOfWeek.FRIDAY
            ).sumOf { hoursPerDay[it] ?: 0.0 }
            JobInputType.TOTAL_MONTHLY_HOURS -> totalMonthlyHours * (5.0 / 7.0) / 4.3 // Пропорционально будним дням
        }
    }

    fun getWeekendHours(): Double {
        return when (inputType) {
            JobInputType.DAILY_HOURS -> listOf(
                DayOfWeek.SATURDAY,
                DayOfWeek.SUNDAY
            ).sumOf { hoursPerDay[it] ?: 0.0 }
            JobInputType.TOTAL_MONTHLY_HOURS -> totalMonthlyHours * (2.0 / 7.0) / 4.3 // Пропорционально выходным дням
        }
    }
}

enum class DayOfWeek(val displayName: String) {
    MONDAY("Пн"),
    TUESDAY("Вт"),
    WEDNESDAY("Ср"),
    THURSDAY("Чт"),
    FRIDAY("Пт"),
    SATURDAY("Сб"),
    SUNDAY("Вс")
}

enum class JobInputType(val displayName: String) {
    DAILY_HOURS("По часам в день"),
    TOTAL_MONTHLY_HOURS("Общие часы в месяц")
}
