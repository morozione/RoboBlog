package com.morozione.roboblog.core

import java.lang.RuntimeException

enum class BlogType(val id: Int) {
    GLOBAL(0), USER(1);

    companion object {
        fun getInstanceById(id: Int) = when (id) {
            GLOBAL.id -> GLOBAL
            USER.id -> USER
            else -> throw RuntimeException("Here is no exists type with id: $id")
        }
    }
}