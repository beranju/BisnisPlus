package com.beran.core.domain.repository

import com.beran.core.common.Resource
import com.beran.core.domain.model.BusinessModel
import kotlinx.coroutines.flow.Flow

interface IBisnisRepository {
    fun createNewBisnis(
        businessModel: BusinessModel
    ): Flow<Resource<Unit>>

    fun editBisnisData(
        bisnisName: String,
        bisnisCategory: String,
        commodity: String
    ): Flow<Resource<Unit>>
}