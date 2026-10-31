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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Speaker
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.AppScreen
import com.example.model.Persona
import com.example.model.RecommendationItem
import com.example.ui.components.PipelineJourneyType
import com.example.ui.components.PipelineVisualizerBottomSheet
import com.example.ui.theme.BorderHairline
import com.example.ui.theme.FinSenseCyan
import com.example.ui.theme.FinSenseNavy
import com.example.ui.theme.FinSensePrimary
import com.example.ui.theme.FinSenseSurface
import com.example.ui.theme.FinSenseSurfaceHigh
import com.example.ui.theme.FinSenseSurfaceLow
import com.example.ui.theme.StatusError
import com.example.ui.theme.StatusErrorBg
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.StatusSuccessBg
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.launch

data class PaytmTxn(
    val id: String,
    val title: String,
    val dateHeader: String,
    val time: String,
    val upiRef: String,
    val bankAccount: String,
    val amountFormatted: String,
    val isCredit: Boolean,
    val category: String, // Paid, Received, Cashback, Bill
    val icon: ImageVector,
    val avatarBg: Color,
    val journeyTag: String? = null
)

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun FinSenseDashboardScreen(
    currentPersona: Persona,
    onNavigate: (AppScreen) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    var selectedDashboardTab by remember { mutableStateOf(0) } // 0: Overview, 1: Trendlytics, 2: Paytm Passbook
    var isInsightsExpanded by remember { mutableStateOf(true) }
    var selectedHistoryFilter by remember { mutableStateOf(0) } // 0: All, 1: Paid, 2: Received, 3: Cashbacks, 4: Bills
    var selectedTxnForReceipt by remember { mutableStateOf<PaytmTxn?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var activePipelineType by remember { mutableStateOf<PipelineJourneyType?>(null) }
    val scope = rememberCoroutineScope()

    val personalRecommendations = listOf(
        RecommendationItem(
            id = "rec_car_insurance",
            title = "Explore vehicle insurance options",
            badge = "FINSENSE ACTION",
            savingsTag = "Save ₹2,400",
            description = "Review policies to save up to ₹2,400 on renewal. Honda City expiring soon.",
            targetScreen = AppScreen.RECOMMENDATION_DETAILS,
            iconType = "CAR",
            ctaText = "Explore Insurance"
        ),
        RecommendationItem(
            id = "rec_smart_savings",
            title = "Smart Savings 7.4% Auto-Sweep",
            badge = "AUTO-SWEEP",
            savingsTag = "Earn +₹4,150",
            description = "Earn 7.4% on idle funds above ₹10,000 threshold. 2.7x higher than regular savings.",
            targetScreen = AppScreen.SMART_SAVINGS_DETAILS,
            iconType = "SAVINGS",
            ctaText = "Activate Sweep"
        ),
        RecommendationItem(
            id = "rec_health_cover",
            title = "₹10 Lakhs Family Health & Tax Cover",
            badge = "TAX BENEFIT",
            savingsTag = "Save ₹7,500 Tax",
            description = "Zero paper claims, 9,500+ cashless network hospitals, Section 80D tax deduction.",
            targetScreen = AppScreen.HEALTH_COVER_DETAILS,
            iconType = "HEALTH",
            ctaText = "Compare Plans"
        )
    )

    val businessRecommendations = listOf(
        RecommendationItem(
            id = "rec_loan",
            title = "Pre-Approved Working Capital Loan",
            badge = "MERCHANT LENDING",
            savingsTag = "Unlock ₹1,50,000",
            description = "Instant credit to bank account. 1.0%/mo rate with automated daily settlement deduction.",
            targetScreen = AppScreen.WORKING_CAPITAL_LOANS,
            iconType = "LOAN",
            ctaText = "Apply in 2 Min"
        ),
        RecommendationItem(
            id = "rec_shop_insurance",
            title = "Shop & Stock Inventory Cover",
            badge = "MERCHANT SECURITY",
            savingsTag = "Save ₹1,800/yr",
            description = "Protect ₹25 Lakhs Rajesh Electronics stock from fire, burglary & flood damage.",
            targetScreen = AppScreen.SHOP_INSURANCE_DETAILS,
            iconType = "SHOP",
            ctaText = "Insure Store"
        ),
        RecommendationItem(
            id = "rec_settlement",
            title = "Resolve Settlement Delay (#SET-88291)",
            badge = "URGENT ISSUE",
            savingsTag = "₹1,450 Pending",
            description = "Bank clearing cycle ongoing for ₹1,450 batch. Track live status & instant resolution.",
            targetScreen = AppScreen.MERCHANT_ISSUE_RESOLUTION,
            iconType = "SETTLEMENT",
            ctaText = "Track Resolution"
        )
    )

    val activeRecommendations = if (currentPersona == Persona.PERSONAL) personalRecommendations else businessRecommendations

    // Realistic Paytm transactions
    val paytmTransactions = listOf(
        PaytmTxn(
            id = "TXN-881",
            title = "Paid to Swiggy",
            dateHeader = "Today, 30 Oct 2026",
            time = "08:24 PM",
            upiRef = "430291049281",
            bankAccount = "SBI ••4412",
            amountFormatted = "- ₹420",
            isCredit = false,
            category = "Paid",
            icon = Icons.Default.Restaurant,
            avatarBg = Color(0xFFFC8019),
            journeyTag = "Food & Dining"
        ),
        PaytmTxn(
            id = "TXN-880",
            title = "Salary Credited - TechCorp Labs",
            dateHeader = "Today, 30 Oct 2026",
            time = "09:00 AM",
            upiRef = "CMS-88192801",
            bankAccount = "HDFC Bank ••9012",
            amountFormatted = "+ ₹78,000",
            isCredit = true,
            category = "Received",
            icon = Icons.Default.AccountBalance,
            avatarBg = Color(0xFF00A664),
            journeyTag = "Auto-Sweep Surplus Inflow"
        ),
        PaytmTxn(
            id = "TXN-879",
            title = "Money received from Amit Verma",
            dateHeader = "Yesterday, 29 Oct 2026",
            time = "02:40 PM",
            upiRef = "429910482911",
            bankAccount = "Paytm Wallet",
            amountFormatted = "+ ₹1,500",
            isCredit = true,
            category = "Received",
            icon = Icons.Default.Person,
            avatarBg = Color(0xFF7B1FA2)
        ),
        PaytmTxn(
            id = "TXN-878",
            title = "Paid to Uber India",
            dateHeader = "Yesterday, 29 Oct 2026",
            time = "10:15 AM",
            upiRef = "429810294812",
            bankAccount = "SBI ••4412",
            amountFormatted = "- ₹340",
            isCredit = false,
            category = "Paid",
            icon = Icons.Default.DirectionsCar,
            avatarBg = Color(0xFF212121),
            journeyTag = "Transport"
        ),
        PaytmTxn(
            id = "TXN-877",
            title = "Paid to Apollo Pharmacy & Clinic",
            dateHeader = "26 Oct 2026",
            time = "04:15 PM",
            upiRef = "430018291024",
            bankAccount = "SBI ••4412",
            amountFormatted = "- ₹3,850",
            isCredit = false,
            category = "Paid",
            icon = Icons.Default.LocalHospital,
            avatarBg = Color(0xFF00897B),
            journeyTag = "FinSense Health & Tax Trigger"
        ),
        PaytmTxn(
            id = "TXN-876",
            title = "Electricity Bill Paid - BESCOM",
            dateHeader = "22 Oct 2026",
            time = "10:12 AM",
            upiRef = "BBPS88190241",
            bankAccount = "Paytm Payments Bank",
            amountFormatted = "- ₹2,480",
            isCredit = false,
            category = "Bills",
            icon = Icons.Default.Bolt,
            avatarBg = Color(0xFFE53935)
        ),
        PaytmTxn(
            id = "TXN-875",
            title = "Cashback received from Paytm",
            dateHeader = "20 Oct 2026",
            time = "06:30 PM",
            upiRef = "CB-99214810",
            bankAccount = "Paytm Wallet",
            amountFormatted = "+ ₹75",
            isCredit = true,
            category = "Cashback",
            icon = Icons.Default.Star,
            avatarBg = Color(0xFFF9A825)
        ),
        PaytmTxn(
            id = "TXN-874",
            title = "Paid to SpeedX Motor Works",
            dateHeader = "16 Oct 2026",
            time = "11:30 AM",
            upiRef = "429011928374",
            bankAccount = "HDFC Bank ••9012",
            amountFormatted = "- ₹6,450",
            isCredit = false,
            category = "Paid",
            icon = Icons.Default.DirectionsCar,
            avatarBg = Color(0xFF1E88E5),
            journeyTag = "FinSense Car Insurance Trigger"
        )
    )

    val filteredTransactions = paytmTransactions.filter { txn ->
        val matchesSearch = searchQuery.isBlank() ||
                txn.title.contains(searchQuery, ignoreCase = true) ||
                txn.upiRef.contains(searchQuery, ignoreCase = true)

        val matchesFilter = when (selectedHistoryFilter) {
            1 -> !txn.isCredit
            2 -> txn.isCredit
            3 -> txn.category == "Cashback"
            4 -> txn.category == "Bills"
            else -> true
        }

        matchesSearch && matchesFilter
    }

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
                Column {
                    Text(
                        text = if (currentPersona == Persona.PERSONAL) "FinSense Dashboard" else "Merchant Intelligence",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = if (currentPersona == Persona.PERSONAL) "Updated just now • AI Financial Intelligence" else "Rajesh Electronics & Co. • Store Analytics",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE0E8FF))
                ) {
                    Icon(
                        imageVector = Icons.Default.Insights,
                        contentDescription = "Insights",
                        tint = FinSenseNavy,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // TOP VIEW SWITCHER: [ Overview ] [ Trendlytics ] [ Paytm Passbook ]
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(FinSenseSurfaceLow)
                    .padding(3.dp)
                    .testTag("dashboard_view_switcher")
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    // Tab 0: Overview
                    val isOverview = selectedDashboardTab == 0
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(if (isOverview) FinSenseCyan else Color.Transparent)
                            .clickable { selectedDashboardTab = 0 }
                            .testTag("tab_overview")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Insights,
                                contentDescription = null,
                                tint = if (isOverview) Color.White else TextMuted,
                                modifier = Modifier.size(15.dp)
                            )
                            Text(
                                text = "Overview",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = if (isOverview) FontWeight.Bold else FontWeight.Medium,
                                color = if (isOverview) Color.White else TextMuted
                            )
                        }
                    }

                    // Tab 1: Trendlytics
                    val isTrendlytics = selectedDashboardTab == 1
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .weight(1.1f)
                            .height(38.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(if (isTrendlytics) FinSenseCyan else Color.Transparent)
                            .clickable { selectedDashboardTab = 1 }
                            .testTag("tab_trendlytics")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.TrendingUp,
                                contentDescription = null,
                                tint = if (isTrendlytics) Color.White else TextMuted,
                                modifier = Modifier.size(15.dp)
                            )
                            Text(
                                text = "Trendlytics",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = if (isTrendlytics) FontWeight.Bold else FontWeight.Medium,
                                color = if (isTrendlytics) Color.White else TextMuted
                            )
                        }
                    }

                    // Tab 2: Paytm Passbook
                    val isPassbook = selectedDashboardTab == 2
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .weight(1.2f)
                            .height(38.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(if (isPassbook) FinSenseNavy else Color.Transparent)
                            .clickable { selectedDashboardTab = 2 }
                            .testTag("tab_passbook")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountBalanceWallet,
                                contentDescription = null,
                                tint = if (isPassbook) Color.White else TextMuted,
                                modifier = Modifier.size(15.dp)
                            )
                            Text(
                                text = "Paytm Passbook",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = if (isPassbook) FontWeight.Bold else FontWeight.Medium,
                                color = if (isPassbook) Color.White else TextMuted
                            )
                        }
                    }
                }
            }
        }

        // ==========================================
        // VIEW 0: OVERVIEW (with quick jumps to Trendlytics & Passbook)
        // ==========================================
        if (selectedDashboardTab == 0) {
            // Quick Feature Jumps Row
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F4FC)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedDashboardTab = 1 }
                            .testTag("btn_jump_to_trendlytics")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.TrendingUp,
                                contentDescription = null,
                                tint = FinSenseCyan,
                                modifier = Modifier.size(16.dp)
                            )
                            Column {
                                Text(
                                    text = "Trendlytics",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = FinSenseNavy
                                )
                                Text(
                                    text = "6-Mo Spends",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontSize = 10.sp,
                                    color = TextMuted
                                )
                            }
                        }
                    }

                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFEBF3FF)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedDashboardTab = 2 }
                            .testTag("btn_jump_to_passbook")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountBalanceWallet,
                                contentDescription = null,
                                tint = FinSenseNavy,
                                modifier = Modifier.size(16.dp)
                            )
                            Column {
                                Text(
                                    text = "Paytm Passbook",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = FinSenseNavy
                                )
                                Text(
                                    text = "₹4,820 Balance",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontSize = 10.sp,
                                    color = TextMuted
                                )
                            }
                        }
                    }

                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF3E5F5)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                activePipelineType = if (currentPersona == Persona.PERSONAL)
                                    PipelineJourneyType.PERSONAL_CAR_INSURANCE
                                else
                                    PipelineJourneyType.MERCHANT_WORKING_CAPITAL
                            }
                            .testTag("btn_jump_to_pipeline")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = Color(0xFF7B1FA2),
                                modifier = Modifier.size(16.dp)
                            )
                            Column {
                                Text(
                                    text = "AI Pipeline",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF4A148C)
                                )
                                Text(
                                    text = "5-Step Flow",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontSize = 10.sp,
                                    color = TextMuted
                                )
                            }
                        }
                    }
                }
            }

            // Main Metric Card (Adapts to Personal vs Business)
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = FinSenseSurface),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, BorderHairline, RoundedCornerShape(16.dp))
                        .testTag("month_spending_summary_card")
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (currentPersona == Persona.PERSONAL) "THIS MONTH'S SPENDING" else "OCTOBER INFLOWS",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMuted,
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 0.5.sp
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (currentPersona == Persona.PERSONAL) StatusErrorBg else Color(0xFFE8F8F1))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.TrendingUp,
                                    contentDescription = null,
                                    tint = if (currentPersona == Persona.PERSONAL) StatusError else StatusSuccess,
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = if (currentPersona == Persona.PERSONAL) "+18% vs Sep" else "+24% vs Sep",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (currentPersona == Persona.PERSONAL) StatusError else StatusSuccess,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = if (currentPersona == Persona.PERSONAL) "₹48,920" else "₹2,84,900",
                                style = MaterialTheme.typography.displayLarge,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = if (currentPersona == Persona.PERSONAL) "of ₹65,000 budget" else "across 324 orders",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextMuted,
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                        }

                        // Progress Bar
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(FinSenseSurfaceLow)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(fraction = if (currentPersona == Persona.PERSONAL) 0.75f else 0.88f)
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(FinSenseNavy)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = if (currentPersona == Persona.PERSONAL) "75% of budget consumed" else "Gross Margin: 26.4%",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                            Text(
                                text = if (currentPersona == Persona.PERSONAL) "₹16,080 remaining" else "Receivables: ₹14,200",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = FinSenseNavy
                            )
                        }
                    }
                }
            }

            // Spending Breakdown Card
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = FinSenseSurface),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, BorderHairline, RoundedCornerShape(16.dp))
                        .testTag("spending_breakdown_card")
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (currentPersona == Persona.PERSONAL) "Spending Breakdown" else "Collections by Channel",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "4 CATEGORIES",
                                style = MaterialTheme.typography.labelSmall,
                                color = FinSenseNavy,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (currentPersona == Persona.PERSONAL) {
                            CategoryProgressRow("Shopping", 37, "₹18,200", FinSenseCyan)
                            CategoryProgressRow("Healthcare & Pharmacy", 25, "₹12,400", Color(0xFF00897B))
                            CategoryProgressRow("Food & Dining", 22, "₹10,870", Color(0xFFF3972A))
                            CategoryProgressRow("Vehicle Maintenance", 16, "₹7,450", FinSensePrimary)
                        } else {
                            CategoryProgressRow("QR Soundbox Inflow", 45, "₹1,28,200", FinSenseCyan)
                            CategoryProgressRow("In-store UPI Scan", 30, "₹85,470", Color(0xFF3F5BA3))
                            CategoryProgressRow("Card Swipe POS", 15, "₹42,730", Color(0xFFF3972A))
                            CategoryProgressRow("Payment Links & Remote", 10, "₹28,500", StatusSuccess)
                        }
                    }
                }
            }

            // AI Detection Card
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE6F8FE)),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFF71D2FF).copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                        .clickable {
                            onNavigate(if (currentPersona == Persona.PERSONAL) AppScreen.RECOMMENDATION_DETAILS else AppScreen.MERCHANT_ISSUE_RESOLUTION)
                        }
                        .testTag("ai_detection_card")
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(FinSenseCyan)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (currentPersona == Persona.PERSONAL) "AI DETECTION" else "MERCHANT AI PULSE",
                                style = MaterialTheme.typography.labelSmall,
                                color = FinSenseNavy,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = if (currentPersona == Persona.PERSONAL)
                                    "Vehicle & Healthcare spends spiked by +18%."
                                else
                                    "Weekend collections jumped by +24%.",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = if (currentPersona == Persona.PERSONAL)
                                    "Due to SpeedX motor service and Apollo Clinic invoices this month."
                                else
                                    "Settlement delay detected on card batch #SET-88291 (₹1,450).",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }

            // 3 Rich Contextual Action Recommendations Section Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = FinSenseNavy,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = if (currentPersona == Persona.PERSONAL) "Personal FinSense Actions (3)" else "Merchant Business Actions (3)",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    Text(
                        text = "Swipe to view",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted
                    )
                }
            }

            // Horizontal Carousel of 3 Recommendations
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(activeRecommendations) { rec ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = FinSenseSurface),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier
                                .width(280.dp)
                                .border(1.dp, BorderHairline, RoundedCornerShape(16.dp))
                                .testTag("recommendation_card_${rec.id}")
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Box(
                                            contentAlignment = Alignment.Center,
                                            modifier = Modifier
                                                .size(42.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(Color(0xFFE0E8FF))
                                        ) {
                                            Icon(
                                                imageVector = when (rec.iconType) {
                                                    "CAR" -> Icons.Default.DirectionsCar
                                                    "SAVINGS" -> Icons.Default.Savings
                                                    "HEALTH" -> Icons.Default.Security
                                                    "LOAN" -> Icons.Default.AccountBalanceWallet
                                                    "SHOP" -> Icons.Default.Storefront
                                                    else -> Icons.Default.AutoAwesome
                                                },
                                                contentDescription = null,
                                                tint = FinSenseNavy,
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }

                                        Column {
                                            Text(
                                                text = rec.badge,
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = FinSenseNavy
                                            )
                                            Text(
                                                text = rec.savingsTag,
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF603600),
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(Color(0xFFFFDCBE))
                                                    .padding(horizontal = 6.dp, vertical = 1.dp)
                                            )
                                        }
                                    }
                                }

                                Text(
                                    text = rec.title,
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary,
                                    maxLines = 2
                                )

                                Text(
                                    text = rec.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary,
                                    maxLines = 2
                                )

                                Button(
                                    onClick = { onNavigate(rec.targetScreen) },
                                    colors = ButtonDefaults.buttonColors(containerColor = FinSenseCyan),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(44.dp)
                                ) {
                                    Text(
                                        text = rec.ctaText,
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // ==========================================
        // VIEW 1: TRENDLYTICS (Detailed Spending Trends & Cashflow)
        // ==========================================
        if (selectedDashboardTab == 1) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = FinSenseSurface),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, BorderHairline, RoundedCornerShape(16.dp))
                        .testTag("trendlytics_chart_card")
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "6-Month Spending Velocity",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "Monthly average: ₹44,400 • Runway steady",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextMuted
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFFE8F4FC)
                            ) {
                                Text(
                                    text = "May - Oct 2026",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = FinSenseNavy,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        // Visual 6-month bar chart
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(FinSenseSurfaceLow)
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            val months = listOf(
                                "May" to 41200,
                                "Jun" to 43800,
                                "Jul" to 46100,
                                "Aug" to 44500,
                                "Sep" to 48920,
                                "Oct" to 42100
                            )
                            val maxVal = 50000f

                            months.forEachIndexed { idx, (month, amount) ->
                                val isCurrent = idx == months.size - 1
                                val heightFraction = (amount / maxVal).coerceIn(0.2f, 1f)

                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Bottom,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = "₹${amount / 1000}k",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontSize = 10.sp,
                                        fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isCurrent) FinSenseNavy else TextMuted
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Box(
                                        modifier = Modifier
                                            .width(24.dp)
                                            .height((100 * heightFraction).dp)
                                            .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                            .background(
                                                if (isCurrent) FinSenseCyan else FinSenseNavy.copy(alpha = 0.65f)
                                            )
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = month,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isCurrent) FinSenseNavy else TextMuted
                                    )
                                }
                            }
                        }

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
                                    imageVector = Icons.Default.TrendingDown,
                                    contentDescription = null,
                                    tint = StatusSuccess,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "14% lower spend run-rate than September",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = StatusSuccess,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }

            // 50/30/20 Budget Health Card
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = FinSenseSurface),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, BorderHairline, RoundedCornerShape(16.dp))
                        .testTag("trendlytics_budget_rule_card")
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
                            Column {
                                Text(
                                    text = "50 / 30 / 20 Budget Rule Gauge",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "Benchmark against disciplined savings principles",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextMuted
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = StatusSuccessBg
                            ) {
                                Text(
                                    text = "Grade A-",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = StatusSuccess,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        // Needs
                        CategoryProgressRow("Needs (Groceries, Rent, Bills)", 52, "₹25,400 (Target ≤ 50%)", FinSenseNavy)
                        // Wants
                        CategoryProgressRow("Wants (Dining, Shopping)", 28, "₹13,700 (Target ≤ 30%)", Color(0xFFFC8019))
                        // Savings
                        CategoryProgressRow("Savings & Investments", 20, "₹9,820 (Target ≥ 20%)", StatusSuccess)
                    }
                }
            }

            // Category Velocity & AI Triggers
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
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Category Velocity vs Last Month",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        VelocityRow("Vehicle & Transport", "₹7,450", "+18%", true, "Triggered Car Insurance Review")
                        VelocityRow("Healthcare & Pharmacy", "₹12,400", "+42%", true, "Triggered Family Health & 80D")
                        VelocityRow("Food & Dining", "₹10,870", "-6%", false, "Under Control")
                        VelocityRow("Shopping & Tech", "₹18,200", "+5%", true, "Discretionary Spends")
                    }
                }
            }

            // Predictive Month-End Run Rate & Surplus
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F4FC)),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = FinSenseNavy,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Predictive Month-End Surplus",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = FinSenseNavy
                            )
                        }

                        Text(
                            text = "Based on current run-rate, your October total spend will be ~₹51,800 against your ₹65,000 budget ceiling. You will have an idle surplus of ₹13,200.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextPrimary
                        )

                        Button(
                            onClick = { onNavigate(AppScreen.SMART_SAVINGS_DETAILS) },
                            colors = ButtonDefaults.buttonColors(containerColor = FinSenseCyan),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                        ) {
                            Text(
                                text = "Activate 7.4% Auto-Sweep on ₹13,200 Surplus",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        // ==========================================
        // VIEW 2: PAYTM PASSBOOK & PAYMENT HISTORY
        // ==========================================
        if (selectedDashboardTab == 2) {
            // Authentic Paytm Balance Header Card
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF002E6E)),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("paytm_balance_card")
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF00BAF2))
                                )
                                Text(
                                    text = "PAYTM PASSBOOK",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF00BAF2),
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color.White.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = "Paytm Verified",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Column {
                            Text(
                                text = "Paytm Balance",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                            Text(
                                text = "₹4,820.50",
                                style = MaterialTheme.typography.displayLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        // Sub-wallets strip
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White.copy(alpha = 0.1f))
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Wallet: ₹1,320.50", color = Color.White, style = MaterialTheme.typography.bodySmall)
                            Text(text = "FASTag: ₹3,000", color = Color.White, style = MaterialTheme.typography.bodySmall)
                            Text(text = "Gift Card: ₹500", color = Color.White, style = MaterialTheme.typography.bodySmall)
                        }

                        // Linked Bank Accounts strip
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
                                    imageVector = Icons.Default.AccountBalance,
                                    contentDescription = null,
                                    tint = Color(0xFF00BAF2),
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "SBI ••4412 (UPI Primary)",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                            }

                            Text(
                                text = "HDFC ••9012",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }

            // Search Bar
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search by merchant, friend or UPI Ref...") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = TextMuted)
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = "Clear", tint = TextMuted)
                            }
                        }
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = FinSenseSurface,
                        unfocusedContainerColor = FinSenseSurface,
                        focusedBorderColor = FinSenseCyan,
                        unfocusedBorderColor = BorderHairline
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("paytm_history_search")
                )
            }

            // Filter Chips: All, Paid, Received, Cashback, Bills
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val filterLabels = listOf("All", "Paid (-)", "Received (+)", "Cashbacks", "Bills")
                    items(filterLabels.indices.toList()) { idx ->
                        val isSelected = selectedHistoryFilter == idx
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSelected) FinSenseNavy else FinSenseSurfaceHigh,
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .clickable { selectedHistoryFilter = idx }
                                .testTag("filter_chip_$idx")
                        ) {
                            Text(
                                text = filterLabels[idx],
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else TextPrimary,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }

            // Grouped Paytm Transactions
            val groupedTxns = filteredTransactions.groupBy { it.dateHeader }

            groupedTxns.forEach { (dateHeader, txns) ->
                item {
                    Text(
                        text = dateHeader,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                items(txns) { txn ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = FinSenseSurface),
                        shape = RoundedCornerShape(14.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, BorderHairline, RoundedCornerShape(14.dp))
                            .clickable { selectedTxnForReceipt = txn }
                            .testTag("paytm_txn_${txn.id}")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(txn.avatarBg)
                                ) {
                                    Icon(
                                        imageVector = txn.icon,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                    Text(
                                        text = txn.title,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )

                                    Text(
                                        text = "${txn.time} • UPI: ${txn.bankAccount}",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontSize = 11.sp,
                                        color = TextMuted
                                    )

                                    if (txn.journeyTag != null) {
                                        Surface(
                                            shape = RoundedCornerShape(4.dp),
                                            color = Color(0xFFE8F4FC),
                                            modifier = Modifier.padding(top = 2.dp)
                                        ) {
                                            Text(
                                                text = txn.journeyTag,
                                                style = MaterialTheme.typography.labelSmall,
                                                fontSize = 10.sp,
                                                color = FinSenseNavy,
                                                fontWeight = FontWeight.SemiBold,
                                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                            )
                                        }
                                    }
                                }
                            }

                            Column(
                                horizontalAlignment = Alignment.End,
                                verticalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Text(
                                    text = txn.amountFormatted,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (txn.isCredit) Color(0xFF00A664) else TextPrimary
                                )

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = Color(0xFF00A664),
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Text(
                                        text = "Success",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontSize = 11.sp,
                                        color = Color(0xFF00A664),
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Expandable Recent Financial Insights Card (for Overview)
        if (selectedDashboardTab == 0) {
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
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { isInsightsExpanded = !isInsightsExpanded },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Insights,
                                    contentDescription = null,
                                    tint = FinSenseNavy,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = if (currentPersona == Persona.PERSONAL) "Recent Financial Insights" else "Recent Merchant Telemetry",
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "2 NEW",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextMuted,
                                    fontWeight = FontWeight.Bold
                                )
                                Icon(
                                    imageVector = if (isInsightsExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                    contentDescription = null,
                                    tint = TextMuted,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        AnimatedVisibility(visible = isInsightsExpanded) {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                HorizontalDivider(color = BorderHairline, thickness = 0.8.dp)

                                if (currentPersona == Persona.PERSONAL) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Box(
                                                contentAlignment = Alignment.Center,
                                                modifier = Modifier
                                                    .size(28.dp)
                                                    .clip(CircleShape)
                                                    .background(Color(0xFFE0E8FF))
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Bolt,
                                                    contentDescription = null,
                                                    tint = FinSenseNavy,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                            Text(
                                                text = "Utility bills lower by 6%",
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.Medium,
                                                color = TextPrimary
                                            )
                                        }
                                        Text(
                                            text = "Optimal",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = TextSecondary,
                                            fontWeight = FontWeight.SemiBold,
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(FinSenseSurfaceLow)
                                                .padding(horizontal = 8.dp, vertical = 2.dp)
                                        )
                                    }

                                    HorizontalDivider(color = BorderHairline, thickness = 0.8.dp)

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Box(
                                                contentAlignment = Alignment.Center,
                                                modifier = Modifier
                                                    .size(28.dp)
                                                    .clip(CircleShape)
                                                    .background(Color(0xFFFFDCBE))
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Restaurant,
                                                    contentDescription = null,
                                                    tint = Color(0xFF8B5000),
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                            Text(
                                                text = "Weekend dining spike (+₹3,100)",
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.Medium,
                                                color = TextPrimary
                                            )
                                        }
                                        Text(
                                            text = "+14%",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = StatusError,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(StatusErrorBg)
                                                .padding(horizontal = 8.dp, vertical = 2.dp)
                                        )
                                    }
                                } else {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Box(
                                                contentAlignment = Alignment.Center,
                                                modifier = Modifier
                                                    .size(28.dp)
                                                    .clip(CircleShape)
                                                    .background(Color(0xFFE8F8F1))
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Speaker,
                                                    contentDescription = null,
                                                    tint = StatusSuccess,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                            Text(
                                                text = "Soundbox 4G Voice Latency: 0.8s",
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.Medium,
                                                color = TextPrimary
                                            )
                                        }
                                        Text(
                                            text = "Excellent",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = StatusSuccess,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(Color(0xFFE8F8F1))
                                                .padding(horizontal = 8.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Quick Entry to FinSense AI Copilot Chat
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8EDFF)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigate(AppScreen.COPILOT) }
                    .border(1.dp, BorderHairline, RoundedCornerShape(16.dp))
                    .testTag("launch_copilot_from_dashboard")
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(FinSenseNavy)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Column {
                            Text(
                                text = if (currentPersona == Persona.PERSONAL)
                                    "Have questions about this month?"
                                else
                                    "Need merchant assistance or credit audits?",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = if (currentPersona == Persona.PERSONAL)
                                    "Ask FinSense AI for personal recommendations"
                                else
                                    "Ask FinSense Merchant AI for instant resolution",
                                style = MaterialTheme.typography.bodySmall,
                                color = FinSenseNavy,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = FinSenseNavy,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // AUTHENTIC PAYTM PAYMENT RECEIPT DIALOG
    selectedTxnForReceipt?.let { txn ->
        Dialog(onDismissRequest = { selectedTxnForReceipt = null }) {
            Card(
                colors = CardDefaults.cardColors(containerColor = FinSenseSurface),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderHairline, RoundedCornerShape(20.dp))
                    .testTag("paytm_receipt_dialog")
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Paytm Success Header
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE8F8F1))
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF00A664),
                            modifier = Modifier.size(34.dp)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = if (txn.isCredit) "Money Received Successfully" else "Paid Successfully",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = txn.title,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = txn.amountFormatted.replace("- ", "").replace("+ ", ""),
                            style = MaterialTheme.typography.displayLarge,
                            fontWeight = FontWeight.Bold,
                            color = if (txn.isCredit) Color(0xFF00A664) else FinSenseNavy
                        )
                    }

                    HorizontalDivider(color = BorderHairline, thickness = 0.8.dp)

                    // Receipt Details Grid
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(FinSenseSurfaceLow)
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ReceiptDetailRow("UPI Ref No", txn.upiRef)
                        ReceiptDetailRow("Bank Account", txn.bankAccount)
                        ReceiptDetailRow("Timestamp", "${txn.dateHeader}, ${txn.time}")
                        ReceiptDetailRow("Order ID", "PYTM-20261030-${txn.id.replace("TXN-", "882")}")
                        if (txn.journeyTag != null) {
                            ReceiptDetailRow("FinSense Trigger", txn.journeyTag)
                        }
                    }

                    // Action buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                scope.launch {
                                    snackbarHostState.showSnackbar("Payment receipt shared via WhatsApp/SMS")
                                }
                                selectedTxnForReceipt = null
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = FinSenseSurfaceHigh),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(imageVector = Icons.Default.Share, contentDescription = null, tint = TextPrimary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Share", color = TextPrimary, style = MaterialTheme.typography.labelMedium)
                        }

                        Button(
                            onClick = { selectedTxnForReceipt = null },
                            colors = ButtonDefaults.buttonColors(containerColor = FinSenseNavy),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "Done", color = Color.White, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    // Modal Bottom Sheet for 5-Step AI Pipeline Visualizer (Slide 3)
    activePipelineType?.let { pipelineType ->
        PipelineVisualizerBottomSheet(
            initialPipelineType = pipelineType,
            onDismiss = { activePipelineType = null }
        )
    }
}

@Composable
private fun ReceiptDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = TextMuted)
        Text(text = value, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold, color = TextPrimary)
    }
}

@Composable
private fun VelocityRow(category: String, amount: String, change: String, isIncrease: Boolean, note: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(FinSenseSurfaceLow)
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = category, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = TextPrimary)
            Text(text = note, style = MaterialTheme.typography.bodySmall, fontSize = 11.sp, color = TextMuted)
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = amount, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = TextPrimary)
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = if (isIncrease) StatusErrorBg else StatusSuccessBg
            ) {
                Text(
                    text = change,
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 11.sp,
                    color = if (isIncrease) StatusError else StatusSuccess,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
private fun CategoryProgressRow(
    name: String,
    percentage: Int,
    amount: String,
    color: Color
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(color)
                )
                Text(
                    text = name,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary
                )
                Text(
                    text = "($percentage%)",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }
            Text(
                text = amount,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        // Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(FinSenseSurfaceLow)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = percentage / 100f)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(color)
            )
        }
    }
}
