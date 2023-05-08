package com.beran.bisnisplus.data

data class StockItem(
    val stockId: String,
    val stockName: String,
    val quantity: Double?,
    val unit: String? = "Kg",
    val unitPrice: Long,
)

val dummyStocks = listOf(
    StockItem("1", "Cabai Merah", 200.0,"Kg", 20000),
    StockItem("2", "Cabai Hijau", 200.0,"Kg", 20000),
    StockItem("3", "Cabai Rawit", 200.0, "Kg",20000),
    StockItem("4", "Cabai Merah", 200.0,"Kg", 20000),
    StockItem("5", "Cabai Merah", 200.0,"Kg", 20000),
)