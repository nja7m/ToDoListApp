package com.example.todolistapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
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
        var newTaskToolBar = findViewById<Toolbar>(R.id.newTaskToolbar)
        var time = Calendar.getInstance()
        var hour = time.get(Calendar.HOUR_OF_DAY)
        var minutes = time.get(Calendar.MINUTE)

        var calender = Calendar.getInstance()
        var year = calender.get(Calendar.YEAR)
        var month = calender.get(Calendar.MONTH)
        var day = calender.get(Calendar.DAY_OF_MONTH)

        newTaskToolBar.title = "New Task"
        setSupportActionBar(newTaskToolBar)
        var newTask = Task(
            null,
            "",
            0,
            0,
            "",
            false
        )

        newTaskDate.setOnClickListener {
            var datePickerDialog = DatePickerDialog(this, { view, year, month, dayOfMonth ->
                newTaskDate.text = "$dayOfMonth/${month + 1}/$year"
                newTask.date = convertDateToLong(newTaskDate.text.toString())

                Toast.makeText(this, newTask.date.toString(), Toast.LENGTH_SHORT).show()
            }, year, month, day)
            datePickerDialog.show()
        }

        newTaskTime.setOnClickListener {
            var timePickerDialog = TimePickerDialog(this, { view, hour, minutes ->
                newTaskTime.text = "$hour:$minutes"
                newTask.time = convertTimeToLong(newTaskTime.text.toString())

            }, hour, minutes, false)
            timePickerDialog.show()
        }
        addButton.setOnClickListener {

            if (newTaskTitle.text.isEmpty() || newTaskDate.text.isEmpty()) {
                Toast.makeText(this, "Title and date must be entered", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            var task: MutableMap<String, Any> = HashMap()
            task["Title"] = newTaskTitle.text.toString()
            task["Date"] = newTask.date
            task["Time"] = newTask.time
            task["Description"] = newTaskDescription.text.toString()
            task["Status"] = newTask.status

            Toast.makeText(this, "Adding task", Toast.LENGTH_SHORT).show()
            Globals.db.collection("tasks")
                .add(task)
                .addOnSuccessListener { documentReference ->
                    var newTaskIntent = Intent(this, MainActivity::class.java)
                    newTaskIntent.putExtra("newTask", newTask)
                    startActivity(newTaskIntent)

                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to add new task", Toast.LENGTH_SHORT).show()
                }

        }
        cancelButton.setOnClickListener {
            finish()
        }
    }

    fun convertDateToLong(date: String): Long {
        var df = SimpleDateFormat("dd/MM/yyyy")
        return df.parse(date).time
    }

    fun convertTimeToLong(time: String): Long {
        var df = SimpleDateFormat("HH:mm")
        return df.parse(time).time
    }


}
