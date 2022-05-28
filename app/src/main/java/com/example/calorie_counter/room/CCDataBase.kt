package com.example.calorie_counter.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.calorie_counter.room.entity.Meal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = arrayOf(Meal::class), version = 1, exportSchema = false)
public  abstract class CCDataBase : RoomDatabase(){
    abstract fun dao(): CCDao

    private class CCDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback(){
        override fun onCreate(db : SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.dao())
                }
            }
        }
        suspend fun populateDatabase(dao: CCDao) {
            dao.deleteAll()
            plusMeal("Chocolate", 20, 1.2f, 5.2f, 11.6f, 100, dao)
            plusMeal("3.2% Milk", 250, 7.5f, 8f, 11.8f, 150, dao)
            plusMeal("Cookie", 40, 2.6f, 5.8f, 28.7f, 175, dao)
            plusMeal("Cookie", 40, 2.6f, 5.8f, 28.7f, 175, dao)
            plusMeal("Cookie", 40, 2.6f, 5.8f, 28.7f, 175, dao)
            plusMeal("Cookie", 40, 2.6f, 5.8f, 28.7f, 175, dao)
            plusMeal("Cookie", 40, 2.6f, 5.8f, 28.7f, 175, dao)
            plusMeal("Cookie", 40, 2.6f, 5.8f, 28.7f, 175, dao)
            plusMeal("Cookie", 40, 2.6f, 5.8f, 28.7f, 175, dao)
            plusMeal("Cookie", 40, 2.6f, 5.8f, 28.7f, 175, dao)
            plusMeal("Cookie", 40, 2.6f, 5.8f, 28.7f, 175, dao)
            plusMeal("Cookie", 40, 2.6f, 5.8f, 28.7f, 175, dao)
            plusMeal("Cookie", 40, 2.6f, 5.8f, 28.7f, 175, dao)
            plusMeal("Cookie", 40, 2.6f, 5.8f, 28.7f, 175, dao)
            plusMeal("Cookie", 40, 2.6f, 5.8f, 28.7f, 175, dao)


        }
        suspend fun plusMeal(name: String, weight: Int, protein: Float, fat: Float, carboh: Float, calories: Int, dao: CCDao){
            val meal = Meal(0, name, weight, protein, fat, carboh, calories)
            dao.insert(meal)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: CCDataBase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): CCDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CCDataBase::class.java,
                    "database"
                ).addCallback(CCDatabaseCallback(scope)).build()
                INSTANCE = instance
                instance
            }
        }
    }
}