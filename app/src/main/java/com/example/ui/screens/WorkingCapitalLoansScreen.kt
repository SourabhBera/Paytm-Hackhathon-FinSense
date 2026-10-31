package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EventRepeat
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SyncAlt
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BorderHairline
import com.example.ui.theme.FinSenseCyan
import com.example.ui.theme.FinSenseNavy
import com.example.ui.theme.FinSenseSurface
import com.example.ui.theme.FinSenseSurfaceContainer
import com.example.ui.theme.FinSenseSurfaceHigh
import com.example.ui.theme.FinSenseSurfaceLow
import com.example.ui.theme.StatusWarning
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.components.AssessmentMetric
import com.example.ui.components.RecommendationRationaleDropdown
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun WorkingCapitalLoansScreen(
    onBack: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    var capitalAmount by remember { mutableFloatStateOf(150000f) }
    var selectedPlan by remember { mutableIntStateOf(2) } // 1: 6-Month, 2: 12-Month
    var isEligibilityExpanded by remember { mutableStateOf(false) }
    var isApplicationSubmitted by remember { mutableStateOf(false) }
    var activeJargonDefinition by remember { mutableStateOf<com.example.ui.components.JargonDefinition?>(null) }
    val scope = rememberCoroutineScope()

    val formattedCapital = "₹" + NumberFormat.getNumberInstance(Locale("en", "IN")).format(capitalAmount.toInt())

    val dailyDebit = if (selectedPlan == 1) "₹592" else "₹444"

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(FinSenseSurfaceHigh)
                            .testTag("loan_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Text(
                        text = "Working Capital Loans",
                        style = MaterialTheme.typography.headlineMedium,
                        color = TextPrimary
                    )
                }

                Text(
                    text = "Business",
                    style = MaterialTheme.typography.labelSmall,
                    color = FinSenseNavy,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(FinSenseSurfaceHigh)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }
        }

        // Why this recommendation is shown (Transaction History & Financial Assessment)
        item {
            RecommendationRationaleDropdown(
                title = "Why is this credit & loan recommendation shown?",
                collapsedSummary = "Monthly QR turnover ₹2,84,900 • CIBIL 790 • Pre-approved for ₹1.5L - ₹2.5L",
                transactionHistoryTitle = "₹2,84,900 in monthly QR customer payments across 412 transactions",
                transactionHistoryDetail = "Continuous high-velocity UPI settlement volume logged at Rajesh Electronics & Co. (averaging ₹18,450/day with 99.4% transaction reliability and zero payment disputes).",
                financialAssessmentTitle = "Store Cashflow Stability & Debt Servicing Capacity",
                financialAssessmentDetail = "Your debt-to-turnover ratio is under 6%, indicating high repayment headroom. A daily micro-debit of ₹444 - ₹592 from morning customer QR transfers pays down principal continuously without month-end liquidity stress.",
                assessmentMetrics = listOf(
                    AssessmentMetric("Monthly Turnover", "₹2,84,900"),
                    AssessmentMetric("Credit Bureau Score", "790 (Prime)"),
                    AssessmentMetric("Daily Debt Capacity", "₹800/day"),
                    AssessmentMetric("Approval Status", "Pre-Approved")
                ),
                aiRecommendationRationale = "Securing this working capital limit now prepares your store inventory for the festive electronics peak without collateral or rigid monthly EMI penalties.",
                initiallyExpanded = true,
                testTagPrefix = "loan_rationale"
            )
        }

        // Estimated Eligible Amount Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = FinSenseSurface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderHairline, RoundedCornerShape(16.dp))
                    .testTag("eligible_amount_card")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "ESTIMATED ELIGIBLE AMOUNT",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMuted,
                                fontSize = 10.sp,
                                letterSpacing = 0.5.sp,
                                maxLines = 1
                            )
                            Text(
                                text = "Illustrative",
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 9.sp,
                                color = TextSecondary,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(FinSenseSurfaceHigh)
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "₹1,50,000",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(2.dp),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(FinSenseSurfaceHigh)
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Bolt,
                                    contentDescription = null,
                                    tint = FinSenseNavy,
                                    modifier = Modifier.size(13.dp)
                                )
                                Text(
                                    text = "Pre-approved",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 10.5.sp,
                                    color = FinSenseNavy,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(FinSenseSurfaceLow)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = null,
                            tint = FinSenseCyan,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
            }
        }

        // Interactive Amount Adjuster Slider Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = FinSenseSurface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderHairline, RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Required Capital",
                            style = MaterialTheme.typography.labelMedium,
                            color = TextSecondary
                        )
                        Text(
                            text = formattedCapital,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = FinSenseNavy
                        )
                    }

                    Slider(
                        value = capitalAmount,
                        onValueChange = {
                            capitalAmount = it
                            if (it <= 100000f) selectedPlan = 1 else selectedPlan = 2
                        },
                        valueRange = 50000f..150000f,
                        steps = 9,
                        colors = SliderDefaults.colors(
                            thumbColor = FinSenseCyan,
                            activeTrackColor = FinSenseCyan,
                            inactiveTrackColor = FinSenseSurfaceHigh
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("loan_amount_slider")
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "₹50,000 (Min)", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                        Text(text = "₹1,00,000", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                        Text(text = "₹1,50,000 (Max)", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                    }
                }
            }
        }

        // Loan Tenure Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Select Repayment Plan",
                    style = MaterialTheme.typography.headlineSmall,
                    color = TextPrimary
                )
                Text(
                    text = "2 tailored offers",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted
                )
            }
        }

        // Option 1: Flexible 6-Month Term
        item {
            LoanPlanCard(
                title = "Flexible 6-Month Term",
                badge = "Most Popular",
                badgeColor = Color(0xFFDAE1FF),
                badgeText = FinSenseNavy,
                description = "Rapid operational turnaround",
                amount = "₹1,00,000",
                interest = "1.1% / mo",
                monthlyEmi = "₹17,760",
                tenureText = "6 Months tenure",
                dailyRate = "₹592 / day",
                isSelected = selectedPlan == 1,
                onClick = {
                    selectedPlan = 1
                    capitalAmount = 100000f
                },
                tag = "loan_option_6m"
            )
        }

        // Option 2: Growth 12-Month Term (Lowest EMI)
        item {
            LoanPlanCard(
                title = "Growth 12-Month Term",
                badge = "Lowest EMI",
                badgeColor = FinSenseSurfaceHigh,
                badgeText = FinSenseNavy,
                description = "Long-term equipment or stock growth",
                amount = "₹1,50,000",
                interest = "1.0% / mo",
                monthlyEmi = "₹13,320",
                tenureText = "12 Months tenure",
                dailyRate = "₹444 / day",
                isSelected = selectedPlan == 2,
                onClick = {
                    selectedPlan = 2
                    capitalAmount = 150000f
                },
                tag = "loan_option_12m"
            )
        }

        // Repayment Breakdown Card (2-Column Grid)
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = FinSenseSurface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderHairline, RoundedCornerShape(16.dp))
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
                        Text(
                            text = "Repayment Breakdown",
                            style = MaterialTheme.typography.headlineSmall,
                            color = TextPrimary
                        )
                        Text(
                            text = "Automated",
                            style = MaterialTheme.typography.labelSmall,
                            color = FinSenseNavy,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(FinSenseSurfaceHigh)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Daily Auto-Debit
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(FinSenseSurfaceLow)
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SyncAlt,
                                    contentDescription = null,
                                    tint = FinSenseNavy,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "Daily Auto-Debit",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextMuted,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 1
                                )
                            }
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = dailyDebit,
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "/day",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextMuted,
                                    modifier = Modifier.padding(bottom = 2.dp)
                                )
                            }
                            Text(
                                text = "Deducted from QR settlement",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextMuted,
                                fontSize = 10.sp,
                                lineHeight = 13.sp
                            )
                            Surface(
                                color = FinSenseSurfaceHigh,
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .clickable {
                                        activeJargonDefinition = com.example.ui.components.JargonGlossary.items["Daily Auto-Settlement Debit"]
                                    }
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = FinSenseCyan,
                                        modifier = Modifier.size(11.dp)
                                    )
                                    Text(
                                        text = "Decode Debit",
                                        fontSize = 9.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = FinSenseNavy
                                    )
                                }
                            }
                        }

                        // Processing Fee
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(FinSenseSurfaceLow)
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocalOffer,
                                    contentDescription = null,
                                    tint = StatusWarning,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "Processing Fee",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextMuted,
                                    maxLines = 1
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.Bottom,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "₹0",
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "₹2,999",
                                    style = MaterialTheme.typography.bodySmall,
                                    textDecoration = TextDecoration.LineThrough,
                                    color = TextMuted,
                                    modifier = Modifier.padding(bottom = 2.dp)
                                )
                            }
                            Text(
                                text = "Limited Period Waiver",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF8B5000),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Bank Account info
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
                                imageVector = Icons.Default.AccountBalanceWallet,
                                contentDescription = null,
                                tint = TextMuted,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Credit to: Primary HDFC Current A/c (••9012)",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                maxLines = 1,
                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Instant",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = FinSenseNavy
                        )
                    }
                }
            }
        }

        // Transparent Terms & Plain-Language APR Decoder Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = FinSenseSurfaceLow),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderHairline),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("loan_terms_decoder_card")
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
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
                                imageVector = Icons.Default.Bolt,
                                contentDescription = null,
                                tint = FinSenseCyan,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "TRANSPARENT TERMS DECODER",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = FinSenseNavy,
                                letterSpacing = 0.5.sp,
                                maxLines = 1,
                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                            )
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        com.example.ui.components.JargonChip(
                            term = "Flat vs Reducing APR",
                            onClick = {
                                activeJargonDefinition = com.example.ui.components.JargonGlossary.items["Flat vs Reducing APR"]
                            }
                        )
                    }

                    Text(
                        text = "Why we show Effective APR: While standard lenders quote 1.0%/mo flat rate, FinSense discloses the true effective APR (18.2% diminishing) with zero hidden prepayment penalties.",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        lineHeight = 17.sp
                    )
                }
            }
        }

        // Expandable Eligibility Accordion
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = FinSenseSurface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderHairline, RoundedCornerShape(16.dp))
                    .testTag("eligibility_accordion")
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isEligibilityExpanded = !isEligibilityExpanded }
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.HelpOutline,
                                contentDescription = null,
                                tint = FinSenseNavy,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "How is eligibility calculated?",
                                style = MaterialTheme.typography.headlineSmall,
                                color = TextPrimary
                            )
                        }

                        Icon(
                            imageVector = if (isEligibilityExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    AnimatedVisibility(visible = isEligibilityExpanded) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            LoanEligibilityPoint("QR Soundbox Inflow", "Based on continuous 90-day UPI collection consistency.")
                            LoanEligibilityPoint("No Physical Paperwork", "Automated digital verification via partner NBFCs.")
                            LoanEligibilityPoint("Flexible Deduction", "Repayments sync directly with your daily customer inflows.")
                        }
                    }
                }
            }
        }

        // Trust badge
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = null,
                    tint = FinSenseNavy,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Lending Partners: Tata Capital • Suryoday SFB • Clix Capital",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted,
                    fontSize = 10.sp
                )
            }
        }

        // Apply CTA Button
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Button(
                    onClick = {
                        isApplicationSubmitted = true
                        scope.launch {
                            snackbarHostState.showSnackbar("Working Capital Loan offer reserved! KYC verification initiated.")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = if (isApplicationSubmitted) FinSenseNavy else FinSenseCyan),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("apply_loan_button")
                ) {
                    Text(
                        text = if (isApplicationSubmitted) "Offer Reserved! KYC Next" else "Apply in 2 Minutes",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = if (isApplicationSubmitted) Icons.Default.CheckCircle else Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Text(
                    text = "Zero collateral required • Disbursement within 2 hours",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Modal Bottom Sheet for Plain-Language Term Decoder
    com.example.ui.components.JargonDecoderBottomSheet(
        definition = activeJargonDefinition,
        onDismiss = { activeJargonDefinition = null }
    )
}

@Composable
private fun LoanPlanCard(
    title: String,
    badge: String,
    badgeColor: Color,
    badgeText: Color,
    description: String,
    amount: String,
    interest: String,
    monthlyEmi: String,
    tenureText: String,
    dailyRate: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    tag: String
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) FinSenseSurfaceLow else FinSenseSurface
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 3.dp else 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = if (isSelected) FinSenseCyan else BorderHairline,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .testTag(tag)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            modifier = Modifier.weight(1f, fill = false),
                            maxLines = 1,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                        )
                        Text(
                            text = badge,
                            style = MaterialTheme.typography.labelSmall,
                            color = badgeText,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(badgeColor)
                                .padding(horizontal = 6.dp, vertical = 2.dp),
                            maxLines = 1
                        )
                    }
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted,
                        maxLines = 1
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Radio Indicator
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .border(
                            1.5.dp,
                            if (isSelected) FinSenseCyan else BorderHairline,
                            CircleShape
                        )
                ) {
                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(FinSenseCyan)
                        )
                    }
                }
            }

            // Grid 3 columns - Responsive with weights
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(FinSenseSurface)
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Amount", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                    Text(text = amount, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = TextPrimary)
                }
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Interest", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                    Text(text = interest, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = TextPrimary)
                }
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Monthly EMI", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                    Text(text = monthlyEmi, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = FinSenseNavy)
                }
            }

            // Tenure & Daily Rate
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        tint = TextMuted,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = tenureText,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted
                    )
                }

                Text(
                    text = dailyRate,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = FinSenseNavy
                )
            }
        }
    }
}

@Composable
private fun LoanEligibilityPoint(heading: String, text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = FinSenseNavy,
            modifier = Modifier
                .size(16.dp)
                .padding(top = 2.dp)
        )
        Text(
            text = "$heading: $text",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )
    }
}
