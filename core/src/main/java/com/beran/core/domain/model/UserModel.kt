package com.beran.core.domain.model

data class UserModel(
    val uid: String,
    val name: String,
    val email: String,
    val phoneNumber: String? = null,
    val photoUrl: String? = null,
    val bisnisId: String? = null
)
