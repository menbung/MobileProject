package com.example.projectmp

import java.io.Serializable

data class Menu(var menuName:String, var category:String, var price:Int) : Serializable{
    constructor():this("","",0)
}