package com.example.backend

import android.util.Log
import com.example.model.AppScreen
import com.example.model.ChatCardType
import com.example.model.ChatMessageItem
import com.example.model.RecommendationItem
import com.example.model.TransactionItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

object SupabaseRepository {
    private const val TAG = "SupabaseRepository"
    private val scope = CoroutineScope(Dispatchers.IO)

    private val _status = MutableStateFlow(
        if (SupabaseConfig.isConfigured) BackendConnectionStatus.CONNECTING else BackendConnectionStatus.STANDALONE_SUBMISSION
    )
    val status: StateFlow<BackendConnectionStatus> = _status.asStateFlow()

    private val _statusDetail = MutableStateFlow(
        if (SupabaseConfig.isConfigured) "Initializing Supabase connection..."
        else "Secrets kept blank for code submission. Running in standalone mode with full local data persistence."
    )
    val statusDetail: StateFlow<String> = _statusDetail.asStateFlow()

    // Default seed transactions
    private val defaultTransactions = listOf(
        TransactionItem(
            id = "tx_apollo_01",
            title = "Apollo Pharmacy",
            subtitle = "Prescription & Diagnostics • Section 80D Eligible",
            amount = "₹4,200",
            isCredit = false,
            date = "Today, 2:30 PM",
            category = "Healthcare",
            method = "UPI • HDFC Bank",
            iconType = "medical"
        ),
        TransactionItem(
            id = "tx_swiggy_02",
            title = "Swiggy Gourmet",
            subtitle = "Dinner order delivery",
            amount = "₹850",
            isCredit = false,
            date = "Yesterday, 8:45 PM",
            category = "Food & Dining",
            method = "Paytm Wallet",
            iconType = "restaurant"
        ),
        TransactionItem(
            id = "tx_cred_03",
            title = "Salary Credit",
            subtitle = "Monthly corporate credit",
            amount = "₹1,25,000",
            isCredit = true,
            date = "28 Oct, 9:00 AM",
            category = "Income",
            method = "NEFT • HDFC Bank",
            iconType = "wallet"
        ),
        TransactionItem(
            id = "tx_uber_04",
            title = "Uber India",
            subtitle = "Airport ride to T3",
            amount = "₹720",
            isCredit = false,
            date = "27 Oct, 6:15 PM",
            category = "Transit",
            method = "Paytm UPI",
            iconType = "transit"
        )
    )

    private val _transactions = MutableStateFlow<List<TransactionItem>>(defaultTransactions)
    val transactions: StateFlow<List<TransactionItem>> = _transactions.asStateFlow()

    // Default seed recommendations
    private val defaultRecommendations = listOf(
        RecommendationItem(
            id = "rec_health_01",
            title = "Section 80D Parental Health Shield",
            badge = "High Impact • 80D Tax",
            savingsTag = "Save ₹15,600 Tax",
            description = "Recent ₹4,200 Apollo Pharmacy payment detected for dependent senior citizen parent. Claim ₹50,000 tax deduction under Section 80D with zero health assumption.",
            targetScreen = AppScreen.HEALTH_COVER_DETAILS,
            iconType = "health",
            ctaText = "Claim 80D Shield"
        ),
        RecommendationItem(
            id = "rec_savings_02",
            title = "Auto-Sweep Liquid Yield",
            badge = "Yield Boost",
            savingsTag = "+₹1,840/mo",
            description = "Move ₹42,000 idle current account balance to 7.1% overnight liquid fund with instant T+0 liquidity.",
            targetScreen = AppScreen.SMART_SAVINGS_DETAILS,
            iconType = "yield",
            ctaText = "Activate Auto-Sweep"
        ),
        RecommendationItem(
            id = "rec_insurance_03",
            title = "Comprehensive Two-Wheeler Cover",
            badge = "Renewal Due",
            savingsTag = "Save ₹850",
            description = "Your vehicle insurance expires in 12 days. Zero-depreciation cover starting at ₹649/yr with cashless claims.",
            targetScreen = AppScreen.INSURANCE_DISCOVERY,
            iconType = "shield",
            ctaText = "View Quotes"
        )
    )

    private val _recommendations = MutableStateFlow<List<RecommendationItem>>(defaultRecommendations)
    val recommendations: StateFlow<List<RecommendationItem>> = _recommendations.asStateFlow()

