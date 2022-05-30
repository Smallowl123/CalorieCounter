package com.example.calorie_counter.room

import androidx.lifecycle.*
import com.example.calorie_counter.room.entity.Food
import com.example.calorie_counter.room.entity.Meal
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class CCViewModel(private val repository: CCRepository) : ViewModel() {
    val allMeals: LiveData<List<Meal>> = repository.allMeals.asLiveData()
    val allFood: LiveData<List<Food>> = repository.allFood.asLiveData()

    fun insert(meal: Meal) = viewModelScope.launch {
        repository.insert(meal)
    }
}

class CCViewModelFactory(private val repository: CCRepository) : ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T{
        if (modelClass.isAssignableFrom(CCViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return CCViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}