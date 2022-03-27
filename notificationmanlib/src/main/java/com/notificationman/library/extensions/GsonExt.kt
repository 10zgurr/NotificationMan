package com.notificationman.library.extensions

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

private val gson = Gson()

fun List<*>.convertToString(): String? =
    try {
        gson.toJson(this)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

fun String.convertToList(): List<UUID>? =
    try {
        val type = object : TypeToken<List<UUID>?>() {}.type
        gson.fromJson(this, type)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }