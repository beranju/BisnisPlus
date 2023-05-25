package com.beran.bisnisplus.data

data class PembukuanModel(
    val id: String,
    val kategori: String,
    val judulBuku: String,
    val agen: String,
    val date: String
)


val listDummyBuku = listOf(
    PembukuanModel(id = "1", kategori = "Barang Masuk", judulBuku = "Cabai Merah", agen = "Manahan Sihombing", date = "Kemarin"),
    PembukuanModel(id = "2", kategori = "Barang Masuk", judulBuku = "Cabai Hijau", agen = "Manahan Sihombing", date = "Kemarin"),
    PembukuanModel(id = "3", kategori = "Barang Masuk", judulBuku = "Cabai Rawit", agen = "Manahan Sihombing", date = "Kemarin")
)