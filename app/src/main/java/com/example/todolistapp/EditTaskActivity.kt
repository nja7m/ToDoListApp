package com.example.todolistapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class EditTaskActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_task)
        var task = intent.getSerializableExtra("task") as Task
        var editName = findViewById<EditText>(R.id.editTextEditTitle)
        var editDate = findViewById<TextView>(R.id.textViewEditTaskDate)
        var editTime = findViewById<TextView>(R.id.textViewEditTaskTime)
        var editDescription = findViewById<EditText>(R.id.editTextEditDescription)
        var buttonSaveEdit = findViewById<FloatingActionButton>(R.id.floatingActionButtonSave)
        var buttonCancelEdit =
            findViewById<FloatingActionButton>(R.id.floatingActionButtonCancelEdit)
        var editToolbar = findViewById<Toolbar>(R.id.EditToolbar)

        editToolbar.title = "Edit Task"
        setSupportActionBar(editToolbar)



        editName.setText(task.title)
        editDate.text = convertLongToDate(task.date)
        editTime.text = convertLongToTime(task.time)
        editDescription.setText(task.description)

        var time = Calendar.getInstance()
        var hour = time.get(Calendar.HOUR_OF_DAY)
        var minutes = time.get(Calendar.MINUTE)

        var calender = Calendar.getInstance()
        var year = calender.get(Calendar.YEAR)
        var month = calender.get(Calendar.MONTH)
        var day = calender.get(Calendar.DAY_OF_MONTH)

        editDate.setOnClickListener {
            var datePickerDialog = DatePickerDialog(this, { view, year, month, dayOfMonth ->
                editDate.text = "$dayOfMonth/${month + 1}/$year"
            }, year, month, day)
            datePickerDialog.show()
        }

        editTime.setOnClickListener {
            var timePickerDialog = TimePickerDialog(this, { view, hour, minutes ->
                editTime.text = "$hour:$minutes"
            }, hour, minutes, false)
            timePickerDialog.show()
        }

        buttonSaveEdit.setOnClickListener {
            var taskMap: MutableMap<String, Any> = HashMap()
            taskMap["Title"] = editName.text.toString()
            taskMap["Date"] = convertDateToLong(editDate.text.toString())
            taskMap["Time"] = convertTimeToLong(editTime.text.toString())
            taskMap["Description"] = editDescription.text.toString()

            var db = FirebaseFirestore.getInstance()
            db.collection("tasks").document(task.id!!)
                .update(taskMap)
                .addOnSuccessListener {
                    var updateIntent = Intent(this, MainActivity::class.java)
                    startActivity(updateIntent)
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Task did not updated", Toast.LENGTH_SHORT).show()
                }
        }
        buttonCancelEdit.setOnClickListener {

            finish()
            var intent = Intent(this, MainActivity::class.java)

            startActivity(intent)
        }
    }

    fun convertLongToDate(time: Long): String {
        var date = Date(time)   // Converts time (long) to date
        var formatter = SimpleDateFormat("dd/MM/yyyy")  // Creates a formatter
        return formatter.format(date)   // Formats the date into a string
    }

    fun convertLongToTime(date: Long): String {
        var date = Date(date)
        var formatter = SimpleDateFormat("HH:mm")
        return formatter.format(date)
    }

    fun convertDateToLong(date: String): Long {
        var df = SimpleDateFormat("dd/MM/yyyy") // Creates a formatter
        return df.parse(date).time  // Converts time from formatter into Long type
    }

    fun convertTimeToLong(time: String): Long {
        var df = SimpleDateFormat("HH:mm")
        return df.parse(time).time
    }
}