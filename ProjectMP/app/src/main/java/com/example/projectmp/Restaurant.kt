package com.example.projectmp

import java.io.Serializable

data class Restaurant(var resName:String, var category:String, var location:String, var number:String, var url:String,
                      var GPA:Double, var menus:ArrayList<Menu>, var comments:ArrayList<Comment>) : Serializable {
    constructor():this("noinfo","noinfo","noinfo","noinfo","noinfo",
        0.0,ArrayList<Menu>(),ArrayList<Comment>())
}