package com.earningformula.utils

import com.earningformula.data.models.DayOfWeek
import com.earningformula.data.models.Job
import com.earningformula.data.models.JobInputType
import com.earningformula.data.models.WorkConfiguration
import com.earningformula.data.models.CalculationResult
import com.earningformula.data.models.TotalCalculationResult
import java.text.DecimalFormat
import kotlin.math.roundToInt

object SalaryCalculator {
    
    private const val WEEKS_PER_MONTH = 4.3
    
    /**
     * Рассчитывает часы работы за неделю для конкретной работы
     */
    fun calculateWeeklyHours(job: Job): Double {
        return when (job.inputType) {
            JobInputType.DAILY_HOURS -> job.hoursPerDay.values.sum()
            JobInputType.TOTAL_MONTHLY_HOURS -> job.totalMonthlyHours / WEEKS_PER_MONTH
        }
    }

    /**
     * Рассчитывает часы работы за месяц для конкретной работы
     */
    fun calculateMonthlyHours(job: Job): Double {
        return when (job.inputType) {
            JobInputType.DAILY_HOURS -> calculateWeeklyHours(job) * WEEKS_PER_MONTH
            JobInputType.TOTAL_MONTHLY_HOURS -> job.totalMonthlyHours
        }
    }

    /**
     * Рассчитывает почасовую ставку для конкретной работы
     * Формула: зарплата в месяц / часы в месяц
     */
    fun calculateHourlyRate(job: Job): Double {
        val monthlyHours = calculateMonthlyHours(job)
        return if (monthlyHours > 0) job.monthlySalary / monthlyHours else 0.0
    }

    /**
     * Рассчитывает часы работы в будние дни (Пн-Пт)
     */
    fun calculateWeekdayHours(job: Job): Double {
        return when (job.inputType) {
            JobInputType.DAILY_HOURS -> {
                val weekdays = listOf(
                    DayOfWeek.MONDAY,
                    DayOfWeek.TUESDAY,
                    DayOfWeek.WEDNESDAY,
                    DayOfWeek.THURSDAY,
                    DayOfWeek.FRIDAY
                )
                weekdays.sumOf { job.hoursPerDay[it] ?: 0.0 }
            }
            JobInputType.TOTAL_MONTHLY_HOURS -> {
                // Пропорционально будним дням (5 из 7 дней недели)
                job.totalMonthlyHours * (5.0 / 7.0) / WEEKS_PER_MONTH
            }
        }
    }

