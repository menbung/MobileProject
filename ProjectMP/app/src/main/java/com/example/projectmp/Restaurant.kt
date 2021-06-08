package com.example.projectmp

data class Restaurant(var resName:String, var category:String, var location:String, var number:String, var url:String,
                      var GPA:Float, var menus:ArrayList<Menu>, var comments:ArrayList<Comment>) {
}