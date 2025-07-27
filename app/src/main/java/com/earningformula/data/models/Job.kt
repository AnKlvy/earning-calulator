package com.earningformula.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Job(
    val id: String = "",
    val name: String = "",
    val monthlySalary: Double = 0.0,
    val hoursPerDay: Map<DayOfWeek, Double> = mapOf(
        DayOfWeek.MONDAY to 0.0,
        DayOfWeek.TUESDAY to 0.0,
        DayOfWeek.WEDNESDAY to 0.0,
        DayOfWeek.THURSDAY to 0.0,
        DayOfWeek.FRIDAY to 0.0,
        DayOfWeek.SATURDAY to 0.0,
        DayOfWeek.SUNDAY to 0.0
    )
) : Parcelable {
    
    fun getWeeklyHours(): Double {
        return hoursPerDay.values.sum()
    }
    
    fun getMonthlyHours(): Double {
        return getWeeklyHours() * 4.3 // 4.3 недели в месяце в среднем
    }
    
    fun getHourlyRate(): Double {
        val monthlyHours = getMonthlyHours()
        return if (monthlyHours > 0) monthlySalary / monthlyHours else 0.0
    }
    
    fun getWeekdayHours(): Double {
        return listOf(
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY
        ).sumOf { hoursPerDay[it] ?: 0.0 }
    }
    
    fun getWeekendHours(): Double {
        return listOf(
            DayOfWeek.SATURDAY,
            DayOfWeek.SUNDAY
        ).sumOf { hoursPerDay[it] ?: 0.0 }
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
