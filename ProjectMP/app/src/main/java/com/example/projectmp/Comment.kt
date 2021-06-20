package com.example.projectmp

import java.io.Serializable

data class Comment(var userId: String, var comment: String, var GPA: Double, var date: String) : Serializable{
    constructor():this("","",0.0,"")
}