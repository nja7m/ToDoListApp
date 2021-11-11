package com.example.todolistapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

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
                var db = FirebaseFirestore.getInstance()

                db.collection("tasks").document(task.id!!)
                    .delete()
                    .addOnSuccessListener {
                            finish()
                        var intent = Intent(this,MainActivity::class.java)
                        startActivity(intent)
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Task has not be deleted", Toast.LENGTH_SHORT).show()
                    }
            }

            alertDialog.setNegativeButton("No"){ dialog, which ->
                dialog.cancel()
            }

            var exitDialog = alertDialog.create()

            exitDialog.show()
        }
        imageEdit.setOnClickListener {
                var editIntent = Intent(this,EditTaskActivity::class.java)
                editIntent.putExtra("task",task)
                startActivity(editIntent)
        }
    }


}