package com.example.gramasuvidha.utils

import android.content.Context
import com.google.gson.Gson
import java.io.InputStreamReader

// Import your data classes from the models package
import com.example.gramasuvidha.models.Notice
import com.example.gramasuvidha.models.NoticeResponse

fun loadNoticesFromAsset(context: Context): List<Notice> {
    val inputStream = context.assets.open("mock_notices.json")
    val reader = InputStreamReader(inputStream)
    val response = Gson().fromJson(reader, NoticeResponse::class.java)
    reader.close()
    return response.notices
}
