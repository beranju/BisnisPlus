package com.beran.core.domain.usecase.bisnis

import com.beran.core.common.Resource
import com.beran.core.domain.model.BusinessModel
import com.beran.core.domain.repository.IBisnisRepository
import kotlinx.coroutines.flow.Flow

class BisnisInteractor(private val repository: IBisnisRepository) : BisnisUseCase {
    override fun createNewBisnis(
        businessModel: BusinessModel
    ): Flow<Resource<Unit>> =
        repository.createNewBisnis(businessModel)

    override fun editBisnisData(
        businessModel: BusinessModel
    ): Flow<Resource<Unit>> = repository.editBisnisData(businessModel)
}