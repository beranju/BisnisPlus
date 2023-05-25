package com.beran.core.domain.model

data class BusinessModel(
    var bisnisId: String? = null,
    val userId: String? = null,
    val bisnisName: String? = null,
    val bisnisCategory: String? = null,
    val commodity: String? = null,
    val createdAt: Long? = null,
    val updatedAt: Long? = null
)