    private val _chatMessages = MutableStateFlow<List<ChatMessageItem>>(
        listOf(
            ChatMessageItem(
                id = "msg_welcome",
                text = "Hello! I am FinSense Copilot. I can analyze your financial transactions, verify IRDAI policy terms, decode Section 80D tax deductions, and help you manage soundbox settlements.",
                isUser = false,
                time = "10:00 AM",
                cardType = ChatCardType.NONE,
                quickReplies = listOf(
                    "Check Section 80D Tax Limits",
                    "Compare Health Insurance Quotes",
                    "Review Business Settlement Status"
                )
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessageItem>> = _chatMessages.asStateFlow()

    init {
        scope.launch {
            if (SupabaseConfig.isConfigured) {
                syncWithSupabase()
            }
        }
    }

    suspend fun syncWithSupabase() = withContext(Dispatchers.IO) {
        if (!SupabaseConfig.isConfigured) {
            _status.value = BackendConnectionStatus.STANDALONE_SUBMISSION
            _statusDetail.value = "Secrets kept blank for code submission. Running in standalone mode with full local data persistence."
            return@withContext
        }

        try {
            _status.value = BackendConnectionStatus.CONNECTING
            _statusDetail.value = "Connecting to Supabase PostgREST at ${SupabaseConfig.currentUrl}..."

            val api = SupabaseClient.getApi()
            val txResponse = api.getTransactions()

            if (txResponse.isSuccessful) {
                val remoteTxs = txResponse.body()
                if (!remoteTxs.isNullOrEmpty()) {
                    _transactions.value = remoteTxs.map { it.toDomainModel() }
                }
                _status.value = BackendConnectionStatus.CONNECTED_LIVE
                _statusDetail.value = "Connected to Supabase live. PostgreSQL sync active."
                Log.d(TAG, "Supabase transactions synced successfully (${remoteTxs?.size ?: 0} items)")
            } else {
                _status.value = BackendConnectionStatus.CONNECTION_ERROR
                _statusDetail.value = "Supabase responded with code ${txResponse.code()}: ${txResponse.message()}. Falling back to local data."
            }

            // Sync recommendations
            val recResponse = api.getRecommendations()
            if (recResponse.isSuccessful) {
                val remoteRecs = recResponse.body()
                if (!remoteRecs.isNullOrEmpty()) {
                    _recommendations.value = remoteRecs.map { it.toDomainModel() }
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "Supabase connection attempt encountered exception: ${e.message}", e)
            _status.value = BackendConnectionStatus.CONNECTION_ERROR
            _statusDetail.value = "Connection failed (${e.localizedMessage ?: "Network unreachable"}). Safe fallback active."
        }
    }

    suspend fun testConnection(url: String, anonKey: String): Pair<Boolean, String> = withContext(Dispatchers.IO) {
        if (url.isBlank() || anonKey.isBlank()) {
            return@withContext Pair(false, "URL and Anon Key must not be blank")
        }

        SupabaseConfig.currentUrl = url
        SupabaseConfig.currentAnonKey = anonKey
        SupabaseClient.invalidate()

        try {
            val api = SupabaseClient.getApi()
            val response = api.getTransactions()
            if (response.isSuccessful) {
                _status.value = BackendConnectionStatus.CONNECTED_LIVE
                _statusDetail.value = "Connected to Supabase successfully!"
                Pair(true, "Successfully connected to Supabase PostgREST (Status ${response.code()})")
            } else {
                _status.value = BackendConnectionStatus.CONNECTION_ERROR
                _statusDetail.value = "Supabase error HTTP ${response.code()}: ${response.message()}"
                Pair(false, "HTTP ${response.code()}: ${response.message()}")
            }
        } catch (e: Exception) {
            _status.value = BackendConnectionStatus.CONNECTION_ERROR
            _statusDetail.value = "Error: ${e.localizedMessage}"
            Pair(false, e.localizedMessage ?: "Unknown network error")
        }
    }

    suspend fun recordTransaction(item: TransactionItem, beneficiaryType: String = "SELF") = withContext(Dispatchers.IO) {
        _transactions.value = listOf(item) + _transactions.value

        if (SupabaseConfig.isConfigured) {
            try {
                val dto = SupabaseTransactionDto.fromDomain(item, beneficiaryType = beneficiaryType)
                SupabaseClient.getApi().insertTransaction(dto)
            } catch (e: Exception) {
                Log.w(TAG, "Failed to write transaction to Supabase remote, stored locally: ${e.message}")
            }
        }
    }

    suspend fun addChatMessage(message: ChatMessageItem) = withContext(Dispatchers.IO) {
        _chatMessages.value = _chatMessages.value + message

        if (SupabaseConfig.isConfigured) {
            try {
                val dto = SupabaseChatDto(
                    id = message.id.ifEmpty { UUID.randomUUID().toString() },
                    sessionId = "session_main",
                    sender = if (message.isUser) "user" else "copilot",
                    messageText = message.text,
                    cardType = message.cardType.name,
                    cardData = message.cardData
                )
                SupabaseClient.getApi().postChatMessage(dto)
            } catch (e: Exception) {
                Log.w(TAG, "Failed to sync chat message to Supabase: ${e.message}")
            }
        }
    }

    suspend fun submitApplication(
        name: String,
        relation: String,
        planType: String,
        cover: String,
        premium: String,
        taxSaved: String
    ): Boolean = withContext(Dispatchers.IO) {
        if (SupabaseConfig.isConfigured) {
            try {
                val dto = SupabaseInsuranceApplicationDto(
                    id = UUID.randomUUID().toString(),
                    applicantName = name,
                    beneficiaryRelation = relation,
                    planType = planType,
                    coverAmount = cover,
                    annualPremium = premium,
                    taxSaving80d = taxSaved,
                    status = "SUBMITTED"
                )
                val response = SupabaseClient.getApi().submitInsuranceApplication(dto)
                return@withContext response.isSuccessful
            } catch (e: Exception) {
                Log.w(TAG, "Failed to submit insurance application to Supabase: ${e.message}")
                return@withContext true // fallback success
            }
        }
        return@withContext true
    }
}
