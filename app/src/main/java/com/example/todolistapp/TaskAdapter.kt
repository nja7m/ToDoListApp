package com.example.todolistapp

import android.content.Intent
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class TaskAdapter(var data: MutableList<Task>, var fireBase: FirebaseFirestore?) :
    RecyclerView.Adapter<TaskHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        var v = LayoutInflater.from(parent.context).inflate(R.layout.task_card_row, parent, false)
        return TaskHolder(v)
    }

    fun completedTaskLine(taskTitle: TextView, isChecked: Boolean) {
        if (isChecked) {
            taskTitle.paintFlags = taskTitle.paintFlags or STRIKE_THRU_TEXT_FLAG
        } else {
            taskTitle.paintFlags = taskTitle.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        holder.taskTitle.text = data[position].title

        var todaysMilliseconds = System.currentTimeMillis() - (86400 * 1000)

        holder.taskCompletionCheckBox!!.isChecked = data[position].status
        holder.taskDueDateIndicator.visibility =
            if (data[position].date < todaysMilliseconds) View.VISIBLE else View.INVISIBLE

        completedTaskLine(holder.taskTitle, data[position].status)
        holder.taskCompletionCheckBox.setOnCheckedChangeListener { compoundButton, isChecked ->
            fireBase!!
                .collection("tasks")
                .document(data[position].id!!)
                .update(
                    mapOf(
                        "Status" to isChecked
                    )
                )
            holder.taskTitle.paintFlags = if (isChecked) {
                holder.taskTitle.paintFlags or STRIKE_THRU_TEXT_FLAG
            } else {
                holder.taskTitle.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
            }

        }


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
    var taskDueDateIndicator = v.findViewById<ImageView>(R.id.imageViewAlert)

}

