package com.example.todolistapp

import java.io.Serializable
import java.sql.Time
import java.util.*

class Task(var title:String, var time: String, var date : String, var description:String, var status: Boolean ) :Serializable {
}