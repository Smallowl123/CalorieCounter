package com.example.calorie_counter.activities

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.calorie_counter.R

class ProfileActivity : AppCompatActivity() {
    private lateinit var sharedPref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        initSharedPreferences()
        setClickListeners()
        setSupportActionBar(findViewById(R.id.action_toolbar_profile))

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }

    private fun initSharedPreferences() {
        sharedPref = getSharedPreferences("shared", Context.MODE_PRIVATE)

        findViewById<TextView>(R.id.gender_text).text =
            sharedPref.getString("saved_gender", "Женский")
        findViewById<TextView>(R.id.age_text).text =
            sharedPref.getString("saved_age", "25")
        findViewById<TextView>(R.id.height_text).text =
            sharedPref.getString("saved_height", "170")
        findViewById<TextView>(R.id.weight_text).text =
            sharedPref.getString("saved_weight", "70.0")
        findViewById<TextView>(R.id.profile_calories_text).text =
            sharedPref.getString("saved_calories", "1600")
        findViewById<TextView>(R.id.profile_protein_text).text =
            sharedPref.getString("saved_protein", "80.0")
        findViewById<TextView>(R.id.profile_fat_text).text =
            sharedPref.getString("saved_fat", "62.2")
        findViewById<TextView>(R.id.profile_carboh_text).text =
            sharedPref.getString("saved_carboh", "180.0")
    }

    private fun setClickListeners() {
        val spinner = findViewById<Spinner>(R.id.spinner)
        spinner.setSelection(2)

        findViewById<LinearLayout>(R.id.gender_layout).setOnClickListener {
            var str = "Женский"
            if (sharedPref.getString("saved_gender", "Женский").equals(str)) {
                str = "Мужской"
            }
            with(sharedPref.edit()) {
                putString("saved_gender", str)
                apply()
            }
            findViewById<TextView>(R.id.gender_text).text = str
        }
        findViewById<LinearLayout>(R.id.age_layout).setOnClickListener {
            putOneIntAsStringFromDialog("Введите возраст", "saved_age", findViewById(R.id.age_text))
        }
        findViewById<LinearLayout>(R.id.height_layout).setOnClickListener {
            putOneIntAsStringFromDialog(
                "Введите рост (см)",
                "saved_height",
                findViewById(R.id.height_text)
            )
        }
        findViewById<LinearLayout>(R.id.calories_layout).setOnClickListener {
            val caloriesOld: Float =
                sharedPref.getString("saved_calories", "1600").toString().toFloat()
            val caloriesTextView: TextView = findViewById(R.id.profile_calories_text)
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            val viewInflated: View = LayoutInflater.from(this)
                .inflate(
                    R.layout.single_int_dialog,
                    findViewById(R.id.constraint_layout_profile),
                    false
                )
            val input = viewInflated.findViewById<View>(R.id.input_single_int) as EditText
            viewInflated.findViewById<TextView>(R.id.input_single_int_text).text = "Введите калории"
            builder.setView(viewInflated)
            builder.setPositiveButton(
                android.R.string.ok
            ) { dialog, _ ->
                dialog.dismiss()

                val int = input.text.toString()
                val k: Float = caloriesOld / int.toFloat()
                var protein: Float =
                    sharedPref.getString("saved_protein", "80").toString().toFloat()
                var fat: Float = sharedPref.getString("saved_fat", "62.2").toString().toFloat()
                var carboh: Float = sharedPref.getString("saved_carboh", "180").toString().toFloat()

                protein /= k
                fat /= k
                carboh /= k
                with(sharedPref.edit()) {
                    putString("saved_calories", int)
                    putString("saved_protein", protein.format(1))
                    putString("saved_fat", fat.format(1))
                    putString("saved_carboh", carboh.format(1))
                    apply()
                }
                caloriesTextView.text = int
                findViewById<TextView>(R.id.profile_protein_text).text = protein.format(1)
                findViewById<TextView>(R.id.profile_fat_text).text = fat.format(1)
                findViewById<TextView>(R.id.profile_carboh_text).text = carboh.format(1)

            }
            builder.setNegativeButton(
                android.R.string.cancel
            ) { dialog, _ -> dialog.cancel() }

            builder.show()
        }
        findViewById<LinearLayout>(R.id.weight_layout).setOnClickListener {
            putOneFloatAsStringFromDialog(
                "Введите вес (кг)",
                "saved_weight",
                findViewById(R.id.weight_text)
            )
        }

        findViewById<ImageView>(R.id.refresh_button).setOnClickListener {
            val caloriesOld: Float =
                sharedPref.getString("saved_calories", "1600").toString().toFloat()
            val weight: Float = sharedPref.getString("saved_weight", "70").toString().toFloat()
            val height: Float = sharedPref.getString("saved_height", "170").toString().toFloat()
            val age: Float = sharedPref.getString("saved_age", "25").toString().toFloat()
            var protein: Float = sharedPref.getString("saved_protein", "80").toString().toFloat()
            var fat: Float = sharedPref.getString("saved_fat", "62.2").toString().toFloat()
            var carboh: Float = sharedPref.getString("saved_carboh", "180").toString().toFloat()


            var calories: Float = 10f * weight + 6.25f * height - 5f * age

            if (sharedPref.getString("saved_gender", "Женский").equals("Женский")) {
                calories -= 161
            } else {
                calories += 5
            }

            when (spinner.selectedItemPosition) {
                0 -> calories *= 1.2f
                1 -> calories *= 1.1f
                //2 -> calories
                3 -> calories *= 0.9f
                4 -> calories *= 0.8f
            }

            val k = caloriesOld / calories
            protein /= k
            fat /= k
            carboh /= k


            with(sharedPref.edit()) {
                putString("saved_calories", calories.format(0))
                putString("saved_protein", protein.format(1))
                putString("saved_fat", fat.format(1))
                putString("saved_carboh", carboh.format(1))
                apply()
            }

            findViewById<TextView>(R.id.profile_calories_text).text = calories.format(0)
            findViewById<TextView>(R.id.profile_protein_text).text = protein.format(1)
            findViewById<TextView>(R.id.profile_fat_text).text = fat.format(1)
            findViewById<TextView>(R.id.profile_carboh_text).text = carboh.format(1)
        }

        findViewById<LinearLayout>(R.id.pfc_layout).setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            val viewInflated: View = LayoutInflater.from(this)
                .inflate(
                    R.layout.pfc_ratio_dialog,
                    findViewById(R.id.constraint_layout_profile),
                    false
                )
            val proteinRatio = viewInflated.findViewById<EditText>(R.id.protein_ratio)
            val fatRatio = viewInflated.findViewById<EditText>(R.id.fat_ratio)
            val carbohRatio = viewInflated.findViewById<EditText>(R.id.carboh_ratio)
            builder.setView(viewInflated)
            builder.setPositiveButton(
                android.R.string.ok
            ) { dialog, _ ->
                dialog.dismiss()
                val pRatio = proteinRatio.text.toString().toFloat()
                val fRatio = fatRatio.text.toString().toFloat()
                val cRatio = carbohRatio.text.toString().toFloat()
                val calories = sharedPref.getString("saved_calories", "1600").toString().toFloat()
                val sumRatio = pRatio + fRatio + cRatio

                val protein = (calories * (pRatio / sumRatio) / 4f).format(1)
                val fat = (calories * (fRatio / sumRatio) / 9f).format(1)
                val carboh = (calories * (cRatio / sumRatio) / 4f).format(1)

                with(sharedPref.edit()) {
                    putString("saved_protein", protein)
                    putString("saved_fat", fat)
                    putString("saved_carboh", carboh)
                    apply()
                }
                findViewById<TextView>(R.id.profile_protein_text).text = protein
                findViewById<TextView>(R.id.profile_fat_text).text = fat
                findViewById<TextView>(R.id.profile_carboh_text).text = carboh
            }
            builder.setNegativeButton(
                android.R.string.cancel
            ) { dialog, _ -> dialog.cancel() }

            builder.show()
        }
    }

    private fun putOneIntAsStringFromDialog(title: String, key: String, textView: TextView) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val viewInflated: View = LayoutInflater.from(this)
            .inflate(
                R.layout.single_int_dialog,
                findViewById(R.id.constraint_layout_profile),
                false
            )
        val input = viewInflated.findViewById<View>(R.id.input_single_int) as EditText
        viewInflated.findViewById<TextView>(R.id.input_single_int_text).text = title
        builder.setView(viewInflated)
        builder.setPositiveButton(
            android.R.string.ok
        ) { dialog, _ ->
            dialog.dismiss()
            val int = input.text.toString()
            with(sharedPref.edit()) {
                putString(key, int)
                apply()
            }
            textView.text = int

        }
        builder.setNegativeButton(
            android.R.string.cancel
        ) { dialog, _ -> dialog.cancel() }

        builder.show()

    }

    private fun putOneFloatAsStringFromDialog(title: String, key: String, textView: TextView) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val viewInflated: View = LayoutInflater.from(this)
            .inflate(
                R.layout.single_float_dialog,
                findViewById(R.id.constraint_layout_profile),
                false
            )
        val input = viewInflated.findViewById<View>(R.id.input_single_float) as EditText
        viewInflated.findViewById<TextView>(R.id.input_single_float_text).text = title
        builder.setView(viewInflated)
        builder.setPositiveButton(
            android.R.string.ok
        ) { dialog, _ ->
            dialog.dismiss()
            val float = input.text.toString().toFloat().format(1)
            with(sharedPref.edit()) {
                putString(key, float)
                apply()
            }
            textView.text = float

        }
        builder.setNegativeButton(
            android.R.string.cancel
        ) { dialog, _ -> dialog.cancel() }

        builder.show()

    }

    private fun Float.format(digits: Int) = "%.${digits}f".format(this)
}