package com.example.calorie_counter.room

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class CCApplication : Application() {
    private val database by lazy { CCDataBase.getDatabase(this, applicationScope) }
    val repository by lazy { CCRepository(database.dao()) }
    private val applicationScope = CoroutineScope(SupervisorJob())
}