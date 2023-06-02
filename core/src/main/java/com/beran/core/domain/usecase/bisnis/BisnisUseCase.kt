package com.beran.core.domain.usecase.bisnis

import com.beran.core.common.Resource
import com.beran.core.domain.model.BusinessModel
import kotlinx.coroutines.flow.Flow

interface BisnisUseCase {
    fun createNewBisnis(
        businessModel: BusinessModel
    ): Flow<Resource<Unit>>

    fun editBisnisData(
        businessModel: BusinessModel
    ): Flow<Resource<Unit>>
}