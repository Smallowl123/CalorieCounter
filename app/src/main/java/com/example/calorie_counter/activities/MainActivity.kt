package com.example.calorie_counter.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calorie_counter.R
import com.example.calorie_counter.adapters.MealsListAdapter
import com.example.calorie_counter.room.CCApplication
import com.example.calorie_counter.room.CCViewModel
import com.example.calorie_counter.room.CCViewModelFactory
import com.example.calorie_counter.room.entity.Meal

class MainActivity : AppCompatActivity() {
    private val ccViewModel: CCViewModel by viewModels {
        CCViewModelFactory((application as CCApplication).repository)
    }
    private var backFromProfile = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.action_toolbar_main))
        setButtonListener()
        readSharedPreferences()
        setRecyclerView()
    }

    private fun updateMealWeight(meal: Meal) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val viewInflated: View = LayoutInflater.from(this)
            .inflate(
                R.layout.single_int_dialog,
                findViewById(R.id.constraint_layout_main),
                false
            )
        val input = viewInflated.findViewById<View>(R.id.input_single_int) as EditText
        builder.setView(viewInflated)
        builder.setPositiveButton(
            android.R.string.ok
        ) { dialog, _ ->
            dialog.dismiss()
            val weight = Integer.parseInt(input.text.toString())
            if (weight != 0) {
                val k: Float = meal.weight.toFloat() / weight.toFloat()
                ccViewModel.updateMeal(
                    Meal(
                        meal.id,
                        meal.name,
                        weight,
                        meal.protein / k,
                        meal.fat / k,
                        meal.carboh / k,
                        meal.calories / k
                    )
                )
            } else {
                ccViewModel.deleteMeal(meal)
            }
        }
        builder.setNegativeButton(
            android.R.string.cancel
        ) { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.clean -> ccViewModel.deleteAllMeals()
            R.id.profile -> {
                backFromProfile = true
                val intent = Intent(this@MainActivity, ProfileActivity::class.java)
                startActivity(intent)
            }
        }
        return true
    }

    private fun updateCPFC(meals: List<Meal>) {
        var caloriesSum = 0f
        var proteinSum = 0f
        var fatSum = 0f
        var carbohSum = 0f
        for (meal in meals) {
            caloriesSum += meal.calories
            proteinSum += meal.protein
            fatSum += meal.fat
            carbohSum += meal.carboh
        }
        findViewById<TextView>(R.id.calories_income_text).text = caloriesSum.format(0)
        findViewById<TextView>(R.id.protein_income_text).text = proteinSum.format(1)
        findViewById<TextView>(R.id.fat_income_text).text = fatSum.format(1)
        findViewById<TextView>(R.id.carboh_income_text).text = carbohSum.format(1)

        if (backFromProfile) {
            readSharedPreferences()
            backFromProfile = false
        }
    }

    private fun Float.format(digits: Int) = "%.${digits}f".format(this)

    private fun readSharedPreferences() {
        val sharedPref = getSharedPreferences("shared", Context.MODE_PRIVATE)
        findViewById<TextView>(R.id.calories_norm_text).text =
            sharedPref.getString("saved_calories", "1600")
        findViewById<TextView>(R.id.protein_norm_text).text =
            sharedPref.getString("saved_protein", "80.0")
        findViewById<TextView>(R.id.fat_norm_text).text =
            sharedPref.getString("saved_fat", "62.2")
        findViewById<TextView>(R.id.carboh_norm_text).text =
            sharedPref.getString("saved_carboh", "180.0")
    }

    private fun setButtonListener() {
        val button = findViewById<ImageView>(R.id.add_meal_button)
        button.setOnClickListener {
            val intent = Intent(this@MainActivity, SearchActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview_main)
        val adapter = MealsListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        ccViewModel.allMeals.observe(
            this
        ) { meals ->
            meals?.let { adapter.submitList(it) }
            updateCPFC(meals)
        }
        adapter.setOnItemClickListener(object : MealsListAdapter.OnItemClickListener {
            override fun onItemClick(meal: Meal) {
                updateMealWeight(meal)
            }
        })
    }

}