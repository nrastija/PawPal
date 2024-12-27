package com.example.pawpal.entities

enum class Kategorija(val id: Int, val naziv: String) {
    RESET(0, "Reset"),
    ZDRAVLJE(1, "Zdravlje"),
    HRANA(2, "Hrana"),
    HIGIJENA(3, "Higijena"),
    OSTALO(4, "Ostalo");

    companion object {
        // Helper function to get a category by ID
        fun fromId(id: Int): Kategorija? = values().find { it.id == id }
    }
}