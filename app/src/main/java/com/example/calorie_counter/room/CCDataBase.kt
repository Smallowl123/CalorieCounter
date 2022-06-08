package com.example.calorie_counter.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.calorie_counter.room.entity.Food
import com.example.calorie_counter.room.entity.Meal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Meal::class, Food::class], version = 1, exportSchema = false)
abstract class CCDataBase : RoomDatabase() {
    abstract fun dao(): CCDao

    private class CCDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.dao())
                }
            }
        }

        suspend fun populateDatabase(dao: CCDao) {
            dao.deleteAllMeals()
            dao.deleteAllFood()

            plusFood("Картофель по-деревенски", 2f, 8f, 18.1f, 128f, dao)
            plusFood("Картофель", 2f, 0.4f, 18.1f, 80f, dao)
            plusFood("Рис белый", 6.7f, 0.7f, 78.9f, 344f, dao)
            plusFood("Шампиньоны", 4.3f, 1f, 1.0f, 27f, dao)
            plusFood("Яйцо куриное", 12.7f, 11.6f, 0.8f, 160f, dao)
            plusFood("Бекон", 23f, 45f, 0f, 500f, dao)
            plusFood("Печенье овсяное", 6.5f, 14.4f, 71.8f, 437f, dao)
            plusFood("Крекер", 11.3f, 13.4f, 67.1f, 434f, dao)
            plusFood("Fanta", 0f, 0f, 8f, 33f, dao)
            plusFood("Овощной салат", 1f, 3.1f, 3.5f, 45.3f, dao)
            plusFood("Багет", 8.4f, 1.3f, 55.6f, 268f, dao)
            plusFood("Клубника", 0.6f, 0.4f, 7f, 30f, dao)
            plusFood("Котлеты из индейки", 12f, 14f, 1f, 180f, dao)
            plusFood("Сырники", 15.5f, 4.8f, 17.8f, 175f, dao)
            plusFood("Суп с курицей", 4f, 3f, 3.0f, 128f, dao)
            plusFood("Безе", 2f, 0f, 78f, 304.8f, dao)
            plusFood("Томат", 0.6f, 0.2f, 4.2f, 20f, dao)
            plusFood("Киви", 1f, 0.6f, 10.3f, 48f, dao)
            plusFood("Масло оливковое", 0f, 99.8f, 0f, 898f, dao)
            plusFood("Макароны из твердых сортов пшеницы", 11f, 1.1f, 74.5f, 362f, dao)
            plusFood("Кефир 3,2%", 2.8f, 3.2f, 4.1f, 56f, dao)
            plusFood("Молоко 3,2%", 3f, 3.2f, 4.7f, 60f, dao)
            plusFood("Соевый соус", 5.5f, 0f, 13.0f, 75f, dao)
            plusFood("Банан", 1.5f, 0.1f, 21.8f, 89f, dao)
            plusFood("Творог 5%", 16f, 5f, 3f, 121f, dao)
            plusFood("Тартин пшеничный", 9.8f, 1.5f, 48.5f, 232f, dao)
            plusFood("Хлеб кукурузный с сыром", 8f, 3.1f, 68.4f, 333f, dao)
            plusFood("Пицца", 10.8f, 8.4f, 18.1f, 267f, dao)
            plusFood("Нори", 4f, 2.5f, 4f, 55f, dao)
            plusFood("Сливки 10%", 3f, 10f, 4f, 118f, dao)

        }

        suspend fun plusFood(
            name: String,
            protein: Float,
            fat: Float,
            carboh: Float,
            calories: Float,
            dao: CCDao
        ) {
            val food = Food(0, name, protein, fat, carboh, calories, 0, false)
            dao.insertFood(food)
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