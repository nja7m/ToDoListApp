package com.example.todolistapp

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class TaskActivityDetails:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.task_activity_details)
        var task = intent.getSerializableExtra("task") as Task
        var title = findViewById<TextView>(R.id.textViewTaskTitle)
        var taskDate = findViewById<TextView>(R.id.textViewDate)
        var taskTime = findViewById<TextView>(R.id.textViewTime)
        var taskDescription = findViewById<TextView>(R.id.textViewDescription)
        var imageDelete = findViewById<ImageView>(R.id.imageViewDelete)
        var imageEdit = findViewById<ImageView>(R.id.imageViewEdit)

        Log.d("NJ", task.title)

        title.text = task.title
        taskDate.text = task.date
        taskTime.text = task.time
        taskDescription.text = task.description

        imageDelete.setOnClickListener {
            var alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle("Delete Task")
            alertDialog.setMessage("Do you want to delete this task?")
            alertDialog.setPositiveButton("Yes"){ dialog, which ->
                finish()
            }

            alertDialog.setNegativeButton("No"){ dialog, which ->
                dialog.cancel()
            }

            var exitDialog = alertDialog.create()

            exitDialog.show()
        }
    }
}