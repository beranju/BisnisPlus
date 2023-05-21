package com.beran.core.domain.model

data class UserModel(
    val uid: String? = null,
    val name: String? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
    val photoUrl: String? = null,
    val bisnisId: List<String>? = null,
    val createdAt: Long? = null,
    val updatedAt: Long? = null
)
