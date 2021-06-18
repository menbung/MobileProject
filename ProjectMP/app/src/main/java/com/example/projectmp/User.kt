package com.example.projectmp

data class User(var id:String, var uri:String, var nickname:String, var email:String, var isCertified:Boolean) {
    constructor():this("noId", "404", "noName", "noEmail", false)
}