package com.example.todolistapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot

import com.google.firebase.firestore.QuerySnapshot

import androidx.annotation.NonNull
import com.example.todolistapp.Globals.db

import com.google.android.gms.tasks.OnCompleteListener


class MainActivity : AppCompatActivity() {

    lateinit var tasklist: MutableList<Task>
   // private lateinit var taskAdapter: TaskAdapter
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var taskAdapter: TaskAdapter
        tasklist = mutableListOf<Task>()

        var greeting = findViewById<TextView>(R.id.textViewGreeting)
        var name = findViewById<TextView>(R.id.textViewName)
        var todayTasksText = findViewById<TextView>(R.id.textViewTodaytask)
        var taskNumberText = findViewById<TextView>(R.id.textViewNumberofTasks)

        var addButton = findViewById<FloatingActionButton>(R.id.floatingActionButtonAdded)
        var dateCardView = findViewById<CardView>(R.id.cardViewDate)

        var taskRecyclerView = findViewById<RecyclerView>(R.id.TaskRecyclerView)

        taskAdapter = TaskAdapter(tasklist)
        taskRecyclerView.layoutManager = LinearLayoutManager(this)
        //taskRecyclerView.adapter = TaskAdapter()

        addButton.setOnClickListener {
            var intent = Intent(this, NewTask::class.java)
            startActivity(intent)

        }
        db.collection("tasks")
            .get()
            .addOnSuccessListener { task ->
                    for (document in task.documents) {
                        //val retrievedTask = Task(
                            tasklist.add( Task(
                            document.data!!.get("Title").toString(),
                            document.data!!.get("Time").toString(),
                            document.data!!.get("Date").toString(),
                            document.data!!.get("Description").toString(),
                            document.data!!.get("Status") as Boolean
                        ))


                        //Log.d("Firebase doc", retrievedTask.toString())

                    }
                taskRecyclerView.adapter = TaskAdapter(tasklist)
                }
            .addOnFailureListener { e ->
                Log.d("Firebase error", e.message.toString())
            }
//        if (intent.hasExtra("newTask")) {
//            tasklist.add(intent.getSerializableExtra("newTask") as Task)
//            taskAdapter.notifyDataSetChanged()
//        }
    }

}