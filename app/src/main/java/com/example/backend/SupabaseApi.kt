package com.example.backend

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface SupabaseApi {

    @GET("rest/v1/transactions")
    suspend fun getTransactions(
        @Query("select") select: String = "*",
        @Query("order") order: String = "created_at.desc"
    ): Response<List<SupabaseTransactionDto>>

    @Headers("Prefer: return=representation")
    @POST("rest/v1/transactions")
    suspend fun insertTransaction(
        @Body transaction: SupabaseTransactionDto
    ): Response<List<SupabaseTransactionDto>>

    @GET("rest/v1/recommendations")
    suspend fun getRecommendations(
        @Query("select") select: String = "*",
        @Query("status") status: String = "eq.ACTIVE",
        @Query("order") order: String = "created_at.desc"
    ): Response<List<SupabaseRecommendationDto>>

    @GET("rest/v1/copilot_chat")
    suspend fun getChatHistory(
        @Query("session_id") sessionId: String = "eq.session_main",
        @Query("select") select: String = "*",
        @Query("order") order: String = "created_at.asc"
    ): Response<List<SupabaseChatDto>>

    @Headers("Prefer: return=representation")
    @POST("rest/v1/copilot_chat")
    suspend fun postChatMessage(
        @Body message: SupabaseChatDto
    ): Response<List<SupabaseChatDto>>

    @Headers("Prefer: return=representation")
    @POST("rest/v1/insurance_applications")
    suspend fun submitInsuranceApplication(
        @Body application: SupabaseInsuranceApplicationDto
    ): Response<List<SupabaseInsuranceApplicationDto>>
}
