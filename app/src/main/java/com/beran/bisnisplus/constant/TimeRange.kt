package com.beran.bisnisplus.constant

enum class TimeRange(val range: String) {
    Today("Hari ini"),
    Yesterday("Kemarin"),
    Week("Minggu ini"),
    Month("Bulan ini"),
    Custom("Pilih tanggal sendiri")
}