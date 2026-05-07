package com.example.gramasuvidha.models

data class Notice(
    val notice_id: String,
    val title_en: String,
    val title_kn: String,
    val content_en: String,
    val content_kn: String,
    val date: String,
    val category: String,
    val priority: String // "high", "medium", "low"
)

data class NoticeResponse(val notices: List<Notice>)
