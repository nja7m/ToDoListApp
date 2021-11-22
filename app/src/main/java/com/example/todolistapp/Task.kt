package com.example.todolistapp

import java.io.Serializable

class Task(
    var id: String? = null,
    var title: String,
    var time: Long,
    var date: Long,
    var description: String,
    var status: Boolean
) : Serializable