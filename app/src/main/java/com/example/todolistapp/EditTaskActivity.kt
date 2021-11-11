package com.example.todolistapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class EditTaskActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_task)
        var task = intent.getSerializableExtra("task") as Task
        var editName = findViewById<EditText>(R.id.editTextEditTitle)
        var editDate = findViewById<TextView>(R.id.textViewEditTaskDate)
        var editTime = findViewById<TextView>(R.id.textViewEditTaskTime)
        var editCalendar = findViewById<ImageView>(R.id.imageViewEditDate)
        var editClock = findViewById<ImageView>(R.id.imageViewEditTime)
        var editDescription = findViewById<EditText>(R.id.editTextEditDescription)
        var buttonSaveEdit = findViewById<FloatingActionButton>(R.id.floatingActionButtonSave)
        var buttonCancelEdit = findViewById<FloatingActionButton>(R.id.floatingActionButtonCancelEdit)

        editName.setText(task.title)
        editDate.text = task.date
        editTime.text = task.time
        editDescription.setText(task.description)

        var time = Calendar.getInstance()
        var hour =  time.get(Calendar.HOUR_OF_DAY)
        var minutes = time.get(Calendar.MINUTE)

        var calender = Calendar.getInstance()
        var year =  calender.get(Calendar.YEAR)
        var month = calender.get(Calendar.MONTH)
        var  day = calender.get(Calendar.DAY_OF_MONTH)

        editCalendar.setOnClickListener {
            var datePickerDialog = DatePickerDialog(this,{view,year,month,dayOfMonth ->
                editDate.text = "$dayOfMonth/${month+1}/$year"
            },year,month,day)
            datePickerDialog.show()
        }

        editClock.setOnClickListener {
            var timePickerDialog = TimePickerDialog(this,{view,hour,minutes ->
                editTime.text = "$hour:$minutes"
//                Toast.makeText(this, getString(R.string.myToastString), Toast.LENGTH_SHORT).show()
            },hour,minutes, false)
            timePickerDialog.show()
        }

        buttonSaveEdit.setOnClickListener {
            val taskMap: MutableMap<String, Any> = HashMap()
            taskMap["Title"] = editName.text.toString()
            taskMap["Date"] = editDate.text.toString()
            taskMap["Time"] = editTime.text.toString()
            taskMap["Description"] = editDescription.text.toString()

            var db = FirebaseFirestore.getInstance()
            db.collection("tasks").document(task.id!!)
                .update(taskMap)
                .addOnSuccessListener {
                    var updateIntent = Intent(this,MainActivity::class.java)
                    startActivity(updateIntent)
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Task did not updated", Toast.LENGTH_SHORT).show()
                }
        }
        buttonCancelEdit.setOnClickListener {

            finish()
            var intent = Intent(this,MainActivity::class.java)

            startActivity(intent)
        }
    }
}