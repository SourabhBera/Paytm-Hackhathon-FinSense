package com.example.backend

import com.example.model.AppScreen
import com.example.model.ChatCardType
import com.example.model.ChatMessageItem
import com.example.model.RecommendationItem
import com.example.model.TransactionItem
import com.squareup.moshi.Json

data class SupabaseTransactionDto(
    @field:Json(name = "id") val id: String,
    @field:Json(name = "user_id") val userId: String? = "usr_default_01",
    @field:Json(name = "title") val title: String,
    @field:Json(name = "subtitle") val subtitle: String,
    @field:Json(name = "amount") val amount: String,
    @field:Json(name = "is_credit") val isCredit: Boolean = false,
    @field:Json(name = "date") val date: String,
    @field:Json(name = "category") val category: String,
    @field:Json(name = "method") val method: String,
    @field:Json(name = "icon_type") val iconType: String = "shopping",
    @field:Json(name = "beneficiary_type") val beneficiaryType: String = "SELF" // "SELF", "SENIOR_CITIZEN_PARENTS", "FAMILY"
) {
    fun toDomainModel(): TransactionItem {
        return TransactionItem(
            id = id,
            title = title,
            subtitle = subtitle,
            amount = amount,
            isCredit = isCredit,
            date = date,
            category = category,
            method = method,
            iconType = iconType
        )
    }

    companion object {
        fun fromDomain(domain: TransactionItem, userId: String = "usr_default_01", beneficiaryType: String = "SELF"): SupabaseTransactionDto {
            return SupabaseTransactionDto(
                id = domain.id,
                userId = userId,
                title = domain.title,
                subtitle = domain.subtitle,
                amount = domain.amount,
                isCredit = domain.isCredit,
                date = domain.date,
                category = domain.category,
                method = domain.method,
                iconType = domain.iconType,
                beneficiaryType = beneficiaryType
            )
        }
    }
}

data class SupabaseRecommendationDto(
    @field:Json(name = "id") val id: String,
    @field:Json(name = "user_id") val userId: String? = "usr_default_01",
    @field:Json(name = "title") val title: String,
    @field:Json(name = "badge") val badge: String,
    @field:Json(name = "savings_tag") val savingsTag: String,
    @field:Json(name = "description") val description: String,
    @field:Json(name = "target_screen") val targetScreen: String,
    @field:Json(name = "icon_type") val iconType: String = "health",
    @field:Json(name = "section_80d_eligible") val section80dEligible: Boolean = false,
    @field:Json(name = "tax_deduction_amount") val taxDeductionAmount: Double = 0.0,
    @field:Json(name = "status") val status: String = "ACTIVE"
) {
    fun toDomainModel(): RecommendationItem {
        val screen = try {
            AppScreen.valueOf(targetScreen)
        } catch (_: Exception) {
            AppScreen.HEALTH_COVER_DETAILS
        }
        return RecommendationItem(
            id = id,
            title = title,
            badge = badge,
            savingsTag = savingsTag,
            description = description,
            targetScreen = screen,
            iconType = iconType,
            ctaText = "Explore Now"
        )
    }
}

data class SupabaseChatDto(
    @field:Json(name = "id") val id: String,
    @field:Json(name = "session_id") val sessionId: String = "session_main",
    @field:Json(name = "sender") val sender: String, // "user" | "copilot"
    @field:Json(name = "message_text") val messageText: String,
    @field:Json(name = "card_type") val cardType: String? = "NONE",
    @field:Json(name = "card_data") val cardData: String? = null,
    @field:Json(name = "language_code") val languageCode: String = "en-IN"
) {
    fun toDomainModel(): ChatMessageItem {
        val parsedCardType = try {
            if (cardType != null) ChatCardType.valueOf(cardType) else ChatCardType.NONE
        } catch (_: Exception) {
            ChatCardType.NONE
        }
        return ChatMessageItem(
            id = id,
            text = messageText,
            isUser = sender.equals("user", ignoreCase = true),
            time = "Just now",
            cardType = parsedCardType,
            cardData = cardData
        )
    }
}

data class SupabaseInsuranceApplicationDto(
    @field:Json(name = "id") val id: String,
    @field:Json(name = "applicant_name") val applicantName: String,
    @field:Json(name = "beneficiary_relation") val beneficiaryRelation: String,
    @field:Json(name = "plan_type") val planType: String,
    @field:Json(name = "cover_amount") val coverAmount: String,
    @field:Json(name = "annual_premium") val annualPremium: String,
    @field:Json(name = "tax_saving_80d") val taxSaving80d: String,
    @field:Json(name = "status") val status: String = "SUBMITTED"
)
