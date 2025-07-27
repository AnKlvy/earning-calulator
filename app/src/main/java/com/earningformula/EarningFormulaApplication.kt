package com.earningformula

import android.app.Application
import com.earningformula.data.local.SharedPreferencesManager

class EarningFormulaApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Инициализируем SharedPreferencesManager
        SharedPreferencesManager.initialize(this)
    }
}
