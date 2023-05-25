package com.beran.core.data.repository

import com.beran.core.common.Constant
import com.beran.core.common.Resource
import com.beran.core.domain.model.BusinessModel
import com.beran.core.domain.repository.IBisnisRepository
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class BisnisRepository(
    private val firestore: FirebaseFirestore
) : IBisnisRepository {
    override fun createNewBisnis(
        businessModel: BusinessModel
    ): Flow<Resource<Unit>> =
        flow {
            emit(Resource.Loading)
            try {
                val docRefId = "${businessModel.userId}"
                firestore.collection(Constant.bisnisRef)
                    .document(docRefId).collection("business")
                    .add(businessModel).await()
                val userQuerySnapshot =
                    firestore.collection(Constant.userRef).whereEqualTo("uid", businessModel.userId)
                        .get()
                        .await()
                val userDocuments = userQuerySnapshot.documents
                if (userDocuments.isNotEmpty()) {
                    val id = userDocuments.first().id
                    firestore.collection(Constant.userRef).document(id)
                        .update("bisnisId", FieldValue.arrayUnion(businessModel.bisnisId)).await()
                    emit(Resource.Success(Unit))
                } else {
                    emit(Resource.Error("No resource found"))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(e.message ?: "Something went wrong!"))
            }
        }.flowOn(Dispatchers.IO)

    override fun editBisnisData(
        bisnisName: String,
        bisnisCategory: String,
        commodity: String
    ): Flow<Resource<Unit>> =
        flow {
            emit(Resource.Success(Unit))
        }

}