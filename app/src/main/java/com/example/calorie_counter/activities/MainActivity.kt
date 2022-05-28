package com.example.calorie_counter.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calorie_counter.R
import com.example.calorie_counter.recyclerview.MealsListAdapter
import com.example.calorie_counter.room.CCApplication
import com.example.calorie_counter.room.CCViewModel
import com.example.calorie_counter.room.CCViewModelFactory
import com.example.calorie_counter.room.entity.Meal

class MainActivity : AppCompatActivity() {
    private val ccViewModel: CCViewModel by viewModels {
        CCViewModelFactory((application as CCApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.action_toolbar))
        val button = findViewById<ImageView>(R.id.add_meal_button)

        button.setOnClickListener {
            val intent = Intent(this@MainActivity, SearchActivity::class.java)
            startActivity(intent)
            //TODO Переход к другой активности с поиском всякого там
        }
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = MealsListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        ccViewModel.allMeals.observe(
            this,
            Observer { meals ->
                meals?.let { adapter.submitList(it) }
                updateSums(meals)
            })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }

    private fun updateSums(meals: List<Meal>) {
        var caloriesSum = 0
        var proteinSum = 0f
        var fatSum = 0f
        var carbohSum = 0f
        for (meal in meals) {
            caloriesSum += meal.calories
            proteinSum += meal.protein
            fatSum += meal.fat
            carbohSum += meal.carboh
        }
        findViewById<TextView>(R.id.calories_income_text).text = caloriesSum.toString()
        findViewById<TextView>(R.id.protein_income_text).text = proteinSum.format(1)
        findViewById<TextView>(R.id.fat_income_text).text = fatSum.format(1)
        findViewById<TextView>(R.id.carboh_income_text).text = carbohSum.format(1)
    }

    fun Float.format(digits: Int) = "%.${digits}f".format(this)

}