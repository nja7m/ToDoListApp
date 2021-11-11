package com.example.todolistapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class TaskAdapter(var data: MutableList<Task>) : RecyclerView.Adapter<TaskHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        var v = LayoutInflater.from(parent.context).inflate(R.layout.task_card_row, parent, false)
        return TaskHolder(v)
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        holder.taskTitle.text = data[position].title
        holder.taskCompletionCheckBox.isChecked = data[position].status
//        holder.taskDescription.text = data[position].description
//        holder.taskDate.text = data[position].date
//        holder.taskTime.text = data[position].time
//            Picasso.get().load(data[position].link).into(holder.imageViewPoster)

        holder.itemView.setOnClickListener {
            println(data[position].title)
            var intent = Intent(holder.itemView.context, TaskActivityDetails::class.java)
            var m = data[position]

            intent.putExtra("task", data[position])
            holder.itemView.context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }
}

class TaskHolder(v: View) : RecyclerView.ViewHolder(v) {

    var taskTitle = v.findViewById<TextView>(R.id.rowTaskTitle)
    var taskCompletionCheckBox = v.findViewById<CheckBox>(R.id.rowCompletedCheckbox)

}

