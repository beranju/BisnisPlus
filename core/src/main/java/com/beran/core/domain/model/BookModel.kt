package com.beran.core.domain.model

data class BookModel(
    var bookId: String? = null,
    var bisnisId: String? = null,
    var amount: Double = 0.0,
    var mitra: String? = null,
    var note: String? = null,
    var category: String? = null,
    var type: String? = null,
    var state: String = "Lunas",
    var createdAt: Long? = null,
    var updatedAt: Long? = null,
    var listStock: Stocks? = null
    )
data class Stocks(
    var stocks: List<StockItem> = emptyList()
)
data class StockItem(
    var stockId: String? = null,
    var stockName: String? = null,
    var unit: Double = 0.0,
    var unitName: String = "Kg",
    var unitPrice: Double = 0.0,
    var totalAmount: Double = 0.0
)