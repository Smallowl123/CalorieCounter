package com.example.calorie_counter.room

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class CCApplication : Application() {
    val database by lazy { CCDataBase.getDatabase(this, applicationScope) }
    val repository by lazy { CCRepository(database.dao()) }
    val applicationScope = CoroutineScope(SupervisorJob())
}