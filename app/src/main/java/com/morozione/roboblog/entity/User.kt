package com.morozione.roboblog.entity

data class User(
        var id: String = "",
        var email: String = "",
        var name: String = "",
        var image: String = "",
        var rating: Int = 0
)