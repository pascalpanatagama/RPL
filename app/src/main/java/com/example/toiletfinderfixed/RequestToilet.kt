package com.example.toiletfinderfixed

data class RequestToilet(
    val nama: String,
    val gender: String,
    val tipe: String,
    val jam_operasional: String,
    val latitude: String,
    val longitude: String,
    val id_toilet: String
)