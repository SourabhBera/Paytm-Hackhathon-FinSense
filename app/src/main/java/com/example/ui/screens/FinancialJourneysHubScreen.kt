package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppScreen
import com.example.model.Persona
import com.example.ui.components.JargonChip
import com.example.ui.components.JargonDecoderBottomSheet
import com.example.ui.components.JargonDefinition
import com.example.ui.components.JargonGlossary
import com.example.ui.components.PipelineJourneyType
import com.example.ui.components.PipelineVisualizerBottomSheet
import com.example.ui.theme.BorderHairline
import com.example.ui.theme.FinSenseCyan
import com.example.ui.theme.FinSenseNavy
import com.example.ui.theme.FinSenseNavyDark
import com.example.ui.theme.FinSenseSurface
import com.example.ui.theme.FinSenseSurfaceContainer
import com.example.ui.theme.FinSenseSurfaceHigh
import com.example.ui.theme.FinSenseSurfaceLow
import com.example.ui.theme.StatusError
import com.example.ui.theme.StatusErrorBg
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.StatusSuccessBg
import com.example.ui.theme.StatusWarning
import com.example.ui.theme.StatusWarningBg
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.launch

data class JourneyMetric(
    val label: String,
    val value: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinancialJourneysHubScreen(
    currentPersona: Persona,
    onNavigate: (AppScreen) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val scope = rememberCoroutineScope()
    var selectedFilterIndex by remember { mutableIntStateOf(0) }
    var activeJargonDefinition by remember { mutableStateOf<JargonDefinition?>(null) }
    var activePipelineType by remember { mutableStateOf<PipelineJourneyType?>(null) }

    val filterChips = if (currentPersona == Persona.PERSONAL) {
        listOf("All Journeys", "Insurance", "Credit & Loans", "Savings")
    } else {
        listOf("All Journeys", "Working Capital", "Shop Insurance", "Settlement Sweeps")
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("financial_journeys_hub_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))

            // Hero Track 2 Header Banner
            Card(
                colors = CardDefaults.cardColors(containerColor = FinSenseNavyDark),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("track2_hero_banner")
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.weight(1f, fill = false)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(FinSenseCyan.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = FinSenseCyan,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Text(
                                text = "AI FINANCIAL JOURNEYS",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = FinSenseCyan,
                                letterSpacing = 1.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        Surface(
                            color = Color.White.copy(alpha = 0.15f),
                            shape = CircleShape
                        ) {
                            Text(
                                text = if (currentPersona == Persona.PERSONAL) "Personal Safety Net" else "Merchant Growth Line",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                maxLines = 1
                            )
                        }
                    }

                    Text(
                        text = if (currentPersona == Persona.PERSONAL)
                            "Simpler, faster and more human protection"
                        else
                            "Working capital & shop coverage on your terms",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Text(
                        text = if (currentPersona == Persona.PERSONAL)
                            "Proactive needs detected from your real payments. Zero paperwork, decoded policy clauses, and instant digital issuance."
                        else
                            "Pre-approved credit and stock insurance matched to your daily QR collections. No collateral, automated daily micro-repayments.",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.8f),
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(2.dp))
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color.White.copy(alpha = 0.15f),
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .clickable {
                                activePipelineType = if (currentPersona == Persona.PERSONAL)
                                    PipelineJourneyType.PERSONAL_CAR_INSURANCE
                                else
                                    PipelineJourneyType.MERCHANT_WORKING_CAPITAL
                            }
                            .testTag("hero_view_pipeline_btn")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = FinSenseCyan,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "⚡ Inspect 5-Step AI Pipeline (Slide 3)",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        // Horizontal Category Filter Pills
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.testTag("journey_filter_row")
            ) {
                items(filterChips.indices.toList()) { index ->
                    val isSelected = selectedFilterIndex == index
                    Surface(
                        color = if (isSelected) FinSenseNavy else FinSenseSurface,
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) FinSenseNavy else BorderHairline
                        ),
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { selectedFilterIndex = index }
                            .testTag("filter_chip_$index")
                    ) {
                        Text(
                            text = filterChips[index],
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else TextPrimary,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        // Section: Plain-Language Terms Decoder Quick-Bar
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = FinSenseSurface),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderHairline),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("jargon_decoder_quickbar")
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.weight(1f, fill = false)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = FinSenseCyan,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "TAP TO DECODE COMPLEX TERMS",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMuted,
                                letterSpacing = 0.5.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "AI Verified",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = StatusSuccess
                        )
                    }

                    // Horizontal chips
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val terms = if (currentPersona == Persona.PERSONAL) {
                            listOf("IDV", "Zero Depreciation", "NCB", "Engine Protector", "Waiting Period & Co-Pay", "Auto-Sweep Accretion")
                        } else {
                            listOf("Flat vs Reducing APR", "Daily Auto-Settlement Debit", "Consumables Cover", "Auto-Sweep Accretion")
                        }

                        items(terms) { termKey ->
                            JargonChip(
                                term = termKey,
                                onClick = {
                                    activeJargonDefinition = JargonGlossary.items[termKey]
                                }
                            )
                        }
                    }
                }
            }
        }

        // Active / In-Flight Journeys Section
        item {
            val countBadge = if (currentPersona == Persona.PERSONAL) {
                when (selectedFilterIndex) {
                    1 -> "2 Active"
                    2 -> "1 Active"
                    3 -> "1 Active"
                    else -> "4 Active"
                }
            } else {
                when (selectedFilterIndex) {
                    1 -> "1 Pre-Approved"
                    2 -> "1 Pre-Approved"
                    3 -> "1 Active"
                    else -> "3 Journeys"
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "In-Flight Financial Journeys",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Surface(
                    color = StatusSuccessBg,
                    shape = CircleShape
                ) {
                    Text(
                        text = countBadge,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = StatusSuccess,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }
        }

        if (currentPersona == Persona.PERSONAL) {
            // Personal Journey 1: Motor Insurance (SpeedX trigger)
            if (selectedFilterIndex == 0 || selectedFilterIndex == 1) {
                item {
                    ActiveJourneyCard(
                        title = "Honda City Insurance Renewal",
                        subtitle = "KA-01-MJ-4029 • Expiring in 28 days",
                        badge = "MOTOR CARE",
                        savingsTag = "Save ₹2,200",
                        metrics = listOf(
                            JourneyMetric("Vehicle IDV", "₹7,20,000"),
                            JourneyMetric("Est. Quote", "₹2,199/yr"),
                            JourneyMetric("NCB Bonus", "35% Saved"),
                            JourneyMetric("Coverage", "Zero Dep")
                        ),
                        progressText = "Step 2 of 3: Quotes Evaluated",
                        progress = 0.66f,
                        icon = Icons.Default.DirectionsCar,
                        contextTrigger = "Triggered by ₹6,450 SpeedX garage bill on 16 Oct",
                        ctaLabel = "Complete Renewal",
                        onCtaClick = { onNavigate(AppScreen.INSURANCE_DISCOVERY) },
                        secondaryCtaLabel = "Zero Dep",
                        onSecondaryCtaClick = {
                            activeJargonDefinition = JargonGlossary.items["Zero Depreciation"]
                        },
                        onDetailsClick = { onNavigate(AppScreen.RECOMMENDATION_DETAILS) },
                        onViewPipeline = { activePipelineType = PipelineJourneyType.PERSONAL_CAR_INSURANCE },
                        testTag = "personal_journey_car_insurance"
                    )
                }
            }

            // Personal Journey 2: Pre-Approved Personal Credit Line
            if (selectedFilterIndex == 0 || selectedFilterIndex == 2) {
                item {
                    ActiveJourneyCard(
                        title = "Pre-Approved ₹1,00,000 Credit Line",
                        subtitle = "Instant bank transfer • 0 processing fee",
                        badge = "INSTANT LENDING",
                        savingsTag = "11.5% APR",
                        metrics = listOf(
                            JourneyMetric("Credit Line", "₹1,00,000"),
                            JourneyMetric("Bureau Score", "CIBIL 790"),
                            JourneyMetric("Proc. Fee", "₹0 Waiver"),
                            JourneyMetric("Disbursal", "Instant")
                        ),
                        progressText = "Step 1 of 3: Pre-Approved by CIBIL 790",
                        progress = 0.33f,
                        icon = Icons.Default.Payments,
                        contextTrigger = "Triggered by consistent salary deposits & ₹24,850 avg wallet balance",
                        ctaLabel = "Review & Withdraw",
                        onCtaClick = { onNavigate(AppScreen.WORKING_CAPITAL_LOANS) },
                        secondaryCtaLabel = "Decode APR",
                        onSecondaryCtaClick = {
                            activeJargonDefinition = JargonGlossary.items["Flat vs Reducing APR"]
                        },
                        onDetailsClick = {
                            activeJargonDefinition = JargonGlossary.items["Flat vs Reducing APR"]
                        },
                        onViewPipeline = { activePipelineType = PipelineJourneyType.MERCHANT_WORKING_CAPITAL },
                        testTag = "personal_journey_credit_line"
                    )
                }
            }

            // Personal Journey 3: Family Health & 80D Tax Cover
            if (selectedFilterIndex == 0 || selectedFilterIndex == 1) {
                item {
                    ActiveJourneyCard(
                        title = "Family Health & Tax Cover (₹10L)",
                        subtitle = "Cashless at 9,500+ hospitals • Sec 80D rebate",
                        badge = "HEALTH & TAX",
                        savingsTag = "Save ₹7,500 Tax",
                        metrics = listOf(
                            JourneyMetric("Sum Insured", "₹10 Lakhs"),
                            JourneyMetric("Sec 80D", "₹7,500 Saved"),
                            JourneyMetric("Co-Pay", "0% Co-Pay"),
                            JourneyMetric("Network", "9,500+ Hosp")
                        ),
                        progressText = "Step 1 of 3: Health Profile Ready",
                        progress = 0.33f,
                        icon = Icons.Default.HealthAndSafety,
                        contextTrigger = "Identified ₹12,400 pharmacy & clinic spends this cycle",
                        ctaLabel = "Compare Plans",
                        onCtaClick = { onNavigate(AppScreen.HEALTH_COVER_DETAILS) },
                        secondaryCtaLabel = "Co-Pay Info",
                        onSecondaryCtaClick = {
                            activeJargonDefinition = JargonGlossary.items["Waiting Period & Co-Pay"]
                        },
                        onDetailsClick = {
                            activeJargonDefinition = JargonGlossary.items["Waiting Period & Co-Pay"]
                        },
                        onViewPipeline = { activePipelineType = PipelineJourneyType.PERSONAL_HEALTH_TAX },
                        testTag = "personal_journey_health_cover"
                    )
                }
            }

            // Personal Journey 4: Smart Savings Auto-Sweep
            if (selectedFilterIndex == 0 || selectedFilterIndex == 3) {
                item {
                    ActiveJourneyCard(
                        title = "Smart Savings Auto-Sweep (7.4% p.a.)",
                        subtitle = "Active • ₹14,850 earning high FD interest",
                        badge = "WEALTH ACCRETION",
                        savingsTag = "Active Mandate",
                        metrics = listOf(
                            JourneyMetric("Earning Rate", "7.4% p.a."),
                            JourneyMetric("Swept Cash", "₹14,850"),
                            JourneyMetric("Liquidity", "100% 24/7"),
                            JourneyMetric("Threshold", "> ₹10,000")
                        ),
                        progressText = "Step 3 of 3: Earning Daily",
                        progress = 1.0f,
                        icon = Icons.Default.Savings,
                        contextTrigger = "Sweeps idle cash > ₹10,000 automatically with 24/7 UPI liquidity",
                        ctaLabel = "Adjust Sweep Rules",
                        onCtaClick = { onNavigate(AppScreen.SMART_SAVINGS_DETAILS) },
                        secondaryCtaLabel = "Decode Sweep",
                        onSecondaryCtaClick = {
                            activeJargonDefinition = JargonGlossary.items["Auto-Sweep Accretion"]
                        },
                        onDetailsClick = { onNavigate(AppScreen.SMART_SAVINGS_DETAILS) },
                        testTag = "personal_journey_smart_savings"
                    )
                }
            }
        } else {
            // Business Journey 1: Pre-Approved Working Capital Loan
            if (selectedFilterIndex == 0 || selectedFilterIndex == 1) {
                item {
                    ActiveJourneyCard(
                        title = "Pre-Approved Working Capital Loan",
                        subtitle = "Disburses to Primary HDFC A/c within 2 hours",
                        badge = "MERCHANT LENDING",
                        savingsTag = "₹444/day Micro-Debit",
                        metrics = listOf(
                            JourneyMetric("Loan Limit", "₹1,50,000"),
                            JourneyMetric("Daily Debit", "₹444 / day"),
                            JourneyMetric("Effective APR", "1.0% / mo"),
                            JourneyMetric("Tenure Choice", "6 or 12 Mo")
                        ),
                        progressText = "Step 2 of 3: Ready for 1-Tap Transfer",
                        progress = 0.66f,
                        icon = Icons.Default.Payments,
                        contextTrigger = "Triggered by ₹2,84,900 monthly QR collections & 99.4% settlement score",
                        ctaLabel = "Simulate & Disburse Loan",
                        onCtaClick = { onNavigate(AppScreen.WORKING_CAPITAL_LOANS) },
                        secondaryCtaLabel = "Decode APR",
                        onSecondaryCtaClick = {
                            activeJargonDefinition = JargonGlossary.items["Flat vs Reducing APR"]
                        },
                        onDetailsClick = {
                            activeJargonDefinition = JargonGlossary.items["Daily Auto-Settlement Debit"]
                        },
                        onViewPipeline = { activePipelineType = PipelineJourneyType.MERCHANT_WORKING_CAPITAL },
                        testTag = "business_journey_working_capital"
                    )
                }
            }

            // Business Journey 2: Shop & Stock Inventory Protection
            if (selectedFilterIndex == 0 || selectedFilterIndex == 2) {
                item {
                    ActiveJourneyCard(
                        title = "Shop & Stock Fire/Theft Protection",
                        subtitle = "₹25 Lakhs coverage for Rajesh Electronics & Co.",
                        badge = "SHOP INSURANCE",
                        savingsTag = "From ₹199/mo",
                        metrics = listOf(
                            JourneyMetric("Sum Insured", "₹25 Lakhs"),
                            JourneyMetric("Tariff Rate", "₹199 / mo"),
                            JourneyMetric("Protection", "Stock & Fire"),
                            JourneyMetric("Claim SLA", "24-Hr Instant")
                        ),
                        progressText = "Step 1 of 3: Inventory Profile Audited",
                        progress = 0.33f,
                        icon = Icons.Default.Storefront,
                        contextTrigger = "Audit confirms ₹18.5L in electronics stock susceptible to monsoon leaks",
                        ctaLabel = "Review Shop Quotes",
                        onCtaClick = { onNavigate(AppScreen.SHOP_INSURANCE_DETAILS) },
                        secondaryCtaLabel = "Shop Terms",
                        onSecondaryCtaClick = {
                            activeJargonDefinition = JargonGlossary.items["Consumables Cover"]
                        },
                        onDetailsClick = { onNavigate(AppScreen.SHOP_INSURANCE_DETAILS) },
                        onViewPipeline = { activePipelineType = PipelineJourneyType.MERCHANT_WORKING_CAPITAL },
                        testTag = "business_journey_shop_insurance"
                    )
                }
            }

            // Business Journey 3: Merchant Daily Settlement Auto-Sweep
            if (selectedFilterIndex == 0 || selectedFilterIndex == 3) {
                item {
                    ActiveJourneyCard(
                        title = "Settlement Auto-Sweep (7.4% p.a.)",
                        subtitle = "Overnight high-yield sweep for idle QR collections",
                        badge = "WEALTH ACCRETION",
                        savingsTag = "+₹3,750 / yr",
                        metrics = listOf(
                            JourneyMetric("Swept Cash", "₹24,800"),
                            JourneyMetric("Daily Return", "7.4% p.a."),
                            JourneyMetric("Liquidity", "Instant 24/7"),
                            JourneyMetric("Linked A/c", "HDFC Current")
                        ),
                        progressText = "Step 3 of 3: Earning Daily",
                        progress = 1.0f,
                        icon = Icons.Default.Savings,
                        contextTrigger = "Sweeps collections above ₹15,000 each midnight with zero prepayment lock-in",
                        ctaLabel = "Adjust Sweep Rules",
                        onCtaClick = { onNavigate(AppScreen.SMART_SAVINGS_DETAILS) },
                        secondaryCtaLabel = "Decode Sweep",
                        onSecondaryCtaClick = {
                            activeJargonDefinition = JargonGlossary.items["Auto-Sweep Accretion"]
                        },
                        onDetailsClick = { onNavigate(AppScreen.SMART_SAVINGS_DETAILS) },
                        onViewPipeline = { activePipelineType = PipelineJourneyType.MERCHANT_SETTLEMENT_SWEEP },
                        testTag = "business_journey_settlement_sweep"
                    )
                }
            }
        }

        // Section: Digital Policy Vault & DigiLocker
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = FinSenseSurface),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderHairline),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("digital_vault_card")
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.weight(1f, fill = false)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = FinSenseNavy,
                                modifier = Modifier.size(20.dp)
                            )
                            Column {
                                Text(
                                    text = "Digital Policy & Loan Vault",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "Synced with DigiLocker & Insurance Repository",
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        Surface(
                            color = StatusSuccessBg,
                            shape = CircleShape
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = StatusSuccess,
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = "Verified",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = StatusSuccess
                                )
                            }
                        }
                    }

                    HorizontalDivider(color = BorderHairline)

                    // Vault Item 1
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (currentPersona == Persona.PERSONAL)
                                    "CareGuard Motor Policy #POL-88219"
                                else
                                    "HDFC Business Working Capital Line #LOAN-4012",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "Valid through 18 Nov 2025 • Digital Certificate",
                                fontSize = 11.sp,
                                color = TextMuted,
                                maxLines = 1
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                scope.launch {
                                    snackbarHostState.showSnackbar("Policy certificate downloaded to device storage")
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = FinSenseSurfaceContainer),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("download_policy_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Download,
                                contentDescription = "Download",
                                tint = FinSenseNavy,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "PDF",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = FinSenseNavy
                            )
                        }
                    }

                    if (currentPersona == Persona.BUSINESS) {
                        HorizontalDivider(color = BorderHairline.copy(alpha = 0.5f))

                        // Vault Item 2 for Business
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Tata AIG ShopSecure Policy #SHOP-99120",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "Valid through 12 Oct 2026 • Rajesh Electronics",
                                    fontSize = 11.sp,
                                    color = TextMuted,
                                    maxLines = 1
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Button(
                                onClick = {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Shop insurance certificate downloaded to device storage")
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = FinSenseSurfaceContainer),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                                modifier = Modifier.testTag("download_shop_policy_btn")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Download,
                                    contentDescription = "Download",
                                    tint = FinSenseNavy,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "PDF",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = FinSenseNavy
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    // Modal Bottom Sheet for Plain-Language Term Decoder
    JargonDecoderBottomSheet(
        definition = activeJargonDefinition,
        onDismiss = { activeJargonDefinition = null }
    )

    // Modal Bottom Sheet for 5-Step AI Pipeline Visualizer (Slide 3)
    activePipelineType?.let { pipelineType ->
        PipelineVisualizerBottomSheet(
            initialPipelineType = pipelineType,
            onDismiss = { activePipelineType = null }
        )
    }
}

@Composable
private fun ActiveJourneyCard(
    title: String,
    subtitle: String,
    badge: String,
    savingsTag: String,
    metrics: List<JourneyMetric> = emptyList(),
    progressText: String,
    progress: Float,
    icon: ImageVector,
    contextTrigger: String,
    ctaLabel: String,
    onCtaClick: () -> Unit,
    secondaryCtaLabel: String? = null,
    onSecondaryCtaClick: (() -> Unit)? = null,
    onDetailsClick: () -> Unit,
    onViewPipeline: (() -> Unit)? = null,
    testTag: String
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = FinSenseSurface),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderHairline),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag(testTag)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Top Row - Adaptive & Responsive Layout
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(FinSenseSurfaceContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = FinSenseNavy,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Surface(
                            color = FinSenseSurfaceLow,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = badge,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = FinSenseNavy,
                                letterSpacing = 0.5.sp,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Text(
                            text = title,
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            lineHeight = 19.sp
                        )
                        Text(
                            text = subtitle,
                            fontSize = 11.5.sp,
                            color = TextSecondary,
                            lineHeight = 15.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Surface(
                        color = StatusSuccessBg,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = savingsTag,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = StatusSuccess,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            maxLines = 1
                        )
                    }

                    if (onViewPipeline != null) {
                        Surface(
                            color = Color(0xFFE8F4FC),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .clickable { onViewPipeline() }
                                .testTag("${testTag}_pipeline_btn")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(3.dp),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = FinSenseCyan,
                                    modifier = Modifier.size(10.dp)
                                )
                                Text(
                                    text = "AI Pipeline",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = FinSenseNavy
                                )
                            }
                        }
                    }
                }
            }

            // Key Highlight Metrics Grid (Responsive 4 columns)
            if (metrics.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(FinSenseSurfaceLow)
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    metrics.forEach { metric ->
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(
                                text = metric.label,
                                fontSize = 9.5.sp,
                                color = TextMuted,
                                maxLines = 1,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = metric.value,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = FinSenseNavy,
                                maxLines = 1,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            // Progress Bar
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = progressText,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary,
                        modifier = Modifier.weight(1f, fill = false),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${(progress * 100).toInt()}%",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = FinSenseCyan,
                        modifier = Modifier.padding(start = 6.dp)
                    )
                }
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(CircleShape),
                    color = FinSenseCyan,
                    trackColor = FinSenseSurfaceLow
                )
            }

            // Context Trigger Box - Responsive alignment
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(FinSenseSurfaceLow)
                    .clickable { onDetailsClick() }
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = FinSenseCyan,
                    modifier = Modifier
                        .size(15.dp)
                        .padding(top = 1.dp)
                )
                Text(
                    text = contextTrigger,
                    fontSize = 11.sp,
                    color = TextMuted,
                    modifier = Modifier.weight(1f),
                    lineHeight = 15.sp
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Details",
                    tint = TextMuted,
                    modifier = Modifier
                        .size(14.dp)
                        .padding(top = 1.dp)
                )
            }

            // Action Buttons (Primary CTA + Optional Secondary Action)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onCtaClick,
                    colors = ButtonDefaults.buttonColors(containerColor = FinSenseCyan),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                ) {
                    Text(
                        text = ctaLabel,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }

                if (secondaryCtaLabel != null && onSecondaryCtaClick != null) {
                    OutlinedButton(
                        onClick = onSecondaryCtaClick,
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, FinSenseCyan.copy(alpha = 0.5f)),
                        modifier = Modifier.height(44.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = FinSenseNavy,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = secondaryCtaLabel,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = FinSenseNavy,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}
