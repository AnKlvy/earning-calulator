package com.earningformula.data.models

data class CalculationResult(
    val job: Job,
    val weeklyHours: Double,
    val monthlyHours: Double,
    val hourlyRate: Double,
    val weekdayHours: Double,
    val weekendHours: Double
) {
    companion object {
        fun fromJob(job: Job): CalculationResult {
            return CalculationResult(
                job = job,
                weeklyHours = job.getWeeklyHours(),
                monthlyHours = job.getMonthlyHours(),
                hourlyRate = job.getHourlyRate(),
                weekdayHours = job.getWeekdayHours(),
                weekendHours = job.getWeekendHours()
            )
        }
    }
}

data class TotalCalculationResult(
    val totalWeeklyHours: Double,
    val totalMonthlyHours: Double,
    val totalMonthlySalary: Double,
    val averageHourlyRate: Double,
    val totalWeekdayHours: Double,
    val totalWeekendHours: Double,
    val jobResults: List<CalculationResult>
) {
    companion object {
        fun fromConfiguration(configuration: WorkConfiguration): TotalCalculationResult {
            val jobResults = configuration.jobs.map { CalculationResult.fromJob(it) }
            
            return TotalCalculationResult(
                totalWeeklyHours = configuration.getTotalWeeklyHours(),
                totalMonthlyHours = configuration.getTotalMonthlyHours(),
                totalMonthlySalary = configuration.getTotalMonthlySalary(),
                averageHourlyRate = configuration.getAverageHourlyRate(),
                totalWeekdayHours = configuration.getTotalWeekdayHours(),
                totalWeekendHours = configuration.getTotalWeekendHours(),
                jobResults = jobResults
            )
        }
    }
}
