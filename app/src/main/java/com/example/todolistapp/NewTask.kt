package com.example.todolistapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*
import kotlin.collections.HashMap


class NewTask : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_task)

        var newTaskTitle = findViewById<EditText>(R.id.editTextTitle)
        var newTaskDate = findViewById<TextView>(R.id.textViewNewTaskDate)
        var newTaskTime = findViewById<TextView>(R.id.textViewNewTaskTime)
        var newTaskDescription = findViewById<EditText>(R.id.editTextDescrition)
        var addButton = findViewById<FloatingActionButton>(R.id.floatingActionButtonAddNew)
        var cancelButton = findViewById<FloatingActionButton>(R.id.floatingActionButtonCancel)
        var calendar = findViewById<ImageView>(R.id.imageViewDate)
        var clock = findViewById<ImageView>(R.id.imageViewTime)

        var time = Calendar.getInstance()
        var hour =  time.get(Calendar.HOUR_OF_DAY)
        var minutes = time.get(Calendar.MINUTE)

        var calender = Calendar.getInstance()
        var year =  calender.get(Calendar.YEAR)
        var month = calender.get(Calendar.MONTH)
        var  day = calender.get(Calendar.DAY_OF_MONTH)

        calendar.setOnClickListener {
            var datePickerDialog = DatePickerDialog(this,{view,year,month,dayOfMonth ->
                newTaskDate.text = "$dayOfMonth/${month+1}/$year"
            },year,month,day)
            datePickerDialog.show()
        }

        clock.setOnClickListener {
            var timePickerDialog = TimePickerDialog(this,{view,hour,minutes ->
                newTaskTime.text = "$hour:$minutes"
//                Toast.makeText(this, getString(R.string.myToastString), Toast.LENGTH_SHORT).show()
            },hour,minutes, false)
            timePickerDialog.show()
        }
        addButton.setOnClickListener {

            if (newTaskTitle.text.isEmpty() || newTaskDate.text.isEmpty()) {
                Toast.makeText(this, "Title and date must be entered", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            var newTask = Task(
                null,
                newTaskTitle.text.toString(),
                newTaskDate.text.toString(),
                newTaskTime.text.toString(),
                newTaskDescription.text.toString(),
                false
            )

            val task: MutableMap<String, Any> = HashMap()
            task["Title"] = newTask.title
            task["Date"] = newTask.date
            task["Time"] = newTask.time
            task["Description"] = newTask.description
            task["Status"] = newTask.status


// Add a new document with a generated ID

// Add a new document with a generated ID
//            val taskInstance = Task(newTask.title,newTask.date,newTask.time,newTask.description,newTask.status)
            Toast.makeText(this, "Adding task", Toast.LENGTH_SHORT).show()
            Globals.db.collection("tasks")
                .add(task)
                .addOnSuccessListener { documentReference ->
                    Log.d("Firebase", "document added ${documentReference.id}")
                    var newTaskIntent = Intent(this, MainActivity::class.java)
                    newTaskIntent.putExtra("newTask", newTask)
                    startActivity(newTaskIntent)

                }
                .addOnFailureListener { e ->
                    Log.d("Firebase", "Failed to add task")
                    Toast.makeText(this, "Failed to add new task", Toast.LENGTH_SHORT).show()
                }

        }
        cancelButton.setOnClickListener {
            finish()
        }
    }


}
