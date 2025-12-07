package com.example.academicachievers

data class Scholarship(
    val id: Int,
    val name: String,
    val country: String,
    val deadline: String?,
    val link: String,
    val checklist: List<String>
)