    /**
     * Рассчитывает часы работы в выходные дни (Сб-Вс)
     */
    fun calculateWeekendHours(job: Job): Double {
        return when (job.inputType) {
            JobInputType.DAILY_HOURS -> {
                val weekends = listOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)
                weekends.sumOf { job.hoursPerDay[it] ?: 0.0 }
            }
            JobInputType.TOTAL_MONTHLY_HOURS -> {
                // Пропорционально выходным дням (2 из 7 дней недели)
                job.totalMonthlyHours * (2.0 / 7.0) / WEEKS_PER_MONTH
            }
        }
    }
    
    /**
     * Рассчитывает результаты для одной работы
     */
    fun calculateJobResult(job: Job): CalculationResult {
        return CalculationResult(
            job = job,
            weeklyHours = calculateWeeklyHours(job),
            monthlyHours = calculateMonthlyHours(job),
            hourlyRate = calculateHourlyRate(job),
            weekdayHours = calculateWeekdayHours(job),
            weekendHours = calculateWeekendHours(job)
        )
    }
    
    /**
     * Рассчитывает общие результаты для всех работ
     */
    fun calculateTotalResults(configuration: WorkConfiguration): TotalCalculationResult {
        val jobResults = configuration.jobs.map { calculateJobResult(it) }
        
        val totalWeeklyHours = jobResults.sumOf { it.weeklyHours }
        val totalMonthlyHours = jobResults.sumOf { it.monthlyHours }
        val totalMonthlySalary = configuration.jobs.sumOf { it.monthlySalary }
        val averageHourlyRate = if (totalMonthlyHours > 0) totalMonthlySalary / totalMonthlyHours else 0.0
        
        return TotalCalculationResult(
            totalWeeklyHours = totalWeeklyHours,
            totalMonthlyHours = totalMonthlyHours,
            totalMonthlySalary = totalMonthlySalary,
            averageHourlyRate = averageHourlyRate,
            totalWeekdayHours = jobResults.sumOf { it.weekdayHours },
            totalWeekendHours = jobResults.sumOf { it.weekendHours },
            jobResults = jobResults
        )
    }
    
    /**
     * Форматирует число для отображения (округляет до 2 знаков после запятой)
     */
    fun formatNumber(number: Double): String {
        val formatter = DecimalFormat("#,##0.##")
        return formatter.format(number)
    }
    
    /**
     * Форматирует часы для отображения (округляет до 1 знака после запятой)
     */
    fun formatHours(hours: Double): String {
        val formatter = DecimalFormat("#,##0.#")
        return formatter.format(hours)
    }
    
    /**
     * Форматирует деньги для отображения
     */
    fun formatMoney(amount: Double): String {
        val formatter = DecimalFormat("#,##0")
        return "${formatter.format(amount)} ₽"
    }
    
    /**
     * Валидирует корректность данных работы
     */
    fun validateJob(job: Job): List<String> {
        val errors = mutableListOf<String>()

        if (job.name.isBlank()) {
            errors.add("Название работы не может быть пустым")
        }

        if (job.monthlySalary <= 0) {
            errors.add("Зарплата должна быть больше 0")
        }

        when (job.inputType) {
            JobInputType.DAILY_HOURS -> {
                val totalHours = calculateWeeklyHours(job)
                if (totalHours <= 0) {
                    errors.add("Общее количество часов в неделю должно быть больше 0")
                }

                if (totalHours > 168) { // 24 часа * 7 дней
                    errors.add("Общее количество часов в неделю не может превышать 168")
                }

                // Проверяем, что часы в день не превышают 24
                job.hoursPerDay.forEach { (day, hours) ->
                    if (hours > 24) {
                        errors.add("Часы в ${day.displayName} не могут превышать 24")
                    }
                    if (hours < 0) {
                        errors.add("Часы в ${day.displayName} не могут быть отрицательными")
                    }
                }
            }
            JobInputType.TOTAL_MONTHLY_HOURS -> {
                if (job.totalMonthlyHours <= 0) {
                    errors.add("Общее количество часов в месяц должно быть больше 0")
                }

                if (job.totalMonthlyHours > 744) { // 31 день * 24 часа
                    errors.add("Общее количество часов в месяц не может превышать 744")
                }
            }
        }

        return errors
    }
    
    /**
     * Создает пример конфигурации для демонстрации
     */
    fun createSampleConfiguration(): WorkConfiguration {
        val freelanceJob = Job(
            id = "1",
            name = "Фриланс заказ",
            monthlySalary = 240000.0,
            inputType = JobInputType.TOTAL_MONTHLY_HOURS,
            totalMonthlyHours = 120.0
        )

        val mainJob = Job(
            id = "2",
            name = "Основная работа",
            monthlySalary = 150000.0,
            hoursPerDay = mapOf(
                DayOfWeek.MONDAY to 3.5,
                DayOfWeek.TUESDAY to 3.5,
                DayOfWeek.WEDNESDAY to 3.5,
                DayOfWeek.THURSDAY to 3.5,
                DayOfWeek.FRIDAY to 3.5,
                DayOfWeek.SATURDAY to 0.0,
                DayOfWeek.SUNDAY to 0.0
            )
        )

        return WorkConfiguration(
            id = "sample",
            name = "Пример конфигурации",
            jobs = listOf(freelanceJob, mainJob)
        )
    }
}
