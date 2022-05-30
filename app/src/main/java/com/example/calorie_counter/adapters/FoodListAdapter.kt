package com.example.calorie_counter.adapters

import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.calorie_counter.R
import com.example.calorie_counter.room.entity.Food
import com.example.calorie_counter.room.entity.Meal


class FoodListAdapter : ListAdapter<Food, FoodListAdapter.FoodViewHolder>(FoodComparator()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        return FoodViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameView: TextView = itemView.findViewById(R.id.rv_name)
        private val proteinView: TextView = itemView.findViewById(R.id.rv_protein)
        private val fatView: TextView = itemView.findViewById(R.id.rv_fat)
        private val carbohView: TextView = itemView.findViewById(R.id.rv_carboh)
        private val caloriesView: TextView = itemView.findViewById(R.id.rv_calories)

        fun bind(food: Food) {
            itemView.setOnClickListener {
                val weight: Int = 250

                val builder: AlertDialog.Builder = AlertDialog.Builder(itemView.context)
                builder.setTitle("Title")
// I'm using fragment here so I'm using getView() to provide ViewGroup
// but you can provide here any other instance of ViewGroup from your Fragment / Activity
// I'm using fragment here so I'm using getView() to provide ViewGroup
// but you can provide here any other instance of ViewGroup from your Fragment / Activity
                val viewInflated: View = LayoutInflater.from(itemView.context)
                    .inflate(R.layout.fragment_type_weight_dialog, itemView.parent as ViewGroup?, false)
// Set up the input
// Set up the input
                val input = viewInflated.findViewById<View>(R.id.input) as EditText
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                builder.setView(viewInflated)

// Set up the buttons

// Set up the buttons
                builder.setPositiveButton(android.R.string.ok,
                    DialogInterface.OnClickListener { dialog, which ->
                        dialog.dismiss()
                        var m_Text = input.text.toString()
                    })
                builder.setNegativeButton(android.R.string.cancel,
                    DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

                builder.show()

            }
            //TODO Добавить всплывающее меню по лонг клику с удалением, по клику - добавлением еды в лист основного экрана
            nameView.text = food.name

            var s = food.protein.toString()
            var t = "Б: $s"
            proteinView.text = t

            s = food.fat.toString()
            t = "Ж: $s"
            fatView.text = t

            s = food.calories.toString()
            t = "К: $s"
            caloriesView.text = t

            s = food.carboh.toString()
            t = "У: $s"
            carbohView.text = t
        }

        fun createMeal(food: Food, weight: Int): Meal{
            return Meal(
                0,
                food.name,
                weight,
                food.protein * (weight / 100),
                food.fat * (weight / 100),
                food.carboh * (weight / 100),
                food.calories * (weight / 100)
            )
        }

        companion object {
            fun create(parent: ViewGroup): FoodViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_card, parent, false)
                return FoodViewHolder(view)
            }
        }
    }

    class FoodComparator : DiffUtil.ItemCallback<Food>() {
        override fun areItemsTheSame(oldItem: Food, newItem: Food): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Food, newItem: Food): Boolean {
            return oldItem.name == newItem.name
        }
    }
}
interface CallBackInterface
