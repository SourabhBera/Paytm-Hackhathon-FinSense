package com.example.model

enum class Persona {
    PERSONAL,
    BUSINESS
}

enum class AppScreen {
    HOME,
    ACTIVITY,
    FINSENSE,
    COPILOT,
    RECOMMENDATION_DETAILS,
    INSURANCE_DISCOVERY,
    SMART_SAVINGS_DETAILS,
    HEALTH_COVER_DETAILS,
    SHOP_INSURANCE_DETAILS,
    SOUNDBOX_UPGRADE_DETAILS,
    MERCHANT_ISSUE_RESOLUTION,
    MERCHANT_SUPPORT_CHAT,
    WORKING_CAPITAL_LOANS,
    PROFILE
}

data class RecommendationItem(
    val id: String,
    val title: String,
    val badge: String,
    val savingsTag: String,
    val description: String,
    val targetScreen: AppScreen,
    val iconType: String,
    val ctaText: String = "Explore Now"
)

data class TransactionItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val amount: String,
    val isCredit: Boolean,
    val date: String,
    val category: String,
    val method: String,
    val iconType: String
)

data class MerchantPaymentItem(
    val id: String,
    val title: String,
    val time: String,
    val amount: String,
    val status: String,
    val iconType: String
)

data class InsuranceQuoteItem(
    val id: String,
    val provider: String,
    val idv: String,
    val premium: String,
    val tag: String?,
    val features: List<String>,
    val iconType: String
)

data class LoanPlanItem(
    val id: Int,
    val title: String,
    val tag: String,
    val description: String,
    val amount: String,
    val interestRate: String,
    val monthlyEmi: String,
    val dailyDebit: String,
    val tenureMonths: Int
)

enum class ChatCardType {
    NONE,
    INSURANCE_QUOTE,
    LOAN_OFFER,
    TERMS_DECODER,
    SETTLEMENT_STATUS,
    SOUNDBOX_DIAGNOSTIC
}

data class ChatMessageItem(
    val id: String,
    val text: String,
    val isUser: Boolean,
    val time: String,
    val hasInsuranceCard: Boolean = false,
    val hasLoanCard: Boolean = false,
    val cardType: ChatCardType = if (hasInsuranceCard) ChatCardType.INSURANCE_QUOTE else if (hasLoanCard) ChatCardType.LOAN_OFFER else ChatCardType.NONE,
    val cardData: String? = null,
    val quickReplies: List<String> = emptyList()
)
